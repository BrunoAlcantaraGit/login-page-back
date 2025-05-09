package com.login.login_api.repository;

import com.login.login_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

   Optional<User> findByEmail(String email);
}
