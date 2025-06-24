package com.planitsquare.holidaykeeper.repository;

import com.planitsquare.holidaykeeper.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
}
