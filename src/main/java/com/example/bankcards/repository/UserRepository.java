package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUserId(UUID userId);
    Optional<User> findUserByEmail(String email);
    List<User> findAllByRole(Role role);
}
