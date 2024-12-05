package br.com.fiap.techchallenge.infra.entrypoints.queue.payment;

import br.com.fiap.techchallenge.application.usecases.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model.PaymentRequestDTO;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class RequestPaymentQueueListenerTest {

    @Mock
    PaymentMapper paymentMapper;

    @Mock
    CreatePaymentUseCase createPaymentUseCase;

    @InjectMocks
    RequestPaymentQueueListener requestPaymentQueueListener;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveProcessarSolicitacaoDePagamento() {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setExternalOrderId(RandomStringUtils.random(11, false, true));
        paymentRequestDTO.setPayer(RandomStringUtils.randomAlphabetic(15));
        paymentRequestDTO.setPaymentAmount(BigDecimal.valueOf(Math.random() * 100));
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setExternalOrderId(paymentRequestDTO.getExternalOrderId());
        paymentRequest.setPayer(paymentRequestDTO.getPayer());
        paymentRequest.setPaymentAmount(paymentRequestDTO.getPaymentAmount());
        when(paymentMapper.fromDataTransferObjetToDomain(paymentRequestDTO)).thenReturn(paymentRequest);

        Map<String, String> map = new HashMap<>();
        requestPaymentQueueListener.listen(map, paymentRequestDTO);

        verify(paymentMapper, times(1)).fromDataTransferObjetToDomain(paymentRequestDTO);
        verify(createPaymentUseCase, times(1)).execute(any(PaymentRequest.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
}
