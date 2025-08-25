package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleEnum {
    ADMIN,
    USER;

    @JsonCreator
    public static RoleEnum from(String value) {
        return value == null ? null : RoleEnum.valueOf(value.toUpperCase());
    }
}
