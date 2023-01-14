package project.entity;

import project.enums.OrderStatus;
import java.time.LocalDateTime;

public class Order {

    private Long id;
    private User user;
    private OrderStatus status;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public Order() {
    }

    public Order(Long id, User user, OrderStatus status, Integer price, LocalDateTime createdAt, LocalDateTime closedAt) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.price = price;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", status=" + status +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", closedAt=" + closedAt +
                '}';
    }
}
