package co.edu.uptc.edakafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import co.edu.uptc.edakafka.model.Order;
import co.edu.uptc.edakafka.utils.JsonUtils;

@Service
public class OrderEventConsumer {
    @Autowired
    private OrderService orderService;
    private JsonUtils jsonUtils = new JsonUtils();

    @KafkaListener(topics = "order_events", groupId = "order_group")
    public void processOrder(String message, @Header(KafkaHeaders.RECEIVED_KEY) String action) {
        Order order = jsonUtils.fromJson(message, Order.class);
        if ("ADD".equals(action)) {
            orderService.save(order);
            System.out.println("[CONSUMER] Orden guardada en DB: " + order.getOrderid());
        }
    }
}
