package project.entity;

public class Blacklist {

    private User user;

    public Blacklist() {
    }

    public Blacklist(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Blacklist{" +
                "user=" + user +
                '}';
    }
}
