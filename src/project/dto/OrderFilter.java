package project.dto;

import project.entity.User;
import project.enums.OrderStatus;

public record OrderFilter(int limit, int offset, User user, OrderStatus status, Integer price) {
}
