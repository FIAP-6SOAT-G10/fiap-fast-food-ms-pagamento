package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;

public class ConsultPaymentUseCase {

    private final IPaymentRepository paymentRepository;

    public ConsultPaymentUseCase(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public Payment findPaymentById(String internalPaymentId) throws PaymentNotFoundException {
        try {
            return this.paymentRepository.findPayment(internalPaymentId);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new PaymentNotFoundException("O pagamento " + internalPaymentId + " não foi localizado.");
        }
    }
}
