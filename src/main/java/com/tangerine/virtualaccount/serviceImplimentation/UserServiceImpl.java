package com.tangerine.virtualaccount.serviceImplimentation;

import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.CreateAccountResponse;
import com.tangerine.virtualaccount.util.VirtualAccountUtil;
import com.tangerine.virtualaccount.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    //For Termii SMS
    //private final TermiiSmsServiceImpl termiiSmsService;

    //For Dotgo SMS
    private final DotgoSmsServiceImpl dotgoSmsService;

    //For Sendgrid Email
    private final SendGridEmailServiceImpl sendGridEmailService;

    @Value("${squad.authorization.key}")
    private String auth_key;
    @Value("${beneficiary.account}")
    private String beneficiary_account;


    @Override
    public ResponseEntity<CreateAccountResponse> createAcc(CreateAccountRequest createAccountRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +auth_key);

        HttpEntity<AltAccountRequest> entity = new HttpEntity<>(mapToAltAccountRequest(createAccountRequest), headers);
        System.out.println("Here's what goes for the virtual acc: " + mapToAltAccountRequest(createAccountRequest));
        try {

            CreateAccountResponse response = restTemplate.exchange(VirtualAccountUtil.SQUAD_CREATE_ACC_URL, HttpMethod.POST, entity, CreateAccountResponse.class).getBody();
            System.out.println("Response from Squad: " + response.getData().getFirst_name());

            //For Termii SMS Begins. Also, uncomment the TermiiServiceImpl autowire on line 22
//            TermiiSmsRequest termiiSmsRequest = new TermiiSmsRequest();
//            termiiSmsService.sendSms(response.getData(), mapToAltAccountRequest(createAccountRequest), createAccountRequest, termiiSmsRequest);
//            System.out.println("Here are the sms details: " + response.getData() + " " + createAccountRequest.getMobile_num() + " " + termiiSmsRequest);

            //For Termii SMS Ends


            //For Dotgo SMS Begins
            dotgoSmsService.RequestToKonnect(response.getData(), mapToAltAccountRequest(createAccountRequest), createAccountRequest);
            System.out.println("Here are the sms details: " + response.getData() + " " + createAccountRequest.getMobile_num());

            //For Dotgo SMS Ends
            System.out.println("SMS Sent");

            //For Sendgrid email Begins
            sendGridEmailService.sendEmail(response.getData(), mapToAltAccountRequest(createAccountRequest), createAccountRequest);
            System.out.println("Email sent");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
           // return response;
        } catch (HttpClientErrorException e){
            e.printStackTrace();
            CreateAccountResponse errorResponse = new CreateAccountResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(errorResponse, e.getStatusCode());
            //return errorResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }




    private AltAccountRequest mapToAltAccountRequest(CreateAccountRequest createAccountRequest) {
        AltAccountRequest altAccountRequest = new AltAccountRequest();
        String username = "Tangerine" + createAccountRequest.getProduct_type().value() + "-" + createAccountRequest.getFirst_name();

        altAccountRequest.setFirst_name(username);
        altAccountRequest.setLast_name(createAccountRequest.getLast_name());
        altAccountRequest.setMobile_num(createAccountRequest.getMobile_num());
        altAccountRequest.setEmail(createAccountRequest.getEmail());
        altAccountRequest.setBvn(createAccountRequest.getBvn());
        altAccountRequest.setDob(createAccountRequest.getDob());
        altAccountRequest.setAddress(createAccountRequest.getAddress());
        altAccountRequest.setGender(createAccountRequest.getGender().value());
        altAccountRequest.setCustomer_identifier(createAccountRequest.getCustomer_identifier());
        altAccountRequest.setBeneficiary_account(beneficiary_account);

        return altAccountRequest;
    }

}
