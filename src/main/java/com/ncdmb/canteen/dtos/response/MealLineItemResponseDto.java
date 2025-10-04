package com.ncdmb.canteen.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealLineItemResponseDto {

    private String mealName;
    private Integer quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal totalAmount;

}