package br.com.fiap.techchallenge.application.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public interface IPaymentProviderRepository {

    Payment createPaymentRequestOnPaymentProvider(Payment payment);

    PaymentResponse consultPayment(String resource);

    PaymentResponse consultPaymentMethod(PaymentResponse payment);
}
