package project.validator;

import project.dao.UserDao;
import project.dto.CreateUserDto;
import project.enums.Role;

public class CreateUserValidator implements Validator<CreateUserDto> {

    private static final CreateUserValidator INSTANCE = new CreateUserValidator();
    private final UserDao userDao = UserDao.getInstance();

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(CreateUserDto object) {
        var validationResult = new ValidationResult();
        if (object.getUsername().equals("")) {
            validationResult.add(Error.of("invalid.username", "Username is invalid"));
        }
        if (object.getEmail().equals("") || !isUnique(object.getEmail())) {
            validationResult.add(Error.of("invalid.email", "Email is invalid"));
        }
        if (object.getPassword().equals("")) {
            validationResult.add(Error.of("invalid.password", "Password is invalid"));
        }
        if (object.getRole().equals("") || Role.find(object.getRole()) == null) {
            validationResult.add(Error.of("invalid.role", "Role is invalid"));
        }
        return validationResult;
    }

    public boolean isUnique(String email) {
        return userDao.findAll().stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }
}
