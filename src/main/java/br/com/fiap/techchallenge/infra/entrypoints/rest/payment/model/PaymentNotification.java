package br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model;

public class PaymentNotification {
    private PaymentData data;
    private String action;
    private String type;

    public PaymentData getData() {
        return data;
    }

    public void setData(PaymentData data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
