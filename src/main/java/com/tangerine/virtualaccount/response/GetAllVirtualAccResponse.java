package com.tangerine.virtualaccount.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllVirtualAccResponse {
    private int status;
    private boolean success;
    private String message;
    private AllVirtualAccResponse[] data;

}
