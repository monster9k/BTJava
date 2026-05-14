package com.gym.gui.Staff;

import javax.swing.*;
import java.awt.*;

import com.gym.entity.User;
import com.gym.service.UserService;

import static com.gym.gui.AppStyle.*;

public class EditStaffDialog extends JDialog {
    private final UserService userService = new UserService();

    public EditStaffDialog(JFrame parent, User user) {
        super(parent, "Sua nhan vien", true);
        setSize(480, 420);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm(user);
    }

    private void buildForm(User user) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JTextField tfUsername = makeStyledTextField(user.getUsername(), 20);
        tfUsername.setEditable(false);
        JTextField tfFullName = makeStyledTextField(user.getFullname() != null ? user.getFullname() : "", 20);
        JTextField tfPhone = makeStyledTextField(user.getPhone() != null ? user.getPhone() : "", 20);

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Ten dang nhap:"), gbc);
        gbc.gridy = row++; add(tfUsername, gbc);
        gbc.gridy = row++; add(styledLabel("Ho va ten:"), gbc);
        gbc.gridy = row++; add(tfFullName, gbc);
        gbc.gridy = row++; add(styledLabel("So dien thoai:"), gbc);
        gbc.gridy = row++; add(tfPhone, gbc);

        JButton btnSave = makeActionButton("Luu thay doi", ACCENT_BLUE);
        btnSave.addActionListener(e -> {
            String fullName = tfFullName.getText().trim();
            String phone = tfPhone.getText().trim();
            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long nhap ho ten.");
                return;
            }
            boolean ok = userService.updateProfileById(user.getId(), fullName, phone);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Cap nhat that bai.", "Loi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Cap nhat thanh cong.");
            dispose();
        });

        gbc.gridy = row;
        gbc.insets = new Insets(20, 15, 10, 15);
        add(btnSave, gbc);
    }
}

