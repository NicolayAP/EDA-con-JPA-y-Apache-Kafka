package co.edu.uptc.edakafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import co.edu.uptc.edakafka.model.Order;
import co.edu.uptc.edakafka.utils.JsonUtils;

@Service
public class OrderEventProducer {
    private final String TOPIC = "order_events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private JsonUtils jsonUtils = new JsonUtils();

    public void sendOrderEvent(Order order, String action) {
        // Enviamos el objeto convertido a JSON
        // 'Action' ayuda a que el consumidor sepa qué hacer (ADD, EDIT, etc.)
        kafkaTemplate.send(TOPIC, action, jsonUtils.toJson(order));
    }
}
