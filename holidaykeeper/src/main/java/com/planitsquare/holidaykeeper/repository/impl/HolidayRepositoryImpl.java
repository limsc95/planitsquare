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

        if (condition.countryCode() != null) {
            builder.and(holiday.country.countryCode.eq(condition.countryCode()));
        }

        if (condition.year() != null) {
            builder.and(holiday.date.year().eq(condition.year()));
        }

        if (condition.fromDate() != null) {
            builder.and(holiday.date.goe(condition.fromDate()));
        }

        if (condition.toDate() != null) {
            builder.and(holiday.date.loe(condition.toDate()));
        }

        if (condition.types() != null) {
            builder.and(holiday.types.containsIgnoreCase(condition.types()));
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

    @Override
    public List<Holiday> findByCountryAndYear(String countryCode, int year) {
        QHoliday holiday = QHoliday.holiday;

        return queryFactory
                .selectFrom(holiday)
                .where(
                        holiday.country.countryCode.eq(countryCode),
                        holiday.date.year().eq(year))
                .fetch();
    }

    @Override
    public void deleteByCountryAndYear(String countryCode, int year) {
        QHoliday holiday = QHoliday.holiday;

        queryFactory.delete(holiday)
                .where(
                        holiday.country.countryCode.eq(countryCode),
                        holiday.date.year().eq(year)
                )
                .execute();
    }
}
