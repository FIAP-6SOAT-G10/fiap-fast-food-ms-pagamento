package br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @Column(name = "internal_payment_id")
    private String internalPaymentId;

    @Column(name = "external_payment_id")
    private Long externalPaymentId;

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

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_type")
    private String paymentType;

    public String getInternalPaymentId() {
        return internalPaymentId;
    }

    public void setInternalPaymentId(String internalPaymentId) {
        this.internalPaymentId = internalPaymentId;
    }

    public Long getExternalPaymentId() {
        return externalPaymentId;
    }

    public void setExternalPaymentId(Long externalPaymentId) {
        this.externalPaymentId = externalPaymentId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getPaymentRequestDate() {
        return paymentRequestDate;
    }

    public void setPaymentRequestDate(LocalDateTime paymentRequestDate) {
        this.paymentRequestDate = paymentRequestDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
