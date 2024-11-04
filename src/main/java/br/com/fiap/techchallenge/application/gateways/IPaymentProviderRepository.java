package br.com.fiap.techchallenge.application.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.MercadoLibreResponse;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public interface IPaymentProviderRepository {

    void createPaymentRequestOnPaymentProvider(Payment payment);

    MercadoLibreResponse consultPayment(String resource);
}
