package br.com.fiap.techchallenge.domain.entities.pagamento;

public enum PaymentStatusEnum {
    WAITING("waiting"),
    WAITING_AUTHORIZATION("waiting_authorization"),
    AUTHORIZED("authorized"),
    DENIED("denied");

    private final String nominalStatus;

    private PaymentStatusEnum(final String nominalStatus) {
        this.nominalStatus = nominalStatus;
    }

    public String getNominalStatus() {
        return nominalStatus;
    }
}
