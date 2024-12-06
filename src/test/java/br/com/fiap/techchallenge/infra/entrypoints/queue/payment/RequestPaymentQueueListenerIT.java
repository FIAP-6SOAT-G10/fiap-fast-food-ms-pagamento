package br.com.fiap.techchallenge.infra.entrypoints.queue.payment;

import br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model.PaymentRequestDTO;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RequestPaymentQueueListenerIT {

    @Autowired
    private RequestPaymentQueueListener requestPaymentQueueListener;

    @Test
    void deveProcessarSolicitacaoDePagamento() {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setExternalOrderId(RandomStringUtils.random(11, false, true));
        paymentRequestDTO.setPayer(RandomStringUtils.randomAlphabetic(22));
        paymentRequestDTO.setPaymentAmount(BigDecimal.valueOf(Math.random() * 100).setScale(2, RoundingMode.CEILING));

        requestPaymentQueueListener.listen(paymentRequestDTO);

        assertNotNull(paymentRequestDTO);
    }

}
