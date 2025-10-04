package com.ncdmb.canteen.dtos.request;


import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionRequestDto {
    private BigDecimal totalAmount;
    private String ticketId;
    private Integer operatorId;
    private Integer canteenId;

    private Boolean isStaffTransaction;
    // Staff transaction fields

    private String staffId;
    /*private BigDecimal staffPaid;
    private BigDecimal ncdmbPaid;*/

    private List<MealLineItemRequestDto> mealLineItems;
}
