package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
import br.com.fiap.techchallenge.infra.gateways.MercadoPagoPaymentProviderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerifyPaymentUseCaseTest {

    @Mock
    MercadoPagoPaymentProviderRepository mercadoPagoPaymentProviderRepository;

    @InjectMocks
    VerifyPaymentUseCase verifyPaymentUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConsultarPagamento_QuandoDetalhesDoPagamentoDisponivel() {
        String resource = "/9999999991";
        PaymentResponse paymentResponse = new PaymentResponse();
        when(mercadoPagoPaymentProviderRepository.consultPayment(resource)).thenReturn(paymentResponse);
        when(mercadoPagoPaymentProviderRepository.consultPaymentMethod(paymentResponse)).thenReturn(paymentResponse);

        PaymentResponse paymentResponseReceived = verifyPaymentUseCase.execute(resource);

        assertNotNull(paymentResponseReceived);
        verify(mercadoPagoPaymentProviderRepository, times(1)).consultPayment(resource);
        verify(mercadoPagoPaymentProviderRepository, times(1)).consultPaymentMethod(paymentResponse);
    }

    @Test
    void deveConsultarPagamento_QuandoDetalhesDoPagamentoIndisponivel() {
        String resource = "/9999999991";
        when(mercadoPagoPaymentProviderRepository.consultPayment(resource)).thenReturn(null);

        PaymentResponse paymentResponseReceived = verifyPaymentUseCase.execute(resource);

        assertNull(paymentResponseReceived);
        verify(mercadoPagoPaymentProviderRepository, times(1)).consultPayment(resource);
        verify(mercadoPagoPaymentProviderRepository, never()).consultPaymentMethod(any(PaymentResponse.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

}
