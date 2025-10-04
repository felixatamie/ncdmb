package com.ncdmb.canteen.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NCDMBStaffResponseDto {
    private Integer id;
    private String staffId;
    private String name;
    private String email;
    private String phone;
    private String nfcId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CadreResponseDto cadre;
}