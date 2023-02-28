package fa.forum.utils;

import fa.forum.models.UserModel;
import fa.forum.security.UserDetailsIm;
import fa.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionUtil {
    private final UserService userService;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public SessionUtil(UserService userService, SessionRegistry sessionRegistry) {
        this.userService = userService;
        this.sessionRegistry = sessionRegistry;
    }

    public List<UserDetailsIm> getAllLoggedUser() {
        List<UserDetailsIm> allUsers = new ArrayList<>();

        for (final Object principal: sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserModel)
                allUsers.add((UserDetailsIm) principal);
        }

        return allUsers;
    }

    public UserModel getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userService.findByUsername(username).orElse(null);
    }

}
