package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.gateways.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static br.com.fiap.techchallenge.PaymentHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfirmPaymentUseCaseTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    VerifyPaymentUseCase verifyPaymentUseCase;

    @Mock
    NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase;

    @InjectMocks
    ConfirmPaymentUseCase confirmPaymentUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConfirmarPagamento() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
        String resource = "/9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalReference("9999999991");
        PaymentRequest paymentRequest = buildPaymentRequest();
        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
        Payment payment = buildPayment(paymentEntity);
        when(verifyPaymentUseCase.execute(resource)).thenReturn(paymentResponse);
        when(paymentRepository.findPayment(paymentResponse.getExternalReference())).thenReturn(payment);
        Payment paymentReturned = buildPayment(paymentEntity);
        paymentReturned.setPaymentStatus("PAID");
        when(paymentRepository.finishPayment(payment)).thenReturn(paymentReturned);

        Payment paymentReceived = confirmPaymentUseCase.execute(resource);

        assertEquals(payment.getInternalPaymentId(), paymentReceived.getInternalPaymentId());
        assertEquals(payment.getExternalPaymentId(), paymentReceived.getExternalPaymentId());
        assertEquals(payment.getExternalId(), paymentReceived.getExternalId());
        assertEquals(payment.getPayer(), paymentReceived.getPayer());
        assertEquals(payment.getPaymentAmount(), paymentReceived.getPaymentAmount());
        assertEquals(payment.getPaymentDate(), paymentReceived.getPaymentDate());
        assertEquals(payment.getPaymentRequestDate(), paymentReceived.getPaymentRequestDate());
        assertEquals(paymentReturned.getPaymentStatus(), paymentReceived.getPaymentStatus());
        assertEquals(payment.getPaymentMethod(), paymentReceived.getPaymentMethod());
        assertEquals(payment.getPaymentType(), paymentReceived.getPaymentType());

        verify(notifyPaymentConsumerUseCase, times(1)).execute(any(Payment.class));
    }

    @Test
    void deveRetornarNuloAoConfirmarPagamento_QuandoPagamentoNaoRealizado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
        String resource = "99999999991";
        when(verifyPaymentUseCase.execute(resource)).thenReturn(null);

        confirmPaymentUseCase.execute(resource);

        verify(paymentRepository, never()).findPayment(anyString());
        verify(notifyPaymentConsumerUseCase, never()).execute(any(Payment.class));
        verify(paymentRepository, never()).finishPayment(any(Payment.class));
    }

    @Test
    void deveLancarExcecaoAoConfirmarPagamento_QuandoPagamentoNaoIdentificado_IdentificadorNaoLocalizado() {
        String resource = "9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalReference("9999999991");
        when(verifyPaymentUseCase.execute(resource)).thenReturn(paymentResponse);
        when(paymentRepository.findPayment(anyString())).thenThrow(new IllegalArgumentException("Pagamento não encontrado"));

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> confirmPaymentUseCase.execute(resource));

        assertEquals("Pagamento não encontrado", exception.getMessage());

        verify(notifyPaymentConsumerUseCase, never()).execute(any(Payment.class));
        verify(paymentRepository, never()).finishPayment(any(Payment.class));
    }

    @Test
    void deveLancarExcecaoAoConfirmarPagamento_QuandoPagamentoJaProcessado_PagamentoEfetuado() {
        String resource = "/9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalReference("9999999991");
        PaymentRequest paymentRequest = buildPaymentRequest();
        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
        Payment payment = buildPayment(paymentEntity);
        payment.setPaymentStatus("PAID");
        when(verifyPaymentUseCase.execute(resource)).thenReturn(paymentResponse);
        when(paymentRepository.findPayment(paymentResponse.getExternalReference())).thenReturn(payment);

        assertThrows(PaymentAlreadyProcessedException.class, () -> confirmPaymentUseCase.execute(resource));

        verify(notifyPaymentConsumerUseCase, never()).execute(any(Payment.class));
        verify(paymentRepository, never()).finishPayment(any(Payment.class));
    }

    @Test
    void deveLancarExcecaoAoConfirmarPagamento_QuandoPagamentoJaProcessado_PagamentoNegado() {
        String resource = "/9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalReference("9999999991");
        PaymentRequest paymentRequest = buildPaymentRequest();
        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
        Payment payment = buildPayment(paymentEntity);
        payment.setPaymentStatus("DENIED");
        when(verifyPaymentUseCase.execute(resource)).thenReturn(paymentResponse);
        when(paymentRepository.findPayment(paymentResponse.getExternalReference())).thenReturn(payment);

        assertThrows(PaymentAlreadyProcessedException.class, () -> confirmPaymentUseCase.execute(resource));

        verify(notifyPaymentConsumerUseCase, never()).execute(any(Payment.class));
        verify(paymentRepository, never()).finishPayment(any(Payment.class));
    }

    @Test
    void deveConfirmarPagamento_QuandoPagamentoConcluido_SituacaoPago() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
        String resource = "/9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalReference("9999999991");
        paymentResponse.setOrderStatus("paid");
        PaymentRequest paymentRequest = buildPaymentRequest();
        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
        Payment payment = buildPayment(paymentEntity);
        Payment paymentFinished = buildPayment(paymentEntity);
        paymentFinished.setPaymentStatus("paid");
        when(verifyPaymentUseCase.execute(resource)).thenReturn(paymentResponse);
        when(paymentRepository.findPayment(paymentResponse.getExternalReference())).thenReturn(payment);
        when(paymentRepository.finishPayment(payment)).thenReturn(paymentFinished);

        Payment paymentReceived = confirmPaymentUseCase.execute(resource);

        assertEquals(payment.getInternalPaymentId(), paymentReceived.getInternalPaymentId());
        assertEquals(payment.getExternalId(), paymentReceived.getExternalId());
        assertEquals(paymentFinished.getPaymentStatus(), paymentReceived.getPaymentStatus());
        verify(notifyPaymentConsumerUseCase, times(1)).execute(any(Payment.class));
    }

    @Test
    void deveConfirmarPagamento_QuandoPagamentoNaoAutorizado_SituacaoNegado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
        String resource = "/9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalReference("9999999991");
        paymentResponse.setOrderStatus("denied");
        PaymentRequest paymentRequest = buildPaymentRequest();
        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
        Payment payment = buildPayment(paymentEntity);
        Payment paymentFinished = buildPayment(paymentEntity);
        paymentFinished.setPaymentStatus("denied");
        when(verifyPaymentUseCase.execute(resource)).thenReturn(paymentResponse);
        when(paymentRepository.findPayment(paymentResponse.getExternalReference())).thenReturn(payment);
        when(paymentRepository.finishPayment(payment)).thenReturn(paymentFinished);

        Payment paymentReceived = confirmPaymentUseCase.execute(resource);

        assertEquals(payment.getInternalPaymentId(), paymentReceived.getInternalPaymentId());
        assertEquals(payment.getExternalId(), paymentReceived.getExternalId());
        assertEquals(paymentFinished.getPaymentStatus(), paymentReceived.getPaymentStatus());
        verify(notifyPaymentConsumerUseCase, times(1)).execute(any(Payment.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

}
