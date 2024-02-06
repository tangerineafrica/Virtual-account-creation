package com.tangerine.virtualaccount.service;

import com.sendgrid.Response;
import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.AccountResponse;

import java.io.IOException;

public interface SendGridEmailService {
    Response sendEmail(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest accountRequest) throws IOException;
}
