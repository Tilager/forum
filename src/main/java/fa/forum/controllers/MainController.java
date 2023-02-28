package fa.forum.controllers;

import fa.forum.models.UserModel;
import fa.forum.services.MessageService;
import fa.forum.services.NewsService;
import fa.forum.services.UserService;
import fa.forum.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    private final SessionUtil sessionUtil;
    private final MessageService messageService;
    private final UserService userService;
    private final NewsService newsService;

    @Autowired
    public MainController(SessionUtil sessionUtil, MessageService messageService, UserService userService, NewsService newsService) {
        this.sessionUtil = sessionUtil;
        this.messageService = messageService;
        this.userService = userService;
        this.newsService = newsService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("loggedUsers", userService.findAllByOnline());

        return "index";
    }

    @GetMapping("/chat")
    public String chat(Model model) {
        model.addAttribute("messages", messageService.findAllReversed());
        model.addAttribute("authUserUUID", sessionUtil.getLoggedUser().getUuid());
        model.addAttribute("allMsgFiles", messageService.getAllMsgWithFile());
        model.addAttribute("loggedUsers", userService.findAllByOnline());

        return "chat";
    }

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("allNews", newsService.findAllOrderByAddedDate());

        return "news";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        UserModel loggedUser = sessionUtil.getLoggedUser();
        model.addAttribute("authUser", loggedUser);
        model.addAttribute("files", userService.getAllFilesNameByUUID(loggedUser.getUuid()));

        return "profile";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
