package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
import br.com.fiap.techchallenge.infra.gateways.MercadoPagoPaymentProviderRepository;
import br.com.fiap.techchallenge.infra.gateways.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static br.com.fiap.techchallenge.PaymentHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MakePaymentUseCaseTest {

    @Mock
    MercadoPagoPaymentProviderRepository mercadoPagoPaymentProviderRepository;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    MakePaymentUseCase makePaymentUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRealizarPagamento_EAguardarAutorizacao() {
        PaymentRequest paymentRequest = buildPaymentRequest();
        PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
        Payment payment = buildPayment(paymentEntity);
        when(paymentRepository.savePayment(payment.getExternalId())).thenReturn(payment);
        when(mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(payment)).thenReturn(payment);

        Payment paymentMade = makePaymentUseCase.execute(payment.getExternalId());

        assertEquals(payment.getExternalId(), paymentMade.getExternalId());
        assertEquals(payment.getPaymentStatus(), paymentMade.getPaymentStatus());
        verify(paymentRepository, times(1)).savePayment(payment.getExternalId());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

}
