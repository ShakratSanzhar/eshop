package project.dto;

import project.entity.User;

public record CartFilter(int limit, int offset, User user, Integer price) {
}
