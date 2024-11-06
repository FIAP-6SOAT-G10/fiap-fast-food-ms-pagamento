package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MercadoPagoOrderPaymentResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("payments")
    private List<MercadoPagoPaymentResponse> payments;

    @JsonProperty("total_amount")
    private double totalAmount;

    @JsonProperty("paid_amount")
    private double paidAmount;

    @JsonProperty("order_status")
    private String orderStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public List<MercadoPagoPaymentResponse> getPayments() {
        return payments;
    }

    public void setPayments(List<MercadoPagoPaymentResponse> payments) {
        this.payments = payments;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}