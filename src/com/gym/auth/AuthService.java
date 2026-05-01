package com.gym.service;

import com.gym.auth.UserSession;
import com.gym.dao.IUserDAO;
import com.gym.dao.impl.UserDAOImpl;
import com.gym.entity.User;
import com.gym.util.SecurityUtil;

public class AuthService {
    private IUserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    // Xử lý Đăng nhập
    public boolean login(String username, String rawPassword) {
        if (username.isEmpty() || rawPassword.isEmpty()) return false;

        String hashedPassword = SecurityUtil.hashPassword(rawPassword);
        User user = userDAO.login(username, hashedPassword);

        if (user != null) {
            UserSession.login(user);
            return true;
        }
        return false;
    }

    // Xử lý Đăng ký tài khoản
    public boolean register(String username, String rawPassword, String fullName, int roleId) {
        if (username.isEmpty() || rawPassword.isEmpty() || fullName.isEmpty()) return false;

        String hashedPassword = SecurityUtil.hashPassword(rawPassword);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFullname(fullName);
        newUser.setRoleId(roleId);
        newUser.setStatus(true);

        int result = userDAO.insert(newUser);
        return result > 0;
    }
}