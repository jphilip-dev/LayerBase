package com.jphilips.shared.spring.redis.util;

public class CacheKeys {
    private CacheKeys() {}

    public static class Auth {
        public static final String AUTH_TOKEN = "auth:token:";
        public static final String AUTH_TOKEN_TAG = "auth:token-tag:";
        public static final String AUTH_TOKEN_BY_USER_ID = "auth:token-by-userId:";
        public static final String AUTH_OTP_BY_USER_ID= "auth:otp-by-userId:";

        public static final String USER_BY_EMAIL = "auth:user-by-email:";
        public static final String USER_BY_ID = "auth:user-by-id:";
        public static final String USER_PAGE = "auth:user-page:";
        public static final String USER_PAGE_TAG = "auth:user-page-tag";

    }

    public static class UserDetails{
        public static final String USER_DETAILS_BY_ID = "auth:user-details-by-id:";
        public static final String USER_DETAILS_PAGE = "auth:user-details-page:";
        public static final String USER_DETAILS_TAG = "auth:user-details-page-tag";
    }

    public static class Product {
        public static final String DETAILS = "product-details";
        public static final String LIST = "product-list";
    }
}

