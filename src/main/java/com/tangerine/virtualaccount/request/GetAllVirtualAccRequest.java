package com.tangerine.virtualaccount.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllVirtualAccRequest {
    private int page;
    private int perPage;
    private String startDate;
    private String endDate;

}
