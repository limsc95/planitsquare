package com.planitsquare.holidaykeeper.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidaySearchCondition {
    private String countryCode;
    private Integer year;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String types;
}