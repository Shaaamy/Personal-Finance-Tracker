package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CurrencyType {
    EGP,   // Egyptian Pound
    USD,   // US Dollar
    EUR,   // Euro
    GBP,   // British Pound
    JPY;    // Japanese Yen


    @JsonCreator
    public static CurrencyType from(String value) {
        return value == null ? null : CurrencyType.valueOf(value.toUpperCase());
    }
}
