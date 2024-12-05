package br.com.fiap.techchallenge.infra.entrypoints.queue.payment;

import br.com.fiap.techchallenge.application.usecases.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
import br.com.fiap.techchallenge.infra.entrypoints.queue.payment.model.PaymentRequestDTO;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestPaymentQueueListener {

    private final PaymentMapper paymentMapper;
    private final CreatePaymentUseCase createPaymentUseCase;

    @SqsListener("${aws.sqs.payment-requests-queue}")
    public void listen(PaymentRequestDTO paymentRequestDTO) {
        log.info("Mensagem recebida da fila {}", paymentRequestDTO);

        PaymentRequest paymentRequest = paymentMapper.fromDataTransferObjetToDomain(paymentRequestDTO);
        createPaymentUseCase.execute(paymentRequest);
    }

}
