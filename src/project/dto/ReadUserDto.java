package project.dto;

import lombok.Builder;
import lombok.Value;
import project.enums.Role;

@Value
@Builder
public class ReadUserDto {

    Long id;
    String username;
    String email;
    Role role;
}
