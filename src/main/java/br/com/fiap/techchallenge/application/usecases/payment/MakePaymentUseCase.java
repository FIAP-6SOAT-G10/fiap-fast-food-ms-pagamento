package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentProviderRepository;
import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public class MakePaymentUseCase {

    private final IPaymentRepository paymentRepository;
    private final IPaymentProviderRepository paymentProviderRepository;

    public MakePaymentUseCase(IPaymentRepository paymentRepository, IPaymentProviderRepository paymentProviderRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentProviderRepository = paymentProviderRepository;
    }

    public Payment execute(String externalOrderId) {
        Payment payment = this.paymentRepository.savePayment(externalOrderId);
        return this.paymentProviderRepository.createPaymentRequestOnPaymentProvider(payment);
    }

}
