package com.gym.auth;

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
        if (username.isEmpty() || rawPassword.isEmpty() || fullName.isEmpty()) {
            System.out.println("✗ Register: Empty input");
            return false;
        }

        System.out.println("Register: Hashing password for username=" + username);
        String hashedPassword = SecurityUtil.hashPassword(rawPassword);
        System.out.println("Register: Hash result=" + hashedPassword);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFullname(fullName);
        newUser.setRoleId(roleId);
        newUser.setStatus(true);

        System.out.println("Register: About to insert user...");
        int result = userDAO.insert(newUser);
        System.out.println("Register: Insert result=" + result);
        return result > 0;
    }

    // Kiểm tra username đã tồn tại trong database
    public boolean checkUsernameExists(String username) {
        User user = userDAO.findByUsername(username);
        return user != null;
    }
}