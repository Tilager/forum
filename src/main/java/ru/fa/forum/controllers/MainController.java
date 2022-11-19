package ru.fa.forum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.fa.forum.models.UserModel;
import ru.fa.forum.services.UsersService;
import ru.fa.forum.utils.EnvUTF8;
import ru.fa.forum.utils.UsersValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("")
public class MainController {
    private final UsersValidator usersValidator;
    private final UsersService usersService;

    @Autowired
    public MainController(UsersValidator usersValidator, UsersService usersService, EnvUTF8 env) {
        this.usersValidator = usersValidator;
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("user") UserModel userModel) {
        return "register";
    }

    @PostMapping("/register")
    public String registerProcess(@ModelAttribute("user") @Valid UserModel user,
                                  BindingResult bindingResult) {
        usersValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "register";

        usersService.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
