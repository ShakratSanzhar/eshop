package project.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.dao.UserDetailDao;
import project.dto.CreateUserDetailsDto;
import project.exception.ValidationException;
import project.mapper.CreateUserDetailsMapper;
import project.validator.CreateUserDetailsValidator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetailsService {

    private static final UserDetailsService INSTANCE = new UserDetailsService();
    private final UserDetailDao userDetailDao = UserDetailDao.getInstance();
    private final CreateUserDetailsValidator createUserDetailsValidator = CreateUserDetailsValidator.getInstance();
    private final CreateUserDetailsMapper createUserDetailsMapper = CreateUserDetailsMapper.getInstance();

    public Long create(CreateUserDetailsDto userDetailsDto) {
        var validationResult = createUserDetailsValidator.isValid(userDetailsDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var userDetailsEntity = createUserDetailsMapper.mapFrom(userDetailsDto);
        userDetailDao.save(userDetailsEntity);
        return userDetailsEntity.getUser().getId();
    }

    public static UserDetailsService getInstance() {
        return INSTANCE;
    }
}
