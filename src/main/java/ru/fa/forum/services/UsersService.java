package ru.fa.forum.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fa.forum.models.UserModel;
import ru.fa.forum.repositories.UsersRepository;

import java.util.Optional;

@Service
public class UsersService {
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UsersService(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    public Optional<UserModel> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public void save (UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        usersRepository.save(user);
        logger.info("Saved user - " + user.getUsername());
    }
}
