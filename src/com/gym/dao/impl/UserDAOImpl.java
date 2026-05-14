package com.gym.dao.impl;

import com.gym.dao.BaseDAO;
import com.gym.dao.IUserDAO;
import com.gym.entity.User;

import java.util.List;

public class UserDAOImpl extends BaseDAO implements IUserDAO {

    
    private User mapUser(java.sql.ResultSet rs) throws java.sql.SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password")); 
        u.setFullname(rs.getString("full_name"));
        u.setPhone(rs.getString("phone"));
        u.setRoleId(rs.getInt("role_id"));
        u.setStatus(rs.getBoolean("status"));
        return u;
    }


    @Override
    public List<User> findAllStaff() {
        String sql = "SELECT * FROM users WHERE role_id = 2"; 
        return executeQuery(sql, this::mapUser);
    }

    @Override
    public User login(String username, String password) {
        
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = true";

        
        List<User> users = executeQuery(sql, this::mapUser, username, password);

        
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = executeQuery(sql, this::mapUser, username);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = executeQuery(sql, this::mapUser, id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public int insert(User user) {
        
        String sql = "INSERT INTO users (username, password, full_name, phone, role_id, status) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            int result = executeUpdate(sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getFullname(),
                    user.getPhone(),
                    user.getRoleId(),
                    user.isStatus());
            System.out.println("✓ Insert user SUCCESS: username=" + user.getUsername() + ", rows affected=" + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ Insert user FAILED: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return executeUpdate(sql, newPassword, userId);
    }

    public int updateProfileByUsername(String username, String fullName, String phone) {
        String sql = "UPDATE users SET full_name = ?, phone = ? WHERE username = ?";
        return executeUpdate(sql, fullName, phone, username);
    }

    public int updateProfileById(int userId, String fullName, String phone) {
        String sql = "UPDATE users SET full_name = ?, phone = ? WHERE id = ?";
        return executeUpdate(sql, fullName, phone, userId);
    }

    public int updatePasswordByUsername(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        return executeUpdate(sql, newPassword, username);
    }

    @Override
    public int toggleUserStatus(int userId, boolean status) {
        
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        return executeUpdate(sql, status, userId);
    }
}
