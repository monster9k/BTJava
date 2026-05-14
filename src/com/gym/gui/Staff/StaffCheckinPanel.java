package com.gym.gui.Staff;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.entity.CheckIn;
import com.gym.entity.Member;
import com.gym.entity.GymPackage;
import com.gym.entity.Subscription;
import com.gym.service.CheckInService;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.gym.gui.AppStyle.*;














public class StaffCheckinPanel extends JPanel {

    private final StaffDashboard owner;
    private JTextField           searchField;
    private JPanel               memberCard;
    private JLabel               checkinCountLabel;
    private DefaultTableModel    historyModel;
    private int                  checkinCount = 0;
    private Subscription         selectedSubscription;
    private final MemberService memberService = new MemberService();
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final PackageService packageService = new PackageService();
    private final CheckInService checkInService = new CheckInService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public StaffCheckinPanel(StaffDashboard owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 14));
        build();
    }

    
    private void build() {
        add(buildTitleRow(),  BorderLayout.NORTH);
        add(buildBody(),      BorderLayout.CENTER);
    }

    
    
    
    private JPanel buildTitleRow() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel lbl = new JLabel("Check-in Khách");
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_WHITE);

        checkinCountLabel = new JLabel("Hôm nay: " + checkinCount + " lượt");
        checkinCountLabel.setFont(FONT_SMALL);
        checkinCountLabel.setForeground(StaffDashboard.STAFF_ACCENT);
        checkinCountLabel.setBorder(new CompoundBorder(
            new RoundedBorder(StaffDashboard.STAFF_ACCENT, 1, 8),
            new EmptyBorder(3, 10, 3, 10)
        ));

        p.add(lbl,               BorderLayout.WEST);
        p.add(checkinCountLabel, BorderLayout.EAST);
        return p;
    }

    
    
    
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setBackground(BG_DARK);
        body.add(buildSearchBar(), BorderLayout.NORTH);

        JPanel cols = new JPanel(new GridLayout(1, 2, 16, 0));
        cols.setBackground(BG_DARK);
        cols.add(buildMemberCard());
        cols.add(buildHistoryPanel());
        body.add(cols, BorderLayout.CENTER);
        return body;
    }

    
    
    
    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setBackground(CARD_BG);
        bar.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 10),
            new EmptyBorder(10, 14, 10, 10)
        ));

        searchField = new JTextField();
        searchField.setBackground(CARD_BG);
        searchField.setForeground(TEXT_GRAY);
        searchField.setCaretColor(TEXT_WHITE);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setText("Nhập mã hội viên hoặc số điện thoại...");
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getForeground().equals(TEXT_GRAY)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Nhập mã hội viên hoặc số điện thoại...");
                    searchField.setForeground(TEXT_GRAY);
                }
            }
        });
        searchField.addActionListener(e -> doSearch()); 

        JButton btnSearch = makeActionButton("Tìm kiếm", StaffDashboard.STAFF_ACCENT);
        Color buttonFg = UIManager.getColor("Button.foreground");
        btnSearch.setForeground(buttonFg != null ? buttonFg : Color.BLACK);
        btnSearch.addActionListener(e -> doSearch());

        bar.add(searchField, BorderLayout.CENTER);
        bar.add(btnSearch,   BorderLayout.EAST);
        return bar;
    }

    
    
    
    private JPanel buildMemberCard() {
        memberCard = new JPanel(new BorderLayout(0, 14));
        memberCard.setBackground(CARD_BG);
        memberCard.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(20, 22, 20, 22)
        ));
        renderEmptyState();
        return memberCard;
    }

    
    private void renderEmptyState() {
        memberCard.removeAll();
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(CARD_BG);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel ico  = new JLabel("Tìm hội viên");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg = new JLabel("Không tìm thấy hội viên");
        msg.setFont(FONT_MENU_B);
        msg.setForeground(ACCENT_RED);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub  = new JLabel("Nhập mã HV hoặc SĐT ở thanh tìm kiếm");
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(ico);
        inner.add(Box.createVerticalStrut(10));
        inner.add(msg);
        inner.add(Box.createVerticalStrut(5));
        inner.add(sub);
        center.add(inner);
        memberCard.add(center, BorderLayout.CENTER);
        memberCard.revalidate();
        memberCard.repaint();
    }

    
    private void renderMemberFound(String memberId, String name, String phone,
                                    String pkg, String expiry, boolean allowed) {
        memberCard.removeAll();
        Color ac = allowed ? StaffDashboard.STAFF_ACCENT : ACCENT_RED;
        String statusTxt = allowed ? "DUOC PHEP VAO TAP" : "GOI HET HAN / BI KHOA";

        
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        badge.setBackground(new Color(ac.getRed(), ac.getGreen(), ac.getBlue(), 22));
        badge.setBorder(new CompoundBorder(
            new RoundedBorder(ac, 1, 8),
            new EmptyBorder(6, 16, 6, 16)
        ));
        JLabel badgeLbl = new JLabel(statusTxt);
        badgeLbl.setFont(FONT_MENU_B);
        badgeLbl.setForeground(ac);
        badge.add(badgeLbl);

        
        JLabel av = new JLabel("TH");
        av.setFont(new Font("Segoe UI", Font.BOLD, 18));
        av.setHorizontalAlignment(SwingConstants.CENTER);
        av.setPreferredSize(new Dimension(66, 66));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nameLbl.setForeground(TEXT_WHITE);
        nameLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel idLbl = new JLabel(memberId + "  •  " + phone);
        idLbl.setFont(FONT_SMALL);
        idLbl.setForeground(TEXT_GRAY);
        idLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel avBlock = new JPanel();
        avBlock.setOpaque(false);
        avBlock.setLayout(new BoxLayout(avBlock, BoxLayout.Y_AXIS));
        av.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        idLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        avBlock.add(av);
        avBlock.add(Box.createVerticalStrut(8));
        avBlock.add(nameLbl);
        avBlock.add(Box.createVerticalStrut(3));
        avBlock.add(idLbl);

        
        JPanel details = new JPanel(new GridLayout(3, 2, 6, 8));
        details.setBackground(new Color(35, 40, 58));
        details.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(12, 14, 12, 14)
        ));
        details.add(styledLabel("Gói tập:"));
        details.add(styledValue(pkg));
        details.add(styledLabel("Hết hạn:"));
        details.add(styledValue(expiry));
        details.add(styledLabel("Trạng thái:"));
        JLabel stLbl = new JLabel(allowed ? "Active" : "Hết hạn");
        stLbl.setFont(FONT_MENU_B);
        stLbl.setForeground(ac);
        details.add(stLbl);

        
        JButton btnConfirm = new JButton(allowed ? "XAC NHAN CHECK-IN" : "LIEN HE QUAN LY");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        Color btnFg = UIManager.getColor("Button.foreground");
        btnConfirm.setForeground(btnFg != null ? btnFg : Color.BLACK);
        Color btnBg = UIManager.getColor("Button.background");
        if (btnBg != null) {
            btnConfirm.setBackground(btnBg);
        }
        btnConfirm.setBorderPainted(true);
        btnConfirm.setFocusPainted(true);
        btnConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirm.setEnabled(allowed);
        btnConfirm.setPreferredSize(new Dimension(0, 44));
        btnConfirm.addActionListener(e -> doConfirmCheckin(memberId, name, pkg));

        
        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.setOpaque(false);
        south.add(details,    BorderLayout.CENTER);
        south.add(btnConfirm, BorderLayout.SOUTH);

        memberCard.add(badge,   BorderLayout.NORTH);
        memberCard.add(avBlock, BorderLayout.CENTER);
        memberCard.add(south,   BorderLayout.SOUTH);
        memberCard.revalidate();
        memberCard.repaint();
    }

    
    
    
    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel title = new JLabel("Lịch sử check-in hôm nay");
        title.setFont(FONT_MENU_B);
        title.setForeground(TEXT_WHITE);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã HV", "Họ tên", "Gói tập", "Giờ vào"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        loadHistoryToday();

        JTable table = new JTable(historyModel);
        styleTableAppearance(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void loadHistoryToday() {
        historyModel.setRowCount(0);
        int index = 1;
        for (CheckIn c : checkInService.getCheckInsForDate(LocalDate.now())) {
            Subscription s = subscriptionService.getSubscriptionById(c.getSubscriptionId());
            if (s == null) {
                continue;
            }
            Member m = memberService.getMemberById(s.getMemberId());
            GymPackage p = packageService.getPackageById(s.getPackageId());
            String memberCode = m != null ? m.getMemberCode() : "";
            String name = m != null ? memberService.resolveDisplayName(m) : "";
            String pkg = p != null ? p.getPackageName() : "";
            String time = c.getCheckInTime() != null
                    ? c.getCheckInTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    : "";
            historyModel.addRow(new Object[]{String.valueOf(index++), memberCode, name, pkg, time});
        }
        checkinCount = historyModel.getRowCount();
        checkinCountLabel.setText("Hôm nay: " + checkinCount + " lượt");
    }

    
    
    
    private void doSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty() || query.startsWith("Nhập mã")) return;
        Member member = memberService.findByCodeOrPhone(query);
        if (member == null) {
            renderNotFound();
            return;
        }

        selectedSubscription = chooseSubscriptionForCheckIn(member.getId());
        Subscription subForDisplay = selectedSubscription != null
                ? selectedSubscription
                : subscriptionService.getLatestSubscription(member.getId());

        GymPackage pkg = subForDisplay != null ? packageService.getPackageById(subForDisplay.getPackageId()) : null;

        String pkgName = pkg != null ? pkg.getPackageName() : "";
        String expiry = subForDisplay != null && subForDisplay.getEndDate() != null
                ? subForDisplay.getEndDate().format(dateFmt) : "";
        boolean allowed = selectedSubscription != null;

        String displayName = memberService.resolveDisplayName(member);
        String phone = memberService.resolvePhone(member);
        renderMemberFound(member.getMemberCode(), displayName, phone, pkgName, expiry, allowed);
    }

    private Subscription chooseSubscriptionForCheckIn(int memberId) {
        java.util.List<Subscription> valid = subscriptionService.getValidSubscriptionsForCheckIn(memberId);
        if (valid.isEmpty()) {
            return null;
        }
        if (valid.size() == 1) {
            return valid.get(0);
        }
        String[] options = new String[valid.size()];
        for (int i = 0; i < valid.size(); i++) {
            Subscription s = valid.get(i);
            GymPackage p = packageService.getPackageById(s.getPackageId());
            String name = p != null ? p.getPackageName() : "";
            String range = s.getStartDate().format(dateFmt) + " - " + s.getEndDate().format(dateFmt);
            options[i] = name + " (" + range + ")";
        }
        String picked = (String) JOptionPane.showInputDialog(
                this,
                "Hội viên có nhiều gói đang hoạt động. Chọn gói để check-in:",
                "Chọn gói",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (picked == null) {
            return null;
        }
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(picked)) {
                return valid.get(i);
            }
        }
        return valid.get(0);
    }

    private void renderNotFound() {
        memberCard.removeAll();
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(CARD_BG);
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        JLabel ico = new JLabel("Khong tim thay");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel msg = new JLabel("Không tìm thấy hội viên");
        msg.setFont(FONT_MENU_B);
        msg.setForeground(ACCENT_RED);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("Nhập mã HV hoặc SĐT ở thanh tìm kiếm");
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(ico);
        inner.add(Box.createVerticalStrut(10));
        inner.add(msg);
        inner.add(Box.createVerticalStrut(5));
        inner.add(sub);
        center.add(inner);
        memberCard.add(center, BorderLayout.CENTER);
        memberCard.revalidate();
        memberCard.repaint();
    }

    
    
    
    private void doConfirmCheckin(String memberId, String name, String pkg) {
        Member member = memberService.getMemberByCode(memberId);
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hội viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedSubscription == null) {
            JOptionPane.showMessageDialog(this, "Không có gói hợp lệ để check-in.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean ok = checkInService.checkInBySubscription(member.getId(), selectedSubscription.getId());
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Check-in thất bại. Vui lòng kiểm tra trạng thái gói.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        int stt = historyModel.getRowCount() + 1;
        historyModel.addRow(new Object[]{String.valueOf(stt), memberId, name, pkg, time});
        checkinCountLabel.setText("Hôm nay: " + historyModel.getRowCount() + " lượt");

        JOptionPane.showMessageDialog(this,
            "Check-in thành công!\n" + name + " - " + time,
            "Check-in", JOptionPane.INFORMATION_MESSAGE
        );
        selectedSubscription = null;
        renderEmptyState();
        searchField.setText("Nhập mã hội viên hoặc số điện thoại...");
        searchField.setForeground(TEXT_GRAY);
    }
}