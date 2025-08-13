package com.ecom.notification;

import com.ecom.notification.payload.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
public class OrderEventConsumer {

    @Bean
    public Consumer<OrderCreatedEvent> orderCreated() {
        return event -> {
            log.info("Order Created Event received for order: {}", event);
            log.info("Order Created Event received for order: {}", event.getOrderId());
            log.info("Order Created Event received for userId: {}", event.getUserId());
        };
    }

}
