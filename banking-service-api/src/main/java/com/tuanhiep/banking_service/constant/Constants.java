package com.tuanhiep.banking_service.constant;


public final class Constants {
    private Constants() {
        // private constructor để tránh new Constants()
    }

    public static final String ACCOUNT_KEY_PREFIX = "ACCOUNT_";
    public static final String CARD_KEY_PREFIX = "CARD_";
    public static final String BALANCE_KEY_PREFIX = "BALANCE_";

    public static final long REDIS_TTL_MINUTES = 10L;
}

