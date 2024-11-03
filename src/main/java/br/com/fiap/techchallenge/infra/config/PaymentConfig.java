package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.application.gateways.INotificationRepository;
import br.com.fiap.techchallenge.application.gateways.IPaymentRepository;
import br.com.fiap.techchallenge.application.usecases.payment.ConfirmPaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.MakePaymentUseCase;
import br.com.fiap.techchallenge.application.usecases.payment.NotifyPaymentConsumerUseCase;
import br.com.fiap.techchallenge.infra.dataproviders.database.persistence.payments.repository.PaymentRedShiftRepository;
import br.com.fiap.techchallenge.infra.gateways.NotificationRepository;
import br.com.fiap.techchallenge.infra.gateways.PaymentRepository;
import br.com.fiap.techchallenge.infra.presenters.PaymentMapper;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Value("${mercadopago.access_token}")
    private String mercadoPagoAccessToken;

    @Value("${mercadopago.notification_url}")
    private String mercadoPagoNotificationUrl;

    @Bean
    public CreatePaymentUseCase buildSaveRequestPaymentUseCase(IPaymentRepository paymentRepository) {
        return new CreatePaymentUseCase(paymentRepository);
    }

    @Bean
    public MakePaymentUseCase buildMakePaymentUseCase(IPaymentRepository paymentRepository) {
        return new MakePaymentUseCase(paymentRepository);
    }

    @Bean
    public ConfirmPaymentUseCase buildConfirmPaymentUseCase(IPaymentRepository paymentRepository, NotifyPaymentConsumerUseCase notifyPaymentConsumerUseCase) {
        return new ConfirmPaymentUseCase(paymentRepository, notifyPaymentConsumerUseCase);
    }

    @Bean
    public NotifyPaymentConsumerUseCase buildNotifyPaymentConsumerUseCase(INotificationRepository notificationRepository) {
        return new NotifyPaymentConsumerUseCase(notificationRepository);
    }

    @Bean
    public INotificationRepository buildNotificationRepository(SnsTemplate snsTemplate) {
        return new NotificationRepository(snsTemplate);
    }

    @Bean
    public IPaymentRepository buildPaymentRepository(PaymentMapper paymentMapper, PaymentRedShiftRepository paymentRedShiftRepository) {
        return new PaymentRepository(paymentMapper, paymentRedShiftRepository);
    }

}
