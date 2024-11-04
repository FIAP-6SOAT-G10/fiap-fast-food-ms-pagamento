package br.com.fiap.techchallenge.application.usecases;

import br.com.fiap.techchallenge.application.gateways.IPaymentProviderRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.MercadoLibreResponse;

public class VerifyPaymentUseCase {

    private final IPaymentProviderRepository paymentProviderRepository;

    public VerifyPaymentUseCase(IPaymentProviderRepository paymentProviderRepository) {
        this.paymentProviderRepository = paymentProviderRepository;
    }


    public MercadoLibreResponse execute(String resource) {
        return this.paymentProviderRepository.consultPayment(resource);
    }
}
