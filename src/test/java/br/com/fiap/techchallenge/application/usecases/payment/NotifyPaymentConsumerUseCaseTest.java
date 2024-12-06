//package br.com.fiap.techchallenge.application.usecases.payment;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
//import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
//import br.com.fiap.techchallenge.infra.gateways.NotificationRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.UUID;
//
//import static br.com.fiap.techchallenge.PaymentHelper.*;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//class NotifyPaymentConsumerUseCaseTest {
//
//    @Mock
//    NotificationRepository notificationRepository;
//
//    @InjectMocks
//    NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase;
//
//    AutoCloseable autoCloseable;
//
//    @BeforeEach
//    void setup() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void deveNotificarConsumidores() {
//        PaymentRequest paymentRequest = buildPaymentRequest();
//        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//        Payment payment = buildPayment(paymentEntity);
//
//        notifyPaymentConsumerUseCase.execute(payment);
//
//        verify(notificationRepository, times(1)).sendNotification(payment);
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }
//
//}
