package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FrequencyEnum {
    DAILY,
    WEEKLY,
    MONTHLY,
    HALF_ANNUAL,
    ANNUAL;

    @JsonCreator
    public static FrequencyEnum from(String value) {
        return value == null ? null : FrequencyEnum.valueOf(value.toUpperCase());
    }
}
