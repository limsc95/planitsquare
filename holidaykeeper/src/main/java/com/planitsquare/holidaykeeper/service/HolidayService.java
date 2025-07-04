package com.planitsquare.holidaykeeper.service;

import com.planitsquare.holidaykeeper.api.HolidayApi;
import com.planitsquare.holidaykeeper.dto.CountryResponse;
import com.planitsquare.holidaykeeper.dto.HolidayResponse;
import com.planitsquare.holidaykeeper.dto.HolidaySearchCondition;
import com.planitsquare.holidaykeeper.entity.Country;
import com.planitsquare.holidaykeeper.entity.Holiday;
import com.planitsquare.holidaykeeper.repository.CountryRepository;
import com.planitsquare.holidaykeeper.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HolidayService {
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final HolidayApi holidayApi;

    public void loadAllCountriesAndHolidaysFor5Years() {
        int startYear = 2020;
        int endYear = 2025;

        AtomicInteger countryCount = new AtomicInteger();
        AtomicInteger holidayCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        List<CountryResponse> countries = holidayApi.getAvailableCountries();

        countries.parallelStream().forEach(countryResponse -> {
            try {
                Country country = countryRepository.findByCountryCode(countryResponse.countryCode());

                if (country == null) {
                    country = Country.builder()
                            .countryCode(countryResponse.countryCode())
                            .name(countryResponse.name())
                            .build();
                    countryRepository.save(country);
                    countryCount.incrementAndGet();
                }

                for (int year = startYear; year <= endYear; year++) {
                    List<HolidayResponse> holidayResponseList = holidayApi.getHolidaysByYearAndCountry(year, country.getCountryCode());

                    for (HolidayResponse holidayResponse : holidayResponseList) {
                        Holiday holiday = Holiday.builder()
                                .date(holidayResponse.date())
                                .localName(holidayResponse.localName())
                                .name(holidayResponse.name())
                                .fixed(holidayResponse.fixed())
                                .global(holidayResponse.global())
                                .counties(holidayResponse.counties() == null ? null : String.join(",", holidayResponse.counties()))
                                .launchYear(holidayResponse.launchYear())
                                .types(holidayResponse.types() == null ? null : String.join(",", holidayResponse.types()))
                                .country(country)
                                .build();

                        holidayRepository.save(holiday);
                        holidayCount.incrementAndGet();
                    }
                }
            }
            catch (Exception e) {
                log.error("국가 {} 데이터 적재 실패: {}", countryResponse.name(), e.getMessage());
                failCount.incrementAndGet();
            }
        });

        log.info("데이터 적제 완료");
        log.info("성공 국가: {}", countryCount.intValue());
        log.info("성공 휴일: {}", holidayCount.intValue());
        log.info("총 실패: {}", failCount.intValue());
    }

    public Page<Holiday> searchHolidays(HolidaySearchCondition condition, Pageable pageable) {
        return holidayRepository.searchHolidays(condition,pageable);
    }

    public void refreshHolidays(String countryCode, Integer year) {
        Country country = countryRepository.findByCountryCode(countryCode);
        if (country == null) {
            throw new IllegalArgumentException("존재하지 않는 국가 코드입니다.");
        }

        List<Holiday> fetchedHolidays = holidayApi.fetchHolidays(year, country);

        Map<LocalDate, Holiday> existingHolidays = holidayRepository
                .findByCountryAndYear(countryCode, year)
                .stream()
                .collect(Collectors.toMap(Holiday::getDate, h -> h));

        Set<LocalDate> fetchedDates = fetchedHolidays.stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        List<Holiday> toInsert = new ArrayList<>();
        List<Holiday> toUpdate = new ArrayList<>();

        for (Holiday fetched : fetchedHolidays) {
            Holiday existing = existingHolidays.get(fetched.getDate());
            if (existing != null) {
                existing.updateFrom(fetched);
                toUpdate.add(existing);
            } else {
                fetched.updateCountry(country);
                toInsert.add(fetched);
            }
        }

        List<Holiday> toDelete = existingHolidays.values().stream()
                        .filter(h -> !fetchedDates.contains(h.getDate()))
                        .toList();

        if (!toDelete.isEmpty()) {
            holidayRepository.deleteAll(toDelete);
        }
        holidayRepository.saveAll(toUpdate);
        holidayRepository.saveAll(toInsert);
    }

    public void deleteHolidays(String countryCode, Integer year) {
        boolean exists = countryRepository.existsByCountryCode(countryCode);
        if (!exists) {
            throw new IllegalArgumentException("존재하지 않는 국가입니다.");
        }

        holidayRepository.deleteByCountryAndYear(countryCode, year);
    }
}
