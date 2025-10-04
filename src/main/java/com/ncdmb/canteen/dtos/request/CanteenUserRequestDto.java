package com.ncdmb.canteen.dtos.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanteenUserRequestDto {
    private String username;
    private String password; // Not password hash!
    private Boolean isActive;
    private Integer roleId;
    private Integer canteenId;
    private String name;
    private String phone;
}