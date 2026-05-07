package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * AddMemberDialog.java
 * Dialog thêm hội viên mới.
 * Mở bằng: new AddMemberDialog(parentFrame).setVisible(true);
 */
public class AddMemberDialog extends JDialog {

    public AddMemberDialog(JFrame parent) {
        super(parent, "➕ Thêm Hội Viên Mới", true);
        setSize(500, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 15, 8, 15);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx   = 0;

        JTextField tfMemberCode = makeStyledTextField("MEM2026001", 20);
        JTextField tfFullName   = makeStyledTextField("Họ và tên khách hàng", 20);
        JTextField tfPhone      = makeStyledTextField("Số điện thoại (09...)", 20);

        String[] genders = {"Nam", "Nữ", "Khác"};
        JComboBox<String> cbGender = makeStyledCombo(genders);

        JSpinner spBirthday  = makeDatePicker();
        JSpinner spCreatedAt = makeDatePicker();

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Mã hội viên:"), gbc);
        gbc.gridy = row++; add(tfMemberCode, gbc);
        gbc.gridy = row++; add(styledLabel("Họ và tên:"), gbc);
        gbc.gridy = row++; add(tfFullName, gbc);
        gbc.gridy = row++; add(styledLabel("Số điện thoại:"), gbc);
        gbc.gridy = row++; add(tfPhone, gbc);
        gbc.gridy = row++; add(styledLabel("Giới tính:"), gbc);
        gbc.gridy = row++; add(cbGender, gbc);
        gbc.gridy = row++; add(styledLabel("Ngày sinh (dd/MM/yyyy):"), gbc);
        gbc.gridy = row++; add(spBirthday, gbc);
        gbc.gridy = row++; add(styledLabel("Ngày đăng ký:"), gbc);
        gbc.gridy = row++; add(spCreatedAt, gbc);

        JButton btnSave = makeActionButton("💾 Lưu hội viên", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            String name = tfFullName.getText().trim();
            if (name.isEmpty() || name.contains("Họ và tên")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên hội viên!");
                return;
            }
            java.util.Date dob = (java.util.Date) spBirthday.getValue();
            // TODO: Gọi memberDAO.insert(...)
            JOptionPane.showMessageDialog(this, "Thêm hội viên \"" + name + "\" thành công!\nNgày sinh: " + dob);
            dispose();
        });

        gbc.gridy = row;
        gbc.insets = new Insets(25, 15, 10, 15);
        add(btnSave, gbc);
    }
}