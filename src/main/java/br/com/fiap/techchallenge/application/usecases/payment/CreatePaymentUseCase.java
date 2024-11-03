package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;

public class CreatePaymentUseCase {

    private final IPaymentRepository paymentRepository;

    public CreatePaymentUseCase(IPaymentRepository paymentRepository
    ) {
        this.paymentRepository = paymentRepository;
    }

    public void execute(PaymentRequest paymentRequest) {
        paymentRepository.createPayment(paymentRequest);
    }
}
