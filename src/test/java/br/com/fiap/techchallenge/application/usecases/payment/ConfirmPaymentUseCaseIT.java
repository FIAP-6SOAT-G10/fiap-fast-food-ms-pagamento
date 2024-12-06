//package br.com.fiap.techchallenge.application.usecases.payment;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
//import br.com.fiap.techchallenge.domain.exceptions.PaymentAlreadyProcessedException;
//import br.com.fiap.techchallenge.domain.exceptions.PaymentNotFoundException;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("integration-test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class ConfirmPaymentUseCaseIT {
//
//    @Autowired
//    ConfirmPaymentUseCase confirmPaymentUseCase;
//
//    @Test
//    void deveRetornarNuloAoConfirmarPagamento_QuandoPagamentoNaoRealizado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
//        String resource = "/25012655721";
//        Payment payment = confirmPaymentUseCase.execute(resource);
//
//        assertNull(payment);
//    }
//
//    @Test
//    void deveLancarExcecaoAoConfirmarPagamento_QuandoPagamentoNaoIdentificado_IdentificadorNaoLocalizado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
//        String resource = "/25012655722";
//
//        assertThrows(Exception.class, () -> confirmPaymentUseCase.execute(resource));
//    }
//
//    @Test
//    void deveLancarExcecaoAoConfirmarPagamento_QuandoPagamentoJaProcessado_PagamentoEfetuado() {
//        String resource = "/25012952621";
//
//        assertThrows(PaymentAlreadyProcessedException.class, () -> confirmPaymentUseCase.execute(resource));
//    }
//
//    @Test
//    void deveLancarExcecaoAoConfirmarPagamento_QuandoPagamentoJaProcessado_PagamentoNegado() throws PaymentAlreadyProcessedException, PaymentNotFoundException {
//        String resource = "/25055072471";
//
//        assertNull(confirmPaymentUseCase.execute(resource));
//    }
//
//}
