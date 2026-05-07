package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.gym.gui.AppStyle.RoundedBorder;

import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * DashboardPanel.java
 * Màn hình tổng quan: 4 stat card + bảng sắp hết hạn + check-in hôm nay.
 *
 * Cách dùng từ AdminDashboard:
 *   showPanel(new DashboardPanel());
 *   // showPanel() nội bộ gọi contentPanel.add(panel, BorderLayout.CENTER)
 *
 * Cách update dữ liệu từ backend:
 *   dash.clearExpiringMembers();
 *   dash.addExpiringMember("MEM001", "Nguyễn Văn A", "0901...", "10/05/2026");
 */
public class DashboardPanel extends JPanel {

    private DefaultTableModel expiringMembersModel;
    private DefaultTableModel todayCheckinModel;

    public DashboardPanel() {
        setBackground(BG_DARK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        build();
    }

    private void build() {
        // --- Row 1: 4 Stat Cards ---
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(BG_DARK);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsRow.add(makeStatCard("Hội viên Active",    "248",        "+12 tháng này",           ACCENT_BLUE,   "🏃"));
        statsRow.add(makeStatCard("Doanh thu tháng",    "32.5M",      "+8% so tháng trước",      ACCENT_GREEN,  "💰"));
        statsRow.add(makeStatCard("Gói sắp hết hạn",   "17",         "trong 5 ngày tới",        ACCENT_ORANGE, "⚠️"));
        statsRow.add(makeStatCard("Nhân viên",          "5",          "đang hoạt động",          ACCENT_RED,    "👥"));

        add(statsRow);
        add(Box.createVerticalStrut(20));

        // --- Row 2: Bảng sắp hết hạn + Check-in ---
        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 0));
        row2.setBackground(BG_DARK);
        row2.add(buildExpiringMembersTable());
        row2.add(buildTodayCheckinPanel());
        add(row2);
    }

    // ==================== STAT CARD ====================

    private JPanel makeStatCard(String label, String value, String sub, Color accent, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new RoundedBorder(accent.darker(), 1, 12),
            new EmptyBorder(16, 18, 16, 18)
        ));
        
        //Panel chứa icon với hiệu ứng background
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40));
        iconPanel.setPreferredSize(new Dimension(44, 44));
        iconPanel.setBorder(new RoundedBorder(new Color(60, 65, 85), 1, 10));
        
        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20)); 
        emojiLbl.setForeground(TEXT_WHITE);
        iconPanel.add(emojiLbl);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBackground(CARD_BG);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(FONT_CARD_N);
        valueLbl.setForeground(TEXT_WHITE);

        top.add(iconPanel, BorderLayout.WEST);
        top.add(valueLbl, BorderLayout.EAST);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(FONT_MENU_B);
        labelLbl.setForeground(TEXT_WHITE);
        labelLbl.setBorder(new EmptyBorder(10, 0, 0, 0)); // Tạo khoảng cách với icon

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(accent);

        card.add(top, BorderLayout.NORTH);
        card.add(labelLbl, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);
        return card;
    }

    // ==================== EXPIRING MEMBERS TABLE ====================

    private JPanel buildExpiringMembersTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel title = new JLabel("⚠️  Hội viên sắp hết hạn (5 ngày tới)");
        title.setFont(FONT_MENU_B);
        title.setForeground(ACCENT_ORANGE);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"Mã HV", "Họ tên", "SĐT", "Hết hạn"};
        expiringMembersModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // Dữ liệu mẫu — thay bằng setData() khi có backend
        addExpiringMember("MEM26001", "Trần Thị Mai",  "0901234567", "05/05/2026");
        addExpiringMember("MEM26002", "Lê Văn Bình",   "0912345678", "06/05/2026");
        addExpiringMember("MEM26015", "Phạm Thu Hà",   "0923456789", "07/05/2026");
        addExpiringMember("MEM26020", "Nguyễn Minh",   "0934567890", "08/05/2026");
        addExpiringMember("MEM26031", "Võ Thị Lan",    "0945678901", "09/05/2026");

        JTable table = new JTable(expiringMembersModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /** Thêm hàng vào bảng sắp hết hạn. Gọi từ backend sau clearExpiringMembers(). */
    public void addExpiringMember(String memberId, String name, String phone, String expiryDate) {
        expiringMembersModel.addRow(new Object[]{memberId, name, phone, expiryDate});
    }

    /** Xóa toàn bộ dữ liệu bảng sắp hết hạn để load lại. */
    public void clearExpiringMembers() {
        expiringMembersModel.setRowCount(0);
    }

    // ==================== TODAY CHECK-IN TABLE ====================

    private JPanel buildTodayCheckinPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel title = new JLabel("✅  Check-in hôm nay");
        title.setFont(FONT_MENU_B);
        title.setForeground(ACCENT_GREEN);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"Mã HV", "Họ tên", "Gói tập", "Giờ vào"};
        todayCheckinModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // Dữ liệu mẫu
        addTodayCheckin("MEM26005", "Đặng Văn Tú",   "Gym 3 tháng",  "06:15");
        addTodayCheckin("MEM26009", "Bùi Thị Loan",  "Yoga 1 tháng", "07:00");
        addTodayCheckin("MEM26012", "Trương Minh",   "Gym VIP",       "07:45");
        addTodayCheckin("MEM26019", "Hồ Thu Nga",    "Zumba",         "08:30");
        addTodayCheckin("MEM26025", "Lý Văn Đức",   "Gym 3 tháng",   "09:00");

        JTable table = new JTable(todayCheckinModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /** Thêm hàng vào bảng check-in hôm nay. */
    public void addTodayCheckin(String memberId, String name, String packageName, String checkInTime) {
        todayCheckinModel.addRow(new Object[]{memberId, name, packageName, checkInTime});
    }

    /** Xóa toàn bộ dữ liệu bảng check-in để load lại. */
    public void clearTodayCheckin() {
        todayCheckinModel.setRowCount(0);
    }
}