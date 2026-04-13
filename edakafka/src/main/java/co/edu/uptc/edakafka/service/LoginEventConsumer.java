package co.edu.uptc.edakafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LoginEventConsumer {

    // Se configura para escuchar el tópico "login-events"
    @KafkaListener(topics = "login-events", groupId = "login_group")
    public void consume(ConsumerRecord<String, String> record) {
        String eventType = record.key();
        String message = record.value();
        System.out.println("[LOGIN CONSUMER] EventType=" + eventType + ", message=" + message);
    }
}
