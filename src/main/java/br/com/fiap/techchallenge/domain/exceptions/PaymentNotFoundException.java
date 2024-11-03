package br.com.fiap.techchallenge.domain.exceptions;

public class PaymentNotFoundException extends Throwable {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
