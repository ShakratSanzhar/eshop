package project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {

    private User user;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String phone;
    private LocalDateTime registrationDate;
}
