package com.ncdmb.canteen.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AmountAndDateDto(
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {}
