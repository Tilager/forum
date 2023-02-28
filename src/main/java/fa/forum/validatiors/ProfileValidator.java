package fa.forum.validatiors;

import fa.forum.dto.UserModelDTO;
import fa.forum.services.UserService;
import fa.forum.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class ProfileValidator implements Validator {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SessionUtil sessionUtil;
    private final Environment env;

    @Autowired
    public ProfileValidator(UserService userService, PasswordEncoder passwordEncoder, SessionUtil sessionUtil, Environment env) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.sessionUtil = sessionUtil;
        this.env = env;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserModelDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserModelDTO user = (UserModelDTO) target;

        if (!user.getUsername().equals("") && !Pattern.matches(Objects.requireNonNull(env.getProperty("registration.username.pattern")), user.getUsername())) {
            errors.rejectValue("username", "", "usernameNotMatchPattern");
        }

        if (!user.getUsername().equals("") && userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "usernameAlreadyExists");
        }

        if (!passwordEncoder.matches(user.getConfirmPassword(), sessionUtil.getLoggedUser().getPassword())) {
            errors.rejectValue("confirmPassword", "", "confirmPasswordNotMatches");
        }

        if (!user.getPassword().equals("") && !Pattern.matches(Objects.requireNonNull(env.getProperty("registration.password.pattern")), user.getPassword())) {
            errors.rejectValue("password", "", "passwordNotMatchPattern");
        }
    }
}
