package com.example.accountverification.controller;

import com.example.accountverification.dtos.request.RetrieveAcctNameRequest;
import com.example.accountverification.dtos.request.UserRegistrationRequest;
import com.example.accountverification.dtos.request.VerifyAccountRequest;
import com.example.accountverification.dtos.response.GetResponse;
import com.example.accountverification.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @MutationMapping
    public GetResponse addBankAccountDetails(@Argument UserRegistrationRequest userRegistrationRequest){
        return userService.addBankAccountDetails(userRegistrationRequest);
    }
    @QueryMapping
    public String retrieveAccountName(@Argument RetrieveAcctNameRequest retrieveAcctNameRequest) throws IOException {
        return userService.retrieveAcctName(retrieveAcctNameRequest);
    }
    @MutationMapping
    public String verifyBankAccount(@Argument VerifyAccountRequest verifyAccountRequest) throws IOException {
        return userService.verifyBankAccount(verifyAccountRequest);
    }
}
