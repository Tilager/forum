package ru.fa.forum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fa.forum.models.UserModel;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByUsername(String username);
}
