package br.com.fiap.techchallenge.domain.entities.pagamento.enums;

public enum PaymentStatusEnum {
    WAITING("waiting"),
    WAITING_AUTHORIZATION("waiting_authorization"),
    PAID("paid"),
    DENIED("denied");

    private final String nominalStatus;

    private PaymentStatusEnum(final String nominalStatus) {
        this.nominalStatus = nominalStatus;
    }

    public String getNominalStatus() {
        return nominalStatus;
    }
}
