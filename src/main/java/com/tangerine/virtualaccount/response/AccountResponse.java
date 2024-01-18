package com.tangerine.virtualaccount.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String first_name;
    private String last_name;
    private String bank_code;
    private String virtual_account_number;
    private String customer_identifier;
}
