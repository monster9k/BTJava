package com.gym.gui.auth;

import com.gym.service.AuthService;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends Frame {
    private TextField txtUsername, txtPassword;
    private Button btnLogin;
    private Label lblMessage, lblRegisterLink;
    private AuthService authService;

    // Định nghĩa bảng màu (Color Palette)
    private final Color bgColor = new Color(245, 238, 250); // Tím pastel nhạt mô phỏng ảnh
    private final Color primaryColor = Color.BLACK; // Đen tuyền cho nút
    private final Font mainFont = new Font("SansSerif", Font.PLAIN, 16);
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 32);

    public LoginFrame() {
        authService = new AuthService();

        setTitle("E-FITNESS - Log In");
        setExtendedState(Frame.MAXIMIZED_BOTH); // Full màn hình
        setBackground(bgColor);
        setLayout(new GridBagLayout()); // Dùng để canh giữa phần form

        // Tạo Panel chứa Form (Giống cái thẻ trắng ở giữa màn hình)
        Panel formPanel = new Panel();
        formPanel.setLayout(new GridLayout(8, 1, 0, 10)); // 8 dòng, khoảng cách 10px
        formPanel.setBackground(bgColor);

        // 1. Logo / Tiêu đề
        Label lblTitle = new Label("E - FITNESS", Label.CENTER);
        lblTitle.setFont(titleFont);
        formPanel.add(lblTitle);

        Label lblSubTitle = new Label("Log in", Label.CENTER);
        lblSubTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        formPanel.add(lblSubTitle);

        // 2. Ô nhập Username (Email)
        Label lblUser = new Label("Username / Email");
        lblUser.setFont(mainFont);
        formPanel.add(lblUser);

        txtUsername = new TextField(20);
        txtUsername.setFont(mainFont);
        formPanel.add(txtUsername);

        // 3. Ô nhập Password
        Label lblPass = new Label("Password");
        lblPass.setFont(mainFont);
        formPanel.add(lblPass);

        txtPassword = new TextField(20);
        txtPassword.setFont(mainFont);
        txtPassword.setEchoChar('•');
        formPanel.add(txtPassword);

        // 4. Label báo lỗi (Ẩn mặc định)
        lblMessage = new Label("", Label.CENTER);
        lblMessage.setForeground(Color.RED);
        lblMessage.setFont(new Font("SansSerif", Font.ITALIC, 14));
        formPanel.add(lblMessage);

        // 5. Nút Login
        btnLogin = new Button("Log in");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setBackground(primaryColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnLogin);

        // 6. Nút Link chuyển qua Register
        lblRegisterLink = new Label("Don't have an account? Register", Label.CENTER);
        lblRegisterLink.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRegisterLink.setForeground(new Color(100, 100, 100)); // Màu xám tối
        lblRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(lblRegisterLink);

        // Thêm formPanel vào giữa Frame chính
        add(formPanel);

        // --- XỬ LÝ SỰ KIỆN ---

        // Bấm nút Đăng nhập
        btnLogin.addActionListener(e -> {
            if (authService.login(txtUsername.getText(), txtPassword.getText())) {
                this.dispose();
                System.out.println("Đăng nhập thành công! Chuyển sang MainFrame...");
                // TODO: new MainFrame().setVisible(true);
            } else {
                lblMessage.setText("Invalid credentials. Try again.");
            }
        });

        // Click vào dòng chữ Đăng ký
        lblRegisterLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new RegisterFrame().setVisible(true);
            }
        });

        // Nút X tắt cửa sổ
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}