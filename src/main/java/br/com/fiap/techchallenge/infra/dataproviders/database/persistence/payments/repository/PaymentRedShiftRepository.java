package br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository;

import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRedShiftRepository extends JpaRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByExternalId(String externalOrderId);
}
