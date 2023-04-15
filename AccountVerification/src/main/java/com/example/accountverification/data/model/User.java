package com.example.accountverification.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name="_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String accountNumber;
    private String bankCode;
    private boolean isVerified;

    public User(String name, String accountNumber, String bankCode) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
    }
}
