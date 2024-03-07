package com.tangerine.virtualaccount.controller;


import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.request.GetAllVirtualAccRequest;
import com.tangerine.virtualaccount.response.CreateAccountResponse;
import com.tangerine.virtualaccount.response.GetAllVirtualAccResponse;
import com.tangerine.virtualaccount.serviceImplimentation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/app/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://tangerine-web-form.vercel.app")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/create-acc")
    public ResponseEntity<CreateAccountResponse> createVirtualAcc (@Valid @RequestBody CreateAccountRequest createAccountRequest) {
            return userServiceImpl.createAcc(createAccountRequest);
    }

    @GetMapping("/get-acc-by-date")
    public ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAccByDate(GetAllVirtualAccRequest getAllVirtualAccRequest) {
        return userServiceImpl.getAllVirtualAccByDate(getAllVirtualAccRequest);
    }

    @GetMapping("/get-all-acc")
    public ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAcc (GetAllVirtualAccRequest getAllVirtualAccRequest) {
        return userServiceImpl.getAllVirtualAcc(getAllVirtualAccRequest);
    }

    @GetMapping("/download-all-acc")
    public ResponseEntity<?> downloadAllVirtualAcc (GetAllVirtualAccRequest getAllVirtualAccRequest) {
        return userServiceImpl.downloadAllVirtualAcc(getAllVirtualAccRequest);
    }

    @GetMapping("/download-acc-by-date")
    public ResponseEntity<?> downloadAllVirtualAccByDate (GetAllVirtualAccRequest getAllVirtualAccRequest) {
        return userServiceImpl.downloadAllVirtualAccByDate(getAllVirtualAccRequest);
    }
}
