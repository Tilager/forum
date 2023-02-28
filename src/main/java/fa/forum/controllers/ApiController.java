package fa.forum.controllers;

import fa.forum.dto.ConvertersDTO;
import fa.forum.dto.NewsModelDTO;
import fa.forum.dto.UserModelDTO;
import fa.forum.services.MessageService;
import fa.forum.services.NewsService;
import fa.forum.services.UserService;
import fa.forum.utils.SessionUtil;
import fa.forum.validatiors.ProfileValidator;
import fa.forum.validatiors.RegisterValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Log4j2
public class ApiController {
    private final UserService userService;
    private final MessageService messageService;
    private final NewsService newsService;
    private final RegisterValidator registerValidator;
    private final ProfileValidator profileValidator;
    private final ConvertersDTO convertersDTO;
    private final SessionUtil sessionUtil;

    @Autowired
    public ApiController(UserService userService, MessageService messageService, NewsService newsService, RegisterValidator registerValidator, ProfileValidator profileValidator, ConvertersDTO convertersDTO, SessionUtil sessionUtil) {
        this.userService = userService;
        this.messageService = messageService;
        this.newsService = newsService;
        this.registerValidator = registerValidator;
        this.profileValidator = profileValidator;
        this.convertersDTO = convertersDTO;
        this.sessionUtil = sessionUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<List<String>> register(@RequestBody UserModelDTO userModelDTO,
                                                      BindingResult bindingResult) {
        registerValidator.validate(userModelDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors()
                                            .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                                            .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }

        userService.save(convertersDTO.DTOtoUserModel(userModelDTO));

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }

    @PostMapping(path="/changeProfile")
    public ResponseEntity<List<String>> changeProfile(@ModelAttribute UserModelDTO user,
                                                    BindingResult bindingResult) {
        profileValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }

        userService.update(convertersDTO.DTOtoUserModel(user), user.getLogo());

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }

    @GetMapping("/getAuthUserUUID")
    public String getAuthUser() {
        return sessionUtil.getLoggedUser().getUuid();
    }

    @PostMapping ("/sendMessage")
    public void sendMessage(@RequestParam(value = "message", required = false) String msg,
                            @RequestParam(value = "file", required = false) MultipartFile file) {
        messageService.sendMessage(msg, file);
    }

    @PostMapping("/saveNews")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveNews(@ModelAttribute NewsModelDTO news) {
        try {
            if (news.getLogo().isEmpty())
                return new ResponseEntity<>("Logo empty", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(convertersDTO.NewsModelToDTO(newsService.saveNews(convertersDTO.DTOtoNewsModel(news), news.getLogo())),
                                        HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/editNews")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editNews(@RequestBody NewsModelDTO news) {
        try {
            return new ResponseEntity<>(convertersDTO.NewsModelToDTO(newsService.editNews(convertersDTO.DTOtoNewsModel(news))), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
