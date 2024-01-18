package com.tangerine.virtualaccount.service;

import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.AccountResponse;

import java.io.IOException;

public interface SendGridEmailService {
    String sendEmail(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest accountRequest) throws IOException;
}
