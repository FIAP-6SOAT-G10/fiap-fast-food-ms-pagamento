package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.application.gateways.INotificationRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;

@Slf4j
public class NotificationRepository implements INotificationRepository {

    private final SnsTemplate snsTemplate;
    private final String destination;

    public NotificationRepository(SnsTemplate snsTemplate, String destination) {
        this.snsTemplate = snsTemplate;
        this.destination = destination;
    }

    @Override
    public boolean sendNotification(Payment payment) {
        try {
            snsTemplate.convertAndSend(destination, payment);
            return true;
        } catch (MessagingException messagingException) {
            log.error("Falha ao enviar a mensagem de notificação aos consumidores", messagingException);
            throw messagingException;
        } catch (Exception exception) {
            log.error("Erro genérico ao enviar a mensagem de notificação aos consumidores", exception);
            throw exception;
        }
    }
}
