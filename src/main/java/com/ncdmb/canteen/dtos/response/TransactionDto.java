package com.ncdmb.canteen.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class TransactionDto {

    private Integer id;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private Integer operatorId;
    private String canteenName;

}
