package br.com.fiap.techchallenge.application.usecases;

import br.com.fiap.techchallenge.application.gateways.IPaymentProviderRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VerifyPaymentUseCase {

    private final IPaymentProviderRepository paymentProviderRepository;

    public VerifyPaymentUseCase(IPaymentProviderRepository paymentProviderRepository) {
        this.paymentProviderRepository = paymentProviderRepository;
    }


    public PaymentResponse execute(String resource) {
        log.info("Consultando pagamento.");
        PaymentResponse paymentResponse = this.paymentProviderRepository.consultPayment(resource);
        if (paymentResponse != null) {
            paymentResponse = this.paymentProviderRepository.consultPaymentMethod(paymentResponse);
        }
        return paymentResponse;

    }
}
