package com.planitsquare.holidaykeeper.repository;

import com.planitsquare.holidaykeeper.dto.HolidaySearchCondition;
import com.planitsquare.holidaykeeper.entity.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayRepositoryCustom {
    Page<Holiday> searchHolidays(HolidaySearchCondition condition, Pageable pageable);
}
