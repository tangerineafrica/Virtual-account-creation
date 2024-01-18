package com.tangerine.virtualaccount.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private String first_name;
    private String last_name;
    private String mobile_num;
    private String email;
    private String bvn;
    private String dob;
    private String address;
    private String gender;
    private String customer_identifier;
    private String beneficiary_account;
}
