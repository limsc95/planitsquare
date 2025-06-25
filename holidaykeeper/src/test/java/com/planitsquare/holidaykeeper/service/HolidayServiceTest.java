package com.planitsquare.holidaykeeper.service;

import com.planitsquare.holidaykeeper.HolidaykeeperApplication;
import com.planitsquare.holidaykeeper.dto.HolidaySearchCondition;
import com.planitsquare.holidaykeeper.entity.Country;
import com.planitsquare.holidaykeeper.entity.Holiday;
import com.planitsquare.holidaykeeper.repository.CountryRepository;
import com.planitsquare.holidaykeeper.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = HolidaykeeperApplication.class)
@DisplayName("HolidayService 단위 테스트")
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        holidayRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Disabled("조회 테스트로 임시 비활성화")
    @Test
    @DisplayName("5년치 공휴일이 정상적으로 적재되는지 확인")
    void testLoadAllCountriesAndHolidays() {
        // when
        holidayService.loadAllCountriesAndHolidaysFor5Years();

        // then
        long holidayCount = holidayRepository.count();
        long countryCount = countryRepository.count();

        assertTrue(holidayCount > 0, "공휴일이 저장되어야 합니다.");
        assertTrue(countryCount > 0, "국가 정보가 저장되어야 합니다.");
    }

    @Test
    @DisplayName("조건에 따른 공휴일 검색")
    void testSearchHolidaysByCondition() {
        //given
        Country kr = Country.builder()
                .countryCode("KR")
                .name("대한민국")
                .build();
        countryRepository.save(kr);

        holidayRepository.save(Holiday.builder()
                .country(kr)
                .date(LocalDate.of(2025, 1, 1))
                .localName("새해")
                .name("New Year's Day")
                .types("Public")
                .build());

        holidayRepository.save(Holiday.builder()
                .country(kr)
                .date(LocalDate.of(2025, 6, 6))
                .localName("현충일")
                .name("Memorial Day")
                .types("Public")
                .build());

        HolidaySearchCondition condition = HolidaySearchCondition.builder()
                .countryCode("KR")
                .year(2025)
                .fromDate(LocalDate.of(2025, 1, 1))
                .toDate(LocalDate.of(2025, 12, 31))
                .types("Public")
                .build();

        // when
        Page<Holiday> result = holidayService.search(condition, PageRequest.of(0, 10));

        // then
        assertEquals(2, result.getTotalElements(), "조건에 맞는 공휴일이 2건 조회되어야 합니다.");
    }
}