package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * SubscriptionPanel.java
 * Panel đăng ký / gia hạn gói tập. Bảng danh sách subscription + nút đăng ký mới.
 */
public class SubscriptionPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final JFrame owner;

    public SubscriptionPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Toolbar ---
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Đăng ký / Gia hạn Gói tập");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAdd = makeActionButton("➕ Đăng ký gói mới", ACCENT_ORANGE);
        btnAdd.addActionListener(e -> new AddSubscriptionDialog(owner).setVisible(true));
        toolBar.add(title,  BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        // --- Table ---
        String[] cols = {"ID","Mã HV","Hội viên","Gói tập","Ngày bắt đầu","Ngày hết hạn","Giá mua (VNĐ)","TT Gói","TT Thanh toán","Ngày tạo"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableModel.addRow(new Object[]{"S001","MEM26001","Trần Thị Mai",  "Gym 3 tháng", "01/02/2026","02/05/2026","900,000",   "🔴 Hết hạn","✅ Đã TT",  "01/02/2026 08:30"});
        tableModel.addRow(new Object[]{"S002","MEM26001","Trần Thị Mai",  "Gym 3 tháng", "03/05/2026","01/08/2026","900,000",   "✅ Active",  "✅ Đã TT",  "03/05/2026 09:00"});
        tableModel.addRow(new Object[]{"S003","MEM26002","Lê Văn Bình",   "Yoga 1 tháng","05/04/2026","04/05/2026","400,000",   "✅ Active",  "✅ Đã TT",  "05/04/2026 10:15"});
        tableModel.addRow(new Object[]{"S004","MEM26003","Phạm Thu Hà",   "Gym VIP",     "01/05/2026","31/05/2026","800,000",   "✅ Active",  "⏳ Chưa TT","01/05/2026 14:00"});
        tableModel.addRow(new Object[]{"S005","MEM26004","Nguyễn V Minh", "Gym 6 tháng", "01/01/2026","30/06/2026","1,600,000", "✅ Active",  "✅ Đã TT",  "01/01/2026 11:45"});

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }
}