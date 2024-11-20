package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MakePaymentUseCaseIT {

    @Autowired
    private MakePaymentUseCase makePaymentUseCase;

    @Test
    void deveRealizarPagamento_EAguardarAutorizacao() {
        String externalOrderId = "1234567893";
        Payment payment = makePaymentUseCase.execute(externalOrderId);

        assertNotNull(payment);
        assertEquals(externalOrderId, payment.getExternalId());
    }

}
