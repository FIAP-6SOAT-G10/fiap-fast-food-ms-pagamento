package br.com.fiap.techchallenge.infra.entrypoints.rest.payment;

import br.com.fiap.techchallenge.application.usecases.payment.ConfirmPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.ConsultPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.MakePaymentUseCase;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentNotification;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentResponseDTO;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.fiap.techchallenge.infra.utils.ConstantUtil.TOPIC_MERCHANT_ORDER;

@Slf4j
@RestController
@Tag(name = "Payment", description = "Conjunto de operações que podem ser realizadas no contexto do webhook.")
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final MakePaymentUseCase makePaymentUseCase;
    private final ConfirmPaymentUseCase confirmPaymentUseCase;
    private final ConsultPaymentUseCase consultPaymentUseCase;

    private final PaymentMapper paymentMapper;

    @PostMapping(path = "/{externalOrderId}/checkout")
    public ResponseEntity<PaymentResponseDTO> makePayment(@PathVariable("externalOrderId") String externalOrderId) {
        log.info("Criar ordem de pagamento para o pedido {}", externalOrderId);
        try {
            Payment payment = makePaymentUseCase.execute(externalOrderId);
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentMapper.fromDomainToDataTransferObject(payment));
        } catch (Exception exception) {
            log.error("Erro genérico", exception);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/confirmation")
    public ResponseEntity<?> receivePaymentConfirmation(@RequestBody PaymentNotification paymentNotification) {
        log.info("Estímulo recebido via Webhook {}", paymentNotification);
        if (isCallbackFromMerchantOrderTopic(paymentNotification)) {
            try {
                Payment payment = confirmPaymentUseCase.execute(paymentNotification.getResource());
                if (payment == null) {
                    return null;
                }
                return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.fromDomainToDataTransferObject(payment));
            } catch (PaymentAlreadyProcessedException paymentAlreadyProcessedException) {
                return ResponseEntity.badRequest().body(paymentAlreadyProcessedException.getMessage());
            } catch (PaymentNotFoundException paymentNotFoundException) {
                return ResponseEntity.notFound().build();
            }
        }
        return null;
    }

    @GetMapping(path = "/{internalPaymentId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByInternalPaymentId(@PathVariable("internalPaymentId") String internalPaymentId) {
        log.info("Consultando pagamento {}", internalPaymentId);
        Payment payment = null;
        try {
            payment = consultPaymentUseCase.findPaymentById(internalPaymentId);
        } catch (PaymentNotFoundException paymentNotFoundException) {
            log.error(paymentNotFoundException.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.fromDomainToDataTransferObject(payment));
    }

    private boolean isCallbackFromMerchantOrderTopic(PaymentNotification paymentNotification) {
        return paymentNotification.getTopic() != null && paymentNotification.getTopic().equalsIgnoreCase(TOPIC_MERCHANT_ORDER);
    }

}
