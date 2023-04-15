package com.example.accountverification.dtos.request;

import lombok.Data;

@Data
public class RetrieveAcctNameRequest {
    private String accountNumber;
    private String bankCode;
//    private String accountName;
}
