package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.entities.pagamento.enums.PaymentStatusEnum;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentEntity;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentRedShiftRepository;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PaymentRepository implements IPaymentRepository {

    private final PaymentMapper paymentMapper;
    private final PaymentRedShiftRepository paymentRedShiftRepository;

    public PaymentRepository(PaymentMapper paymentMapper, PaymentRedShiftRepository paymentRedShiftRepository) {
        this.paymentMapper = paymentMapper;
        this.paymentRedShiftRepository = paymentRedShiftRepository;
    }

    @Override
    public void createPayment(PaymentRequest paymentRequest) {
        PaymentEntity paymentEntity = paymentMapper.fromDomainToEntity(paymentRequest);
        this.paymentRedShiftRepository.save(paymentEntity);
    }

    @Override
    public Payment savePayment(String externalOrderId) {
        log.info("Gravando pagamento {}",  externalOrderId);
        Optional<PaymentEntity> paymentEntityOptional = this.paymentRedShiftRepository.findByExternalId(externalOrderId);
        if (paymentEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();
        paymentEntity.setPaymentStatus(PaymentStatusEnum.WAITING_AUTHORIZATION.getNominalStatus());

        return paymentMapper.fromEntityToDomain(this.paymentRedShiftRepository.save(paymentEntity));
    }

    @Override
    public Payment findPayment(String internalPaymentId) {
        log.info("Recuperando pagamento do banco de dados");
        Optional<PaymentEntity> paymentEntityOptional = findPaymentByInternalPaymentId(internalPaymentId);
        if (paymentEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }

        return paymentMapper.fromEntityToDomain(paymentEntityOptional.get());
    }

    @Override
    public Payment finishPayment(Payment payment) {
        log.info("Finalizando o pagamento");
        Optional<PaymentEntity> paymentEntityOptional = findPaymentByInternalPaymentId(payment.getInternalPaymentId());
        if (paymentEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("Pagamento não encontrado");
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();
        paymentEntity.setExternalPaymentId(payment.getExternalPaymentId());
        paymentEntity.setPaymentStatus(payment.getPaymentStatus());
        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setPaymentMethod(payment.getPaymentMethod());
        paymentEntity.setPaymentType(payment.getPaymentType());

        return paymentMapper.fromEntityToDomain(this.paymentRedShiftRepository.save(paymentEntity));
    }

    private Optional<PaymentEntity> findPaymentByInternalPaymentId(String internalPaymentId) {
        return this.paymentRedShiftRepository.findById(internalPaymentId);
    }
}
