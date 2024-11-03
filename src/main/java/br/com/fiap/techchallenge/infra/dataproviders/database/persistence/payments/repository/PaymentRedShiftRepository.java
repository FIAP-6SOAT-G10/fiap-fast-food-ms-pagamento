package br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRedShiftRepository extends JpaRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByExternalId(String externalOrderId);
}
