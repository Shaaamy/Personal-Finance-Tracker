package com.NtgSummerTrainingApp.PersonalFinanceTracker.handler;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String code;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
