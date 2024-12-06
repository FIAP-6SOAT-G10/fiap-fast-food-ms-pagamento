//package br.com.fiap.techchallenge.infra.gateways;
//
//import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentRequest;
//import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
//import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.PaymentEntity;
//import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.MercadoPagoClient;
//import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoOrderPaymentResponse;
//import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoPaymentDetailResponse;
//import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoPaymentRequest;
//import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoPaymentResponse;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.UUID;
//
//import static br.com.fiap.techchallenge.MercadoPagoHelper.buildMercadoPagoOrderPaymentResponse;
//import static br.com.fiap.techchallenge.PaymentHelper.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class MercadoPagoPaymentProviderRepositoryTest {
//
//    AutoCloseable autoCloseable;
//
//    @Mock
//    MercadoPagoClient mercadoPagoClient;
//
//    MercadoPagoPaymentProviderRepository mercadoPagoPaymentProviderRepository;
//
//    @BeforeEach
//    void setup() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
//        mercadoPagoPaymentProviderRepository = new MercadoPagoPaymentProviderRepository(mercadoPagoClient, "http://teste.com.br/api/payment/confirmation");
//    }
//
//    @Nested
//    class CriarPagamento {
//
//        @Test
//        void deveCriarPagamento() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment paymentSent = buildPayment(paymentEntity);
//            when(mercadoPagoClient.generatePaymentRequest(any(MercadoPagoPaymentRequest.class))).thenReturn(ResponseEntity.ok().build());
//
//            Payment paymentReturned = mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(paymentSent);
//
//            assertEquals(paymentSent.getInternalPaymentId(), paymentReturned.getInternalPaymentId());
//            assertEquals(paymentSent.getExternalPaymentId(), paymentReturned.getExternalPaymentId());
//            assertEquals(paymentSent.getExternalId(), paymentReturned.getExternalId());
//            assertEquals(paymentSent.getPayer(), paymentReturned.getPayer());
//            assertEquals(paymentSent.getPaymentAmount(), paymentReturned.getPaymentAmount());
//            assertEquals(paymentSent.getPaymentDate(), paymentReturned.getPaymentDate());
//            assertEquals(paymentSent.getPaymentRequestDate(), paymentReturned.getPaymentRequestDate());
//            assertEquals(paymentSent.getPaymentStatus(), paymentReturned.getPaymentStatus());
//            assertEquals(paymentSent.getPaymentMethod(), paymentReturned.getPaymentMethod());
//            assertEquals(paymentSent.getPaymentType(), paymentReturned.getPaymentType());
//
//            verify(mercadoPagoClient, times(1)).generatePaymentRequest(any(MercadoPagoPaymentRequest.class));
//        }
//
//        @Test
//        void deveLancarExcecao_QuandoPagamentoNaoForGerado_ErroNoProvedorDePagamento() {
//            PaymentRequest paymentRequest = buildPaymentRequest();
//            PaymentEntity paymentEntity = buildPaymentEntity(UUID.randomUUID().toString(), paymentRequest);
//            Payment paymentSent = buildPayment(paymentEntity);
//            when(mercadoPagoClient.generatePaymentRequest(any(MercadoPagoPaymentRequest.class))).thenReturn(ResponseEntity.internalServerError().build());
//
//            Exception exception = assertThrows(RuntimeException.class, () -> mercadoPagoPaymentProviderRepository.createPaymentRequestOnPaymentProvider(paymentSent));
//
//            assertEquals("Erro ao gerar o pagamento com o provedor de pagamentos", exception.getMessage());
//        }
//
//    }
//
//    @Nested
//    class ConsultarPagamento {
//
//        @Test
//        void deveConsultarPagamento() {
//            String resource = "/9999999991";
//            MercadoPagoPaymentResponse payment = new MercadoPagoPaymentResponse();
//            payment.setId(123456789L);
//            MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse = buildMercadoPagoOrderPaymentResponse(List.of(payment));
//            when(mercadoPagoClient.consultOrderDetails(anyString())).thenReturn(ResponseEntity.ok(mercadoPagoOrderPaymentResponse));
//
//            PaymentResponse paymentResponse = mercadoPagoPaymentProviderRepository.consultPayment(resource);
//
//            assertEquals(9999999991L, paymentResponse.getId());
//            assertEquals("closed", paymentResponse.getStatus());
//            assertEquals("9999999992", paymentResponse.getExternalReference());
//            assertEquals(123456789L, paymentResponse.getExternalPaymentId());
//            assertEquals(199.9, paymentResponse.getTotalAmount());
//            assertEquals(199.9, paymentResponse.getPaidAmount());
//            assertEquals("closed", paymentResponse.getOrderStatus());
//        }
//
//        @Test
//        void deveConsultarPagamento_QuandoPagamentoPendente_RetornarVazioParaPagamentoAindaNaoConfirmado() {
//            String resource = "/9999999991";
//            MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse = new MercadoPagoOrderPaymentResponse();
//            mercadoPagoOrderPaymentResponse.setStatus("open");
//            when(mercadoPagoClient.consultOrderDetails(anyString())).thenReturn(ResponseEntity.ok(mercadoPagoOrderPaymentResponse));
//
//            PaymentResponse paymentResponse = mercadoPagoPaymentProviderRepository.consultPayment(resource);
//
//            assertNull(paymentResponse);
//        }
//
//        @Test
//        void deveConsultarPagamento_QuandoPagamentoPendente_RetornarZeroNoIdDoPagamento() {
//            String resource = "/9999999991";
//            List payments = mock(List.class);
//            MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse = buildMercadoPagoOrderPaymentResponse(payments);
//            when(mercadoPagoClient.consultOrderDetails(anyString())).thenReturn(ResponseEntity.ok(mercadoPagoOrderPaymentResponse));
//            when(payments.get(0)).thenReturn(null);
//
//            PaymentResponse paymentResponse = mercadoPagoPaymentProviderRepository.consultPayment(resource);
//
//            assertEquals(9999999991L, paymentResponse.getId());
//            assertEquals("closed", paymentResponse.getStatus());
//            assertEquals("9999999992", paymentResponse.getExternalReference());
//            assertEquals(0L, paymentResponse.getExternalPaymentId());
//            assertEquals(199.9, paymentResponse.getTotalAmount());
//            assertEquals(199.9, paymentResponse.getPaidAmount());
//            assertEquals("closed", paymentResponse.getOrderStatus());
//        }
//
//        @Test
//        void deveConsultarPagamento_QuandoPagamentoPendente_RetornarNuloParaPagamentoAindaNaoConfirmado() {
//            String resource = "/9999999991";
//            when(mercadoPagoClient.consultOrderDetails(anyString())).thenReturn(ResponseEntity.ok(null));
//
//            PaymentResponse paymentResponse = mercadoPagoPaymentProviderRepository.consultPayment(resource);
//
//            assertNull(paymentResponse);
//        }
//
//    }
//
//    @Nested
//    class ConsultarFormaDePagamento {
//
//        @Test
//        void deveConsultarFormaDePagamento() {
//            PaymentResponse payment = new PaymentResponse();
//            payment.setExternalPaymentId(9999999991L);
//            MercadoPagoPaymentDetailResponse mercadoPagoPaymentDetailResponse = new MercadoPagoPaymentDetailResponse();
//            mercadoPagoPaymentDetailResponse.setId(9999999991L);
//            mercadoPagoPaymentDetailResponse.setPaymentMethodId("visa");
//            mercadoPagoPaymentDetailResponse.setPaymentTypeId("credit");
//            when(mercadoPagoClient.consultPaymentMethod(payment.getExternalPaymentId())).thenReturn(ResponseEntity.ok(mercadoPagoPaymentDetailResponse));
//
//            PaymentResponse paymentResponse = mercadoPagoPaymentProviderRepository.consultPaymentMethod(payment);
//
//            assertEquals(9999999991L, paymentResponse.getExternalPaymentId());
//            assertEquals("visa", paymentResponse.getPaymentMethod());
//            assertEquals("credit", paymentResponse.getPaymentType());
//        }
//
//        @Test
//        void deveConsultarFormaDePagamento_QuandoFormaDePagamentoIndisponivel_RetornarDadosBasicos() {
//            PaymentResponse payment = new PaymentResponse();
//            payment.setExternalPaymentId(9999999991L);
//            when(mercadoPagoClient.consultPaymentMethod(payment.getExternalPaymentId())).thenReturn(ResponseEntity.ok(null));
//
//            PaymentResponse paymentResponse = mercadoPagoPaymentProviderRepository.consultPaymentMethod(payment);
//
//            assertEquals(9999999991L, paymentResponse.getExternalPaymentId());
//            assertNull(paymentResponse.getPaymentMethod());
//            assertNull(paymentResponse.getPaymentType());
//        }
//
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }
//
//}
