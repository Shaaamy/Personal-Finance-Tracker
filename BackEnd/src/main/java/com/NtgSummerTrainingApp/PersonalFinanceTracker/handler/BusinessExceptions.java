package com.NtgSummerTrainingApp.PersonalFinanceTracker.handler;

public  class BusinessExceptions {
    public static class DuplicateResourceException extends RuntimeException {
        public DuplicateResourceException(String message) {
            super(message);
        }
    }
    public static class TokenException extends RuntimeException {
        public TokenException(String message) {
            super(message);
        }
    }
    public static class InvalidPasswordResetTokenException extends RuntimeException {
        public InvalidPasswordResetTokenException(String message) {
            super(message);
        }
    }
    public static class ExpiredPasswordResetTokenException extends RuntimeException {
        public ExpiredPasswordResetTokenException(String message) {
            super(message);
        }
    }
}
