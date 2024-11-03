package br.com.fiap.techchallenge.infra.presenters;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentStatusEnum;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentEntity;
import br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model.PaymentRequestDTO;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PaymentMapper {
    public PaymentRequest fromDataTransferObjetToDomain(PaymentRequestDTO paymentRequestDTO) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setExternalOrderId(paymentRequestDTO.getExternalOrderId());
        paymentRequest.setPayer(paymentRequestDTO.getPayer());
        paymentRequest.setPaymentAmount(paymentRequestDTO.getPaymentAmount());
        return paymentRequest;
    }

    public PaymentEntity fromDomainToEntity(PaymentRequest paymentRequest) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setId(UUID.randomUUID().toString());
        paymentEntity.setExternalId(paymentRequest.getExternalOrderId());
        paymentEntity.setPayer(paymentRequest.getPayer());
        paymentEntity.setPaymentAmount(paymentRequest.getPaymentAmount());
        paymentEntity.setPaymentRequestDate(LocalDateTime.now());
        paymentEntity.setPaymentStatus(PaymentStatusEnum.WAITING.getNominalStatus());
        return paymentEntity;
    }

    public Payment fromEntityToDomain(PaymentEntity paymentEntity) {
        Payment payment = new Payment();
        payment.setInternalPaymentId(paymentEntity.getId());
        payment.setExternalOrderId(paymentEntity.getExternalId());
        payment.setPayer(paymentEntity.getPayer());
        payment.setPaymentAmount(paymentEntity.getPaymentAmount());
        payment.setPaymentDate(paymentEntity.getPaymentDate());
        payment.setPaymentRequestDate(paymentEntity.getPaymentRequestDate());
        payment.setPaymentDate(paymentEntity.getPaymentDate());
        payment.setPaymentRequestDate(paymentEntity.getPaymentRequestDate());
        payment.setPaymentStatus(paymentEntity.getPaymentStatus());
        return payment;
    }

    public PaymentResponse fromDomainToDataTransferObject(Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setExternalOrderId(payment.getExternalOrderId());
        paymentResponse.setInternalPaymentId(payment.getInternalPaymentId());
        paymentResponse.setPayer(payment.getPayer());
        paymentResponse.setPaymentAmount(payment.getPaymentAmount());
        paymentResponse.setPaymentDate(DateMapper.fromLocalDateTimeToStringWithFormat(payment.getPaymentDate(), "yyyy-MM-dd HH:mm:ss"));
        paymentResponse.setPaymentRequestDate(DateMapper.fromLocalDateTimeToStringWithFormat(payment.getPaymentRequestDate(), "yyyy-MM-dd HH:mm:ss"));
        paymentResponse.setPaymentStatus(payment.getPaymentStatus());
        return paymentResponse;
    }
}
