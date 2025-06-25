package com.planitsquare.holidaykeeper.runner;

import com.planitsquare.holidaykeeper.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayInitRunner implements ApplicationRunner {

    private final HolidayService holidayService;

    @Override
    public void run(ApplicationArguments args) {
        holidayService.loadAllCountriesAndHolidaysFor5Years();
    }
}