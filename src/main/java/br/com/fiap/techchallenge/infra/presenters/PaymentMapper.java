package br.com.fiap.techchallenge.infra.presenters;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.entities.pagamento.enums.PaymentStatusEnum;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model.PaymentRequestDTO;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentMapper {
    public PaymentRequest fromDataTransferObjetToDomain(PaymentRequestDTO paymentRequestDTO) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setExternalOrderId(paymentRequestDTO.getExternalOrderId());
        paymentRequest.setPayer(getPayer(paymentRequestDTO));
        paymentRequest.setPaymentAmount(paymentRequestDTO.getPaymentAmount());
        return paymentRequest;
    }

    private static String getPayer(PaymentRequestDTO paymentRequestDTO) {
        return paymentRequestDTO.getPayer() == null || paymentRequestDTO.getPayer().isEmpty() ? "desconhecido@desconhecido.com.br" : paymentRequestDTO.getPayer();
    }

    public PaymentEntity fromDomainToEntity(PaymentRequest paymentRequest) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setInternalPaymentId(UUID.randomUUID().toString());
        paymentEntity.setExternalId(paymentRequest.getExternalOrderId());
        paymentEntity.setPayer(paymentRequest.getPayer());
        paymentEntity.setPaymentAmount(paymentRequest.getPaymentAmount());
        paymentEntity.setPaymentRequestDate(LocalDateTime.now());
        paymentEntity.setPaymentStatus(PaymentStatusEnum.WAITING.getNominalStatus());
        return paymentEntity;
    }

    public Payment fromEntityToDomain(PaymentEntity paymentEntity) {
        Payment payment = new Payment();
        payment.setInternalPaymentId(paymentEntity.getInternalPaymentId());
        payment.setExternalPaymentId(paymentEntity.getExternalPaymentId());
        payment.setExternalId(paymentEntity.getExternalId());
        payment.setPayer(paymentEntity.getPayer());
        payment.setPaymentAmount(paymentEntity.getPaymentAmount());
        payment.setPaymentDate(paymentEntity.getPaymentDate());
        payment.setPaymentRequestDate(paymentEntity.getPaymentRequestDate());
        payment.setPaymentStatus(paymentEntity.getPaymentStatus());
        payment.setPaymentMethod(paymentEntity.getPaymentMethod());
        payment.setPaymentType(paymentEntity.getPaymentType());
        return payment;
    }

    public PaymentResponseDTO fromDomainToDataTransferObject(Payment payment) {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        paymentResponseDTO.setInternalPaymentId(payment.getInternalPaymentId());
        paymentResponseDTO.setExternalPaymentId(payment.getExternalPaymentId());
        paymentResponseDTO.setExternalId(payment.getExternalId());
        paymentResponseDTO.setPayer(payment.getPayer());
        paymentResponseDTO.setPaymentAmount(payment.getPaymentAmount());
        paymentResponseDTO.setPaymentDate(DateMapper.fromLocalDateTimeToStringWithFormat(payment.getPaymentDate(), "yyyy-MM-dd HH:mm:ss"));
        paymentResponseDTO.setPaymentRequestDate(DateMapper.fromLocalDateTimeToStringWithFormat(payment.getPaymentRequestDate(), "yyyy-MM-dd HH:mm:ss"));
        paymentResponseDTO.setPaymentStatus(payment.getPaymentStatus());
        paymentResponseDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentResponseDTO.setPaymentType(payment.getPaymentType());
        return paymentResponseDTO;
    }
}
