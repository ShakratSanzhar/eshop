package project.validator;

import project.dao.UserDetailDao;
import project.dto.CreateUserDetailsDto;
import project.util.LocalDateFormatter;

public class CreateUserDetailsValidator implements Validator<CreateUserDetailsDto> {

    private static final CreateUserDetailsValidator INSTANCE = new CreateUserDetailsValidator();
    private final UserDetailDao userDetailDao = UserDetailDao.getInstance();

    public static CreateUserDetailsValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(CreateUserDetailsDto object) {
        var validationResult = new ValidationResult();
        if (object.getUserId() == null) {
            validationResult.add(Error.of("invalid.userId", "UserId is invalid"));
        }
        if (object.getName().equals("")) {
            validationResult.add(Error.of("invalid.name", "Name is invalid"));
        }
        if (object.getSurname().equals("")) {
            validationResult.add(Error.of("invalid.surname", "Surname is invalid"));
        }
        if (object.getBirthday().equals("") || !LocalDateFormatter.isValid(object.getBirthday())) {
            validationResult.add(Error.of("invalid.birthday", "Birthday is invalid"));
        }
        if (object.getPhone().equals("") || !object.getPhone().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$") || !isUnique(object.getPhone())) {
            validationResult.add(Error.of("invalid.phone", "Phone is invalid"));
        }
        return validationResult;
    }

    public boolean isUnique(String phone) {
        return userDetailDao.findAll().stream()
                .noneMatch(userDetail -> userDetail.getPhone().equals(phone));
    }
}
