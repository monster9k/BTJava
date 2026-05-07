package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * ChangePwdDialog.java
 * Dialog đổi mật khẩu admin.
 * Mở bằng: new ChangePwdDialog(parentFrame).setVisible(true);
 */
public class ChangePwdDialog extends JDialog {

    public ChangePwdDialog(JFrame parent) {
        super(parent, "🔒 Đổi Mật Khẩu", true);
        setSize(540, 380);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 12, 8, 12);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx   = 0;

        JPasswordField oldPwd     = new JPasswordField(20);
        JPasswordField newPwd     = new JPasswordField(20);
        JPasswordField confirmPwd = new JPasswordField(20);
        stylePasswordField(oldPwd);
        stylePasswordField(newPwd);
        stylePasswordField(confirmPwd);

        gbc.gridy=0; add(styledLabel("Mật khẩu hiện tại:"), gbc);
        gbc.gridy=1; add(oldPwd, gbc);
        gbc.gridy=2; add(styledLabel("Mật khẩu mới:"), gbc);
        gbc.gridy=3; add(newPwd, gbc);
        gbc.gridy=4; add(styledLabel("Xác nhận mật khẩu mới:"), gbc);
        gbc.gridy=5; add(confirmPwd, gbc);

        JButton btnSave = makeActionButton("💾 Lưu thay đổi", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            String np = new String(newPwd.getPassword());
            String cp = new String(confirmPwd.getPassword());
            if (!np.equals(cp)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // TODO: Gọi userDAO.changePassword(...)
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        gbc.gridy=6;
        gbc.insets = new Insets(14, 12, 8, 12);
        add(btnSave, gbc);
    }
}