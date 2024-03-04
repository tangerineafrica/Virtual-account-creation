package com.tangerine.virtualaccount.serviceImplimentation;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.AccountResponse;
import com.tangerine.virtualaccount.service.SendGridEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailServiceImpl implements SendGridEmailService {

    @Value("${sendgrid.email.key}")
    private String SENDGRID_API_KEY;
    @Override
    public Response sendEmail(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest accountRequest) throws IOException {

        String messageReceiver = altAccountRequest.getFirst_name();

        Email from = new Email("hello@tangerine.africa");
        Email to = new Email(accountRequest.getEmail(), messageReceiver);

        String subject = "Welcome to Tangerine Life!";
        Content content = new Content("text/plain", "Dear " + messageReceiver + ",\n" + "Thanks for your interest in our insurance product." + "\n" + "For premium payments, kindly make payment to your dedicated bank account." + "\n" + "Bank- GTB" + "\n" + "Acct Number- " + accountResponse.getVirtual_account_number());

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
