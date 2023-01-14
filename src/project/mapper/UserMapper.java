package project.mapper;

import lombok.NoArgsConstructor;
import project.dto.ReadUserDto;
import project.entity.User;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserMapper implements Mapper<User, ReadUserDto> {

    private static final UserMapper INSTANCE = new UserMapper();

    @Override
    public ReadUserDto mapFrom(User object) {
        return ReadUserDto.builder()
                .id(object.getId())
                .username(object.getUsername())
                .email(object.getEmail())
                .role(object.getRole())
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
