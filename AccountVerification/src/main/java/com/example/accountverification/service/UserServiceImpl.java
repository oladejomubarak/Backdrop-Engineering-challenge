package com.example.accountverification.service;

import com.example.accountverification.data.model.User;
import com.example.accountverification.data.repository.UserRepository;
import com.example.accountverification.dtos.request.RetrieveAcctNameRequest;
import com.example.accountverification.dtos.request.UserRegistrationRequest;
import com.example.accountverification.dtos.request.VerifyAccountRequest;
import com.example.accountverification.dtos.response.GetAcctDeserializationResponse;
import com.example.accountverification.dtos.response.GetResponse;
import com.example.accountverification.exceptions.UserRegistrationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private final String payStack_key = "sk_test_0bb79b56692eff6256d41678914bf9acc30b581c";

    @Override
    public GetResponse addBankAccountDetails(UserRegistrationRequest userRegistrationRequest) {
        User foundUser = userRepository.findUserByAccountNumber(userRegistrationRequest.getAccountNumber());
        if (foundUser != null) throw new UserRegistrationException(
                "user with " + userRegistrationRequest.getAccountNumber() + "already exists");

        User user = new User(userRegistrationRequest.getName(), userRegistrationRequest.getAccountNumber(),
                userRegistrationRequest.getBankCode());
        userRepository.save(user);

        GetResponse res = new GetResponse();
        res.setMessage("you are successfully registered");
        return res;
    }

    @Override
    public boolean checkDifferenceOfTwoNames(String firstName, String secondName) {
        String ignoreFirstNameCase = firstName.toLowerCase();
        String ignoreSecondNameCase = secondName.toLowerCase();

        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int difference = levenshteinDistance.apply(ignoreFirstNameCase, ignoreSecondNameCase);
        return difference < 3;
    }

    @Override
    public String retrieveAcctName(RetrieveAcctNameRequest retrieveAcctNameRequest) throws IOException {
        OkHttpClient client = new OkHttpClient();
        User foundUser = userRepository.findUserByAccountNumber(retrieveAcctNameRequest.getAccountNumber());
        String providedName = foundUser.getName();
        Request request = new Request.Builder()
                .url("https://api.paystack.co/bank/resolve?account_number="
                        +retrieveAcctNameRequest.getAccountNumber()+"&bank_code="
                        +retrieveAcctNameRequest.getBankCode())
                .get()
                .addHeader("Authorization", "Bearer " + payStack_key)
                .build();
        try (var response = client.newCall(request).execute().body()) {
            ObjectMapper mapper = new ObjectMapper();
            String mappedAccountName = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                    .readValue(response.string(), GetAcctDeserializationResponse.class)
                    .getData()
                    .getAccount_name();
            boolean name = checkDifferenceOfTwoNames(mappedAccountName, providedName);
            if (name) return providedName;
            else return mappedAccountName;
        }
    }

    @Override
    public void updateUser(String accountNumber) {
        var existingUser = userRepository.findUserByAccountNumber(accountNumber);
        existingUser.setVerified(true);
    }

    @Override
    public String verifyBankAccount(VerifyAccountRequest verifyAccountRequest) throws IOException {
        User existingUser = userRepository.findUserByAccountNumber(verifyAccountRequest.getAccountNumber());
        if (existingUser == null)
            throw new UserRegistrationException("User with the account number " + verifyAccountRequest.getAccountNumber() +
                    "does not exist");
        String providedName = existingUser.getName();
        //String providedName = verifyAccountRequest.getAccountName();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.paystack.co/bank/resolve?account_number=" +
                        verifyAccountRequest.getAccountNumber() +
                        "&bank_code=" + verifyAccountRequest.getBankCode())
                .get()
                .addHeader("Authorization", "Bearer " + payStack_key)
                .build();
        try (var response = client.newCall(request).execute().body()) {
            ObjectMapper mapper = new ObjectMapper();
            String mappedAccountName = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(response.string(), GetAcctDeserializationResponse.class)
                    .getData()
                    .getAccount_name();
//        String verifiedName = retrieveAcctName(verifyAccountRequest);
            boolean checkNames = checkDifferenceOfTwoNames(providedName, mappedAccountName);
//        boolean nameMatch = verifiedName.equals(existingUser.getName()) ||
//                verifiedName.equals(verifyAccountRequest.getAccountName());
            if (!existingUser.isVerified() && checkNames) updateUser(existingUser.getAccountNumber());
            return existingUser.getName() + "has been verified successfully";
        }
    }

    @Override
    public User findUserByAccountNumber(String accountNumber) {
        User foundUser = userRepository.findUserByAccountNumber(accountNumber);
            if(foundUser == null) throw new UserRegistrationException("User not found");
        return foundUser;
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
