package com.tangerine.virtualaccount.service;

import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.AccountResponse;
import org.json.JSONException;

public interface DotgoSmsService {

    void RequestToKonnect(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest createAccountRequest) throws JSONException;

}
