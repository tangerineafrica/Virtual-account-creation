package com.tangerine.virtualaccount.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountResponse {
    private boolean success;
    private String message;
    private AccountResponse data;
    private DotGoSmsResponse smsSuccess;
    private SendgridEmailResponse emailSuccess;

}
