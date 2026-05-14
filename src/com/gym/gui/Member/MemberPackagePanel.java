package com.gym.gui.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.gym.entity.GymPackage;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;

import static com.gym.gui.AppStyle.*;

public class MemberPackagePanel extends JPanel {

    private final int memberId;
    private final PackageService packageService = new PackageService();
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    private DefaultTableModel tableModel;
    private JTable table;

    public MemberPackagePanel(int memberId) {
        this.memberId = memberId;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("Danh sach goi tap");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JButton btnRegister = makeActionButton("Dang ky goi da chon", ACCENT_ORANGE);
        btnRegister.addActionListener(e -> handleRegister());

        header.add(title, BorderLayout.WEST);
        header.add(btnRegister, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] cols = {"ID", "Ten goi", "Thoi han (ngay)", "Gia (VND)", "Mo ta"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);

        loadPackages();
    }

    private void loadPackages() {
        tableModel.setRowCount(0);
        List<GymPackage> packages = packageService.getActivePackages();
        for (GymPackage pkg : packages) {
            String price = currencyFormat.format(pkg.getPrice());
            tableModel.addRow(new Object[]{
                    pkg.getId(),
                    pkg.getPackageName(),
                    pkg.getDurationDays(),
                    price,
                    pkg.getDescription() != null ? pkg.getDescription() : ""
            });
        }
    }

    private void handleRegister() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui long chon goi tap.", "Thong bao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int packageId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        boolean ok = subscriptionService.registerPackagePending(memberId, packageId);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Dang ky that bai. Vui long thu lai.", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Da gui yeu cau dang ky. Vui long thanh toan tai quay de kich hoat.",
                "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
    }
}
