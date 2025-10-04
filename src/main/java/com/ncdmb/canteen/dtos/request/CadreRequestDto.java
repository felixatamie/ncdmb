package com.ncdmb.canteen.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadreRequestDto {
    private String name;
    private BigDecimal ncdmbPercent;
    private BigDecimal staffPercent;
    private String description;
}