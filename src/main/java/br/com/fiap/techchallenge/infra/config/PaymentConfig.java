package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.application.gateways.INotificationRepository;
import br.com.fiap.techchallenge.application.gateways.IPaymentProviderRepository;
import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.application.usecases.payment.VerifyPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.*;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentRedShiftRepository;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.MercadoPagoClient;
import br.com.fiap.techchallenge.infra.gateways.MercadoPagoPaymentProviderRepository;
import br.com.fiap.techchallenge.infra.gateways.NotificationRepository;
import br.com.fiap.techchallenge.infra.gateways.PaymentRepository;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Value("${mercado-pago.notification-url}")
    private String mercadoPagoCallbackNotificationUrl;

    @Value("${aws.sns.updates-payments-topic}")
    private String destinationTopicPaymentsUpdates;

    @Bean
    public CreatePaymentUseCase buildSaveRequestPaymentUseCase(IPaymentRepository paymentRepository) {
        return new CreatePaymentUseCase(paymentRepository);
    }

    @Bean
    public MakePaymentUseCase buildMakePaymentUseCase(IPaymentRepository paymentRepository, IPaymentProviderRepository paymentProviderRepository) {
        return new MakePaymentUseCase(paymentRepository, paymentProviderRepository);
    }

    @Bean
    public VerifyPaymentUseCase buildVerifyPaymentUseCase(IPaymentProviderRepository paymentProviderRepository) {
        return new VerifyPaymentUseCase(paymentProviderRepository);
    }

    @Bean
    public ConfirmPaymentUseCase buildConfirmPaymentUseCase(IPaymentRepository paymentRepository, VerifyPaymentUseCase verifyPaymentUseCase, NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase) {
        return new ConfirmPaymentUseCase(paymentRepository, verifyPaymentUseCase ,notifyPaymentConsumerUseCase);
    }

    @Bean
    public NotifyPaymentConsumerUseCase buildNotifyPaymentConsumerUseCase(INotificationRepository notificationRepository) {
        return new NotifyPaymentConsumerUseCase(notificationRepository);
    }

    @Bean
    public ConsultPaymentUseCase buildConsultPaymentUseCase(IPaymentRepository paymentRepository) {
        return new ConsultPaymentUseCase(paymentRepository);
    }

    @Bean
    public INotificationRepository buildNotificationRepository(SnsTemplate snsTemplate) {
        return new NotificationRepository(snsTemplate, destinationTopicPaymentsUpdates);
    }

    @Bean
    public IPaymentRepository buildPaymentRepository(PaymentMapper paymentMapper, PaymentRedShiftRepository paymentRedShiftRepository) {
        return new PaymentRepository(paymentMapper, paymentRedShiftRepository);
    }

    @Bean
    public IPaymentProviderRepository buildPaymentProviderRepository(MercadoPagoClient mercadoPagoClient) {
        return new MercadoPagoPaymentProviderRepository(mercadoPagoClient, mercadoPagoCallbackNotificationUrl);
    }

    @Bean
    public PaymentMapper buildPaymentMapper() {
        return new PaymentMapper();
    }

}
