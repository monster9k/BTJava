package com.gym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gym_management";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
//    // --- HÀM TEST NHANH ---
//    public static void main(String[] args) {
//        try (Connection conn = getConnection()) {
//            if (conn != null) {
//                System.out.println("Kết nối Database THÀNH CÔNG!");
//                System.out.println("Thông tin DB: " + conn.getMetaData().getDatabaseProductName());
//            }
//        } catch (SQLException e) {
//            System.err.println("Kết nối THẤT BẠI!");
//            e.printStackTrace();
//        }
//    }

}
