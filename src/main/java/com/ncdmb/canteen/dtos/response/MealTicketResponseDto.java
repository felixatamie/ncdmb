package com.ncdmb.canteen.dtos.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealTicketResponseDto {
    private String id;
    private LocalDateTime issuedAt;
    private TransactionResponseDto transaction;
}