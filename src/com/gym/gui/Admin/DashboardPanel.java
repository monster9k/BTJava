package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.entity.CheckIn;
import com.gym.entity.Member;
import com.gym.entity.Subscription;
import com.gym.entity.GymPackage;
import com.gym.service.CheckInService;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.ReportService;
import com.gym.service.SubscriptionService;
import com.gym.service.UserService;

import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.gym.gui.AppStyle.*;













public class DashboardPanel extends JPanel {

    private DefaultTableModel expiringMembersModel;
    private DefaultTableModel todayCheckinModel;
    private final MemberService memberService = new MemberService();
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final CheckInService checkInService = new CheckInService();
    private final PackageService packageService = new PackageService();
    private final ReportService reportService = new ReportService();
    private final UserService userService = new UserService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    public DashboardPanel() {
        setBackground(BG_DARK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        build();
    }

    private void build() {
        
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(BG_DARK);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        int activeMembers = (int) memberService.getAllMembers().stream().filter(Member::isStatus).count();
        String revenue = currencyFormat.format(reportService.getCurrentMonthRevenue()) + "đ";
        int expiring = subscriptionService.getExpiringSubscriptionsDefault().size();
        int staffCount = userService.getAllStaff().size();

        statsRow.add(makeStatCard("Hội viên Active",    String.valueOf(activeMembers), "đang hoạt động", ACCENT_BLUE,   ""));
        statsRow.add(makeStatCard("Doanh thu tháng",    revenue, "tháng hiện tại", ACCENT_GREEN,  ""));
        statsRow.add(makeStatCard("Gói sắp hết hạn",   String.valueOf(expiring), "trong 5 ngày tới", ACCENT_ORANGE, ""));
        statsRow.add(makeStatCard("Nhân viên",          String.valueOf(staffCount), "đang hoạt động", ACCENT_RED,    ""));

        add(statsRow);
        add(Box.createVerticalStrut(20));

        
        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 0));
        row2.setBackground(BG_DARK);
        row2.add(buildExpiringMembersTable());
        row2.add(buildTodayCheckinPanel());
        add(row2);
    }

    

    private JPanel makeStatCard(String label, String value, String sub, Color accent, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new RoundedBorder(accent.darker(), 1, 12),
            new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(FONT_CARD_N);
        valueLbl.setForeground(TEXT_WHITE);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(FONT_MENU_B);
        labelLbl.setForeground(TEXT_WHITE);
        labelLbl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(accent);

        card.add(valueLbl, BorderLayout.NORTH);
        card.add(labelLbl, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);
        return card;
    }

    

    private JPanel buildExpiringMembersTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel title = new JLabel("Hội viên sắp hết hạn (5 ngày tới)");
        title.setFont(FONT_MENU_B);
        title.setForeground(TEXT_WHITE);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"Mã HV", "Họ tên", "SĐT", "Hết hạn"};
        expiringMembersModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loadExpiringMembers();

        JTable table = new JTable(expiringMembersModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    
    public void addExpiringMember(String memberId, String name, String phone, String expiryDate) {
        expiringMembersModel.addRow(new Object[]{memberId, name, phone, expiryDate});
    }

    
    public void clearExpiringMembers() {
        expiringMembersModel.setRowCount(0);
    }

    

    private JPanel buildTodayCheckinPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel title = new JLabel("Check-in hôm nay");
        title.setFont(FONT_MENU_B);
        title.setForeground(TEXT_WHITE);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"Mã HV", "Họ tên", "Gói tập", "Giờ vào"};
        todayCheckinModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        loadTodayCheckins();

        JTable table = new JTable(todayCheckinModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    
    public void addTodayCheckin(String memberId, String name, String packageName, String checkInTime) {
        todayCheckinModel.addRow(new Object[]{memberId, name, packageName, checkInTime});
    }

    
    public void clearTodayCheckin() {
        todayCheckinModel.setRowCount(0);
    }

    private void loadExpiringMembers() {
        clearExpiringMembers();
        for (Subscription s : subscriptionService.getExpiringSubscriptionsDefault()) {
            Member m = memberService.getMemberById(s.getMemberId());
            String code = m != null ? m.getMemberCode() : "";
            String name = m != null ? m.getFullName() : "";
            String phone = m != null ? m.getPhone() : "";
            String expiry = s.getEndDate() != null ? s.getEndDate().format(dateFmt) : "";
            addExpiringMember(code, name, phone, expiry);
        }
    }

    private void loadTodayCheckins() {
        clearTodayCheckin();
        for (CheckIn c : checkInService.getCheckInsForDate(java.time.LocalDate.now())) {
            Subscription s = subscriptionService.getSubscriptionById(c.getSubscriptionId());
            if (s == null) {
                continue;
            }
            Member m = memberService.getMemberById(s.getMemberId());
            GymPackage p = packageService.getPackageById(s.getPackageId());
            String code = m != null ? m.getMemberCode() : "";
            String name = m != null ? m.getFullName() : "";
            String pkg = p != null ? p.getPackageName() : "";
            String time = c.getCheckInTime() != null ? c.getCheckInTime().toLocalDateTime().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "";
            addTodayCheckin(code, name, pkg, time);
        }
    }
}