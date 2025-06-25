package com.planitsquare.holidaykeeper.controller;

import com.planitsquare.holidaykeeper.dto.HolidayResponse;
import com.planitsquare.holidaykeeper.dto.HolidaySearchCondition;
import com.planitsquare.holidaykeeper.entity.Holiday;
import com.planitsquare.holidaykeeper.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {
    private final HolidayService holidayService;

    // runner 를 통한 최초 실행으로 사용 x, 수동으로 실행 시 필요로 인해 유지
    @PostMapping("/init")
    public ResponseEntity<Void> initData() {
        holidayService.loadAllCountriesAndHolidaysFor5Years();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<HolidayResponse>> search(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String types,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        HolidaySearchCondition condition = new HolidaySearchCondition(countryCode.toUpperCase(), year, fromDate, toDate, types);

        Pageable pageable = PageRequest.of(page, size);
        Page<Holiday> holidayPage = holidayService.search(condition, pageable);

        Page<HolidayResponse> response = holidayPage.map(HolidayResponse::from);

        return ResponseEntity.ok(response);
    }
}
