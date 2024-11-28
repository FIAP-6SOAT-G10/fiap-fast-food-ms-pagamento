package br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    private String externalOrderId;
    private String payer;
    private BigDecimal paymentAmount;

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
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

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "externalOrderId='" + externalOrderId + '\'' +
                ", payer='" + payer + '\'' +
                ", paymentAmount=" + paymentAmount +
                '}';
    }
}