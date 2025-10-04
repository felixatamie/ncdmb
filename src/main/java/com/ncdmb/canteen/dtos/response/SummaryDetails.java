package com.ncdmb.canteen.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryDetails {

    private ArrayList<SummaryResponse> summaryResponseList;
    private BigDecimal grandTotalPerPeriod;
}
