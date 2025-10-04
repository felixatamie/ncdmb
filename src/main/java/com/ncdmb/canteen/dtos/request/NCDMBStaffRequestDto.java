package com.ncdmb.canteen.dtos.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NCDMBStaffRequestDto {
    private String staffId;
    private String name;
    private String email;
    private String phone;
    private String status; // enum: ACTIVE, SUSPENDED, INACTIVE
    private Integer cadreId; // FK
}