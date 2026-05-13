package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.gym.entity.GymPackage;
import com.gym.service.PackageService;

import static com.gym.gui.AppStyle.*;

/**
 * PackageManagementPanel.java
 * Panel quản lý gói tập. Hiển thị bảng danh sách gói + nút Thêm gói tập.
 */
public class PackageManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final JFrame owner;
    private PackageService packageService;
    private NumberFormat currencyFormat;

    public PackageManagementPanel(JFrame owner) {
        this.owner = owner;
        this.packageService = new PackageService();
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
        loadPackagesFromDB();
    }

    private void build() {
        // --- Toolbar ---
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Quản lý Gói tập");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAdd = makeActionButton("➕ Thêm gói tập", ACCENT_GREEN);
        btnAdd.addActionListener(e -> new AddPackageDialog(owner).setVisible(true));
        toolBar.add(title,  BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        // --- Table ---
        String[] cols = {"ID", "Tên gói", "Thời hạn (ngày)", "Giá (VNĐ)", "Mô tả", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Load dữ liệu package từ database
     */
    private void loadPackagesFromDB() {
        try {
            clearData();
            java.util.List<GymPackage> packages = packageService.getAllPackages();

            for (GymPackage pkg : packages) {
                String status = pkg.isStatus() ? "✅ Active" : "🔴 Ẩn";
                String price = currencyFormat.format(pkg.getPrice().doubleValue());

                tableModel.addRow(new Object[]{
                    pkg.getId(),
                    pkg.getPackageName(),
                    pkg.getDurationDays(),
                    price,
                    pkg.getDescription() != null ? pkg.getDescription() : "",
                    status,
                    "Sửa"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu gói tập: " + e.getMessage(),
                "Lỗi Database",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("Error loading packages: " + e);
            e.printStackTrace();
        }
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }
}