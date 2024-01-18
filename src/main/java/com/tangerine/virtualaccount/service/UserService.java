package com.tangerine.virtualaccount.service;


import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.CreateAccountResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<CreateAccountResponse> createAcc (CreateAccountRequest createAccountRequest);
}
