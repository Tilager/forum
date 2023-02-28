package fa.forum.services;

import fa.forum.models.UserModel;
import fa.forum.security.UserDetailsIm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServicesIm implements UserDetailsService {
    private final UserService userService;


    @Autowired
    public UserDetailServicesIm(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetailsIm loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> op = userService.findByUsername(username);

        if(op.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsIm(op.get());
    }
}
