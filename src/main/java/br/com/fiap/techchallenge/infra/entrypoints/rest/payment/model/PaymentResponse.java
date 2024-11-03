package br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model;

import java.math.BigDecimal;

public class PaymentResponse {
    private String externalOrderId;
    private String internalPaymentId;
    private String payer;
    private BigDecimal paymentAmount;
    private String paymentDate;
    private String paymentRequestDate;
    private String paymentStatus;

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public String getInternalPaymentId() {
        return internalPaymentId;
    }

    public void setInternalPaymentId(String internalPaymentId) {
        this.internalPaymentId = internalPaymentId;
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
}
