package com.ncdmb.canteen.dtos.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDto {
    private Integer id;
    private String name;
    private String description;
}