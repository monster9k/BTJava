package com.gym.auth;

import com.gym.entity.User;
import com.gym.util.AppConstants;

public class UserSession {
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRoleId() == AppConstants.ROLE_ADMIN;
    }
}