package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;

import com.gym.entity.Member;
import com.gym.service.MemberService;
import com.gym.service.UserService;

import static com.gym.gui.AppStyle.*;





public class MemberManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private java.util.List<Member> cachedMembers;
    private final JFrame owner;
    private final MemberService memberService = new MemberService();
    private final UserService userService = new UserService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MemberManagementPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("Quản lý Hội viên");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBar.setBackground(BG_DARK);
        JTextField searchField = makeStyledTextField("Tìm theo tên hoặc SĐT...", 20);
        JButton btnSearch = makeActionButton("Tìm kiếm", ACCENT_BLUE);
        JButton btnAdd    = makeActionButton("Thêm hội viên", ACCENT_GREEN);
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

        
        String[] cols = {"Mã HV","Họ tên","Tài khoản","SĐT","Giới tính","Ngày sinh","Ngày đăng ký","Trạng thái","Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loadMembers(memberService.getAllMembers());

        table = new JTable(tableModel);
        styleTableAppearance(table);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 8) {
                    showActionMenu(row, e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void loadMembers(java.util.List<Member> members) {
        clearData();
        cachedMembers = members;
        for (Member m : members) {
            String birth = m.getBirthday() != null ? m.getBirthday().format(dateFmt) : "";
            String created = m.getCreatedAt() != null ? m.getCreatedAt().toLocalDate().format(dateFmt) : "";
            String status = m.isStatus() ? "Active" : "Đã khóa";
            String username = "";
            if (m.getUserId() != null) {
                com.gym.entity.User u = userService.getById(m.getUserId());
                if (u != null) {
                    username = u.getUsername();
                }
            }
            addRow(new Object[]{
                    m.getMemberCode(),
                    memberService.resolveDisplayName(m),
                    username,
                    memberService.resolvePhone(m),
                    m.getGender(),
                    birth,
                    created,
                    status,
                    "Sửa | Khóa"
            });
        }
    }

    private void showActionMenu(int row, Component invoker, int x, int y) {
        if (cachedMembers == null || row >= cachedMembers.size()) {
            return;
        }
        Member selected = cachedMembers.get(row);
        JPopupMenu menu = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Sửa thông tin");
        edit.addActionListener(e -> {
            new EditMemberDialog(owner, selected).setVisible(true);
            loadMembers(memberService.getAllMembers());
        });
        JMenuItem toggle = new JMenuItem(selected.isStatus() ? "Khóa hội viên" : "Mở khóa hội viên");
        toggle.addActionListener(e -> handleToggleStatus(selected));
        menu.add(edit);
        menu.add(toggle);
        menu.show(invoker, x, y);
    }

    private void handleToggleStatus(Member member) {
        boolean targetStatus = !member.isStatus();
        String question = targetStatus
                ? "Bạn có chắc muốn mở khóa hội viên này?"
                : "Bạn có chắc muốn khóa hội viên này?";
        int confirm = JOptionPane.showConfirmDialog(
                this,
                question,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        boolean ok = memberService.updateMemberStatus(member.getId(), targetStatus);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật trạng thái hội viên thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (member.getUserId() != null) {
            userService.toggleUserStatus(member.getUserId(), targetStatus);
        }
        loadMembers(memberService.getAllMembers());
    }
}