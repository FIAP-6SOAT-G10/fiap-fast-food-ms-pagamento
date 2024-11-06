package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.INotificationRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyPaymentConsumerUseCase {

    private final INotificationRepository notificationRepository;

    public NotifyPaymentConsumerUseCase(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void execute(Payment payment) {
        log.info("Enviando notificação de pagamento para os consumidores");
        this.notificationRepository.sendNotification(payment);
    }

}
