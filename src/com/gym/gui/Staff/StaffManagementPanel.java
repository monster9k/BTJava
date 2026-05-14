package com.gym.gui.Staff;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import com.gym.entity.User;
import com.gym.service.UserService;
import com.gym.util.AppConstants;

import static com.gym.gui.AppStyle.*;

/**
 * StaffManagementPanel.java
 * Panel quản lý nhân viên. Hiển thị bảng danh sách + nút Thêm nhân viên.
 *
 * Cách update dữ liệu:
 *   panel.clearData();
 *   panel.addRow(new Object[]{"U006","staff05","Lê Thị F","STAFF","✅ Active","Sửa | Khóa"});
 */
public class StaffManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private java.util.List<User> cachedStaff;
    private final JFrame owner;
    private final UserService userService = new UserService();

    public StaffManagementPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Toolbar ---
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Quản lý Nhân viên");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAdd = makeActionButton("➕ Thêm nhân viên", ACCENT_BLUE);
        btnAdd.addActionListener(e -> {
            new AddStaffDialog(owner).setVisible(true);
            loadStaff();
        });
        toolBar.add(title,  BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        // --- Table ---
        String[] cols = {"ID", "Tên đăng nhập", "Họ tên", "SĐT", "Vai trò", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loadStaff();

        table = new JTable(tableModel);
        styleTableAppearance(table);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 6) {
                    showActionMenu(row, e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    /** Xóa toàn bộ dữ liệu bảng. Gọi trước khi load dữ liệu mới. */
    public void clearData() { tableModel.setRowCount(0); }

    /** Thêm một hàng nhân viên vào bảng. */
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void loadStaff() {
        clearData();
        cachedStaff = userService.getAllStaff();
        for (User u : cachedStaff) {
            String role = u.getRoleId() == AppConstants.ROLE_ADMIN ? "ADMIN" : "STAFF";
            String status = u.isStatus() ? "✅ Active" : "🔴 Locked";
            addRow(new Object[]{
                    "U" + u.getId(),
                    u.getUsername(),
                    u.getFullname(),
                    u.getPhone(),
                    role,
                    status,
                    "Sửa | Khóa"
            });
        }
    }

    private void showActionMenu(int row, Component invoker, int x, int y) {
        if (cachedStaff == null || row >= cachedStaff.size()) {
            return;
        }
        User selected = cachedStaff.get(row);
        JPopupMenu menu = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Sửa thông tin");
        edit.addActionListener(e -> {
            new EditStaffDialog(owner, selected).setVisible(true);
            loadStaff();
        });
        JMenuItem toggle = new JMenuItem(selected.isStatus() ? "Khóa tài khoản" : "Mở khóa tài khoản");
        toggle.addActionListener(e -> {
            boolean ok = userService.toggleUserStatus(selected.getId(), !selected.isStatus());
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            loadStaff();
        });
        menu.add(edit);
        menu.add(toggle);
        menu.show(invoker, x, y);
    }
}