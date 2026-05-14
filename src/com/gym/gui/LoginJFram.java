package com.gym.gui;

//import com.gym.entity.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import com.gym.auth.AuthService;
import com.gym.auth.UserSession;
import com.gym.entity.User;
import com.gym.gui.Admin.AdminDashboard;
import com.gym.gui.Member.MemberDashboard;
import com.gym.gui.Staff.*;
import com.gym.util.AppConstants;


/**
 * LoginJFrame - Màn hình đăng nhập sử dụng Swing
 */

public class LoginJFram extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private JButton btnRegister; // New register button
    private final AuthService authService = new AuthService();

    public LoginJFram() {
        initComponents();
        configureFrame();
    }

    private void initComponents() {
        setLayout(new GridLayout(1, 2));

        leftPanel = buildLeftPanel();
        rightPanel = buildRightPanel();

        add(leftPanel);
        add(rightPanel);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        boolean ok = authService.login(username, password);
        if (!ok) {
            lblError.setText("Sai tên đăng nhập hoặc mật khẩu!");
            txtPassword.setText("");
            return;
        }

        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            lblError.setText("Không thể tạo phiên đăng nhập, vui lòng thử lại!");
            txtPassword.setText("");
            return;
        }

        lblError.setText(" ");
        this.dispose();

        String displayName = currentUser.getFullname();
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = currentUser.getUsername();
        }

        if (currentUser.getRoleId() == AppConstants.ROLE_ADMIN) {
            new AdminDashboard(displayName).setVisible(true);
        } else if (currentUser.getRoleId() == AppConstants.ROLE_STAFF) {
            JOptionPane.showMessageDialog(this, "Chào mừng Staff: " + displayName);
            new StaffDashboard(currentUser.getUsername(), displayName).setVisible(true);
        } else if (currentUser.getRoleId() == AppConstants.ROLE_MEMBER) {
            new MemberDashboard(currentUser).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản không có quyền truy cập!");
        }
    }

    // New registration handler
    private void handleRegister() {
        new RegisterDialog(this).setVisible(true);
    }

    // PANEL TRÁI — Logo và Slogan
    private JPanel buildLeftPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(22, 22, 30));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(22, 22, 30));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(new Color(255, 193, 7));
        accentBar.setMaximumSize(new Dimension(60, 5));
        
        JLabel lblLogo = new JLabel("GYM MANAGER");
        lblLogo.setFont(new Font("Tahoma", Font.BOLD, 36));
        lblLogo.setForeground(new Color(255, 193, 7));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        root.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(new Color(245, 246, 250));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(22, 22, 30));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login button
        btnLogin = new JButton(" ĐĂNG NHẬP ");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBackground(new Color(255, 193, 7));
        btnLogin.setFocusPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> handleLogin());

        // Register button/link
        btnRegister = new JButton("Chưa có tài khoản? ĐĂNG KÝ");
        btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnRegister.setBackground(new Color(245, 246, 250));
        btnRegister.setForeground(new Color(255, 193, 7));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.addActionListener(e -> handleRegister());

        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        form.add(Box.createRigidArea(new Dimension(0, 15)));
        form.add(lblError);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(btnLogin);
        form.add(Box.createRigidArea(new Dimension(0, 15)));
        form.add(btnRegister);
        form.add(Box.createVerticalGlue());

        root.add(form, BorderLayout.CENTER);
        return root;
    }

    private void configureFrame() {
        setTitle("GymManager — Login");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // New Registration Dialog Class
    private class RegisterDialog extends JDialog {
        private JTextField txtRegUsername, txtRegFullname, txtRegPhone;
        private JPasswordField txtRegPassword, txtRegConfirmPassword;
        private JButton btnRegister, btnCancel;
        private JLabel lblRegError;

        public RegisterDialog(Frame parent) {
            super(parent, "Đăng ký tài khoản mới", true);
            initComponents();
        }

        private void initComponents() {
            setSize(450, 500); // Reduced height since email is removed
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(245, 246, 250));
            mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

            JPanel form = new JPanel();
            form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
            form.setBackground(new Color(245, 246, 250));
            form.setPreferredSize(new Dimension(370, 350)); // Reduced height

            // Title
            JLabel lblTitle = new JLabel("TẠO TÀI KHOẢN MỚI");
            lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblTitle.setForeground(new Color(22, 22, 30));
            lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Fullname
            JLabel lblFullname = new JLabel("Họ và tên:");
            lblFullname.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblFullname.setAlignmentX(Component.CENTER_ALIGNMENT);
            txtRegFullname = new JTextField();
            txtRegFullname.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txtRegFullname.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Phone
            JLabel lblPhone = new JLabel("Số điện thoại:");
            lblPhone.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblPhone.setAlignmentX(Component.CENTER_ALIGNMENT);
            txtRegPhone = new JTextField();
            txtRegPhone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txtRegPhone.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Username
            JLabel lblUsername = new JLabel("Tên đăng nhập:");
            lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
            txtRegUsername = new JTextField();
            txtRegUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txtRegUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Password
            JLabel lblPassword = new JLabel("Mật khẩu:");
            lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
            txtRegPassword = new JPasswordField();
            txtRegPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txtRegPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Password requirement note
            JLabel lblPasswordNote = new JLabel("(* Mật khẩu phải có ít nhất 6 ký tự)");
            lblPasswordNote.setFont(new Font("Tahoma", Font.PLAIN, 10));
            lblPasswordNote.setForeground(new Color(140, 140, 160));
            lblPasswordNote.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Confirm Password
            JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
            lblConfirmPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblConfirmPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
            txtRegConfirmPassword = new JPasswordField();
            txtRegConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txtRegConfirmPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Error label
            lblRegError = new JLabel(" ");
            lblRegError.setForeground(Color.RED);
            lblRegError.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(new Color(245, 246, 250));
            
            btnRegister = new JButton("ĐĂNG KÝ");
            btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
            btnRegister.setBackground(new Color(255, 193, 7));
            btnRegister.setFocusPainted(false);
            btnRegister.setPreferredSize(new Dimension(120, 35));
            btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRegister.addActionListener(e -> handleRegister());

            btnCancel = new JButton("HỦY");
            btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
            btnCancel.setBackground(Color.LIGHT_GRAY);
            btnCancel.setFocusPainted(false);
            btnCancel.setForeground(Color.BLACK);
            btnCancel.setPreferredSize(new Dimension(120, 35));
            btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(Box.createHorizontalStrut(20));
            buttonPanel.add(btnRegister);

            // Add components to form (EMAIL REMOVED)
            form.add(Box.createVerticalGlue());
            form.add(lblTitle);
            form.add(Box.createRigidArea(new Dimension(0, 25)));
            form.add(lblFullname);
            form.add(Box.createRigidArea(new Dimension(0, 5)));
            form.add(txtRegFullname);
            form.add(Box.createRigidArea(new Dimension(0, 15)));
            form.add(lblPhone);
            form.add(Box.createRigidArea(new Dimension(0, 5)));
            form.add(txtRegPhone);
            form.add(Box.createRigidArea(new Dimension(0, 15)));
            form.add(lblUsername);
            form.add(Box.createRigidArea(new Dimension(0, 5)));
            form.add(txtRegUsername);
            form.add(Box.createRigidArea(new Dimension(0, 15)));
            form.add(lblPassword);
            form.add(Box.createRigidArea(new Dimension(0, 5)));
            form.add(txtRegPassword);
            form.add(Box.createRigidArea(new Dimension(0, 5)));
            form.add(lblPasswordNote);
            form.add(Box.createRigidArea(new Dimension(0, 15)));
            form.add(lblConfirmPassword);
            form.add(Box.createRigidArea(new Dimension(0, 5)));
            form.add(txtRegConfirmPassword);
            form.add(Box.createRigidArea(new Dimension(0, 15)));
            form.add(lblRegError);
            form.add(Box.createRigidArea(new Dimension(0, 20)));
            form.add(buttonPanel);
            form.add(Box.createVerticalGlue());

            mainPanel.add(form, BorderLayout.CENTER);
            add(mainPanel);

            getRootPane().setDefaultButton(btnRegister);
        }

        private void handleRegister() {
            String fullname = txtRegFullname.getText().trim();
            String phone = txtRegPhone.getText().trim();
            String username = txtRegUsername.getText().trim();
            String password = new String(txtRegPassword.getPassword());
            String confirmPassword = new String(txtRegConfirmPassword.getPassword());

            // Validation (EMAIL REMOVED)
            if (fullname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                lblRegError.setText("Vui lòng điền đầy đủ thông tin!");
                return;
            }

            if (password.length() < 6) {
                lblRegError.setText("Mật khẩu phải có ít nhất 6 ký tự!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                lblRegError.setText("Mật khẩu xác nhận không khớp!");
                txtRegConfirmPassword.setText("");
                return;
            }

            // Check if username already exists in database
            boolean usernameExists = authService.checkUsernameExists(username);
            if (usernameExists) {
                lblRegError.setText("Tên đăng nhập đã tồn tại!");
                return;
            }

            boolean created = authService.register(username, password, fullname, phone, AppConstants.ROLE_MEMBER);
            if (!created) {
                lblRegError.setText("Đăng ký thất bại, vui lòng thử lại!");
                return;
            }

            JOptionPane.showMessageDialog(this,
                "Đăng ký thành công!\nTài khoản: " + username + "\nHọ tên: " + fullname + "\n\nBây giờ bạn có thể đăng nhập.",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
        }
       }
    }


