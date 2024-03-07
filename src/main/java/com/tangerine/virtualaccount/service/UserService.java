package com.tangerine.virtualaccount.service;


import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.request.GetAllVirtualAccRequest;
import com.tangerine.virtualaccount.response.CreateAccountResponse;
import com.tangerine.virtualaccount.response.GetAllVirtualAccResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<CreateAccountResponse> createAcc (CreateAccountRequest createAccountRequest);

    ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAccByDate(GetAllVirtualAccRequest getAllVirtualAccRequest);

    ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAcc(GetAllVirtualAccRequest getAllVirtualAccRequest);

    ResponseEntity<?> downloadAllVirtualAcc(GetAllVirtualAccRequest getAllVirtualAccRequest);

    ResponseEntity<?> downloadAllVirtualAccByDate(GetAllVirtualAccRequest getAllVirtualAccRequest);
}
