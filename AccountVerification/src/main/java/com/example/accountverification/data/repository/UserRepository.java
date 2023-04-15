package com.example.accountverification.data.repository;

import com.example.accountverification.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   //User findUserByNameIgnoreCase(String name);
   User findUserByAccountNumber(String accountNumber);
}
