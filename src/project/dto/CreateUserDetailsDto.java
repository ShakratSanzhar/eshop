package project.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class CreateUserDetailsDto {
    Long userId;
    String name;
    String surname;
    String birthday;
    String phone;
    LocalDateTime registrationDate;
}
