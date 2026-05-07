package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * AddStaffDialog.java
 * Dialog thêm nhân viên mới.
 * Mở bằng: new AddStaffDialog(parentFrame).setVisible(true);
 */
public class AddStaffDialog extends JDialog {

    public AddStaffDialog(JFrame parent) {
        super(parent, "➕ Thêm Nhân Viên Mới", true);
        setSize(550, 600);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(10, 15, 10, 15);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx   = 0;

        JTextField     tfId   = makeStyledTextField("Nhập mã nhân viên (VD: U006)", 20);
        JTextField     tfUser = makeStyledTextField("Tên đăng nhập", 20);
        JTextField     tfName = makeStyledTextField("Họ và tên", 20);
        JPasswordField pfPass = new JPasswordField();
        stylePasswordField(pfPass);

        String[] roles = {"STAFF", "ADMIN"};
        JComboBox<String> cbRole = makeStyledCombo(roles);

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Mã nhân viên:"), gbc);
        gbc.gridy = row++; add(tfId, gbc);
        gbc.gridy = row++; add(styledLabel("Tên đăng nhập:"), gbc);
        gbc.gridy = row++; add(tfUser, gbc);
        gbc.gridy = row++; add(styledLabel("Họ và tên:"), gbc);
        gbc.gridy = row++; add(tfName, gbc);
        gbc.gridy = row++; add(styledLabel("Mật khẩu:"), gbc);
        gbc.gridy = row++; add(pfPass, gbc);
        gbc.gridy = row++; add(styledLabel("Vai trò:"), gbc);
        gbc.gridy = row++; add(cbRole, gbc);

        JButton btnSave = makeActionButton("💾 Lưu nhân viên", ACCENT_BLUE);
        btnSave.addActionListener(e -> {
            String name = tfName.getText().trim();
            if (name.isEmpty() || name.equals("Họ và tên")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!");
                return;
            }
            // TODO: Gọi staffDAO.insert(...)
            JOptionPane.showMessageDialog(this, "Thêm nhân viên \"" + name + "\" thành công!");
            dispose();
        });

        gbc.gridy = row;
        gbc.insets = new Insets(20, 15, 10, 15);
        add(btnSave, gbc);
    }
}