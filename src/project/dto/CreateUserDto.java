package project.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserDto {

    String username;
    String email;
    String password;
    String role;
}
