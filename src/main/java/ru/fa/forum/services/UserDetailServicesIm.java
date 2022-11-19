package ru.fa.forum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fa.forum.models.UserModel;
import ru.fa.forum.repositories.UsersRepository;
import ru.fa.forum.security.UserDetailsIm;

import java.util.Optional;

@Service
public class UserDetailServicesIm implements UserDetailsService {
    private final UsersRepository usersRepository;


    @Autowired
    public UserDetailServicesIm(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> op = usersRepository.findByUsername(username);

        if(op.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsIm(op.get());
    }
}
