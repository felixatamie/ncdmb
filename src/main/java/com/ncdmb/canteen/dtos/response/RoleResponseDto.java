package com.ncdmb.canteen.dtos.response;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Set<PermissionResponseDto> permissions;
}