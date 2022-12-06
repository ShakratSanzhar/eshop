package project.dto;

import java.time.LocalDate;

public record UserDetailFilter(int limit, int offset, String name, String surname, LocalDate birthday, String phone) {
}
