package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.application.gateways.INotificationRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationRepository implements INotificationRepository {

    @Value("${aws.sns.updates-payments-topic}")
    private String destination;

    private final SnsTemplate snsTemplate;

    public NotificationRepository(SnsTemplate snsTemplate) {
        this.snsTemplate = snsTemplate;
    }

    @Override
    public void sendNotification(Payment payment) {
        snsTemplate.convertAndSend(destination, payment);
    }
}
