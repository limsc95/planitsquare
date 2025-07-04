package com.planitsquare.holidaykeeper.dto;


import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record HolidaySearchCondition(
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "countryCode는 영문 2자리여야 합니다.")
        String countryCode,

        @Min(value = 1900, message = "year는 1900 이상이어야 합니다.")
        @Max(value = 2100, message = "year는 2100 이하이어야 합니다.")
        Integer year,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fromDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate toDate,

        String types
) {}