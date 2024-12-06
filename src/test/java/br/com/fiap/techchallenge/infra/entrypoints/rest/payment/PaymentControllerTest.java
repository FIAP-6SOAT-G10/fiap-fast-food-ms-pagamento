package br.com.fiap.techchallenge.infra.entrypoints.rest.payment;

import br.com.fiap.techchallenge.application.usecases.payment.ConfirmPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.ConsultPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.MakePaymentUseCase;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model.PaymentRequestDTO;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentNotification;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentResponseDTO;
import br.com.fiap.techchallenge.infra.presenters.DateMapper;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static br.com.fiap.techchallenge.PaymentHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    MakePaymentUseCase makePaymentUseCase;

    @Mock
    ConfirmPaymentUseCase confirmPaymentUseCase;

    @Mock
    ConsultPaymentUseCase consultPaymentUseCase;

    @Mock
    PaymentMapper paymentMapper;

    @InjectMocks
    PaymentController paymentController;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Nested
    class RealizarPagamento {

        @Test
        void deveRealizarPagamento() {
            String externalOrderId = RandomStringUtils.randomNumeric(11);
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            PaymentResponseDTO paymentResponseDTO = buildPaymentResponseDTO(payment);
            when(makePaymentUseCase.execute(externalOrderId)).thenReturn(payment);
            when(paymentMapper.fromDomainToDataTransferObject(payment)).thenReturn(paymentResponseDTO);

            ResponseEntity<PaymentResponseDTO> response = paymentController.makePayment(externalOrderId);
            PaymentResponseDTO paymentResponseDTOReceived = response.getBody();

            assertNotNull(response);
            assertNotNull(paymentResponseDTOReceived);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            assertEquals(paymentResponseDTO.getInternalPaymentId(), paymentResponseDTOReceived.getInternalPaymentId());
            assertEquals(paymentResponseDTO.getExternalPaymentId(), paymentResponseDTOReceived.getExternalPaymentId());
            assertEquals(paymentResponseDTO.getExternalId(), paymentResponseDTOReceived.getExternalId());
            assertEquals(paymentResponseDTO.getPayer(), paymentResponseDTOReceived.getPayer());
            assertEquals(paymentResponseDTO.getPaymentAmount(), paymentResponseDTOReceived.getPaymentAmount());
            assertEquals(paymentResponseDTO.getPaymentDate(), paymentResponseDTOReceived.getPaymentDate());
            assertEquals(paymentResponseDTO.getPaymentRequestDate(), paymentResponseDTOReceived.getPaymentRequestDate());
            assertEquals(paymentResponseDTO.getPaymentStatus(), paymentResponseDTOReceived.getPaymentStatus());
            assertEquals(paymentResponseDTO.getPaymentMethod(), paymentResponseDTOReceived.getPaymentMethod());
            assertEquals(paymentResponseDTO.getPaymentType(), paymentResponseDTOReceived.getPaymentType());

            verify(makePaymentUseCase, times(1)).execute(externalOrderId);
        }

        @Test
        void deveLancarExcecaoAoRealizarPagamento() {
            String externalOrderId = RandomStringUtils.randomNumeric(11);
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            when(makePaymentUseCase.execute(externalOrderId)).thenThrow(new IllegalArgumentException("Pagamento não encontrado"));

            ResponseEntity<PaymentResponseDTO> response = paymentController.makePayment(externalOrderId);

            assertNull(response.getBody());
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

            verify(paymentMapper, never()).fromDomainToDataTransferObject(payment);
        }

    }

    @Nested
    class ConfirmarPagamento {

        @Test
        void deveAutorizarPagamento() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setResource("9999999991");
            paymentNotification.setTopic("merchant_order");
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            PaymentResponseDTO paymentResponseDTO = buildPaymentResponseDTO(payment);
            when(confirmPaymentUseCase.execute(paymentNotification.getResource())).thenReturn(payment);
            when(paymentMapper.fromDomainToDataTransferObject(payment)).thenReturn(paymentResponseDTO);

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);
            PaymentResponseDTO paymentReceived = (PaymentResponseDTO) response.getBody();

            assertNotNull(paymentReceived);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(payment.getInternalPaymentId(), paymentReceived.getInternalPaymentId());
            assertEquals(payment.getExternalPaymentId(), paymentReceived.getExternalPaymentId());
            assertEquals(payment.getExternalId(), paymentReceived.getExternalId());
            assertEquals(payment.getPayer(), paymentReceived.getPayer());
            assertEquals(payment.getPaymentAmount(), paymentReceived.getPaymentAmount());
            assertEquals(payment.getPaymentStatus(), paymentReceived.getPaymentStatus());
            assertEquals(payment.getPaymentMethod(), paymentReceived.getPaymentMethod());
            assertEquals(payment.getPaymentType(), paymentReceived.getPaymentType());
            verify(confirmPaymentUseCase, times(1)).execute(paymentNotification.getResource());
        }

        @Test
        void deveLancarExcecao_QuandoPagamentoJaFoiProcessado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setResource("9999999991");
            paymentNotification.setTopic("merchant_order");
            when(confirmPaymentUseCase.execute(paymentNotification.getResource())).thenThrow(new PaymentAlreadyProcessedException());

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(paymentMapper, never()).fromDomainToDataTransferObject(any(Payment.class));
        }

        @Test
        void deveLancarExcecao_QuandoPagamentoNaoEncontrado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setResource("9999999991");
            paymentNotification.setTopic("merchant_order");
            when(confirmPaymentUseCase.execute(paymentNotification.getResource())).thenThrow(new PaymentNotFoundException("Pagamento não encontrado"));

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            verify(paymentMapper, never()).fromDomainToDataTransferObject(any(Payment.class));
        }

        @Test
        void deveFinalizarProcesso_QuandoRecursoNaoForCallbackDePedido() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setResource("9999999991");
            paymentNotification.setTopic("payment");

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertNull(response);
        }

        @Test
        void deveFinalilzarProcesso_QuandoConfirmacaoNaoForRealizada() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setResource("9999999991");
            paymentNotification.setTopic("merchant_order");
            when(confirmPaymentUseCase.execute(paymentNotification.getResource())).thenReturn(nullable(Payment.class));

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertNull(response);
            verify(paymentMapper, never()).fromDataTransferObjetToDomain(any(PaymentRequestDTO.class));
        }

    }

    @Nested
    class ConsultarPagamento {

        @Test
        void deveConsultarPagamento() throws PaymentNotFoundException {
            String internalPaymentId = UUID.randomUUID().toString();
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(internalPaymentId, paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            PaymentResponseDTO paymentResponseDTO = buildPaymentResponseDTO(payment);
            when(consultPaymentUseCase.findPaymentById(internalPaymentId)).thenReturn(payment);
            when(paymentMapper.fromDomainToDataTransferObject(payment)).thenReturn(paymentResponseDTO);

            ResponseEntity<PaymentResponseDTO> response = paymentController.getPaymentByInternalPaymentId(internalPaymentId);
            PaymentResponseDTO paymentResponseDTOReceived = response.getBody();

            assertNotNull(response);
            assertNotNull(paymentResponseDTOReceived);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(payment.getInternalPaymentId(), paymentResponseDTOReceived.getInternalPaymentId());
            assertEquals(payment.getExternalPaymentId(), paymentResponseDTOReceived.getExternalPaymentId());
            assertEquals(payment.getExternalId(), paymentResponseDTOReceived.getExternalId());
            assertEquals(payment.getPayer(), paymentResponseDTOReceived.getPayer());
            assertEquals(payment.getPaymentAmount(), paymentResponseDTOReceived.getPaymentAmount());
            assertEquals(payment.getPaymentStatus(), paymentResponseDTOReceived.getPaymentStatus());
            assertEquals(payment.getPaymentMethod(), paymentResponseDTOReceived.getPaymentMethod());
            assertEquals(payment.getPaymentType(), paymentResponseDTOReceived.getPaymentType());
            verify(consultPaymentUseCase, times(1)).findPaymentById(internalPaymentId);
        }

        @Test
        void deveConsultarPagamento_RetornarNaoEncontrado_QuandoPagamentoNaoExistir() throws PaymentNotFoundException {
            String internalPaymentId = UUID.randomUUID().toString();
            when(consultPaymentUseCase.findPaymentById(internalPaymentId)).thenReturn(nullable(Payment.class));

            ResponseEntity<PaymentResponseDTO> response = paymentController.getPaymentByInternalPaymentId(internalPaymentId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(paymentMapper, never()).fromDomainToDataTransferObject(any(Payment.class));
        }

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    private PaymentResponseDTO buildPaymentResponseDTO(Payment payment) {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        paymentResponseDTO.setInternalPaymentId(payment.getInternalPaymentId());
        paymentResponseDTO.setExternalPaymentId(payment.getExternalPaymentId());
        paymentResponseDTO.setExternalId(payment.getExternalId());
        paymentResponseDTO.setPayer(payment.getPayer());
        paymentResponseDTO.setPaymentAmount(payment.getPaymentAmount());
        paymentResponseDTO.setPaymentDate(DateMapper.fromLocalDateTimeToStringWithFormat(payment.getPaymentDate(), "yyyy-MM-dd"));
        paymentResponseDTO.setPaymentRequestDate(DateMapper.fromLocalDateTimeToStringWithFormat(payment.getPaymentRequestDate(), "yyyy-MM-dd"));
        paymentResponseDTO.setPaymentStatus(payment.getPaymentStatus());
        paymentResponseDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentResponseDTO.setPaymentType(payment.getPaymentType());
        return paymentResponseDTO;
    }

}
