package com.ncdmb.canteen.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanteenRequestDto {

    private String name;
    private String email;
    private String phoneNumber;

}