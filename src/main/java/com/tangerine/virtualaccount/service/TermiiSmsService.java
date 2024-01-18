package com.tangerine.virtualaccount.service;

import com.mashape.unirest.http.HttpResponse;
import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.request.TermiiSmsRequest;
import com.tangerine.virtualaccount.response.AccountResponse;

public interface TermiiSmsService {
    HttpResponse<String> sendSms(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest accountRequest, TermiiSmsRequest termiiSmsRequest);
}
