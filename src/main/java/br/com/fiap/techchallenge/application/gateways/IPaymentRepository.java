package br.com.fiap.techchallenge.application.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;

public interface IPaymentRepository {

    void createPayment(PaymentRequest paymentRequest);

    Payment savePayment(String externalOrderId);

    Payment findPayment(String internalPaymentId);

    Payment finishPayment(Payment payment);

}