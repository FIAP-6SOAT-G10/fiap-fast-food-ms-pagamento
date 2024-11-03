package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.INotificationRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public class NotifyPaymentConsumerUseCase {

    private final INotificationRepository notificationRepository;

    public NotifyPaymentConsumerUseCase(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void execute(Payment payment) {
        this.notificationRepository.sendNotification(payment);
    }

}
