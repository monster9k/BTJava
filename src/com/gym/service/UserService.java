package com.gym.service;

import com.gym.dao.IUserDAO;
import com.gym.dao.impl.UserDAOImpl;
import com.gym.entity.User;
import com.gym.util.AppConstants;
import com.gym.util.SecurityUtil;

import java.util.List;




public class UserService {
    private final IUserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public User getByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDAO.findByUsername(username.trim());
    }

    public User getById(int id) {
        if (id <= 0) {
            return null;
        }
        return userDAO.findById(id);
    }

    public List<User> getAllStaff() {
        return userDAO.findAllStaff();
    }

    public boolean createUser(String username, String rawPassword, String fullName, String phone, int roleId) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }
        if (roleId != AppConstants.ROLE_ADMIN && roleId != AppConstants.ROLE_STAFF) {
            return false;
        }

        if (userDAO.findByUsername(username.trim()) != null) {
            return false;
        }

        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(SecurityUtil.hashPassword(rawPassword));
        u.setFullname(fullName.trim());
        u.setPhone(phone != null ? phone.trim() : null);
        u.setRoleId(roleId);
        u.setStatus(AppConstants.USER_ACTIVE);

        return userDAO.insert(u) > 0;
    }

    public boolean updateProfile(String username, String fullName, String phone) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }
        return userDAO.updateProfileByUsername(username.trim(), fullName.trim(), phone != null ? phone.trim() : null) > 0;
    }

    public boolean updateProfileById(int userId, String fullName, String phone) {
        if (userId <= 0) {
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }
        return userDAO.updateProfileById(userId, fullName.trim(), phone != null ? phone.trim() : null) > 0;
    }

    public boolean changePassword(String username, String oldRawPassword, String newRawPassword) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (newRawPassword == null || newRawPassword.length() < 6) {
            return false;
        }

        User u = userDAO.findByUsername(username.trim());
        if (u == null) {
            return false;
        }

        String oldHashed = SecurityUtil.hashPassword(oldRawPassword);
        if (!oldHashed.equals(u.getPassword())) {
            return false;
        }

        String newHashed = SecurityUtil.hashPassword(newRawPassword);
        return userDAO.updatePasswordByUsername(username.trim(), newHashed) > 0;
    }

    public boolean changePasswordNoVerify(String username, String newRawPassword) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (newRawPassword == null || newRawPassword.length() < 6) {
            return false;
        }
        String newHashed = SecurityUtil.hashPassword(newRawPassword);
        return userDAO.updatePasswordByUsername(username.trim(), newHashed) > 0;
    }

    public User createMemberUser(String username, String rawPassword, String fullName, String phone) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            return null;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return null;
        }
        if (userDAO.findByUsername(username.trim()) != null) {
            return null;
        }

        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(SecurityUtil.hashPassword(rawPassword));
        u.setFullname(fullName.trim());
        u.setPhone(phone != null ? phone.trim() : null);
        u.setRoleId(AppConstants.ROLE_MEMBER);
        u.setStatus(AppConstants.USER_ACTIVE);

        if (userDAO.insert(u) <= 0) {
            return null;
        }
        return userDAO.findByUsername(username.trim());
    }

    public boolean toggleUserStatus(int userId, boolean status) {
        if (userId <= 0) {
            return false;
        }
        return userDAO.toggleUserStatus(userId, status) > 0;
    }
}
