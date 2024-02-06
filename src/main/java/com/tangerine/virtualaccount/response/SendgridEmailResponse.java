package com.tangerine.virtualaccount.response;

import com.sendgrid.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendgridEmailResponse {
    private int status_code;
}
