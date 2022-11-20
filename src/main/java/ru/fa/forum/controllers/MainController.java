package ru.fa.forum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fa.forum.models.NewsModel;
import ru.fa.forum.models.UserModel;
import ru.fa.forum.services.NewsService;
import ru.fa.forum.services.UsersService;
import ru.fa.forum.utils.EnvUTF8;
import ru.fa.forum.utils.UsersValidator;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.Collection;

@Controller
@RequestMapping("")
public class MainController {
    private final UsersValidator usersValidator;
    private final UsersService usersService;
    private final NewsService newsService;

    @Autowired
    public MainController(UsersValidator usersValidator, UsersService usersService, EnvUTF8 envUTF8, NewsService newsService) {
        this.usersValidator = usersValidator;
        this.usersService = usersService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/news")
    public String news(Model model,
                       @ModelAttribute("news") NewsModel news) {
        model.addAttribute("allNews", newsService.findAllOrderByAddedDate());

        return "news";
    }

    @PostMapping("/news")
    public String putNews(@ModelAttribute("news") NewsModel news,
                          @RequestParam("file") MultipartFile file,
                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "news";

        newsService.saveFile(news, file);
        return "redirect:/news";
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
