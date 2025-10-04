package com.ncdmb.canteen.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanteenResponseDto {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
}