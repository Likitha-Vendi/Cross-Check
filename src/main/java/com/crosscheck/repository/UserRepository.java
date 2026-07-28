package com.crosscheck.repository;

import com.crosscheck.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    List<User> findAllByRoleIgnoreCaseOrderByIdAsc(String role);
    long countByRoleIgnoreCaseAndActiveTrue(String role);
}
