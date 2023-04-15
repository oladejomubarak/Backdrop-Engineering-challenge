package com.example.accountverification.service;

import com.example.accountverification.data.model.User;
import com.example.accountverification.dtos.request.RetrieveAcctNameRequest;
import com.example.accountverification.dtos.request.UserRegistrationRequest;
import com.example.accountverification.dtos.request.VerifyAccountRequest;
import com.example.accountverification.dtos.response.GetResponse;

import java.io.IOException;

public interface UserService {
    GetResponse addBankAccountDetails(UserRegistrationRequest userRegistrationRequest);

    boolean checkDifferenceOfTwoNames(String firstName, String lastName);

    String retrieveAcctName(RetrieveAcctNameRequest retrieveAcctNameRequest) throws IOException;
    void updateUser (String accountNumber);
    String verifyBankAccount(VerifyAccountRequest verifyAccountRequest) throws IOException;

    User findUserByAccountNumber(String accountNumber);

    void deleteAllUsers();
}
