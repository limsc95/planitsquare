package com.planitsquare.holidaykeeper.dto;

import jakarta.validation.constraints.*;

public record HolidayRequest(
        @NotBlank(message = "countryCode를 입력해야 합니다.")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "countryCode는 영문 2자리여야 합니다.")
        String countryCode,

        @NotNull(message = "year를 입력해야 합니다.")
        @Min(value = 1900, message = "year는 1900 이상이어야 합니다.")
        @Max(value = 2100, message = "year는 2100 이하이어야 합니다.")
        Integer year
) {
}
