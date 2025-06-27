package com.planitsquare.holidaykeeper.controller;

import com.planitsquare.holidaykeeper.dto.HolidayRequest;
import com.planitsquare.holidaykeeper.dto.HolidayResponse;
import com.planitsquare.holidaykeeper.dto.HolidaySearchCondition;
import com.planitsquare.holidaykeeper.entity.Holiday;
import com.planitsquare.holidaykeeper.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<HolidayResponse>> searchHolidays(
            @Valid @ModelAttribute HolidaySearchCondition condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // countryCode만 대문자로 변환
        HolidaySearchCondition upperCaseCondition = new HolidaySearchCondition(
                condition.countryCode() != null ? condition.countryCode().toUpperCase() : null,
                condition.year(),
                condition.fromDate(),
                condition.toDate(),
                condition.types()
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<Holiday> holidayPage = holidayService.searchHolidays(upperCaseCondition, pageable);

        Page<HolidayResponse> response = holidayPage.map(HolidayResponse::from);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/refresh")
    public ResponseEntity<Void> refreshHolidays(@RequestBody @Valid HolidayRequest request) {
        holidayService.refreshHolidays(request.countryCode().toUpperCase(), request.year());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteHolidays(@RequestBody @Valid HolidayRequest request) {
        holidayService.deleteHolidays(request.countryCode().toUpperCase(), request.year());
        return ResponseEntity.noContent().build();
    }
}
