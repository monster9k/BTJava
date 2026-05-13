package com.gym.util;

public class AppConstants {
    // Role IDs
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_STAFF = 2;

    // User Status
    public static final boolean USER_ACTIVE = true;
    public static final boolean USER_LOCKED = false;

    // Member Status
    public static final boolean MEMBER_ACTIVE = true;
    public static final boolean MEMBER_INACTIVE = false;

    // Package Status
    public static final boolean PACKAGE_ACTIVE = true;
    public static final boolean PACKAGE_INACTIVE = false;

    // Subscription Status
    public static final int SUBSCRIPTION_ACTIVE = 1;
    public static final int SUBSCRIPTION_EXPIRED = 2;
    public static final int SUBSCRIPTION_CANCELED = 3;
    // Backward compatible
    public static final int STATUS_ACTIVE = SUBSCRIPTION_ACTIVE;
    public static final int STATUS_EXPIRED = SUBSCRIPTION_EXPIRED;
    public static final int STATUS_CANCELED = SUBSCRIPTION_CANCELED;

    // Payment Status
    public static final int PAYMENT_PAID = 1;
    public static final int PAYMENT_UNPAID = 0;

    // Member Code Format
    public static final String MEMBER_CODE_PREFIX = "GYM";

    // Report - Expiring soon days
    public static final int REPORT_EXPIRING_DAYS = 5;
}
