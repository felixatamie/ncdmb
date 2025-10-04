package com.ncdmb.canteen.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadreResponseDto {
    private Integer id;
    private String name;
    private BigDecimal ncdmbPercent;
    private BigDecimal staffPercent;
    private String description;
}
