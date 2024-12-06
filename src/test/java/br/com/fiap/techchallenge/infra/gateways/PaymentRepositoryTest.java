package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.domain.entities.pagamento.enums.PaymentStatusEnum;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentRedShiftRepository;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.techchallenge.PaymentHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaymentRepositoryTest {

    AutoCloseable autoCloseable;

    @Mock
    PaymentRedShiftRepository paymentRedShiftRepository;

    @Mock
    PaymentMapper paymentMapper;

    @InjectMocks
    PaymentRepository paymentRepository;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CriarPagamento {

        @Test
        void deveCriarPagamento() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            when(paymentMapper.fromDomainToEntity(paymentRequest)).thenReturn(mock(PaymentEntity.class));
            when(paymentRedShiftRepository.save(any(PaymentEntity.class))).then(arguments -> arguments.getArgument(0));

            paymentRepository.createPayment(paymentRequest);

            verify(paymentRedShiftRepository, times(1)).save(any(PaymentEntity.class));
        }
    }
    @Nested
    class AtualizarPagamento {

        @Test
        void deveGravarPagamento() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment paymentReceived = buildPayment(paymentEntity);
            when(paymentRedShiftRepository.findByExternalId(paymentRequest.getExternalOrderId())).thenReturn(Optional.of(paymentEntity));
            when(paymentRedShiftRepository.save(paymentEntity)).thenAnswer(arguments -> arguments.getArgument(0));
            when(paymentMapper.fromEntityToDomain(paymentEntity)).thenReturn(paymentReceived);

            Payment paymentSaved = paymentRepository.savePayment(paymentRequest.getExternalOrderId());

            assertEquals(paymentEntity.getExternalId(), paymentSaved.getExternalId());
            assertEquals(paymentEntity.getPayer(), paymentSaved.getPayer());
            assertEquals(paymentEntity.getPaymentAmount(), paymentSaved.getPaymentAmount());
            assertEquals(PaymentStatusEnum.WAITING_AUTHORIZATION.getNominalStatus(), paymentSaved.getPaymentStatus());
            assertEquals(paymentEntity.getInternalPaymentId(), paymentSaved.getInternalPaymentId());
            verify(paymentRedShiftRepository, times(1)).save(paymentEntity);
        }
        @Test
        void deveLancarExcecao_AoGravarPagamento_QuandoPagamentoNaoForEncontrado_IdentificadorExternoNaoExiste() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            when(paymentRedShiftRepository.findByExternalId(paymentEntity.getExternalId())).thenReturn(Optional.empty());

            String externalOrderId = paymentRequest.getExternalOrderId();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> paymentRepository.savePayment(externalOrderId));
            assertEquals("Pagamento não encontrado", exception.getMessage());
            verify(paymentRedShiftRepository, never()).save(any(PaymentEntity.class));
        }

    }
    @Nested
    class ListarPagamento {

        @Test
        void deveRecuperarPagamento() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            when(paymentRedShiftRepository.findById(paymentEntity.getInternalPaymentId())).thenReturn(Optional.of(paymentEntity));
            when(paymentMapper.fromEntityToDomain(paymentEntity)).thenReturn(payment);

            Payment paymentRecovered = paymentRepository.findPayment(paymentEntity.getInternalPaymentId());

            assertEquals(paymentEntity.getInternalPaymentId(), paymentRecovered.getInternalPaymentId());
            assertEquals(paymentEntity.getExternalId(), paymentRecovered.getExternalId());
            assertEquals(paymentEntity.getPayer(), paymentRecovered.getPayer());
            assertEquals(paymentEntity.getPaymentAmount(), paymentRecovered.getPaymentAmount());
        }
        @Test
        void deveLancarExcecao_AoRecuperarPagamento_QuandoPagamentoNaoForEncontrado_IdentificadorInternoNaoExiste() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            String internalPaymentId = paymentEntity.getInternalPaymentId();
            when(paymentRedShiftRepository.findById(internalPaymentId)).thenReturn(Optional.empty());

            Exception exception = assertThrows(IllegalArgumentException.class, () -> paymentRepository.findPayment(internalPaymentId));

            assertEquals("Pagamento não encontrado", exception.getMessage());
            verify(paymentMapper, never()).fromEntityToDomain(any(PaymentEntity.class));
        }


    }
    @Nested
    class FinalizarPagamento {
        @Test
        void deveFinalizarPagamento() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment paymentSend = buildPayment(paymentEntity);

            when(paymentRedShiftRepository.findById(paymentEntity.getInternalPaymentId())).thenReturn(Optional.of(paymentEntity));
            when(paymentRedShiftRepository.save(paymentEntity)).thenAnswer(arguments -> arguments.getArgument(0));
            when(paymentMapper.fromEntityToDomain(paymentEntity)).thenReturn(paymentSend);

            Payment paymentRecovered = paymentRepository.finishPayment(paymentSend);

            verify(paymentRedShiftRepository, times(1)).findById(paymentEntity.getInternalPaymentId());
            verify(paymentRedShiftRepository, times(1)).save(paymentEntity);
            verify(paymentMapper, times(1)).fromEntityToDomain(paymentEntity);

            assertEquals(paymentSend.getInternalPaymentId(), paymentRecovered.getInternalPaymentId());
            assertEquals(paymentSend.getPayer(), paymentRecovered.getPayer());
            assertEquals(paymentSend.getPaymentAmount(), paymentRecovered.getPaymentAmount());
            assertEquals(paymentSend.getExternalId(), paymentRecovered.getExternalId());
        }

        @Test
        void deveLancarExcecao_AoFinalizarPagamento_QuandoPagamentoNaoForEncontrado_IdentificadorInternoNaoExiste() {
            PaymentRequest paymentRequest = buildPaymentRequest();
            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
            Payment payment = buildPayment(paymentEntity);
            when(paymentRedShiftRepository.findById(paymentEntity.getInternalPaymentId())).thenReturn(Optional.empty());

            Exception exception = assertThrows(IllegalArgumentException.class, () -> paymentRepository.finishPayment(payment));

            assertEquals("Pagamento não encontrado", exception.getMessage());

            verify(paymentRedShiftRepository, never()).save(paymentEntity);
        }

    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

}