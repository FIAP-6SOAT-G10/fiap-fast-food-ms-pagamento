package br.com.fiap.techchallenge.application.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public interface INotificationRepository {

    boolean sendNotification(Payment payment);

}