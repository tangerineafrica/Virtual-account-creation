package com.tangerine.virtualaccount.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermiiSmsRequest {

    private String to;
    private String from;
    private String sms;
    private String type;
    private String channel;
    private String api_key;
}
