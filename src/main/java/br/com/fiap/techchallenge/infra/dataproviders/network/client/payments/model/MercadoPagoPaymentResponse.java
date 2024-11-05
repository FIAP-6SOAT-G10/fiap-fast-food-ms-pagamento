package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MercadoPagoPaymentResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
