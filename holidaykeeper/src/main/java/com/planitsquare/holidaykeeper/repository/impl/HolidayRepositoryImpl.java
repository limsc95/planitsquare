package com.planitsquare.holidaykeeper.repository.impl;

import com.planitsquare.holidaykeeper.dto.HolidaySearchCondition;
import com.planitsquare.holidaykeeper.entity.Holiday;
import com.planitsquare.holidaykeeper.entity.QCountry;
import com.planitsquare.holidaykeeper.entity.QHoliday;
import com.planitsquare.holidaykeeper.repository.HolidayRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class HolidayRepositoryImpl implements HolidayRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Holiday> searchHolidays(HolidaySearchCondition condition, Pageable pageable) {
        QHoliday holiday = QHoliday.holiday;
        QCountry country = QCountry.country;

        BooleanBuilder builder = new BooleanBuilder();

        if (condition.getCountryCode() != null) {
            builder.and(holiday.country.countryCode.eq(condition.getCountryCode()));
        }

        if (condition.getYear() != null) {
            builder.and(holiday.date.year().eq(condition.getYear()));
        }

        if (condition.getFromDate() != null) {
            builder.and(holiday.date.goe(condition.getFromDate()));
        }

        if (condition.getToDate() != null) {
            builder.and(holiday.date.loe(condition.getToDate()));
        }

        if (condition.getTypes() != null) {
            builder.and(holiday.types.containsIgnoreCase(condition.getTypes()));
        }

        List<Holiday> result = queryFactory
                .selectFrom(holiday)
                .leftJoin(holiday.country, country).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(holiday.count())
                .from(holiday)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(result, pageable, count != null ? count : 0);
    }
}
