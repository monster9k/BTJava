package com.gym.gui.Admin;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import com.gym.entity.GymPackage;
import com.gym.service.PackageService;

import static com.gym.gui.AppStyle.*;

public class EditPackageDialog extends JDialog {
    private final PackageService packageService = new PackageService();

    public EditPackageDialog(JFrame parent, GymPackage pkg) {
        super(parent, "Sua goi tap", true);
        setSize(520, 660);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm(pkg);
    }

    private void buildForm(GymPackage pkg) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JTextField tfId = makeStyledTextField(String.valueOf(pkg.getId()), 20);
        tfId.setEditable(false);
        JTextField tfName = makeStyledTextField(pkg.getPackageName(), 20);
        JTextField tfDuration = makeStyledTextField(String.valueOf(pkg.getDurationDays()), 20);
        JTextField tfPrice = makeStyledTextField(pkg.getPrice() != null ? pkg.getPrice().toString() : "", 20);
        JPanel descPanel = makeStyledTextArea(pkg.getDescription() != null ? pkg.getDescription() : "", 4, 20);

        int row = 0;
        gbc.gridy = row++; add(styledLabel("ID:"), gbc);
        gbc.gridy = row++; add(tfId, gbc);
        gbc.gridy = row++; add(styledLabel("Ten goi tap:"), gbc);
        gbc.gridy = row++; add(tfName, gbc);
        gbc.gridy = row++; add(styledLabel("Thoi han (ngay):"), gbc);
        gbc.gridy = row++; add(tfDuration, gbc);
        gbc.gridy = row++; add(styledLabel("Gia (VND):"), gbc);
        gbc.gridy = row++; add(tfPrice, gbc);
        gbc.gridy = row++; add(styledLabel("Mo ta:"), gbc);

        gbc.gridy = row++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(descPanel, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnSave = makeActionButton("Luu thay doi", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long nhap ten goi.");
                return;
            }
            try {
                int duration = Integer.parseInt(tfDuration.getText().trim());
                BigDecimal price = new BigDecimal(tfPrice.getText().trim());
                String desc = ((JTextArea) ((JScrollPane) descPanel.getComponent(0)).getViewport().getView()).getText().trim();

                boolean ok = packageService.updatePackage(pkg.getId(), name, duration, price, desc);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Cap nhat that bai.", "Loi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Thoi han va gia phai la so hop le.", "Loi", JOptionPane.ERROR_MESSAGE);
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

