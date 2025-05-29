package com.backend.tryal.shared.redis;

import java.util.UUID;
public class UserCacheKeys {
    private static final String PREFIX = "user:";

    public static String profile(UUID userId) {
        return PREFIX + "profile:" + userId;
    }

    public static String creditBalance(UUID userId) {
        return PREFIX + "credit:" + userId;
    }

    public static String stripeCustomerId(UUID userId) {
        return PREFIX + "stripe:" + userId;
    }

    public static String exists(UUID userId) {
        return PREFIX + "exists:" + userId;
    }

    public static String emailLookup(String email) {
        return PREFIX + "email:" + email.toLowerCase();
    }

    public static String phoneLookup(String phone) {
        return PREFIX + "phone:" + phone;
    }
}
