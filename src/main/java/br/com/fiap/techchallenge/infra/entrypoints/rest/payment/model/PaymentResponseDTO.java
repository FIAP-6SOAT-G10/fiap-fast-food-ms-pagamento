package br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model;

import java.math.BigDecimal;

public class PaymentResponseDTO {
    private String internalPaymentId;
    private Long externalPaymentId;
    private String externalId;
    private String payer;
    private BigDecimal paymentAmount;
    private String paymentDate;
    private String paymentRequestDate;
    private String paymentStatus;
    private String paymentMethod;
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

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentRequestDate() {
        return paymentRequestDate;
    }

    public void setPaymentRequestDate(String paymentRequestDate) {
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
