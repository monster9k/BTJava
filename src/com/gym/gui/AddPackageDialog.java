package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * AddPackageDialog.java
 * Dialog thêm gói tập mới.
 * Mở bằng: new AddPackageDialog(parentFrame).setVisible(true);
 */
public class AddPackageDialog extends JDialog {

    public AddPackageDialog(JFrame parent) {
        super(parent, "➕ Thêm Gói Tập Mới", true);
        pack();
        setMinimumSize(new Dimension(500, 650));
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

        JTextField tfId       = makeStyledTextField("Nhập mã gói (VD: P08)", 20);
        JTextField tfName     = makeStyledTextField("Tên gói (1 tháng, VIP...)", 20);
        JTextField tfDuration = makeStyledTextField("Số ngày sử dụng (VD: 30)", 20);
        JTextField tfPrice    = makeStyledTextField("Giá niêm yết (VD: 500000)", 20);
        JPanel     descPanel  = makeStyledTextArea("Nhập mô tả chi tiết về gói tập...", 4, 20);

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Mã gói tập (ID):"), gbc);
        gbc.gridy = row++; add(tfId, gbc);
        gbc.gridy = row++; add(styledLabel("Tên gói tập:"), gbc);
        gbc.gridy = row++; add(tfName, gbc);
        gbc.gridy = row++; add(styledLabel("Thời hạn (ngày):"), gbc);
        gbc.gridy = row++; add(tfDuration, gbc);
        gbc.gridy = row++; add(styledLabel("Giá hiện tại (VNĐ):"), gbc);
        gbc.gridy = row++; add(tfPrice, gbc);
        gbc.gridy = row++; add(styledLabel("Mô tả thêm:"), gbc);

        gbc.gridy   = row++;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(descPanel, gbc);
        gbc.weighty = 0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        JButton btnSave = makeActionButton("💾 Lưu gói tập", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            if (tfId.getText().trim().isEmpty() || tfName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ ID và Tên gói!");
                return;
            }
            // TODO: Gọi packageDAO.insert(...)
            JOptionPane.showMessageDialog(this, "Thêm gói tập mới thành công!");
            dispose();
        });

        gbc.gridy  = row;
        gbc.insets = new Insets(20, 15, 10, 15);
        add(btnSave, gbc);
    }
}