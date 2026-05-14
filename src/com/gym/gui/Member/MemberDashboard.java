package com.gym.gui.Member;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import com.gym.entity.Member;
import com.gym.entity.User;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.gui.LoginJFram;
import com.gym.service.MemberService;

import static com.gym.gui.AppStyle.*;

public class MemberDashboard extends JFrame {

    static final Color MEMBER_ACCENT = TEXT_WHITE;
    static final Color MEMBER_DIM = SIDEBAR_ACTIVE;

    private JPanel contentPanel;
    private JButton activeMenuBtn;

    private final User user;
    private final Member member;

    public MemberDashboard(User user) {
        this.user = user;
        MemberService memberService = new MemberService();
        this.member = memberService.getMemberByUserId(user.getId());
        if (this.member == null) {
            JOptionPane.showMessageDialog(this,
                    "Tai khoan chua duoc lien ket hoi vien. Vui long lien he quay.",
                    "Thieu thong tin", JOptionPane.WARNING_MESSAGE);
            dispose();
            EventQueue.invokeLater(() -> new LoginJFram().setVisible(true));
            return;
        }
        initUI();
    }

    private void initUI() {
        setTitle("GymPro - Member Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(960, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);

        showPackages();
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setBackground(SIDEBAR_BG);
        sb.setOpaque(true);
        sb.setPreferredSize(new Dimension(220, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBorder(new MatteBorder(0, 0, 0, 1, DIVIDER));

        sb.add(buildLogoBlock());
        sb.add(makeSep());
        sb.add(buildMemberCard());
        sb.add(makeSep());
        sb.add(Box.createVerticalStrut(8));

        JButton btnPackages = menuBtn("Goi tap");
        JButton btnSubs = menuBtn("Goi cua toi");
        JButton btnCheckin = menuBtn("Check-in hom nay");
        JButton btnProfile = menuBtn("Thong tin ca nhan");
        JButton btnLogout = menuBtn("Dang xuat", true);
        btnLogout.setForeground(TEXT_WHITE);

        sb.add(sectionLabel("CHUC NANG"));
        sb.add(btnPackages);
        sb.add(Box.createVerticalStrut(2));
        sb.add(btnSubs);
        sb.add(Box.createVerticalStrut(2));
        sb.add(btnCheckin);
        sb.add(Box.createVerticalStrut(6));
        sb.add(sectionLabel("TAI KHOAN"));
        sb.add(btnProfile);
        sb.add(Box.createVerticalGlue());
        sb.add(makeSep());
        sb.add(btnLogout);
        sb.add(Box.createVerticalStrut(14));

        setActive(btnPackages);
        btnPackages.addActionListener(e -> { setActive(btnPackages); showPackages(); });
        btnSubs.addActionListener(e -> { setActive(btnSubs); showSubscriptions(); });
        btnCheckin.addActionListener(e -> { setActive(btnCheckin); showCheckin(); });
        btnProfile.addActionListener(e -> { setActive(btnProfile); showProfile(); });
        btnLogout.addActionListener(e -> confirmLogout());

        return sb;
    }

    private JPanel buildLogoBlock() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 13));
        p.setBackground(SIDEBAR_BG);
        p.setOpaque(true);
        p.setMaximumSize(new Dimension(220, 56));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel icon = new JLabel("GymPro");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        icon.setForeground(TEXT_WHITE);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel role = new JLabel("Member Portal");
        role.setFont(FONT_SMALL);
        role.setForeground(TEXT_GRAY);
        text.add(role);

        p.add(icon);
        p.add(text);
        return p;
    }

    private JPanel buildMemberCard() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(CARD_BG);
        p.setOpaque(true);
        p.setMaximumSize(new Dimension(220, 64));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel av = new JLabel("MEM");
        av.setFont(new Font("Segoe UI", Font.BOLD, 11));
        av.setHorizontalAlignment(SwingConstants.CENTER);
        av.setForeground(TEXT_WHITE);
        av.setPreferredSize(new Dimension(36, 36));
        av.setBorder(new RoundedBorder(DIVIDER, 1, 10));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        String displayName = member.getFullName() != null ? member.getFullName() : user.getUsername();
        JLabel nameL = new JLabel(displayName);
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(TEXT_WHITE);
        JLabel codeL = new JLabel(member.getMemberCode());
        codeL.setFont(FONT_SMALL);
        codeL.setForeground(TEXT_GRAY);
        info.add(nameL);
        info.add(Box.createVerticalStrut(2));
        info.add(codeL);

        p.add(av, BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private JButton menuBtn(String label) {
        return menuBtn(label, false);
    }

    private JButton menuBtn(String label, boolean isLogout) {
        JButton btn = new JButton("  " + label);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
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
                if (btn != activeMenuBtn && !isLogout) {
                    btn.setBackground(SIDEBAR_HOVER);
                    btn.setForeground(TEXT_WHITE);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeMenuBtn && !isLogout) {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setForeground(SIDEBAR_TEXT);
                }
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
        l.setMaximumSize(new Dimension(220, 24));
        return l;
    }

    private JSeparator makeSep() {
        JSeparator s = new JSeparator();
        s.setForeground(DIVIDER);
        s.setBackground(DIVIDER);
        s.setMaximumSize(new Dimension(220, 1));
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

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        right.setOpaque(false);

        JLabel badge = new JLabel("MEMBER");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(TEXT_WHITE);
        badge.setBorder(new CompoundBorder(
                new RoundedBorder(DIVIDER, 1, 8),
                new EmptyBorder(2, 8, 2, 8)
        ));

        JLabel name = new JLabel(member.getFullName() != null ? member.getFullName() : user.getUsername());
        name.setFont(FONT_MENU_B);
        name.setForeground(TEXT_WHITE);

        right.add(badge);
        right.add(name);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPackages() {
        showPanel(new MemberPackagePanel(member.getId()));
    }

    private void showSubscriptions() {
        showPanel(new MemberSubscriptionPanel(member.getId()));
    }

    private void showCheckin() {
        showPanel(new MemberCheckinPanel(member.getId(), member.getMemberCode()));
    }

    private void showProfile() {
        showPanel(new MemberProfilePanel(user, member));
    }

    private void confirmLogout() {
        int r = JOptionPane.showConfirmDialog(
                this, "Ban muon dang xuat?", "Xac nhan",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            EventQueue.invokeLater(() -> new LoginJFram().setVisible(true));
        }
    }
}
