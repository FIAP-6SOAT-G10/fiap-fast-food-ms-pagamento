//package br.com.fiap.techchallenge.infra.gateways;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
//import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
//import io.awspring.cloud.sns.core.SnsTemplate;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.UUID;
//
//import static br.com.fiap.techchallenge.PaymentHelper.*;
//import static org.mockito.Mockito.*;
//
//class NotificationRepositoryTest {
//
//    AutoCloseable autoCloseable;
//
//    @Mock
//    SnsTemplate snsTemplate;
//
//    @InjectMocks
//    NotificationRepository notificationRepository;
//
//    @BeforeEach
//    void setup() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
//        notificationRepository = new NotificationRepository(snsTemplate, "test-destination-queue");
//    }
//
//    @Nested
//    class NotificarConsumidor {
//
//        @Test
//        void deveEnviarNotificacao() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment payment = buildPayment(paymentEntity);
//
//            notificationRepository.sendNotification(payment);
//
//            verify(snsTemplate, times(1)).convertAndSend(anyString(), any(Payment.class));
//        }
//
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }
//
//}
