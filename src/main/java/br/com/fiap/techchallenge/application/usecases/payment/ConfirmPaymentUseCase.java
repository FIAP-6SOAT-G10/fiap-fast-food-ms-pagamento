package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.enums.PaymentStatusEnum;
import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;

import java.time.LocalDateTime;

public class ConfirmPaymentUseCase {

    private final IPaymentRepository paymentRepository;
    private final NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase;

    public ConfirmPaymentUseCase(IPaymentRepository paymentRepository, NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase) {
        this.paymentRepository = paymentRepository;
        this.notifyPaymentConsumerUseCase = notifyPaymentConsumerUseCase;
    }

    public Payment execute(String internalPaymentId, String paymentStatus) throws PaymentAlreadyProcessedException, PaymentNotFoundException {
        Payment payment;
        try {
            payment = this.paymentRepository.findPayment(internalPaymentId);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new PaymentNotFoundException(illegalArgumentException.getMessage());
        }

        if (isPaymentAlreadyProcessed(payment.getPaymentStatus())) {
            throw new PaymentAlreadyProcessedException();
        }

        if (isPaymentAuthorized(paymentStatus)) {
            payment.setPaymentStatus(paymentStatus);
            payment.setPaymentDate(LocalDateTime.now());
        } else {
            payment.setPaymentStatus(paymentStatus);
        }

        notifyPaymentConsumerUseCase.execute(payment);
        return this.paymentRepository.finishPayment(payment);
    }

    private boolean isPaymentAlreadyProcessed(String paymentStatus) {
        return PaymentStatusEnum.PAID.getNominalStatus().equalsIgnoreCase(paymentStatus) ||
                PaymentStatusEnum.DENIED.getNominalStatus().equalsIgnoreCase(paymentStatus);
    }

    private boolean isPaymentAuthorized(String paymentStatus) {
        return PaymentStatusEnum.PAID.getNominalStatus().equalsIgnoreCase(paymentStatus);
    }
}
