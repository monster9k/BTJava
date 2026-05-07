package com.gym.gui;
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
 
import static com.gym.gui.AppStyle.*;
 
/**
 * AdminDashboard.java  ← Main Frame (khung điều hướng duy nhất)
 *
 * Chỉ chứa:
 *   - Sidebar + menu navigation
 *   - Top bar (tiêu đề trang + thông tin admin)
 *   - contentPanel — nơi swap các Panel con
 *
 * Mọi logic UI chi tiết đã được tách ra:
 *   AppStyle                  → màu sắc, font, helper UI
 *   DashboardPanel            → tổng quan
 *   StaffManagementPanel      → quản lý nhân viên
 *   PackageManagementPanel    → quản lý gói tập
 *   ReportPanel               → báo cáo doanh thu
 *   MemberManagementPanel     → quản lý hội viên
 *   SubscriptionPanel         → đăng ký / gia hạn
 *   CheckinPanel              → check-in
 *   AddStaffDialog            → dialog thêm nhân viên
 *   AddMemberDialog           → dialog thêm hội viên
 *   AddPackageDialog          → dialog thêm gói tập
 *   AddSubscriptionDialog     → dialog đăng ký gói
 *   ChangePwdDialog           → dialog đổi mật khẩu
 */
public class AdminDashboard extends JFrame {
 
    private JPanel  contentPanel;
    private final String adminName;
    private JButton activeMenuBtn = null;
 
    // ===== CONSTRUCTOR =====
    public AdminDashboard(String adminName) {
        this.adminName = adminName;
        initUI();
    }
 
    // ===== KHỞI TẠO FRAME =====
    private void initUI() {
        setTitle("GymPro - Quản Trị Hệ Thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));
 
        add(buildSidebar(), BorderLayout.WEST);
 
        JPanel mainArea = new JPanel(new BorderLayout(0, 0));
        mainArea.setBackground(BG_DARK);
        mainArea.add(buildTopBar(), BorderLayout.NORTH);
 
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_DARK);
        contentPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        mainArea.add(contentPanel, BorderLayout.CENTER);
 
        add(mainArea, BorderLayout.CENTER);
 
        showDashboard(); // Mặc định
    }
 
    // ===== SIDEBAR =====
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setOpaque(true);
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, DIVIDER));
 
        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setMaximumSize(new Dimension(220, 70));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel logoIcon = new JLabel("💪");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoIcon.setForeground(ACCENT_RED);
        JLabel logoText = new JLabel("GymPro");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoText.setForeground(ACCENT_ORANGE);
        logoPanel.add(logoIcon);
        logoPanel.add(logoText);
        sidebar.add(logoPanel);
        sidebar.add(makeDivider());
        sidebar.add(Box.createVerticalStrut(8));
 
        // Menu buttons
        JButton btnDashboard = makeMenuButton("  Dashboard",              true);
        JButton btnStaff     = makeMenuButton("  Quản lý Nhân viên",     false);
        JButton btnPackages  = makeMenuButton("  Quản lý Gói tập",       false);
        JButton btnReport    = makeMenuButton("  Báo cáo Doanh thu",     false);
        JButton btnMembers   = makeMenuButton("  Quản lý Hội viên",      false);
        JButton btnSubs      = makeMenuButton("  Đăng ký / Gia hạn",     false);
        JButton btnCheckin   = makeMenuButton("  Check-in",               false);
 
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(makeSectionLabel("QUẢN LÝ"));
        sidebar.add(btnStaff);
        sidebar.add(btnPackages);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(makeSectionLabel("NGHIỆP VỤ"));
        sidebar.add(btnMembers);
        sidebar.add(btnSubs);
        sidebar.add(btnCheckin);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(makeSectionLabel("THỐNG KÊ"));
        sidebar.add(btnReport);
 
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeDivider());
 
        JButton btnChangePwd = makeMenuButton("  Đổi mật khẩu", false);
        JButton btnLogout    = makeMenuButton("  Đăng xuất",     false);
        btnLogout.setForeground(ACCENT_RED);
        sidebar.add(btnChangePwd);
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(16));
 
        // Events
        activeMenuBtn = btnDashboard;
        setActiveMenu(btnDashboard);
 
        btnDashboard.addActionListener(e -> { setActiveMenu(btnDashboard); showDashboard(); });
        btnStaff.addActionListener(e ->     { setActiveMenu(btnStaff);     showPanel(new StaffManagementPanel(this)); });
        btnPackages.addActionListener(e ->  { setActiveMenu(btnPackages);  showPanel(new PackageManagementPanel(this)); });
        btnReport.addActionListener(e ->    { setActiveMenu(btnReport);    showPanel(new ReportPanel()); });
        btnMembers.addActionListener(e ->   { setActiveMenu(btnMembers);   showPanel(new MemberManagementPanel(this)); });
        btnSubs.addActionListener(e ->      { setActiveMenu(btnSubs);      showPanel(new SubscriptionPanel(this)); });
        btnCheckin.addActionListener(e ->   { setActiveMenu(btnCheckin);   showPanel(new CheckinPanel()); });
        btnChangePwd.addActionListener(e -> new ChangePwdDialog(this).setVisible(true));
        btnLogout.addActionListener(e ->    confirmLogout());
 
        return sidebar;
    }
 
    /** Đặt trạng thái active cho nút menu được chọn. */
    private void setActiveMenu(JButton btn) {
        if (activeMenuBtn != null) {
            activeMenuBtn.setBackground(SIDEBAR_BG);
            activeMenuBtn.setForeground(TEXT_GRAY);
        }
        btn.setBackground(new Color(40, 40, 55));
        btn.setForeground(ACCENT_ORANGE);
        activeMenuBtn = btn;
    }
 
    private JButton makeMenuButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFont(FONT_MENU);
        btn.setForeground(active ? ACCENT_ORANGE : TEXT_GRAY);
        btn.setBackground(active ? new Color(40, 40, 55) : SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 8));
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeMenuBtn) btn.setForeground(TEXT_WHITE);
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeMenuBtn) btn.setForeground(TEXT_GRAY);
            }
        });
        return btn;
    }
 
    private JLabel makeSectionLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(80, 90, 120));
        lbl.setBorder(new EmptyBorder(10, 10, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(220, 28));
        return lbl;
    }
 
    private JSeparator makeDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(DIVIDER);
        sep.setBackground(DIVIDER);
        sep.setMaximumSize(new Dimension(220, 1));
        return sep;
    }
 
    // ===== TOP BAR =====
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(SIDEBAR_BG);
        topBar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, DIVIDER),
            new EmptyBorder(12, 24, 12, 24)
        ));
        topBar.setPreferredSize(new Dimension(0, 56));
 
        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(FONT_HEADER);
        pageTitle.setForeground(TEXT_WHITE);
        topBar.add(pageTitle, BorderLayout.WEST);
 
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfo.setBackground(SIDEBAR_BG);
 
        JLabel badge = new JLabel("ADMIN");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(ACCENT_ORANGE);
        badge.setBorder(new CompoundBorder(
            new RoundedBorder(ACCENT_ORANGE, 1, 8),
            new EmptyBorder(2, 8, 2, 8)
        ));
 
        JLabel adminLabel = new JLabel(adminName);
        adminLabel.setFont(FONT_MENU_B);
        adminLabel.setForeground(TEXT_WHITE);
 
        JLabel avatar = new JLabel("👤");
        avatar.setForeground(TEXT_WHITE);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
 
        userInfo.add(badge);
        userInfo.add(adminLabel);
        userInfo.add(avatar);
        topBar.add(userInfo, BorderLayout.EAST);
 
        return topBar;
    }
 
    // ===== NAVIGATION HELPERS =====
 
    /** Hiển thị DashboardPanel (mặc định khi mở). */
    private void showDashboard() {
        showPanel(new DashboardPanel());
    }
 
    /**
     * Swap nội dung contentPanel sang panel mới.
     * Gọi từ bất kỳ menu button nào.
     */
    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
 
    // ===== ĐĂNG XUẤT =====
    private void confirmLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Bạn có chắc muốn đăng xuất?", "Xác nhận",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // TODO: new LoginFrame().setVisible(true);
            JOptionPane.showMessageDialog(null, "Đã đăng xuất. Hẹn gặp lại!");
            System.exit(0);
        }
    }
}