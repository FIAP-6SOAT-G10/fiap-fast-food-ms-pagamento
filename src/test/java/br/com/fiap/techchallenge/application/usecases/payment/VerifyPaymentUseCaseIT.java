//package br.com.fiap.techchallenge.application.usecases.payment;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("integration-test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class VerifyPaymentUseCaseIT {
//
//    @Autowired
//    private VerifyPaymentUseCase verifyPaymentUseCase;
//
//    @Test
//    void deveConsultarPagamento_QuandoDetalhesDoPagamentoDisponivel() {
//        PaymentResponse paymentResponse = verifyPaymentUseCase.execute("25069761168");
//
//        assertNotNull(paymentResponse);
//        assertEquals("amex", paymentResponse.getPaymentMethod());
//        assertEquals("credit_card", paymentResponse.getPaymentType());
//    }
//
//    @Test
//    void deveConsultarPagamento_QuandoDetalhesDoPagamentoIndisponivel() {
//        PaymentResponse paymentResponse = verifyPaymentUseCase.execute("25152311115");
//
//        assertNull(paymentResponse);
//    }
//
//}
