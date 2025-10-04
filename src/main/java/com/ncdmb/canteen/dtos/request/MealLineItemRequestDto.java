package com.ncdmb.canteen.dtos.request;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealLineItemRequestDto {
    private Integer mealId;
    private Integer quantity;
    private BigDecimal pricePerUnit;
}