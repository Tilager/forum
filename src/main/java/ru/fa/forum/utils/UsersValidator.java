package ru.fa.forum.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.fa.forum.models.UserModel;
import ru.fa.forum.services.UsersService;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class UsersValidator implements Validator {
    private final UsersService usersService;
    private final EnvUTF8 env;

    @Autowired
    public UsersValidator(UsersService usersService, EnvUTF8 environment) {
        this.usersService = usersService;
        this.env = environment;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserModel user = (UserModel) target;

        // такой пользователь уже существует
        if (usersService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("globalError", "",
                    Objects.requireNonNull(env.getProperty("registration.username.existsError")));
        }

        // имя не по паттерну
        if (!Pattern.matches(Objects.requireNonNull(env.getProperty("registration.username.pattern")), user.getUsername())) {
            errors.rejectValue("username", "",
                    Objects.requireNonNull(env.getProperty("registration.username.patternError")));
        }

        // пароли не совпадают
        if (!user.getPassword().equals(user.getConfirm_password())) {
            errors.rejectValue("confirm_password", "",
                    Objects.requireNonNull(env.getProperty("registration.password.notEqualError")));
        }

        // пароль не по паттерну
        if (!Pattern.matches(Objects.requireNonNull(env.getProperty("registration.password.pattern")), user.getPassword())) {
            errors.rejectValue("password", "",
                    Objects.requireNonNull(env.getProperty("registration.password.patternError")));
        }
    }
}
