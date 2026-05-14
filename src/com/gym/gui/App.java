package com.gym.gui;

import java.awt.EventQueue;


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
