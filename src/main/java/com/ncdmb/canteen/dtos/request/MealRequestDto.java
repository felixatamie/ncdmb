package com.ncdmb.canteen.dtos.request;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRequestDto {
    private String name;
    private BigDecimal price;
    private Boolean isActive;
    private Integer canteenId;
    private Integer portionOrUnit;
    private Integer quantity;
}