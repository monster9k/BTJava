package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * PackageManagementPanel.java
 * Panel quản lý gói tập. Hiển thị bảng danh sách gói + nút Thêm gói tập.
 */
public class PackageManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final JFrame owner;

    public PackageManagementPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
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

        // Dữ liệu mẫu
        tableModel.addRow(new Object[]{"P01","Gym 1 tháng",  "30",  "350,000",   "Tập gym cơ bản",    "✅ Active","Sửa"});
        tableModel.addRow(new Object[]{"P02","Gym 3 tháng",  "90",  "900,000",   "Tiết kiệm 14%",     "✅ Active","Sửa"});
        tableModel.addRow(new Object[]{"P03","Gym 6 tháng",  "180", "1,600,000", "Tiết kiệm 24%",     "✅ Active","Sửa"});
        tableModel.addRow(new Object[]{"P04","Gym VIP",      "30",  "800,000",   "PT + phòng riêng",  "✅ Active","Sửa"});
        tableModel.addRow(new Object[]{"P05","Yoga 1 tháng", "30",  "400,000",   "Lớp Yoga buổi sáng","✅ Active","Sửa"});
        tableModel.addRow(new Object[]{"P06","Zumba",        "30",  "350,000",   "Lớp Zumba Dance",   "✅ Active","Sửa"});
        tableModel.addRow(new Object[]{"P07","Combo VIP",    "365", "5,000,000", "Gym + Yoga trọn năm","🔴 Ẩn",   "Sửa"});

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }
}