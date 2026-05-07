package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * MemberManagementPanel.java
 * Panel quản lý hội viên: tìm kiếm, bảng danh sách, nút thêm hội viên.
 */
public class MemberManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final JFrame owner;

    public MemberManagementPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Header + Search Bar ---
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("🏃  Quản lý Hội viên");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBar.setBackground(BG_DARK);
        JTextField searchField = makeStyledTextField("Tìm theo tên hoặc SĐT...", 20);
        JButton btnSearch = makeActionButton("🔍 Tìm kiếm", ACCENT_BLUE);
        JButton btnAdd    = makeActionButton("➕ Thêm hội viên", ACCENT_GREEN);
        btnAdd.addActionListener(e -> new AddMemberDialog(owner).setVisible(true));
        // TODO: btnSearch.addActionListener(e -> search(searchField.getText()));
        searchBar.add(searchField);
        searchBar.add(btnSearch);
        searchBar.add(btnAdd);

        header.add(title,     BorderLayout.WEST);
        header.add(searchBar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- Table ---
        String[] cols = {"Mã HV","Họ tên","SĐT","Giới tính","Ngày sinh","Ngày đăng ký","Trạng thái","Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // Dữ liệu mẫu
        tableModel.addRow(new Object[]{"MEM26001","Trần Thị Mai",   "0901234567","Nữ", "15/03/1998","01/01/2026","✅ Active",   "Sửa | Xóa"});
        tableModel.addRow(new Object[]{"MEM26002","Lê Văn Bình",    "0912345678","Nam","22/07/1995","05/01/2026","✅ Active",   "Sửa | Xóa"});
        tableModel.addRow(new Object[]{"MEM26003","Phạm Thu Hà",    "0923456789","Nữ", "08/11/2000","10/01/2026","✅ Active",   "Sửa | Xóa"});
        tableModel.addRow(new Object[]{"MEM26004","Nguyễn Văn Minh","0934567890","Nam","30/04/1992","12/01/2026","✅ Active",   "Sửa | Xóa"});
        tableModel.addRow(new Object[]{"MEM26005","Võ Thị Lan",     "0945678901","Nữ", "14/06/1999","15/01/2026","🔴 Đã khóa","Sửa | Mở"});

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }
}