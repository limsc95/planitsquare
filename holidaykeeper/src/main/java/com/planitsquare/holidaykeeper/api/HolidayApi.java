package com.planitsquare.holidaykeeper.api;

import com.planitsquare.holidaykeeper.dto.CountryResponse;
import com.planitsquare.holidaykeeper.dto.HolidayResponse;
import com.planitsquare.holidaykeeper.entity.Country;
import com.planitsquare.holidaykeeper.entity.Holiday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HolidayApi {
    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://date.nager.at/api/v3";

    public List<CountryResponse> getAvailableCountries() {
        String url = BASE_URL + "/AvailableCountries";
        CountryResponse[] response = restTemplate.getForObject(url, CountryResponse[].class);
        return response != null ? List.of(response) : List.of();
    }

    public List<HolidayResponse> getHolidaysByYearAndCountry(Integer year, String countryCode) {
        String url = BASE_URL + "/PublicHolidays/" + year + "/" + countryCode;
        HolidayResponse[] response = restTemplate.getForObject(url, HolidayResponse[].class);
        return response != null ? List.of(response) : List.of();
    }

    public List<Holiday> fetchHolidays(Integer year, Country country) {
        String url = BASE_URL + "/PublicHolidays/" + year + "/" + country.getCountryCode();
        HolidayResponse[] response = restTemplate.getForObject(url, HolidayResponse[].class);
        if (response == null) {
            return List.of();
        }
        return Arrays.stream(response)
                .map(res -> res.toEntity(country))
                .toList();
    }
}
