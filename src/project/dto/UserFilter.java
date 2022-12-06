package project.dto;

public record UserFilter (int limit, int offset, String username, String email) {
}
