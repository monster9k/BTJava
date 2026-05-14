package com.gym.dao;

import com.gym.entity.User;
import java.util.List;

public interface IUserDAO {
    User login(String username, String password);
    User findByUsername(String username); // Check tên đăng nhập có tồn tại không
    User findById(int id);
    List<User> findAllStaff(); // Admin xem danh sách nhân viên

    int insert(User user);
    int updatePassword(int userId, String newPassword);
    int updateProfileByUsername(String username, String fullName, String phone);
    int updateProfileById(int userId, String fullName, String phone);
    int updatePasswordByUsername(String username, String newPassword);
    int toggleUserStatus(int userId, boolean status);
}