package com.example.accountverification.service;

import com.example.accountverification.data.model.User;
import com.example.accountverification.dtos.request.RetrieveAcctNameRequest;
import com.example.accountverification.dtos.request.UserRegistrationRequest;
import com.example.accountverification.dtos.request.VerifyAccountRequest;
import com.example.accountverification.dtos.response.GetResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private UserRegistrationRequest userRegistrationRequest;
    private UserRegistrationRequest userRegistrationRequest1;
    private UserRegistrationRequest userRegistrationRequest2;
    private RetrieveAcctNameRequest retrieveAcctNameRequest;

    @BeforeEach
    void setUp() {
        userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setName("OLADEJO MUBARA A");
        userRegistrationRequest.setAccountNumber("3091799950");
        userRegistrationRequest.setBankCode("011");

        userRegistrationRequest1 = new UserRegistrationRequest();
        userRegistrationRequest1.setName("Oladej mubar A");
        userRegistrationRequest1.setAccountNumber("3091799950");
        userRegistrationRequest1.setBankCode("011");

        userRegistrationRequest2 = new UserRegistrationRequest();
        userRegistrationRequest2.setName("OLADEJO MUBARAK A");
        userRegistrationRequest2.setBankCode("011");
        userRegistrationRequest2.setAccountNumber("3091799950");


        retrieveAcctNameRequest = new RetrieveAcctNameRequest();
        retrieveAcctNameRequest.setAccountNumber("3091799950");
        retrieveAcctNameRequest.setBankCode("011");
    }

    @AfterEach
    void tearDown(){
        userService.deleteAllUsers();
    }
    @Test void testThatUserCanRegister(){
        GetResponse register = userService.addBankAccountDetails(userRegistrationRequest);
        assertEquals("you are successfully registered", register.getMessage());
    }
    @Test void testThatIfTwoNamesAreChecked_andTheirDifferencesIsLessThanThree_trueIsReturned(){
        String name1 = "Oladejo mubarak adeshina";
        String name2 = "Oladejo mubarak adeshi";
        boolean checkNames = userService.checkDifferenceOfTwoNames(name1, name2);
        assertTrue(checkNames);
    }
    @Test void testThatIfTwoNamesAreChecked_andTheirDifferencesIsGreaterThanTwo_FalseIsReturned(){
        String name1 = "Oladejo mubarak adeshina";
        String name2 = "Oladejo mubarak adesh";
        boolean checkNames = userService.checkDifferenceOfTwoNames(name1, name2);
        assertFalse(checkNames);
    }
    @Test void testThatNameProvidedByUserOnDBIsReturnedIfItPassesLDVerification() throws IOException {
        userService.addBankAccountDetails(userRegistrationRequest);
        String fetchedName = userService.retrieveAcctName(retrieveAcctNameRequest);
        assertEquals("OLADEJO MUBARA A", fetchedName);
    }
    @Test void testThatNameFetchedFromTheApiIsReturnedIfLDVerificationFails() throws IOException {
        userService.addBankAccountDetails(userRegistrationRequest1);
        String fetchedName = userService.retrieveAcctName(retrieveAcctNameRequest);
        assertEquals("OLADEJO MUBARAK A", fetchedName);
    }
    @Test void testThatWhenUserAddsBankDetailsVerificationIsFalse_thenStatusChangesToTrueAfterVerifyingAddedBankDetailsSuccessfully() throws IOException {
        userService.addBankAccountDetails(userRegistrationRequest2);
        VerifyAccountRequest verifyAccountRequest = new VerifyAccountRequest("3091799950",
                "011", "OLADEJO MUBARAK A");
        User foundUser = userService.findUserByAccountNumber(userRegistrationRequest2.getAccountNumber());
        assertFalse(foundUser.isVerified());
        userService.verifyBankAccount(verifyAccountRequest);
        User foundUserAfterVerification = userService.findUserByAccountNumber("3091799950");
        assertTrue(foundUserAfterVerification.isVerified());
    }
}