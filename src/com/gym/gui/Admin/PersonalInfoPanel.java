package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.CompoundBorder;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.auth.UserSession;
import com.gym.entity.User;
import com.gym.service.UserService;

import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * PersonalInfoPanel.java
 * Panel hiển thị & chỉnh sửa thông tin cá nhân của admin.
 * Hiện gồm: tên hiển thị và đổi mật khẩu (UI demo).
 */
public class PersonalInfoPanel extends JPanel {

    private final JFrame owner;
    private String adminName;
    private final UserService userService = new UserService();


    public PersonalInfoPanel(JFrame owner, String adminName) {
        this.owner = owner;
        this.adminName = adminName;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // Header
        JLabel title = new JLabel(" Thông tin cá nhân");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(BG_DARK);
        content.setLayout(new GridBagLayout());
        add(content, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // Card hiển thị thông tin
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new AppStyle.RoundedBorder(DIVIDER, 1, 12),
                BorderFactory.createEmptyBorder(24, 26, 24, 26)
        ));


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;

        // Tên hiển thị
        c.gridy = 0;
        card.add(styledLabel("Tên hiển thị:"), c);
        c.gridy = 1;
        JTextField tfName = makeStyledTextField(adminName == null ? "" : adminName, 20);
        tfName.setEditable(true);
        card.add(tfName, c);

        // Mật khẩu
        c.gridy = 2;
        card.add(styledLabel("Mật khẩu (đổi):"), c);
        c.gridy = 3;

        JPasswordField pfNew = new JPasswordField(20);
        stylePasswordField(pfNew);
        card.add(pfNew, c);

        c.gridy = 6;
        card.add(styledLabel("Xác nhận mật khẩu:"), c);
        c.gridy = 7;
        JPasswordField pfConfirm = new JPasswordField(20);
        stylePasswordField(pfConfirm);
        card.add(pfConfirm, c);

        JButton btnSave = makeActionButton("Lưu thay đổi", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            String np = new String(pfNew.getPassword());
            String cp = new String(pfConfirm.getPassword());
            if (np.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!np.equals(cp)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User current = UserSession.getCurrentUser();
            if (current == null) {
                JOptionPane.showMessageDialog(this, "Không xác định được tài khoản hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newName = tfName.getText().trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên hiển thị không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean okProfile = userService.updateProfile(current.getUsername(), newName, current.getPhone());
            boolean okPwd = userService.changePasswordNoVerify(current.getUsername(), np);
            if (!okProfile || !okPwd) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Đã lưu thông tin cá nhân.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        });

        c.gridy = 8;
        c.insets = new Insets(20, 8, 8, 8);
        card.add(btnSave, c);

        gbc.gridy = 0;
        content.add(card, gbc);
    }
}

