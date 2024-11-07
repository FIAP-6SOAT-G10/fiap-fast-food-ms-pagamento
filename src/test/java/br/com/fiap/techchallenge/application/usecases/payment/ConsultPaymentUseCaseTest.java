package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.PaymentHelper;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.gateways.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultPaymentUseCaseTest {

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    ConsultPaymentUseCase consultPaymentUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPagamento_QuandoRealizarConsultaDePagamento() {
        String internalPaymentId = UUID.randomUUID().toString();
        PaymentRequest paymentRequest = PaymentHelper.buildPaymentRequest();
        PaymentEntity paymentEntity = PaymentHelper.buildPaymentEntity(internalPaymentId, paymentRequest);
        Payment payment = PaymentHelper.buildPayment(paymentEntity);
        when(paymentRepository.findPayment(internalPaymentId)).thenReturn(payment);

        Payment paymentReturned = consultPaymentUseCase.findPaymentById(internalPaymentId);

        assertEquals(internalPaymentId, paymentReturned.getInternalPaymentId());
        verify(paymentRepository, times(1)).findPayment(anyString());
    }

    @Test
    void deveLancarExcecao_QuandoRealizarConsultaDePagamento_IdentificadorNaoEncontrado() {
        String internalPaymentId = UUID.randomUUID().toString();
        PaymentRequest paymentRequest = PaymentHelper.buildPaymentRequest();
        PaymentEntity paymentEntity = PaymentHelper.buildPaymentEntity(internalPaymentId, paymentRequest);
        when(paymentRepository.findPayment(internalPaymentId)).thenThrow(new IllegalArgumentException("Pagamento não encontrado"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> consultPaymentUseCase.findPaymentById(internalPaymentId));

        assertEquals("Pagamento não encontrado", exception.getMessage());
        verify(paymentRepository, times(1)).findPayment(anyString());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

}