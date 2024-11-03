package br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "payer")
    private String payer;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_request_date")
    private LocalDateTime paymentRequestDate;

    @Column(name = "payment_status")
    private String paymentStatus;

}
