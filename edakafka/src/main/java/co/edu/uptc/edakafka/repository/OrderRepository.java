package co.edu.uptc.edakafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.uptc.edakafka.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
