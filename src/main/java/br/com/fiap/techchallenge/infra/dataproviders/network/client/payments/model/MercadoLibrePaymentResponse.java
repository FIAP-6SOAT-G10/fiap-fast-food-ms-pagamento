package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MercadoLibrePaymentResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("total_amount")
    private double totalAmount;

    @JsonProperty("paid_amount")
    private double paidAmount;

    @JsonProperty("order_status")
    private String orderStatus;

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}