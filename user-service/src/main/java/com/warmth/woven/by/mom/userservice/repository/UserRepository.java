package com.warmth.woven.by.mom.userservice.repository;

import com.warmth.woven.by.mom.userservice.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);

  boolean existsById(String id);

  boolean existsByEmail(String email);
}
