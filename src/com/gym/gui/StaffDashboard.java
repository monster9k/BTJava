package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;

import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.gui.LoginJFram;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gym.gui.AppStyle.*;

/**
 * StaffDashboard.java — Main Frame dành cho role STAFF
 */
public class StaffDashboard extends JFrame {

    static final Color STAFF_ACCENT = new Color(56, 217, 169);
    static final Color STAFF_DIM    = new Color(40, 40, 55);

    private JPanel  contentPanel;
    private JLabel  clockLabel;
    private JButton activeMenuBtn;

    private final String staffUsername;
    private final String staffDisplayName;

    public StaffDashboard(String username, String displayName) {
        this.staffUsername    = username;
        this.staffDisplayName = displayName;
        initUI();
    }

    public StaffDashboard(String username) { this(username, username); }

    // ===================================================================
    //  INIT FRAME
    // ===================================================================
    private void initUI() {
        setTitle("GymPro — Staff Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(960, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildSidebar(),  BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);

        showCheckin();

        // Start clock CHỈ sau khi frame hoàn toàn built & visible
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                startClock();
                removeWindowListener(this); // chỉ cần 1 lần
            }
        });
    }

    // ===================================================================
    //  SIDEBAR
    // ===================================================================
    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setBackground(SIDEBAR_BG);
        sb.setOpaque(true);
        sb.setPreferredSize(new Dimension(210, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBorder(new MatteBorder(0, 0, 0, 1, DIVIDER));

        sb.add(buildLogoBlock());
        sb.add(makeSep());
        sb.add(buildStaffCard());
        sb.add(makeSep());
        sb.add(Box.createVerticalStrut(8));

        JButton btnCheckin = menuBtn("✅", "Check-in Khách");
        JButton btnMember  = menuBtn("➕", "Thêm Hội Viên");
        JButton btnSub     = menuBtn("📦", "Đăng ký / Gia hạn");
        JButton btnProfile = menuBtn("👤", "Thông tin cá nhân");
        JButton btnLogout  = menuBtn("🚪", "Đăng xuất");
        btnLogout.setForeground(ACCENT_RED);

        sb.add(sectionLabel("NGHIỆP VỤ"));
        sb.add(btnCheckin);
        sb.add(Box.createVerticalStrut(2));
        sb.add(btnMember);
        sb.add(Box.createVerticalStrut(2));
        sb.add(btnSub);
        sb.add(Box.createVerticalStrut(6));
        sb.add(sectionLabel("TÀI KHOẢN"));
        sb.add(btnProfile);
        sb.add(Box.createVerticalGlue());
        sb.add(makeSep());
        sb.add(btnLogout);
        sb.add(Box.createVerticalStrut(14));

        setActive(btnCheckin);
        btnCheckin.addActionListener(e -> { setActive(btnCheckin); showCheckin(); });
        btnMember.addActionListener(e  -> { setActive(btnMember);  showPanel(new MemberManagementPanel(this)); });
        btnSub.addActionListener(e     -> { setActive(btnSub);     showPanel(new SubscriptionPanel(this)); });
        btnProfile.addActionListener(e -> { setActive(btnProfile); showPanel(new StaffProfilePanel(this, staffUsername, staffDisplayName)); });
        btnLogout.addActionListener(e  -> confirmLogout());

        return sb;
    }

    private JPanel buildLogoBlock() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 13));
        p.setBackground(SIDEBAR_BG);
        p.setOpaque(true);
        p.setMaximumSize(new Dimension(210, 56));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel icon = new JLabel("💪");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("GymPro");
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));
        name.setForeground(STAFF_ACCENT);
        JLabel role = new JLabel("Staff Portal");
        role.setFont(FONT_SMALL);
        role.setForeground(TEXT_GRAY);
        text.add(name);
        text.add(role);

        p.add(icon);
        p.add(text);
        return p;
    }

    private JPanel buildStaffCard() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(new Color(28, 33, 50));
        p.setOpaque(true);
        p.setMaximumSize(new Dimension(210, 60));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel av = new JLabel("👤") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(56, 217, 169, 35));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(STAFF_ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, getWidth()-2, getHeight()-2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        av.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 17));
        av.setHorizontalAlignment(SwingConstants.CENTER);
        av.setPreferredSize(new Dimension(36, 36));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nameL = new JLabel(staffDisplayName);
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(TEXT_WHITE);
        JLabel onlineL = new JLabel("● Online");
        onlineL.setFont(FONT_SMALL);
        onlineL.setForeground(STAFF_ACCENT);
        info.add(nameL);
        info.add(Box.createVerticalStrut(2));
        info.add(onlineL);

        p.add(av,   BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private JButton menuBtn(String emoji, String label) {
        JButton btn = new JButton(emoji + "  " + label);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(210, 40));
        btn.setFont(FONT_MENU);
        btn.setForeground(TEXT_GRAY);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(9, 18, 9, 8));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeMenuBtn) { btn.setBackground(STAFF_DIM); btn.setForeground(TEXT_WHITE); }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeMenuBtn) { btn.setBackground(SIDEBAR_BG); btn.setForeground(TEXT_GRAY); }
            }
        });
        return btn;
    }

    private void setActive(JButton btn) {
        if (activeMenuBtn != null) {
            activeMenuBtn.setBackground(SIDEBAR_BG);
            activeMenuBtn.setForeground(TEXT_GRAY);
            activeMenuBtn.setFont(FONT_MENU);
        }
        btn.setBackground(STAFF_DIM);
        btn.setForeground(STAFF_ACCENT);
        btn.setFont(FONT_MENU_B);
        activeMenuBtn = btn;
    }

    private JLabel sectionLabel(String t) {
        JLabel l = new JLabel("  " + t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(new Color(75, 85, 115));
        l.setBorder(new EmptyBorder(8, 10, 3, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(210, 24));
        return l;
    }

    private JSeparator makeSep() {
        JSeparator s = new JSeparator();
        s.setForeground(DIVIDER);
        s.setBackground(DIVIDER);
        s.setMaximumSize(new Dimension(210, 1));
        return s;
    }

    // ===================================================================
    //  MAIN AREA
    // ===================================================================
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_DARK);
        main.add(buildTopBar(), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_DARK);
        contentPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        main.add(contentPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
    bar.setBackground(SIDEBAR_BG);
    bar.setPreferredSize(new Dimension(0, 50));
    bar.setBorder(new MatteBorder(0, 0, 1, 0, DIVIDER));

    // Fix: Tăng chiều rộng hoặc để nó tự co giãn hợp lý
    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12)); 
    right.setOpaque(false);

    // Đảm bảo clockLabel có đủ không gian hiển thị
    clockLabel = new JLabel(); 
    clockLabel.setFont(FONT_SMALL);
    clockLabel.setForeground(TEXT_GRAY);
    // Cập nhật text ngay lập tức để lấy PreferredSize
    updateClockText();

        JLabel badge = new JLabel("STAFF");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(STAFF_ACCENT);
        badge.setBorder(new CompoundBorder(
            new RoundedBorder(STAFF_ACCENT, 1, 8),
            new EmptyBorder(2, 8, 2, 8)
        ));

        // // clockLabel: tạo text ngay để có preferred size đúng từ đầu
        // String initTime = LocalDateTime.now()
        //     .format(DateTimeFormatter.ofPattern("HH:mm:ss  |  dd/MM/yyyy"));
        // clockLabel = new JLabel(initTime);
        // clockLabel.setFont(FONT_SMALL);
        // clockLabel.setForeground(TEXT_GRAY);

        right.add(clockLabel);
        right.add(badge);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void updateClockText() {
    clockLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss  |  dd/MM/yyyy")));
}
    // ===================================================================
    //  NAVIGATION
    // ===================================================================
    void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCheckin() {
        showPanel(new StaffCheckinPanel(this));
    }

    // ===================================================================
    //  ĐỒNG HỒ — gọi qua windowOpened, đảm bảo frame đã visible hoàn toàn
    // ===================================================================
    private void startClock() {
        new Timer(1000, e -> clockLabel.setText(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss  |  dd/MM/yyyy"))
        )).start();
    }

    // ===================================================================
    //  ĐĂNG XUẤT
    // ===================================================================
    private void confirmLogout() {
        int r = JOptionPane.showConfirmDialog(
            this, "Bạn muốn đăng xuất?", "Xác nhận",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            java.awt.EventQueue.invokeLater(() -> {
            new LoginJFram().setVisible(true); 
        });
    }
}

}