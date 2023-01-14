package project.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.dao.UserDao;
import project.dto.CreateUserDto;
import project.dto.ReadUserDto;
import project.exception.ValidationException;
import project.mapper.CreateUserMapper;
import project.mapper.UserMapper;
import project.validator.CreateUserValidator;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();

    public Optional<ReadUserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::mapFrom);
    }

    public Long create(CreateUserDto userDto) {
        var validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var userEntity = createUserMapper.mapFrom(userDto);
        userDao.save(userEntity);
        return userEntity.getId();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
