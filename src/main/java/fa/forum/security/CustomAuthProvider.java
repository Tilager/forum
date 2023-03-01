package fa.forum.security;

import fa.forum.services.UserDetailServicesIm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserDetailServicesIm userDetailServices;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthProvider(UserDetailServicesIm userDetailServices, PasswordEncoder passwordEncoder) {
        this.userDetailServices = userDetailServices;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();

        UserDetailsIm user = userDetailServices.loadUserByUsername(username);

        String password = authentication.getCredentials().toString();

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Incorrect password");

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }


}
