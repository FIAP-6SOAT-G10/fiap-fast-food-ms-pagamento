package br.com.fiap.techchallenge.infra.gateways;

import br.com.fiap.techchallenge.application.gateways.IPaymentProviderRepository;
import br.com.fiap.techchallenge.domain.entities.pagamento.MercadoLibreResponse;
import br.com.fiap.techchallenge.domain.entities.pagamento.Payment;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.MercadoPagoClient;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoLibrePaymentResponse;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoCashOut;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoItem;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoPaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Slf4j
public class MercadoPagoPaymentProviderRepository implements IPaymentProviderRepository {

    private final MercadoPagoClient mercadoPagoClient;

    public MercadoPagoPaymentProviderRepository(MercadoPagoClient mercadoPagoClient) {
        this.mercadoPagoClient = mercadoPagoClient;
    }

    @Override
    public void createPaymentRequestOnPaymentProvider(Payment payment) {
        MercadoPagoItem item = new MercadoPagoItem();
        item.setSkuNumber(UUID.randomUUID().toString());
        item.setCategory("payment");
        item.setTitle("Pagamento: " + payment.getInternalPaymentId());
        item.setDescription("payment_" + payment.getInternalPaymentId() + "_order");
        item.setUnitPrice(payment.getPaymentAmount());
        item.setQuantity(1);
        item.setUnitMeasure("unit");
        item.setTotalAmount(payment.getPaymentAmount());

        List<MercadoPagoItem> items = List.of(item);
        MercadoPagoPaymentRequest mercadoPagoPaymentRequest = new MercadoPagoPaymentRequest();
        mercadoPagoPaymentRequest.setExternalReference(payment.getInternalPaymentId());
        mercadoPagoPaymentRequest.setTitle("Pagamento");
        mercadoPagoPaymentRequest.setDescription("Pagamento do Pedido " + payment.getInternalPaymentId());
        mercadoPagoPaymentRequest.setNotificationURL("https://52f6-2804-7f0-9382-1dd9-348a-e044-b978-bea7.ngrok-free.app/api/payments/confirmation");
        mercadoPagoPaymentRequest.setTotalAmount(payment.getPaymentAmount());
        mercadoPagoPaymentRequest.setItems(items);

        try {
            ResponseEntity<Void> response = mercadoPagoClient.generatePaymentRequest(mercadoPagoPaymentRequest);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Erro ao gerar o pagamento com o provedor de pagamentos");
            }

            log.info("Solicitação de pagamento gerada com sucesso");
        } catch (Exception e) {
            log.error("Falha de comunicação com o provedor de pagamentos", e);
        }
    }

    @Override
    public MercadoLibreResponse consultPayment(String resource) {
        String paymentId = resource.substring(resource.lastIndexOf('/') + 1);
        ResponseEntity<MercadoLibrePaymentResponse> response = mercadoPagoClient.consultPaymentDetails(paymentId);

        MercadoLibrePaymentResponse mercadoLibrePaymentResponse = response.getBody();
        if (mercadoLibrePaymentResponse != null && mercadoLibrePaymentResponse.getStatus().equalsIgnoreCase("closed")) {
            MercadoLibreResponse mercadoLibreResponse = new MercadoLibreResponse();
            mercadoLibreResponse.setId(mercadoLibrePaymentResponse.getId());
            mercadoLibreResponse.setStatus(mercadoLibrePaymentResponse.getStatus());
            mercadoLibreResponse.setExternalReference(mercadoLibrePaymentResponse.getExternalReference());
            mercadoLibreResponse.setTotalAmount(mercadoLibrePaymentResponse.getTotalAmount());
            mercadoLibreResponse.setPaidAmount(mercadoLibrePaymentResponse.getPaidAmount());
            mercadoLibreResponse.setOrderStatus(mercadoLibrePaymentResponse.getOrderStatus());
            return mercadoLibreResponse;
        }

        return null;

    }

}
