package com.gym.gui;

//import com.gym.entity.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * LoginJFrame - Màn hình đăng nhập sử dụng Swing
 */
public class LoginJFram extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword; // Dùng JPasswordField bảo mật hơn
    private JButton btnLogin;
    private JLabel lblError;

    public LoginJFram() {
        initComponents();
        configureFrame();
    }

    private void initComponents() {
        // Layout chính chia làm 2 phần bằng nhau
        setLayout(new GridLayout(1, 2));

        leftPanel = buildLeftPanel();
        rightPanel = buildRightPanel();

        add(leftPanel);
        add(rightPanel);
    }

    // PANEL TRÁI — Logo và Slogan
    private JPanel buildLeftPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(22, 22, 30));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(22, 22, 30));

        // Thanh trang trí màu vàng
        JPanel accentBar = new JPanel();
        accentBar.setBackground(new Color(255, 193, 7));
        accentBar.setMaximumSize(new Dimension(60, 5));
        
        // Logo
        JLabel lblLogo = new JLabel("GYM MANAGER");
        lblLogo.setFont(new Font("Tahoma", Font.BOLD, 36));
        lblLogo.setForeground(new Color(255, 193, 7));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slogan
        JLabel lblSlogan = new JLabel("QUẢN LÝ PHÒNG GYM CHUYÊN NGHIỆP");
        lblSlogan.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblSlogan.setForeground(new Color(140, 140, 160));
        lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalGlue());
        center.add(accentBar);
        center.add(Box.createRigidArea(new Dimension(0, 20)));
        center.add(lblLogo);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(lblSlogan);
        center.add(Box.createVerticalGlue());

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // PANEL PHẢI — Form đăng nhập
    private JPanel buildRightPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 246, 250));
        root.setBorder(new EmptyBorder(40, 50, 40, 50)); // Tạo khoảng cách lề

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(new Color(245, 246, 250));

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(22, 22, 30));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Username
        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Password
        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nút đăng nhập
        btnLogin = new JButton(" ĐĂNG NHẬP ");
         btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String us = txtUsername.getText();
        		char[] paChars = txtPassword.getPassword();
        		String pa = new String(paChars);
        		
        	}
        });
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBackground(new Color(255, 193, 7));
        btnLogin.setFocusPainted(false); // Xóa viền focus khi click
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Lỗi
        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ghép component vào form
        form.add(Box.createVerticalGlue());
        form.add(lblTitle);
        form.add(Box.createRigidArea(new Dimension(0, 30)));
        form.add(lblUser);
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(txtUsername);
        form.add(Box.createRigidArea(new Dimension(0, 15)));
        form.add(lblPass);
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(txtPassword);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(lblError);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(btnLogin);
        form.add(Box.createVerticalGlue());

        root.add(form, BorderLayout.CENTER);
        return root;
    }

    private void configureFrame() {
        setTitle("GymManager — Login");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tự động đóng app
        setLocationRelativeTo(null); // Căn giữa màn hình
        setResizable(false);
    }
}