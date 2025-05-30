package com.backend.tryal.shared.redis;

import java.util.UUID;

public class BusinessCacheKeys {
    private static final String PREFIX = "business:";

    public static String profile(UUID businessId) {
        return PREFIX + "profile:" + businessId;
    }

    public static String stripeAccountId(UUID businessId) {
        return PREFIX + "stripe:" + businessId;
    }

    public static String exists(UUID businessId) {
        return PREFIX + "exists:" + businessId;
    }

    public static String emailLookup(String email) {
        return PREFIX + "email:" + email.toLowerCase();
    }

    public static String phoneLookup(String phone) {
        return PREFIX + "phone:" + phone;
    }
}
