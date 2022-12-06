package project.entity;

public class Cart {

    private Long id;
    private User user;
    private Integer price;

    public Cart() {
    }

    public Cart(Long id, User user, Integer price) {
        this.id = id;
        this.user = user;
        this.price = price;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", price=" + price +
                '}';
    }
}
