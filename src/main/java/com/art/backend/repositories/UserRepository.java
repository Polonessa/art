package com.art.backend.repositories;

import com.art.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String s);

    Optional<User> findByLogin(String login);
}
