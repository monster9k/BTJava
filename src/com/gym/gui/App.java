package com.gym.gui;

import java.awt.EventQueue;

/**
 *javac -encoding UTF-8 -d ./bin ./src/com/gym/gui/*.java
java "-Dfile.encoding=UTF-8" -cp ./bin com.gym.gui.App
*Chạy 2 lệnh trên để biên dịch chạy ứng dụng nghe ae
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
                    
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
