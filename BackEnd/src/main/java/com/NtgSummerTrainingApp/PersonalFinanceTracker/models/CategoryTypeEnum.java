package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CategoryTypeEnum {
    INCOME,
    EXPENSE;

    @JsonCreator
    public static CategoryTypeEnum from(String value) {
        return value == null ? null : CategoryTypeEnum.valueOf(value.toUpperCase());
    }
}
