package project.mapper;

import project.dao.UserDao;
import project.dto.CreateUserDetailsDto;
import project.entity.UserDetail;
import project.util.LocalDateFormatter;

public class CreateUserDetailsMapper implements Mapper<CreateUserDetailsDto, UserDetail> {

    private static final CreateUserDetailsMapper INSTANCE = new CreateUserDetailsMapper();
    private final UserDao userDao = UserDao.getInstance();

    @Override
    public UserDetail mapFrom(CreateUserDetailsDto object) {
        return UserDetail.builder()
                .user(userDao.findById(object.getUserId()).get())
                .name(object.getName())
                .surname(object.getSurname())
                .birthday(LocalDateFormatter.format(object.getBirthday()))
                .phone(object.getPhone())
                .registrationDate(object.getRegistrationDate())
                .build();
    }

    public static CreateUserDetailsMapper getInstance() {
        return INSTANCE;
    }
}
