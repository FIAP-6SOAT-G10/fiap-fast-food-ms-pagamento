package br.com.fiap.techchallenge.infra.entrypoints.rest.payment;

import br.com.fiap.techchallenge.application.usecases.payment.ConfirmPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.MakePaymentUseCase;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentNotification;
import br.com.fiap.techchallenge.infra.entrypoints.rest.payment.model.PaymentResponse;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Payment", description = "Conjunto de operações que podem ser realizadas no contexto do webhook.")
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final MakePaymentUseCase makePaymentUseCase;
    private final ConfirmPaymentUseCase confirmPaymentUseCase;

    private final PaymentMapper paymentMapper;

    @PostMapping(path = "/{externalOrderId}/checkout")
    public ResponseEntity<PaymentResponse> makePayment(@PathVariable("externalOrderId") String externalOrderId) {
        try {
            Payment payment = makePaymentUseCase.execute(externalOrderId);
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentMapper.fromDomainToDataTransferObject(payment));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/confirmation")
    public ResponseEntity<?> receivePaymentConfirmation(@RequestBody PaymentNotification paymentNotification) {
        Payment payment = null;
        try {
            payment = confirmPaymentUseCase.execute(paymentNotification.getData().getId(), paymentNotification.getData().getStatus());
        } catch (PaymentAlreadyProcessedException paymentAlreadyProcessedException) {
            return ResponseEntity.badRequest().body(paymentAlreadyProcessedException.getMessage());
        } catch (PaymentNotFoundException paymentNotFoundException) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.fromDomainToDataTransferObject(payment));
    }

}
