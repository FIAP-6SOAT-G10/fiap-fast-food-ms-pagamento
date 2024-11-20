package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultPaymentUseCaseIT {

    @Autowired
    private ConsultPaymentUseCase consultPaymentUseCase;

    @Test
    void deveRetornarPagamento_QuandoRealizarConsultaDePagamento() throws PaymentNotFoundException {
        String internalPaymentId = UUID.fromString("c1078c7e-d1f7-4490-853e-f7a4ded0fe3e").toString();
        Payment payment = consultPaymentUseCase.findPaymentById(internalPaymentId);

        assertNotNull(payment);
        assertEquals(internalPaymentId, payment.getInternalPaymentId());
    }

    @Test
    void deveLancarExcecao_QuandoRealizarConsultaDePagamento_IdentificadorNaoEncontrado() {
        String internalPaymentId = UUID.fromString("c1078c7e-d1f7-4490-853e-f7a4ded0fe3f").toString();

        assertThrows(PaymentNotFoundException.class, () -> consultPaymentUseCase.findPaymentById(internalPaymentId));
    }

}