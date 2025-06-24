package com.planitsquare.holidaykeeper.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

    @Id
    private String countryCode;
    private String name;
}
