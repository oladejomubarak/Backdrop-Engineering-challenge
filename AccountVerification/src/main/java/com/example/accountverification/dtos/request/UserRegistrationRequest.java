package com.example.accountverification.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String name;
    private String accountNumber;
    private String bankCode;
}
