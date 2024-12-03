package br.com.fiap.techchallenge.infra.entrypoints.rest.payment;

import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentNotification;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentControllerIT {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class RealizarPagamento {

        @Test
        void deveRealizarPagamento() {
            String externalOrderId = "1234567892";
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                    .post("/api/payments/{externalOrderId}/checkout", externalOrderId)
            .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        void deveLancarExcecaoAoRealizarPagamento() {
            String externalOrderId = "1234567895";
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                    .post("/api/payments/{externalOrderId}/checkout", externalOrderId)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

    }

    @Nested
    class ConfirmarPagamento {

        @Test
        void deveAutorizarPagamento() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25069761168");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentNotification)
            .when()
                    .post("/api/payments/confirmation")
            .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void deveLancarExcecao_QuandoPagamentoNaoEncontrado() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25069761169");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentNotification)
            .when()
                    .post("/api/payments/confirmation")
            .then()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void deveFinalizarProcesso_QuandoRecursoNaoForCallbackDePedido() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("payment");
            paymentNotification.setResource("25069761169");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentNotification)
            .when()
                    .post("/api/payments/confirmation")
            .then()
                    .body(Matchers.nullValue());
        }

        @Test
        void deveFinalilzarProcesso_QuandoConfirmacaoNaoForRealizada() {
            PaymentNotification paymentNotification = new PaymentNotification();
            paymentNotification.setTopic("merchant_order");
            paymentNotification.setResource("25152311115");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentNotification)
            .when()
                    .post("/api/payments/confirmation")
            .then()
                    .body(Matchers.nullValue());
        }

    }

    @Nested
    class ConsultarPagamento {

        @Test
        void deveConsultarPagamento() {
            String internalPaymentId = "2baa836d-186f-481f-b3a7-341c9c788602";
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                    .get("/api/payments/{internalPaymentId}", internalPaymentId)
            .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveConsultarPagamento_RetornarNaoEncontrado_QuandoPagamentoNaoExistir() {
            String internalPaymentId = UUID.fromString("2baa836d-186f-481f-b3a7-341c9c788603").toString();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                    .get("/api/payments/{internalPaymentId}", internalPaymentId)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

    }

}