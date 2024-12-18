package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import io.awspring.cloud.sns.core.SnsTemplate;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static br.com.fiap.techchallenge.PaymentHelper.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryIT {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SnsTemplate snsTemplate;

    @Nested
    class NotificarConsumidor {

        @Test
        void deveEnviarNotificacao() {

            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);

            boolean isSent = notificationRepository.sendNotification(payment);

            assertTrue(isSent);
        }

        @Test
        void deveLancarExcecao_QuandoEnviarNotificacao_ErroGenerico() {
            assertThrows(Exception.class, () -> notificationRepository.sendNotification(null));
        }

        /**
        @Test
        void deveLancarExcecao_QuandoEnviarNotificacao_FalhaNaComunicacao() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            assertThrows(MessagingException.class, () -> notificationRepository.sendNotification(payment));
        }
        */

    }

}