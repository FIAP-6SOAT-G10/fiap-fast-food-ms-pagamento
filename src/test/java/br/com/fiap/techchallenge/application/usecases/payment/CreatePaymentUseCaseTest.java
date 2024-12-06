package br.com.fiap.techchallenge.application.usecases.payment;

import br.com.fiap.techchallenge.PaymentHelper;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.gateways.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CreatePaymentUseCaseTest {

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    CreatePaymentUseCase createPaymentUseCase;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPagamento() {
        PaymentRequest paymentRequest = PaymentHelper.buildPaymentRequest();
        createPaymentUseCase.execute(paymentRequest);

        verify(paymentRepository, times(1)).createPayment(paymentRequest);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

}