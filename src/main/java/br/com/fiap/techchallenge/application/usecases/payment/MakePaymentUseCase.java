package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public class MakePaymentUseCase {

    private final IPaymentRepository paymentRepository;

    public MakePaymentUseCase(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment execute(String externalOrderId) {
        return this.paymentRepository.sendPayment(externalOrderId);
    }

}
