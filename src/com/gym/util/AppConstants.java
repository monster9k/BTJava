package com.gym.util;

public class AppConstants {
    
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_STAFF = 2;
    public static final int ROLE_MEMBER = 3;

    
    public static final boolean USER_ACTIVE = true;
    public static final boolean USER_LOCKED = false;

    
    public static final boolean MEMBER_ACTIVE = true;
    public static final boolean MEMBER_INACTIVE = false;

    
    public static final boolean PACKAGE_ACTIVE = true;
    public static final boolean PACKAGE_INACTIVE = false;

    
    public static final int SUBSCRIPTION_PENDING = 0;
    public static final int SUBSCRIPTION_ACTIVE = 1;
    public static final int SUBSCRIPTION_EXPIRED = 2;
    public static final int SUBSCRIPTION_CANCELED = 3;
    
    public static final int STATUS_ACTIVE = SUBSCRIPTION_ACTIVE;
    public static final int STATUS_EXPIRED = SUBSCRIPTION_EXPIRED;
    public static final int STATUS_CANCELED = SUBSCRIPTION_CANCELED;

    
    public static final int PAYMENT_PAID = 1;
    public static final int PAYMENT_UNPAID = 0;

    
    public static final String MEMBER_CODE_PREFIX = "GYM";

    
    public static final int REPORT_EXPIRING_DAYS = 5;
}
