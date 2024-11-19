package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.fiap.techchallenge.PaymentHelper.buildPaymentRequest;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreatePaymentUseCaseIT {

    @Autowired
    private CreatePaymentUseCase createPaymentUseCase;

    @Test
    void deveCriarPagamento() {
        PaymentRequest paymentRequest = buildPaymentRequest();
        createPaymentUseCase.execute(paymentRequest);

        Assertions.assertNotNull(paymentRequest);
    }

}
