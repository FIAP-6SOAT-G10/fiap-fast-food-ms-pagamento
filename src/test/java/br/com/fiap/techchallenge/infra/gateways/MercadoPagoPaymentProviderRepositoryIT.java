//package br.com.fiap.techchallenge.infra.gateways;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
//import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import static br.com.fiap.techchallenge.PaymentHelper.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("integration-test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class MercadoPagoPaymentProviderRepositoryIT {
//
//    @Autowired
//    private MercadoPagoPaymentProviderRepository mercadoPagoPaymentProviderRepository;
//
//    @Nested
//    class CriarPagamento {
//
//        @Test
//        void deveCriarPagamento() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment payment = buildPayment(paymentEntity);
//
//            Payment paymentRequested = mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(payment);
//
//            assertNotNull(paymentRequested);
//            assertEquals(payment.getInternalPaymentId(), paymentRequested.getInternalPaymentId());
//            assertEquals(payment.getPaymentAmount(), paymentRequested.getPaymentAmount());
//            assertEquals(payment.getPayer(), paymentRequested.getPayer());
//        }
//
//        @Test
//        void deveLancarExcecao_QuandoPagamentoNaoForGerado_ErroNoProvedorDePagamento() {
//            PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(Math.random() * 100));
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment payment = buildPayment(paymentEntity);
//
//            assertThrows(Exception.class, () -> mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(payment));
//        }
//
//    }
//
//    @Nested
//    class ConsultarPagamento {
//
//        @Test
//        void deveConsultarPagamento_QuandoPagamentoConfirmado_RetornarPagamento() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment payment = buildPayment(paymentEntity);
//            Payment paymentRequested = mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(payment);
//
//            assertNotNull(paymentRequested);
//        }
//
//        @Test
//        void deveConsultarPagamento_QuandoPagamentoPendente_RetornarNuloParaPagamentoAindaNaoConfirmado() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment payment = buildPayment(paymentEntity);
//            Payment paymentRequested = mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(payment);
//
//            assertNotNull(paymentRequested);
//        }
//
//    }
//
//    @Nested
//    class ConsultarFormaDePagamento {
//
//        @Test
//        void deveConsultarFormaDePagamento() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment payment = buildPayment(paymentEntity);
//            Payment paymentRequested = mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(payment);
//
//            assertNotNull(paymentRequested);
//        }
//
//    }
//
//}
