package com.planitsquare.holidaykeeper.repository;

import com.planitsquare.holidaykeeper.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {

    Country findByCountryCode(String countryCode);
}
