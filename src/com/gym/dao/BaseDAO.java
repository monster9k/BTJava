package com.gym.dao;

import com.gym.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAO {

    // Interface tùy chỉnh để ném ra SQLException (giúp code ở lớp con sạch hơn)
    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    protected <T> List<T> executeQuery(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = new ArrayList<>();
        // Try-with-resources đóng cả Connection, PreparedStatement và ResultSet
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL tại BaseDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi Update tại BaseDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}