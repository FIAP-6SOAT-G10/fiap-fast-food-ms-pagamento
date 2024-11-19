package br.com.fiap.techchallenge.infra.entrypoints.rest.payment;

import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentNotification;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentResponseDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentControllerIT {

    @Autowired
    private PaymentController paymentController;

    @Nested
    class RealizarPagamento {

        @Test
        void deveRealizarPagamento() {
            String externalOrderId = "1234567892";
            ResponseEntity<PaymentResponseDTO> response = paymentController.makePayment(externalOrderId);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void deveLancarExcecaoAoRealizarPagamento() {
            String externalOrderId = "1234567895";

            ResponseEntity<PaymentResponseDTO> response = paymentController.makePayment(externalOrderId);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

    }

    @Nested
    class ConfirmarPagamento {

        @Test
        void deveAutorizarPagamento() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25069761168");

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void deveLancarExcecao_QuandoPagamentoJaFoiProcessado() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25069761168");

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void deveLancarExcecao_QuandoPagamentoNaoEncontrado() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25069761169");

            assertThrows(Exception.class, () -> paymentController.receivePaymentConfirmation(paymentNotification));
        }

        @Test
        void deveFinalizarProcesso_QuandoRecursoNaoForCallbackDePedido() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("payment");
            paymentNotification.setResource("25069761169");

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertNull(response);
        }

        @Test
        void deveFinalilzarProcesso_QuandoConfirmacaoNaoForRealizada() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25152311115");

            ResponseEntity<?> response = paymentController.receivePaymentConfirmation(paymentNotification);

            assertNull(response);
        }

    }

    @Nested
    class ConsultarPagamento {

        @Test
        void deveConsultarPagamento() {
            String internalPaymentId = UUID.fromString("2baa836d-186f-481f-b3a7-341c9c788602").toString();

            ResponseEntity<PaymentResponseDTO> response = paymentController.getPaymentByInternalPaymentId(internalPaymentId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void deveConsultarPagamento_RetornarNaoEncontrado_QuandoPagamentoNaoExistir() {
            String internalPaymentId = UUID.fromString("2baa836d-186f-481f-b3a7-341c9c788603").toString();

            assertThrows(IllegalArgumentException.class, () -> paymentController.getPaymentByInternalPaymentId(internalPaymentId));
        }

    }

}