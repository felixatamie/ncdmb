package com.ncdmb.canteen.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StaffTransactionDto(
        Integer id,
        BigDecimal totalAmount,
        LocalDateTime transactionDate,
        String staffName,
        BigDecimal staffPaid,
        BigDecimal ncdmbPaid,
        String cadreName,
        String canteenName,
        String staffId
) {}
