package com.planitsquare.holidaykeeper.service;

import com.planitsquare.holidaykeeper.repository.CountryRepository;
import com.planitsquare.holidaykeeper.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
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
}