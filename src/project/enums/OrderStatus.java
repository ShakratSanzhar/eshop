package project.enums;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    NOT_PAID,
    PAID;

    public static Optional<OrderStatus> find(String orderStatus) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(orderStatus))
                .findFirst();
    }
}
