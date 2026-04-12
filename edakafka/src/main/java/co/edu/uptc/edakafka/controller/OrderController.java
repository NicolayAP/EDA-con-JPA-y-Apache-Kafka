package co.edu.uptc.edakafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.edakafka.model.Order;
import co.edu.uptc.edakafka.service.OrderEventProducer;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderEventProducer producer;

    @PostMapping("/add")
    public String addOrder(@RequestBody Order order) {
        producer.sendOrderEvent(order, "ADD");
        return "Evento ADD enviado para la orden: " + order.getOrderid();
    }
}
