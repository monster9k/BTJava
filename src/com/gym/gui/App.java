package com.gym.gui;

import java.awt.EventQueue;

/**
 * Điểm khởi động chính (Main Entry Point)
 * Chạy toàn bộ ứng dụng từ đây
 */
public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Khởi động cửa sổ đăng nhập
                    LoginJFram frame = new LoginJFram();
                    frame.setVisible(true);
                    
                    // TODO: Sau này thêm logic xử lý đăng nhập
                    // if (loginSuccess) { mở MainWindow, đóng LoginJFram }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
