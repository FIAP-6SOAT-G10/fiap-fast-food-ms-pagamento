package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.application.gateways.IPaymentProviderRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.domain.entities.pagamento.PaymentResponse;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.MercadoPagoClient;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Slf4j
public class MercadoPagoPaymentProviderRepository implements IPaymentProviderRepository {

    private static final String PAYMENT_TITLE = "Pagamento";
    private static final String PAYMENT_CATEGORY = "payment";
    private static final int QUANTITY = 1;
    private static final String UNIT = "unit";
    private static final String ORDER_PAYMENT_STATUS_CLOSED = "closed";

    private final MercadoPagoClient mercadoPagoClient;
    private final String notificationUrl;

    public MercadoPagoPaymentProviderRepository(MercadoPagoClient mercadoPagoClient, String notificationUrl) {
        this.mercadoPagoClient = mercadoPagoClient;
        this.notificationUrl = notificationUrl;
    }

    @Override
    public Payment createPaymentRequestOnPaymentProvider(Payment payment) {
        log.info("Criando solicitação de pagamento no provedor de pagamentos {}", payment.getInternalPaymentId());

        try {
            ResponseEntity<Void> response = mercadoPagoClient.generatePaymentRequest(buildMercadoPagoPaymentRequest(payment));
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Erro ao gerar o pagamento com o provedor de pagamentos");
            }

            log.info("Solicitação de pagamento gerada com sucesso");
            return payment;
        } catch (Exception exception) {
            log.error("Falha na integração com o meio de pagamento", exception);
            throw exception;
        }
    }

    @Override
    public PaymentResponse consultPayment(String resource) {
        log.info("Realizando consulta no provedor de pagamentos");
        String orderId = resource.substring(extractOrderIdFromResource(resource));
        ResponseEntity<MercadoPagoOrderPaymentResponse> response = mercadoPagoClient.consultOrderDetails(orderId);

        MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse = response.getBody();
        if (isPaymentNotConfirmed(mercadoPagoOrderPaymentResponse)) {
            log.info("Pagamento pendente");
            return null;
        }

        log.info("Pagamento confirmado pelo provedor");
        return createPaymentResponse(mercadoPagoOrderPaymentResponse);
    }

    @Override
    public PaymentResponse consultPaymentMethod(PaymentResponse payment) {
        log.info("Realizando consulta de detalhes do pagamento no provedor");
        ResponseEntity<MercadoPagoPaymentDetailResponse> response = mercadoPagoClient.consultPaymentMethod(payment.getExternalPaymentId());

        MercadoPagoPaymentDetailResponse mercadoPagoPaymentDetailResponse = response.getBody();
        if (mercadoPagoPaymentDetailResponse == null) {
            log.info("Detalhes do pagamento não disponível");
            return payment;
        }

        log.info("Detalhes do pagamento obtido");
        payment.setExternalPaymentId(mercadoPagoPaymentDetailResponse.getId());
        payment.setPaymentMethod(mercadoPagoPaymentDetailResponse.getPaymentMethodId());
        payment.setPaymentType(mercadoPagoPaymentDetailResponse.getPaymentTypeId());
        return payment;
    }

    private boolean isPaymentNotConfirmed(MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse) {
        return mercadoPagoOrderPaymentResponse == null || !mercadoPagoOrderPaymentResponse.getStatus().equalsIgnoreCase(ORDER_PAYMENT_STATUS_CLOSED);
    }

    private int extractOrderIdFromResource(String resource) {
        return resource.lastIndexOf('/') + 1;
    }

    private PaymentResponse createPaymentResponse(MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse) {
        log.info("Convertendo resposta do provedor para a entidade de domínio");
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(mercadoPagoOrderPaymentResponse.getId());
        paymentResponse.setStatus(mercadoPagoOrderPaymentResponse.getStatus());
        paymentResponse.setExternalReference(mercadoPagoOrderPaymentResponse.getExternalReference());
        paymentResponse.setExternalPaymentId(getExternalPaymentId(mercadoPagoOrderPaymentResponse.getPayments()));
        paymentResponse.setTotalAmount(mercadoPagoOrderPaymentResponse.getTotalAmount());
        paymentResponse.setPaidAmount(mercadoPagoOrderPaymentResponse.getPaidAmount());
        paymentResponse.setOrderStatus(mercadoPagoOrderPaymentResponse.getOrderStatus());
        return paymentResponse;
    }

    private long getExternalPaymentId(List<MercadoPagoPaymentResponse> payments) {
        MercadoPagoPaymentResponse mercadoPagoPaymentResponse = payments.get(0);
        if (mercadoPagoPaymentResponse == null) {
            return 0;
        }

        return mercadoPagoPaymentResponse.getId();
    }

    private MercadoPagoPaymentRequest buildMercadoPagoPaymentRequest(Payment payment) {
        MercadoPagoPaymentRequest mercadoPagoPaymentRequest = new MercadoPagoPaymentRequest();
        mercadoPagoPaymentRequest.setExternalReference(payment.getInternalPaymentId());
        mercadoPagoPaymentRequest.setTitle(PAYMENT_TITLE);
        mercadoPagoPaymentRequest.setDescription(createPaymentDescription(payment));
        mercadoPagoPaymentRequest.setNotificationURL(notificationUrl);
        mercadoPagoPaymentRequest.setTotalAmount(payment.getPaymentAmount());
        mercadoPagoPaymentRequest.setItems(buildMercadoPagoItem(payment));
        return mercadoPagoPaymentRequest;
    }

    private List<MercadoPagoItem> buildMercadoPagoItem(Payment payment) {
        MercadoPagoItem item = new MercadoPagoItem();
        item.setSkuNumber(UUID.randomUUID().toString());
        item.setCategory(PAYMENT_CATEGORY);
        item.setTitle(createItemPaymentTitle(payment));
        item.setDescription(createItemPaymentDescription(payment));
        item.setUnitPrice(payment.getPaymentAmount());
        item.setQuantity(QUANTITY);
        item.setUnitMeasure(UNIT);
        item.setTotalAmount(payment.getPaymentAmount());
        return List.of(item);
    }

    private String createPaymentDescription(Payment payment) {
        return "Pagamento do Pedido " + payment.getInternalPaymentId();
    }

    private String createItemPaymentDescription(Payment payment) {
        return "payment_" + payment.getInternalPaymentId() + "_order";
    }

    private String createItemPaymentTitle(Payment payment) {
        return "Pagamento: " + payment.getInternalPaymentId();
    }
}
