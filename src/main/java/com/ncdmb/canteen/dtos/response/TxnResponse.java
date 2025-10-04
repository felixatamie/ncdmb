package com.ncdmb.canteen.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TxnResponse {

    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String canteenOperatorName;
    private String canteenName;
    private Object transactedBy;
    private Integer transactionId;
    private List<MealLineItemResponseDto> mealLineItems;

}
