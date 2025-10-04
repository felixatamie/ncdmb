package com.ncdmb.canteen.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NCDMBStaffAndNonStaffCustomersDto {

    private BigDecimal totalAmount;
    private LocalDateTime transactionDate;
    private String staffName;
    private BigDecimal staffPaid;
    private BigDecimal ncdmbPaid;
    private String canteenName;
    private Integer transactionId;
}
