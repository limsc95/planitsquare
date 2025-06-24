package com.planitsquare.holidaykeeper.controller;

import com.planitsquare.holidaykeeper.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {
    private final HolidayService holidayService;

    // TODO: api 호출이 아닌 최초 실행 시 실행으로 수정 필요.
    @PostMapping("/init")
    public ResponseEntity<Void> initData() {
        holidayService.loadAllCountriesAndHolidaysFor5Years();
        return ResponseEntity.ok().build();
    }
}
