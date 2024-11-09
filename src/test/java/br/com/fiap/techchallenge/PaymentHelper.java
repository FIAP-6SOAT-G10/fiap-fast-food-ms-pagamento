package br.com.fiap.techchallenge;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.entities.pagamento.enums.PaymentStatusEnum;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

public class PaymentHelper {

    public static PaymentEntity buildPaymentEntity(String internalPaymentId, PaymentRequest paymentRequest) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setInternalPaymentId(internalPaymentId);
        paymentEntity.setExternalId(paymentRequest.getExternalOrderId());
        paymentEntity.setPayer(paymentRequest.getPayer());
        paymentEntity.setPaymentAmount(paymentRequest.getPaymentAmount());
        paymentEntity.setPaymentStatus(PaymentStatusEnum.WAITING_AUTHORIZATION.getNominalStatus());
        return paymentEntity;
    }

    public static Payment buildPayment(PaymentEntity paymentEntity) {
        Payment paymentReceived = new Payment();
        paymentReceived.setInternalPaymentId(paymentEntity.getInternalPaymentId());
        paymentReceived.setExternalPaymentId(paymentEntity.getExternalPaymentId());
        paymentReceived.setExternalId(paymentEntity.getExternalId());
        paymentReceived.setPayer(paymentEntity.getPayer());
        paymentReceived.setPaymentAmount(paymentEntity.getPaymentAmount());
        paymentReceived.setPaymentDate(paymentEntity.getPaymentDate());
        paymentReceived.setPaymentRequestDate(paymentEntity.getPaymentRequestDate());
        paymentReceived.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentReceived.setPaymentMethod(paymentEntity.getPaymentMethod());
        paymentReceived.setPaymentType(paymentEntity.getPaymentType());
        return paymentReceived;
    }

    public static PaymentRequest buildPaymentRequest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setExternalOrderId(RandomStringUtils.random(11, false, true));
        paymentRequest.setPayer(RandomStringUtils.randomAlphabetic(15));
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(Math.random() * 100));
        return paymentRequest;
    }

}
