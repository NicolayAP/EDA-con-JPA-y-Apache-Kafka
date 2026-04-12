package co.edu.uptc.edakafka.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    private Long orderid;
    private String customerid;
    private String status;

    public Order() {
    }

    public Order(Long orderid, String customerid, String status) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.status = status;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
