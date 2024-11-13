package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentRedShiftRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.fiap.techchallenge.PaymentHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryIT {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentRedShiftRepository paymentRedShiftRepository;

    @Test
    void deveCriarTabela() {
        long totalRecords = paymentRedShiftRepository.count();
        assertThat(totalRecords).isNotNegative();
    }

    @Nested
    class AtualizarPagamento {

        @Test
        void deveGravarPagamento() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            Payment paymentCreated = criarPagamento(paymentRequest);

            Payment payment = paymentRepository.savePayment(paymentRequest.getExternalOrderId());

            assertEquals(paymentCreated.getInternalPaymentId(), payment.getInternalPaymentId());
            assertEquals(paymentCreated.getExternalPaymentId(), payment.getExternalPaymentId());
            assertEquals(paymentCreated.getExternalId(), payment.getExternalId());
            assertEquals(paymentCreated.getPayer(), payment.getPayer());
            assertEquals(paymentCreated.getPaymentAmount(), payment.getPaymentAmount());
            assertEquals(paymentCreated.getPaymentDate(), payment.getPaymentDate());
            assertEquals(paymentCreated.getPaymentRequestDate(), payment.getPaymentRequestDate());
            assertEquals("waiting_authorization", payment.getPaymentStatus());
            assertEquals(paymentCreated.getPaymentMethod(), payment.getPaymentMethod());
            assertEquals(paymentCreated.getPaymentType(), payment.getPaymentType());
        }

        @Test
        void deveLancarExcecao_AoGravarPagamento_QuandoPagamentoNaoForEncontrado_IdentificadorExternoNaoExiste() {
            assertThrows(IllegalArgumentException.class, () -> paymentRepository.savePayment(RandomStringUtils.randomNumeric(11)));
        }

    }

    @Nested
    class CriarPagamento {

        @Test
        void deveCriarPagamento() {
            PaymentRequest paymentRequest = buildPaymentRequest();

            criarPagamento(paymentRequest);

            assertEquals(1, paymentRedShiftRepository.count());
        }

    }

    private Payment criarPagamento(PaymentRequest paymentRequest) {
        return paymentRepository.createPayment(paymentRequest);
    }

    @Nested
    class FinalizarPagamento {

        @Test
        void deveFinalizarPagamento() {
            Payment paymentCreated = criarPagamento(buildPaymentRequest());
            Payment paymentSaved = paymentRepository.savePayment(paymentCreated.getExternalId());
            paymentSaved.setPaymentStatus("paid");
            paymentSaved.setPaymentDate(LocalDateTime.now());
            paymentSaved.setPaymentMethod("credit");
            paymentSaved.setPaymentType("visa");

            Payment paymentFinished = paymentRepository.finishPayment(paymentSaved);

            assertNotNull(paymentFinished);
            assertEquals(paymentSaved.getInternalPaymentId(), paymentFinished.getInternalPaymentId());
            assertEquals(paymentSaved.getPaymentStatus(), paymentFinished.getPaymentStatus());
            assertEquals(paymentSaved.getPaymentDate(), paymentFinished.getPaymentDate());
            assertEquals(paymentSaved.getPaymentMethod(), paymentFinished.getPaymentMethod());
            assertEquals(paymentSaved.getPaymentType(), paymentFinished.getPaymentType());
        }

        @Test
        void deveLancarExcecao_AoFinalizarPagamento_QuandoPagamentoNaoForEncontrado_IdentificadorInternoNaoExiste() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment anotherPayment = buildPayment(paymentEntity);

            assertThrows(IllegalArgumentException.class, () -> paymentRepository.finishPayment(anotherPayment));
        }

    }

    @Nested
    class ListarPagamento {

        @Test
        void deveRecuperarPagamento() {
            Payment paymentCreated = criarPagamento(buildPaymentRequest());
            Payment paymentReceived = paymentRepository.findPayment(paymentCreated.getInternalPaymentId());

            assertNotNull(paymentReceived);
        }

        @Test
        void deveLancarExcecao_AoRecuperarPagamento_QuandoPagamentoNaoForEncontrado_IdentificadorInternoNaoExiste() {
            assertThrows(IllegalArgumentException.class, () -> paymentRepository.findPayment(UUID.randomUUID().toString()));
        }

    }

}