package com.gym.dao.impl;

import com.gym.dao.BaseDAO;
import com.gym.dao.IUserDAO;
import com.gym.entity.User;

import java.util.List;

public class UserDAOImpl extends BaseDAO implements IUserDAO {

    // Hàm map dữ liệu từ ResultSet sang User entity
    private User mapUser(java.sql.ResultSet rs) throws java.sql.SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password")); // Hash password
        u.setFullname(rs.getString("full_name"));
        u.setRoleId(rs.getInt("role_id"));
        u.setStatus(rs.getBoolean("status"));
        return u;
    }


    @Override
    public List<User> findAllStaff() {
        String sql = "SELECT * FROM users WHERE role_id != 1"; // Lấy tất cả user có role_id khác 1 (admin)
        return executeQuery(sql, this::mapUser);
    }

    @Override
    public User login(String username, String password) {
        // Chỉ cho phép user có tài khoản chưa bị khóa đăng nhập
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = true";

        //Gọi hàm executeQuery của BaseDao để thực hiện truy van
        List<User> users = executeQuery(sql, this::mapUser, username, password);

        // Nếu danh sách trả về rỗng -> Sai user/pass -> return null
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public int insert(User user) {
        // Đăng ký tài khoản moi, mặc định role_id = 2 (nhân viên) và status = true (kích hoạt)
        String sql = "INSERT INTO users (username, password, full_name, role_id, status) VALUES (?, ?, ?, ?, ?)";

        return executeUpdate(sql,
                user.getUsername(),
                user.getPassword(),
                user.getFullname(),
                user.getRoleId(),
                user.isStatus());
    }

    @Override
    public int updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return executeUpdate(sql, newPassword, userId);
    }

    @Override
    public int toggleUserStatus(int userId, boolean status) {
        // Khóa hoặc mở khóa tài khoản thay vì xóa cứng (Soft Delete)
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        return executeUpdate(sql, status, userId);
    }
}
