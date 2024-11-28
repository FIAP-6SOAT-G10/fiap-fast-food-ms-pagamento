package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
import br.com.fiap.techchallenge.domain.entities.pagamento.enums.PaymentStatusEnum;
import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ConfirmPaymentUseCase {

    private final IPaymentRepository paymentRepository;
    private final VerifyPaymentUseCase verifyPaymentUseCase;
    private final NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase;

    public ConfirmPaymentUseCase(IPaymentRepository paymentRepository, VerifyPaymentUseCase verifyPaymentUseCase, NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase) {
        this.paymentRepository = paymentRepository;
        this.verifyPaymentUseCase = verifyPaymentUseCase;
        this.notifyPaymentConsumerUseCase = notifyPaymentConsumerUseCase;
    }

    public Payment execute(String resource) throws PaymentAlreadyProcessedException, PaymentNotFoundException {
        log.info("Confirmando situação do pagamento.");
        PaymentResponse paymentResponse = verifyPaymentUseCase.execute(resource);
        if (paymentResponse == null) {
            return null;
        }

        Payment payment;
        try {
            payment = this.paymentRepository.findPayment(paymentResponse.getExternalReference());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new PaymentNotFoundException(illegalArgumentException.getMessage());
        }

        if (isPaymentAlreadyProcessed(payment.getPaymentStatus())) {
            throw new PaymentAlreadyProcessedException();
        }

        if (isPaymentAuthorized(paymentResponse.getOrderStatus())) {
            log.info("Pagamento autorizado");
            payment.setExternalPaymentId(paymentResponse.getExternalPaymentId());
            payment.setPaymentStatus(paymentResponse.getOrderStatus());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(paymentResponse.getPaymentMethod());
            payment.setPaymentType(paymentResponse.getPaymentType());
        } else {
            log.info("Pagamento não autorizado");
            payment.setPaymentStatus(paymentResponse.getOrderStatus());
        }

        notifyPaymentConsumerUseCase.execute(payment);
        return this.paymentRepository.finishPayment(payment);
    }

    private boolean isPaymentAlreadyProcessed(String paymentStatus) {
        log.info("Validando se o pagamento já foi processado anteriormente");
        return PaymentStatusEnum.PAID.getNominalStatus().equalsIgnoreCase(paymentStatus) || PaymentStatusEnum.DENIED.getNominalStatus().equalsIgnoreCase(paymentStatus);
    }

    private boolean isPaymentAuthorized(String paymentStatus) {
        log.info("Validando se o pagamento foi autorizado");
        return PaymentStatusEnum.PAID.getNominalStatus().equalsIgnoreCase(paymentStatus);
    }
}
