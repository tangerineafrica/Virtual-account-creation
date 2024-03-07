package com.tangerine.virtualaccount.serviceImplimentation;

import com.sendgrid.Response;
import com.tangerine.virtualaccount.request.AltAccountRequest;
import com.tangerine.virtualaccount.request.CreateAccountRequest;
import com.tangerine.virtualaccount.request.GetAllVirtualAccRequest;
import com.tangerine.virtualaccount.response.*;
import com.tangerine.virtualaccount.util.VirtualAccountUtil;
import com.tangerine.virtualaccount.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAccByDate(GetAllVirtualAccRequest getAllVirtualAccRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +auth_key);

        getAllVirtualAccRequest.setPerPage(500);
        HttpEntity<GetAllVirtualAccRequest> entity = new HttpEntity<>(getAllVirtualAccRequest, headers);

        String mainUrl = VirtualAccountUtil.SQUAD_GET_ALL_ACC + "?perPage=" + getAllVirtualAccRequest.getPerPage() + "&startDate=" + getAllVirtualAccRequest.getStartDate() + "&endDate=" + getAllVirtualAccRequest.getEndDate();

        try {
            GetAllVirtualAccResponse getAllResponse = restTemplate.exchange(mainUrl, HttpMethod.GET, entity, GetAllVirtualAccResponse.class).getBody();
            return new ResponseEntity<>(getAllResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            GetAllVirtualAccResponse getAllVirtualAccErrorResponse = new GetAllVirtualAccResponse();
            getAllVirtualAccErrorResponse.setSuccess(false);
            getAllVirtualAccErrorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(getAllVirtualAccErrorResponse, e.getStatusCode());
        }

    }


    @Override
    public ResponseEntity<GetAllVirtualAccResponse> getAllVirtualAcc(GetAllVirtualAccRequest getAllVirtualAccRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +auth_key);

        getAllVirtualAccRequest.setPerPage(500);
        HttpEntity<GetAllVirtualAccRequest> entity = new HttpEntity<>(getAllVirtualAccRequest, headers);

        String mainUrl = VirtualAccountUtil.SQUAD_GET_ALL_ACC + "?perPage=" + getAllVirtualAccRequest.getPerPage();

        try {
            GetAllVirtualAccResponse getAllResponse = restTemplate.exchange(mainUrl, HttpMethod.GET, entity, GetAllVirtualAccResponse.class).getBody();
            assert getAllResponse != null;

            return new ResponseEntity<>(getAllResponse, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            GetAllVirtualAccResponse getAllVirtualAccErrorResponse = new GetAllVirtualAccResponse();
            getAllVirtualAccErrorResponse.setSuccess(false);
            getAllVirtualAccErrorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(getAllVirtualAccErrorResponse, e.getStatusCode());
        }
    }

    @Override
    public ResponseEntity<?> downloadAllVirtualAcc(GetAllVirtualAccRequest getAllVirtualAccRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +auth_key);

        getAllVirtualAccRequest.setPerPage(500);
        HttpEntity<GetAllVirtualAccRequest> entity = new HttpEntity<>(getAllVirtualAccRequest, headers);

        String mainUrl = VirtualAccountUtil.SQUAD_GET_ALL_ACC + "?perPage=" + getAllVirtualAccRequest.getPerPage();
        try {
            //Fetch all Virtual Account details from Squad
            GetAllVirtualAccResponse getAllResponse = restTemplate.exchange(mainUrl, HttpMethod.GET, entity, GetAllVirtualAccResponse.class).getBody();
            assert getAllResponse != null;

            // Create a list to add virtual accounts to
            List<AllVirtualAccResponse> accResponseList = new ArrayList<>();
            List<AllVirtualAccResponse> mainAccResponseList = new ArrayList<>();

            // Loop through response from Squad and add virtual accounts to the above list
            for (AllVirtualAccResponse data : getAllResponse.getData()) {
                accResponseList.add(data);
            }

            // Loop through list to remove test accounts
            for (int i = 0; i < accResponseList.size() - 16; i++) {
                mainAccResponseList.add(accResponseList.get(i));
            }

            // Create an Excel workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            int rowNumber = 0;
            Row headerRow = sheet.createRow(rowNumber++);
            headerRow.createCell(0).setCellValue("FIRSTNAME");
            headerRow.createCell(1).setCellValue("LASTNAME");
            headerRow.createCell(2).setCellValue("CUSTOMER IDENTIFIER");
            headerRow.createCell(3).setCellValue("VIRTUAL ACCOUNT NUMBER");
            headerRow.createCell(4).setCellValue("DATE CREATED");

            // for (AllVirtualAccResponse mapResponse : accResponseList) {
            for (AllVirtualAccResponse mapResponse : mainAccResponseList) {
                Row dataRow = sheet.createRow(rowNumber++);
                dataRow.createCell(0).setCellValue(mapResponse.getFirst_name());
                dataRow.createCell(1).setCellValue(mapResponse.getLast_name());
                dataRow.createCell(2).setCellValue(mapResponse.getCustomer_identifier());
                dataRow.createCell(3).setCellValue(mapResponse.getVirtual_account_number());
                dataRow.createCell(4).setCellValue(mapResponse.getCreated_at().toString());
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                byte[] excelBytes = outputStream.toByteArray();

                HttpHeaders downloadHeaders = new HttpHeaders();
                downloadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                downloadHeaders.setContentDisposition(ContentDisposition.attachment().filename("virtual-accounts.xlsx").build());

                return new ResponseEntity<>(excelBytes, downloadHeaders, HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            GetAllVirtualAccResponse getAllVirtualAccErrorResponse = new GetAllVirtualAccResponse();
            getAllVirtualAccErrorResponse.setSuccess(false);
            getAllVirtualAccErrorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(getAllVirtualAccErrorResponse, e.getStatusCode());
        }
    }

    @Override
    public ResponseEntity<?> downloadAllVirtualAccByDate(GetAllVirtualAccRequest getAllVirtualAccRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth_key);

        getAllVirtualAccRequest.setPerPage(500);
        HttpEntity<GetAllVirtualAccRequest> entity = new HttpEntity<>(getAllVirtualAccRequest, headers);

        String mainUrl = VirtualAccountUtil.SQUAD_GET_ALL_ACC + "?perPage=" + getAllVirtualAccRequest.getPerPage() + "&startDate=" + getAllVirtualAccRequest.getStartDate() + "&endDate=" + getAllVirtualAccRequest.getEndDate();

        try {
            GetAllVirtualAccResponse getAllResponse = restTemplate.exchange(mainUrl, HttpMethod.GET, entity, GetAllVirtualAccResponse.class).getBody();
            assert getAllResponse != null;

            // Create a list to add virtual accounts to
            List<AllVirtualAccResponse> accResponseList = new ArrayList<>();

            // Loop through response from Squad and add virtual accounts to the above list
            for (AllVirtualAccResponse data : getAllResponse.getData()) {
                accResponseList.add(data);
            }

            // Create an Excel workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            int rowNumber = 0;
            Row headerRow = sheet.createRow(rowNumber++);
            headerRow.createCell(0).setCellValue("FIRSTNAME");
            headerRow.createCell(1).setCellValue("LASTNAME");
            headerRow.createCell(2).setCellValue("CUSTOMER IDENTIFIER");
            headerRow.createCell(3).setCellValue("VIRTUAL ACCOUNT NUMBER");
            headerRow.createCell(4).setCellValue("DATE CREATED");

            for (AllVirtualAccResponse mapResponse : accResponseList) {
                Row dataRow = sheet.createRow(rowNumber++);
                dataRow.createCell(0).setCellValue(mapResponse.getFirst_name());
                dataRow.createCell(1).setCellValue(mapResponse.getLast_name());
                dataRow.createCell(2).setCellValue(mapResponse.getCustomer_identifier());
                dataRow.createCell(3).setCellValue(mapResponse.getVirtual_account_number());
                dataRow.createCell(4).setCellValue(mapResponse.getCreated_at().toString());
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                byte[] excelBytes = outputStream.toByteArray();

                HttpHeaders downloadHeaders = new HttpHeaders();
                downloadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                downloadHeaders.setContentDisposition(ContentDisposition.attachment().filename("virtual-accounts-by-date.xlsx").build());

                return new ResponseEntity<>(excelBytes, downloadHeaders, HttpStatus.OK);
            } catch (HttpClientErrorException e) {
                e.printStackTrace();
                GetAllVirtualAccResponse getAllVirtualAccErrorResponse = new GetAllVirtualAccResponse();
                getAllVirtualAccErrorResponse.setSuccess(false);
                getAllVirtualAccErrorResponse.setMessage(e.getMessage());
                return new ResponseEntity<>(getAllVirtualAccErrorResponse, e.getStatusCode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
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
        altAccountRequest.setBeneficiary_account("0138427972");
        return altAccountRequest;
    }

}
