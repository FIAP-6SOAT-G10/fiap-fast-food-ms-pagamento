package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;

public class ConsultPaymentUseCase {

    private final IPaymentRepository paymentRepository;

    public ConsultPaymentUseCase(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public Payment findPaymentById(String internalPaymentId) {
        return this.paymentRepository.findPayment(internalPaymentId);
    }
}
