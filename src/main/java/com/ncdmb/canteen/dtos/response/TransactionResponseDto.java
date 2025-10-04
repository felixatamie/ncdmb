package com.ncdmb.canteen.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionResponseDto {
    private Integer transactionId;
    private BigDecimal totalAmount;
    private String ticketId;
    private LocalDateTime createdAt;

    private Boolean isStaffTransaction;

    private String staffId;
    private BigDecimal staffPaid;
    private BigDecimal ncdmbPaid;

    private List<MealLineItemResponseDto> mealLineItems;
}

/*
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private CanteenUser operatorName;
  private Canteen canteenName;
  private Object transactedBy;
  private Integer transactionId;
  private List<MealLineItemResponseDto> mealLineItems;

public class MealLineItemResponseDto {

    private Integer mealId;
    private String mealName;
    private Integer quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal totalAmount;

}
 */
