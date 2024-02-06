package com.tangerine.virtualaccount.serviceImplimentation;

import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.response.AccountResponse;
import com.tangerine.virtualaccount.service.DotgoSmsService;
import com.tangerine.virtualaccount.util.VirtualAccountUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DotgoSmsServiceImpl implements DotgoSmsService {

    @Value("${dotgo.sms.key}")
    private String dotgo_auth_key;

    @Override
    public String RequestToKonnect(AccountResponse accountResponse, AltAccountRequest altAccountRequest, CreateAccountRequest createAccountRequest) throws JSONException {

        HttpURLConnection urlConnection;
        JSONObject json = new JSONObject();
        json.put("id", altAccountRequest.getCustomer_identifier());


        JSONArray jsonArray = new JSONArray();
        jsonArray.put("+234" + altAccountRequest.getMobile_num());
        json.put("to", jsonArray);

        String messageReceiver = altAccountRequest.getFirst_name();
        String smsBody = "Dear " + messageReceiver + ",\n" + "Thanks for your interest in our insurance product." + "\n" + "For premium payments, kindly make payment to your dedicated bank account." + "\n" + "Bank- GTB" + "\n" + "Acct Number- " + accountResponse.getVirtual_account_number();
        json.put("sender_mask", "TANGERINE");
        json.put("body", smsBody);
        String data = json.toString();
        String response = null;

        String newResponse = null;
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(VirtualAccountUtil.DOTGO_SMS_URL).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", dotgo_auth_key);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.close();
            outputStream.close();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            response = sb.toString();
            newResponse = response.replace("\"", "");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newResponse;
    }

}
