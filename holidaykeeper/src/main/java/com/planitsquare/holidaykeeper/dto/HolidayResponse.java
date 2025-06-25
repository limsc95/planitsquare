package com.planitsquare.holidaykeeper.dto;

import com.planitsquare.holidaykeeper.entity.Holiday;

import java.time.LocalDate;
import java.util.List;

public record HolidayResponse(
        LocalDate date,
        String localName,
        String name,
        String countryCode,
        boolean fixed,
        boolean global,
        List<String> types,
        List<String> counties,
        Integer launchYear
) {
    public HolidayResponse(Holiday holiday){
        this(
                holiday.getDate(),
                holiday.getLocalName(),
                holiday.getName(),
                holiday.getCountry().getCountryCode(),
                holiday.getFixed(),
                holiday.getGlobal(),
                convertCommaSeparatedToList(holiday.getTypes()),
                convertCommaSeparatedToList(holiday.getCounties()),
                holiday.getLaunchYear()
        );
    }

    public static HolidayResponse from(Holiday holiday){
        return new HolidayResponse(holiday);
    }

    private static List<String> convertCommaSeparatedToList(String value){
        if (value == null || value.isBlank()) return null;
        return List.of(value.split(","));
    }
}