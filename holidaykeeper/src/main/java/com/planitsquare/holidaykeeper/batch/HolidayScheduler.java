package com.planitsquare.holidaykeeper.batch;

import com.planitsquare.holidaykeeper.api.HolidayApi;
import com.planitsquare.holidaykeeper.dto.CountryResponse;
import com.planitsquare.holidaykeeper.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayScheduler {

    private final HolidayService holidayService;
    private final HolidayApi holidayApi;

    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void refreshCurrentAndPreviousYear(){
        int currentYear = LocalDate.now().getYear();
        int previousYear = currentYear -1;

        List<CountryResponse> countries = holidayApi.getAvailableCountries();

        for (CountryResponse country : countries) {
            try {
                holidayService.refreshHolidays(country.countryCode(), previousYear);
                holidayService.refreshHolidays(country.countryCode(), currentYear);
                log.info("[{}] 국가 {} - {}년 & {}년 재동기화 완료", country.countryCode(), country.name(), previousYear, currentYear);
            } catch (Exception e) {
                log.error("[{}] 국가 {} 재동기화 실패: {}", country.countryCode(), country.name(), e.getMessage());
            }
        }

        log.info("연간 공휴일 자동 재동기화 완료");
    }
}
