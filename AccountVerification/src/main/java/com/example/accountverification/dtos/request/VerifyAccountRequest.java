package com.example.accountverification.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyAccountRequest {
    private String accountNumber;
    private String bankCode;
    private String accountName;
}
