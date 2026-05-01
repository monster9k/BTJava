package com.gym.gui.auth;

import com.gym.service.AuthService;
import com.gym.util.AppConstants;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends Frame {
    private TextField txtUsername, txtPassword, txtFullName;
    private Choice choiceRole;
    private Button btnRegister;
    private Label lblMessage, lblLoginLink;
    private AuthService authService;

    private final Color bgColor = new Color(245, 238, 250);
    private final Color primaryColor = Color.BLACK;
    private final Font mainFont = new Font("SansSerif", Font.PLAIN, 16);

    public RegisterFrame() {
        authService = new AuthService();

        setTitle("E-FITNESS - Register");
        setExtendedState(Frame.MAXIMIZED_BOTH); // Full màn hình
        setBackground(bgColor);
        setLayout(new GridBagLayout());

        Panel formPanel = new Panel();
        formPanel.setLayout(new GridLayout(12, 1, 0, 5));
        formPanel.setBackground(bgColor);

        // Tiêu đề
        Label lblTitle = new Label("Register Account", Label.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        formPanel.add(lblTitle);
        formPanel.add(new Label("")); // Spacer

        // Form fields
        formPanel.add(createLabel("Username"));
        txtUsername = new TextField(25);
        txtUsername.setFont(mainFont);
        formPanel.add(txtUsername);

        formPanel.add(createLabel("Password"));
        txtPassword = new TextField(25);
        txtPassword.setFont(mainFont);
        txtPassword.setEchoChar('•');
        formPanel.add(txtPassword);

        formPanel.add(createLabel("Full Name"));
        txtFullName = new TextField(25);
        txtFullName.setFont(mainFont);
        formPanel.add(txtFullName);

        formPanel.add(createLabel("Role"));
        choiceRole = new Choice();
        choiceRole.setFont(mainFont);
        choiceRole.add("Staff");
        choiceRole.add("Admin");
        formPanel.add(choiceRole);

        lblMessage = new Label("", Label.CENTER);
        lblMessage.setForeground(Color.RED);
        formPanel.add(lblMessage);

        btnRegister = new Button("Register");
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegister.setBackground(primaryColor);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnRegister);

        lblLoginLink = new Label("Already have an account? Log in", Label.CENTER);
        lblLoginLink.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(lblLoginLink);

        add(formPanel);

        // --- XỬ LÝ SỰ KIỆN ---
        btnRegister.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = txtPassword.getText();
            String name = txtFullName.getText();
            int roleId = choiceRole.getSelectedItem().equals("Admin") ? AppConstants.ROLE_ADMIN : AppConstants.ROLE_STAFF;

            if (authService.register(user, pass, name, roleId)) {
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                lblMessage.setText("Please fill all fields!");
            }
        });

        lblLoginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });
    }

    // Helper method để code Clean hơn
    private Label createLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(mainFont);
        return lbl;
    }
}