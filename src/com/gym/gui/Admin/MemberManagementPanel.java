package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;

import com.gym.entity.Member;
import com.gym.service.MemberService;

import static com.gym.gui.AppStyle.*;

/**
 * MemberManagementPanel.java
 * Panel quản lý hội viên: tìm kiếm, bảng danh sách, nút thêm hội viên.
 */
public class MemberManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final JFrame owner;
    private final MemberService memberService = new MemberService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        btnAdd.addActionListener(e -> {
            new AddMemberDialog(owner).setVisible(true);
            loadMembers(memberService.getAllMembers());
        });
        btnSearch.addActionListener(e -> loadMembers(memberService.searchMembers(searchField.getText())));
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

        loadMembers(memberService.getAllMembers());

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void loadMembers(java.util.List<Member> members) {
        clearData();
        for (Member m : members) {
            String birth = m.getBirthday() != null ? m.getBirthday().format(dateFmt) : "";
            String created = m.getCreatedAt() != null ? m.getCreatedAt().toLocalDate().format(dateFmt) : "";
            String status = m.isStatus() ? "✅ Active" : "🔴 Đã khóa";
            addRow(new Object[]{
                    m.getMemberCode(),
                    m.getFullName(),
                    m.getPhone(),
                    m.getGender(),
                    birth,
                    created,
                    status,
                    "Sửa | Xóa"
            });
        }
    }
}