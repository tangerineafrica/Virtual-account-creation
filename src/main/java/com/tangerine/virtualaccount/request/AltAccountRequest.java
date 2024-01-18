package com.tangerine.virtualaccount.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AltAccountRequest {

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
