package br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model;

public class PaymentNotification {
    private String resource;
    private String topic;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "PaymentNotification{" +
                "resource='" + resource + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
