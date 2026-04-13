package co.edu.uptc.edakafka.service;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import co.edu.uptc.edakafka.model.Customer;
import co.edu.uptc.edakafka.model.Login;
import co.edu.uptc.edakafka.utils.JsonUtils;

@Service
public class CustomerEventConsumer {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoginService loginService;

    @KafkaListener(topics = "customer-events", groupId = "customer_group")
    public void handleCustomerEvent(ConsumerRecord<String, String> record) {
        String eventType = record.key();
        String payload = record.value();
        System.out.println("[CUSTOMER CONSUMER] EventType=" + eventType + ", payload=" + payload);

        if ("CUSTOMER_CREATED".equals(eventType)) {
            Customer customer = JsonUtils.fromJson(payload, Customer.class);
            boolean saved = customerService.save(customer);
            if (saved) {
                Login login = buildDefaultLogin(customer);
                loginService.createLogin(login);
                System.out.println("[CUSTOMER CONSUMER] Login creado para customer: " + login.getUsername());
            }
            return;
        }

        if ("CUSTOMER_UPDATED".equals(eventType)) {
            Customer customer = JsonUtils.fromJson(payload, Customer.class);
            customerService.save(customer);
            return;
        }

        if ("CUSTOMER_DELETED".equals(eventType)) {
            Customer customer = customerService.findById(payload);
            if (customer != null) {
                customerService.delete(customer);
                System.out.println("[CUSTOMER CONSUMER] Customer eliminado: " + payload);
            } else {
                System.out.println("[CUSTOMER CONSUMER] DELETE: customer no encontrado");
            }
            return;
        }

        if ("CUSTOMER_FIND_BY_ID".equals(eventType)) {
            System.out.println("[CUSTOMER CONSUMER] CUSTOMER_FIND_BY_ID resultado: " + customerService.findById(payload));
            return;
        }

        if ("CUSTOMER_FIND_ALL".equals(eventType)) {
            List<Customer> customers = customerService.findAll();
            System.out.println("[CUSTOMER CONSUMER] CUSTOMER_FIND_ALL: " + customers.size() + " customers encontrados");
        }
    }

    private Login buildDefaultLogin(Customer customer) {
        String username = customer.getEmail() != null && !customer.getEmail().isBlank()
                ? customer.getEmail()
                : customer.getDocument();
        String password = "W3lc0m3" + customer.getDocument();
        return new Login(username, password, customer.getDocument());
    }
}