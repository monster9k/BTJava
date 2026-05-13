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
        String[] cols = {"ID", "Tên đăng nhập", "Họ tên", "Vai trò", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loadStaff();

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    /** Xóa toàn bộ dữ liệu bảng. Gọi trước khi load dữ liệu mới. */
    public void clearData() { tableModel.setRowCount(0); }

    /** Thêm một hàng nhân viên vào bảng. */
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void loadStaff() {
        clearData();
        for (User u : userService.getAllStaff()) {
            String role = u.getRoleId() == AppConstants.ROLE_ADMIN ? "ADMIN" : "STAFF";
            String status = u.isStatus() ? "✅ Active" : "🔴 Locked";
            addRow(new Object[]{
                    "U" + u.getId(),
                    u.getUsername(),
                    u.getFullname(),
                    role,
                    status,
                    "Sửa | Khóa"
            });
        }
    }
}