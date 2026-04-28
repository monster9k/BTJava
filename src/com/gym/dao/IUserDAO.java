package com.gym.dao;

import com.gym.entity.User;
import java.util.List;

public interface IUserDAO {
    User login(String username, String password);
    List<User> findAllStaff(); // Admin xem danh sách nhân viên

    int insert(User user);
    int updatePassword(int userId, String newPassword);
    int toggleUserStatus(int userId, boolean status);
}