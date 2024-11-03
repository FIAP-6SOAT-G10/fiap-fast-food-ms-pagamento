package br.com.fiap.techchallenge.domain.exceptions;

import br.com.fiap.techchallenge.domain.utils.ConstantUtils;

public class PaymentAlreadyProcessedException extends Throwable {

    public PaymentAlreadyProcessedException() {
        super(ConstantUtils.PAYMENT_ALREADY_PROCESSED_ERROR);
    }

}