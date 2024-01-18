package com.tangerine.virtualaccount.serviceImplimentation;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.request.TermiiSmsRequest;
import com.tangerine.virtualaccount.response.AccountResponse;
import com.tangerine.virtualaccount.service.TermiiSmsService;
import com.tangerine.virtualaccount.util.VirtualAccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermiiSmsServiceImpl implements TermiiSmsService {

    @Value("${termii.sms.key}")
    private String termii_auth_key;
    @Override
    public HttpResponse<String> sendSms(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest accountRequest, TermiiSmsRequest termiiSmsRequest) {

        String messageReceiver = altAccountRequest.getFirst_name().substring(13);

        //Create the product type name for the SMS
        String smsProduct = accountRequest.getProduct_type().toString().replace("_", " ").toLowerCase();
        String smsBody = "Dear " + messageReceiver + ",\n" + "Thanks for your interest in our" + smsProduct + "." + "\n" + "For premium payments, kindly make payment to your dedicated bank account." + "\n" + "Bank- GTB" + "\n" + "Acct Number- " + accountResponse.getVirtual_account_number();

        //Remove first(0) from phone number and add 234 to the front
        String smsNumber = "234" + accountRequest.getMobile_num().substring(1);
        termiiSmsRequest.setTo(smsNumber);
        termiiSmsRequest.setFrom("Tangerine");
        termiiSmsRequest.setSms(smsBody);
        termiiSmsRequest.setType("plain");
        termiiSmsRequest.setApi_key(termii_auth_key);
        termiiSmsRequest.setChannel("generic");

        String body = "{" +
                "\"to\":\"" + smsNumber + "\"," +
                "\"from\":\"" + termiiSmsRequest.getFrom() + "\"," +
                "\"sms\":\"" + termiiSmsRequest.getSms() + "\"," +
                "\"type\":\"" + termiiSmsRequest.getType() + "\"," +
                "\"api_key\":\"" + termiiSmsRequest.getApi_key() + "\"," +
                "\"channel\":\"" + termiiSmsRequest.getChannel() + "\"" +
                "}";

        System.out.println(body);

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post(VirtualAccountUtil.TERMII_SMS_URL)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();

            return response;

        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
