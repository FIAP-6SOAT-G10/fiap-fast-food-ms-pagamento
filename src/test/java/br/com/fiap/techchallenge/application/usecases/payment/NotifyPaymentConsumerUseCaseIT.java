//package br.com.fiap.techchallenge.application.usecases.payment;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
//import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.UUID;
//
//import static br.com.fiap.techchallenge.PaymentHelper.*;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("integration-test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class NotifyPaymentConsumerUseCaseIT {
//
//    @Autowired
//    private NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase;
//
//    @Test
//    void deveNotificarConsumidores() {
//        PaymentRequest paymentRequest = buildPaymentRequest();
//        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//        Payment payment = buildPayment(paymentEntity);
//
//        notifyPaymentConsumerUseCase.execute(payment);
//
//        Assertions.assertNotNull(payment);
//    }
//
//}
