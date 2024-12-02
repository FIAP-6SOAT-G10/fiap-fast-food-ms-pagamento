package br.com.fiap.techchallenge.infra.exception;

import br.com.fiap.techchallenge.domain.ErrosEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String message;
    private final ErrosEnum error;
    private final HttpStatus httpStatusCode;

    protected BaseException(ErrosEnum error, String... violations) {
        super(String.format(error.getMessage(), (Object) violations));
        this.error = error;
        this.message = error.getMessage();
        this.httpStatusCode = error.getHttpStatusCode();
    }
}
