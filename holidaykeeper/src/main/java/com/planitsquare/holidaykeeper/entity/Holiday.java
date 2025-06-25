package com.planitsquare.holidaykeeper.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String localName;

    private String name;

    private Boolean fixed;

    private Boolean global;

    private String counties;

    private Integer launchYear;

    private String types;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code")
    private Country country;

    public void updateFrom(Holiday newData) {
        this.name = newData.getName();
        this.localName = newData.getLocalName();
        this.types = newData.getTypes();
        this.fixed = newData.getFixed();
        this.global = newData.getGlobal();
        this.launchYear = newData.getLaunchYear();
        this.counties = newData.getCounties();
        this.country = newData.getCountry();
    }

    public void updateCountry(Country country) {
        this.country = country;
    }
}