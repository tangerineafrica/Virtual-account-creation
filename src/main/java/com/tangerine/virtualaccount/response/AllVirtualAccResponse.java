package com.tangerine.virtualaccount.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.util.Date;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllVirtualAccResponse {
    private String first_name;
    private String last_name;
    private String customer_identifier;
    private String virtual_account_number;
    private Date created_at;
    private Date updated_at;
    private String bank_code;
    private String beneficiary_account;
}
