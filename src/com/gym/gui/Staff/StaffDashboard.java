package com.gym.gui.Staff;

import javax.swing.*;
import javax.swing.border.*;

import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.gui.LoginJFram;
import com.gym.gui.Admin.MemberManagementPanel;
import com.gym.gui.Admin.SubscriptionPanel;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gym.gui.AppStyle.*;




public class StaffDashboard extends JFrame {

    static final Color STAFF_ACCENT = UIManager.getColor("Label.foreground") != null
            ? UIManager.getColor("Label.foreground")
            : Color.BLACK;
    static final Color STAFF_DIM    = UIManager.getColor("Panel.background") != null
            ? UIManager.getColor("Panel.background")
            : new Color(230, 230, 230);

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

        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                startClock();
                removeWindowListener(this); 
            }
        });
    }

    
    
    
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

        JButton btnCheckin = menuBtn("", "Check-in Khách");
        JButton btnMember  = menuBtn("", "Thêm Hội Viên");
        JButton btnSub     = menuBtn("", "Đăng ký / Gia hạn");
        JButton btnProfile = menuBtn("", "Thông tin cá nhân");
        JButton btnLogout  = menuBtn("", "Đăng xuất", true);
        btnLogout.setForeground(TEXT_WHITE);

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

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("GymPro");
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(TEXT_WHITE);
        JLabel role = new JLabel("Staff Portal");
        role.setFont(FONT_SMALL);
        role.setForeground(TEXT_GRAY);
        text.add(name);
        text.add(role);

        p.add(text);
        return p;
    }

    private JPanel buildStaffCard() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(CARD_BG);
        p.setOpaque(true);
        p.setMaximumSize(new Dimension(210, 60));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel av = new JLabel("NV");
        av.setFont(new Font("Segoe UI", Font.BOLD, 12));
        av.setHorizontalAlignment(SwingConstants.CENTER);
        av.setPreferredSize(new Dimension(36, 36));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nameL = new JLabel(staffDisplayName);
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(TEXT_WHITE);
        JLabel onlineL = new JLabel("Online");
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
        return menuBtn(emoji, label, false);
    }

    private JButton menuBtn(String emoji, String label, boolean isLogout) {
        String text = (emoji == null || emoji.trim().isEmpty()) ? label : (emoji + "  " + label);
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(210, 40));
        btn.setFont(FONT_MENU);
        btn.setForeground(SIDEBAR_TEXT);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(9, 18, 9, 8));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeMenuBtn && !isLogout) { btn.setBackground(SIDEBAR_HOVER); btn.setForeground(TEXT_WHITE); }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeMenuBtn && !isLogout) { btn.setBackground(SIDEBAR_BG); btn.setForeground(SIDEBAR_TEXT); }
            }
        });
        return btn;
    }

    private void setActive(JButton btn) {
        if (activeMenuBtn != null) {
            activeMenuBtn.setBackground(SIDEBAR_BG);
            activeMenuBtn.setForeground(SIDEBAR_TEXT);
            activeMenuBtn.setFont(FONT_MENU);
        }
        btn.setBackground(SIDEBAR_ACTIVE);
        btn.setForeground(TEXT_WHITE);
        btn.setFont(FONT_MENU_B);
        activeMenuBtn = btn;
    }

    private JLabel sectionLabel(String t) {
        JLabel l = new JLabel("  " + t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(SIDEBAR_TEXT);
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

    
    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12)); 
    right.setOpaque(false);

    
    clockLabel = new JLabel(); 
    clockLabel.setFont(FONT_SMALL);
    clockLabel.setForeground(TEXT_GRAY);
    
    updateClockText();

        JLabel badge = new JLabel("STAFF");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(TEXT_WHITE);
        badge.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(2, 8, 2, 8)
        ));

        
        
        
        
        
        

        right.add(clockLabel);
        right.add(badge);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void updateClockText() {
    clockLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss  |  dd/MM/yyyy")));
}
    
    
    
    void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCheckin() {
        showPanel(new StaffCheckinPanel(this));
    }

    
    
    
    private void startClock() {
        new Timer(1000, e -> clockLabel.setText(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss  |  dd/MM/yyyy"))
        )).start();
    }

    
    
    
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