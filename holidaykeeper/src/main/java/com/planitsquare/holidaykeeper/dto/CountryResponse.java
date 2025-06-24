package com.planitsquare.holidaykeeper.dto;

import com.planitsquare.holidaykeeper.entity.Country;

public record CountryResponse(
        String countryCode,
        String name
) {
    public CountryResponse(Country country) {
        this(country.getCountryCode(), country.getName());
    }
}
