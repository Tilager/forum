package fa.forum.validatiors;

import fa.forum.dto.UserModelDTO;
import fa.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class RegisterValidator implements Validator {
    private final Environment env;

    private final UserService userService;

    @Autowired
    public RegisterValidator(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserModelDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserModelDTO user = (UserModelDTO) target;

        // username errors
        if (!Pattern.matches(Objects.requireNonNull(env.getProperty("registration.username.pattern")), user.getUsername())) {
            errors.rejectValue("username", "", "usernameNotMatchPattern");
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "usernameAlreadyExists");
        }

        // password errors
        if(!Pattern.matches(Objects.requireNonNull(env.getProperty("registration.password.pattern")), user.getPassword())) {
            errors.rejectValue("password", "", "passwordNotMatchPattern");
        }

        if(!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("password", "", "passwordNotConfirm");
        }
    }
}
