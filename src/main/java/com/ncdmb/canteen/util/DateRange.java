package com.ncdmb.canteen.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DateRange {

    private LocalDateTime start;
    private LocalDateTime end;

    // constructor + getters

    public static DateRange getDateRange(String period) {
        LocalDate today = LocalDate.now();
        switch (period.toUpperCase()) {
            case "TODAY":
                return new DateRange(today.atStartOfDay(), today.plusDays(1).atStartOfDay().minusSeconds(1));
            case "YESTERDAY":
                LocalDate yesterday = today.minusDays(1);
                return new DateRange(yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay().minusSeconds(1));
            case "THIS_WEEK":
                LocalDate weekStart = today.with(java.time.DayOfWeek.MONDAY);
                LocalDate weekEnd = weekStart.plusDays(6);
                return new DateRange(weekStart.atStartOfDay(), weekEnd.plusDays(1).atStartOfDay().minusSeconds(1));
            case "LAST_WEEK":
                LocalDate lastWeekStart = today.with(java.time.DayOfWeek.MONDAY).minusWeeks(1);
                LocalDate lastWeekEnd = lastWeekStart.plusDays(6);
                return new DateRange(lastWeekStart.atStartOfDay(), lastWeekEnd.plusDays(1).atStartOfDay().minusSeconds(1));
            case "THIS_MONTH":
                LocalDate monthStart = today.withDayOfMonth(1);
                LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
                return new DateRange(monthStart.atStartOfDay(), monthEnd.plusDays(1).atStartOfDay().minusSeconds(1));
            case "LAST_MONTH":
                LocalDate lastMonthStart = today.withDayOfMonth(1).minusMonths(1);
                LocalDate lastMonthEnd = lastMonthStart.plusMonths(1).minusDays(1);
                return new DateRange(lastMonthStart.atStartOfDay(), lastMonthEnd.plusDays(1).atStartOfDay().minusSeconds(1));
            case "THIS_YEAR":
                LocalDate yearStart = today.withDayOfYear(1);
                LocalDate yearEnd = yearStart.plusYears(1).minusDays(1);
                return new DateRange(yearStart.atStartOfDay(), yearEnd.plusDays(1).atStartOfDay().minusSeconds(1));
            case "LAST_YEAR":
                LocalDate lastYearStart = today.withDayOfYear(1).minusYears(1);
                LocalDate lastYearEnd = lastYearStart.plusYears(1).minusDays(1);
                return new DateRange(lastYearStart.atStartOfDay(), lastYearEnd.plusDays(1).atStartOfDay().minusSeconds(1));
            default:
                return new DateRange(null, null); // no filter
        }
    }
}


