package com.tangerine.virtualaccount.controller;


import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.CreateAccountResponse;
import com.tangerine.virtualaccount.serviceImplimentation.UserServiceImpl;
import com.tangerine.virtualaccount.util.ErrorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/app/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/create-acc")
    public ResponseEntity<CreateAccountResponse> createVirtualAcc (@Valid @RequestBody CreateAccountRequest createAccountRequest) {
            return userServiceImpl.createAcc(createAccountRequest);
    }
}
