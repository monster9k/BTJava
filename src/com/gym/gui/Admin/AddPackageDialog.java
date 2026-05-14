package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.math.BigDecimal;

import com.gym.service.PackageService;

import static com.gym.gui.AppStyle.*;

/**
 * AddPackageDialog.java
 * Dialog thêm gói tập mới.
 * Mở bằng: new AddPackageDialog(parentFrame).setVisible(true);
 */
public class AddPackageDialog extends JDialog {
    private final PackageService packageService = new PackageService();
    private final Runnable onSaved;

    public AddPackageDialog(JFrame parent) {
        this(parent, null);
    }

    public AddPackageDialog(JFrame parent, Runnable onSaved) {
        super(parent, "Thêm Gói Tập Mới", true);
        this.onSaved = onSaved;
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

        JTextField tfId       = makeStyledTextField("ID tự động", 20);
        tfId.setEditable(false);
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

        JButton btnSave = makeActionButton("Lưu gói tập", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên gói!");
                return;
            }
            try {
                int duration = Integer.parseInt(tfDuration.getText().trim());
                BigDecimal price = new BigDecimal(tfPrice.getText().trim());
                String desc = ((JTextArea) ((JScrollPane) descPanel.getComponent(0)).getViewport().getView()).getText().trim();

                boolean ok = packageService.addPackage(tfName.getText().trim(), duration, price, desc);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Thêm gói tập thất bại. Vui lòng kiểm tra dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Thời hạn và Giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Thêm gói tập mới thành công!");
            if (onSaved != null) {
                onSaved.run();
            }
            dispose();
        });

        gbc.gridy  = row;
        gbc.insets = new Insets(20, 15, 10, 15);
        add(btnSave, gbc);
    }
}