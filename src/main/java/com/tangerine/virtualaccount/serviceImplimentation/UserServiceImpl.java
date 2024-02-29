package com.tangerine.virtualaccount.serviceImplimentation;

import com.sendgrid.Response;
import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.request.GetAllVirtualAccRequest;
import com.tangerine.virtualaccount.response.CreateAccountResponse;
import com.tangerine.virtualaccount.response.DotGoSmsResponse;
import com.tangerine.virtualaccount.response.GetAllVirtualAccResponse;
import com.tangerine.virtualaccount.response.SendgridEmailResponse;
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

    @Override
    public ResponseEntity<CreateAccountResponse> createAcc(CreateAccountRequest createAccountRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +auth_key);

        HttpEntity<AltAccountRequest> entity = new HttpEntity<>(mapToAltAccountRequest(createAccountRequest), headers);

        try {

            CreateAccountResponse response = restTemplate.exchange(VirtualAccountUtil.SQUAD_CREATE_ACC_URL, HttpMethod.POST, entity, CreateAccountResponse.class).getBody();


            //For Termii SMS Begins. Also, uncomment the TermiiServiceImpl autowire on line 22
//            TermiiSmsRequest termiiSmsRequest = new TermiiSmsRequest();
//            termiiSmsService.sendSms(response.getData(), mapToAltAccountRequest(createAccountRequest), createAccountRequest, termiiSmsRequest);
//            System.out.println("Here are the sms details: " + response.getData() + " " + createAccountRequest.getMobile_num() + " " + termiiSmsRequest);

            //For Termii SMS Ends


            //For Dotgo SMS Begins
            String smsResponse = dotgoSmsService.RequestToKonnect(response.getData(), mapToAltAccountRequest(createAccountRequest), createAccountRequest);
            DotGoSmsResponse dotGoSmsResponse = new DotGoSmsResponse();
            dotGoSmsResponse.setStatus(smsResponse);
            response.setSmsSuccess(dotGoSmsResponse);
            //For Dotgo SMS Ends


            //For Sendgrid email Begins
            Response emailResponse = sendGridEmailService.sendEmail(response.getData(), mapToAltAccountRequest(createAccountRequest), createAccountRequest);
            SendgridEmailResponse sendgridEmailResponse = new SendgridEmailResponse();
            sendgridEmailResponse.setStatus_code(emailResponse.getStatusCode());
            response.setEmailSuccess(sendgridEmailResponse);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (HttpClientErrorException e){
            e.printStackTrace();
            CreateAccountResponse errorResponse = new CreateAccountResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(errorResponse, e.getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAcc(GetAllVirtualAccRequest getAllVirtualAccRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +auth_key);

        HttpEntity<GetAllVirtualAccRequest> entity = new HttpEntity<>(getAllVirtualAccRequest, headers);

        try {
            GetAllVirtualAccResponse getAllResponse = restTemplate.exchange(VirtualAccountUtil.SQUAD_GET_ALL_ACC, HttpMethod.GET, entity, GetAllVirtualAccResponse.class).getBody();
            System.out.println("Here's the getAllResponse: " + getAllResponse);
            return new ResponseEntity<>(getAllResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            GetAllVirtualAccResponse getAllVirtualAccErrorResponse = new GetAllVirtualAccResponse();
            getAllVirtualAccErrorResponse.setSuccess(false);
            getAllVirtualAccErrorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(getAllVirtualAccErrorResponse, e.getStatusCode());
        }
        //return null;
    }


    private AltAccountRequest mapToAltAccountRequest(CreateAccountRequest createAccountRequest) {
        AltAccountRequest altAccountRequest = new AltAccountRequest();

        altAccountRequest.setFirst_name(createAccountRequest.getFirst_name());
        altAccountRequest.setLast_name(createAccountRequest.getLast_name());
        altAccountRequest.setMobile_num(createAccountRequest.getMobile_num());
        altAccountRequest.setEmail(createAccountRequest.getEmail());
        altAccountRequest.setBvn(createAccountRequest.getBvn());
        altAccountRequest.setDob(createAccountRequest.getDob());
        altAccountRequest.setAddress(createAccountRequest.getAddress());
        altAccountRequest.setGender(createAccountRequest.getGender().value());
        altAccountRequest.setCustomer_identifier(createAccountRequest.getCustomer_identifier());

        return altAccountRequest;
    }

}
