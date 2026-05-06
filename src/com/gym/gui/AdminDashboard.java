package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * AdminDashboard.java
 * Giao diện chính dành cho Admin (Chủ phòng gym)
 * Hệ thống Quản lý Phòng Gym
 *
 * Cách dùng: Sau khi đăng nhập, nếu role = ADMIN thì mở frame này.
 * Ví dụ: new AdminDashboard("Nguyễn Văn A").setVisible(true);
 */
public class AdminDashboard extends JFrame {

    // ===== MÀU SẮC CHỦ ĐẠO =====
    private static final Color BG_DARK       = new Color(15, 17, 26);     // Nền chính tối
    private static final Color SIDEBAR_BG    = new Color(21, 24, 38);     // Nền sidebar
    private static final Color CARD_BG       = new Color(28, 32, 50);     // Nền card
    private static final Color ACCENT_ORANGE = new Color(255, 107, 53);   // Cam nổi bật
    private static final Color ACCENT_BLUE   = new Color(64, 156, 255);   // Xanh dương
    private static final Color ACCENT_GREEN  = new Color(56, 217, 169);   // Xanh lá
    private static final Color ACCENT_RED    = new Color(255, 82, 82);    // Đỏ cảnh báo
    private static final Color TEXT_WHITE    = new Color(240, 242, 255);  // Chữ trắng
    private static final Color TEXT_GRAY     = new Color(130, 140, 170);  // Chữ xám
    private static final Color DIVIDER       = new Color(40, 45, 65);     // Đường kẻ

    // ===== FONT =====
    private static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_MENU    = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_MENU_B  = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_CARD_N  = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_CARD_L  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_NORMAL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);

    // ===== COMPONENTS =====
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private String adminName;
    private JButton activeMenuBtn = null;
    
    // ===== TABLE MODELS (để backend update dữ liệu) =====
    private DefaultTableModel expiringMembersModel;
    private DefaultTableModel todayCheckinModel;

    // ===== CONSTRUCTOR =====
    public AdminDashboard(String adminName) {
        this.adminName = adminName;
        initUI();
    }

    private void initUI() {
        setTitle("GymPro - Quản Trị Hệ Thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        // --- SIDEBAR ---
        sidebarPanel = buildSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // --- MAIN AREA ---
        JPanel mainArea = new JPanel(new BorderLayout(0, 0));
        mainArea.setBackground(BG_DARK);

        // Top bar
        mainArea.add(buildTopBar(), BorderLayout.NORTH);

        // Content panel (đổi khi click menu)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_DARK);
        contentPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Mặc định hiển thị Dashboard
        showDashboard();

        mainArea.add(contentPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);
    }

    // ==================== SIDEBAR ====================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setOpaque(true);
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, DIVIDER));

        // Logo / Tên app
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

        // Đường kẻ
        sidebar.add(makeDivider());
        sidebar.add(Box.createVerticalStrut(8));

        // Label section

        // Menu items Admin
        JButton btnDashboard = makeMenuButton("  Dashboard", true);
        JButton btnStaff     = makeMenuButton("  Quản lý Nhân viên", false);
        JButton btnPackages  = makeMenuButton("  Quản lý Gói tập", false);
        JButton btnReport    = makeMenuButton("  Báo cáo Doanh thu", false);
        JButton btnMembers   = makeMenuButton("  Quản lý Hội viên", false);
        JButton btnSubs      = makeMenuButton("  Đăng ký / Gia hạn", false);
        JButton btnCheckin   = makeMenuButton("  Check-in", false);

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

        // Khoảng trống đẩy xuống dưới
        sidebar.add(Box.createVerticalGlue());

        // --- Đổi mật khẩu & Đăng xuất ---
        sidebar.add(makeDivider());
        JButton btnChangePwd = makeMenuButton("  Đổi mật khẩu", false);
        JButton btnLogout    = makeMenuButton("  Đăng xuất", false);
        btnLogout.setForeground(ACCENT_RED);
        sidebar.add(btnChangePwd);
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(16));

        // ==== SỰ KIỆN MENU ====
        activeMenuBtn = btnDashboard;
        setActiveMenu(btnDashboard);

        btnDashboard.addActionListener(e -> { setActiveMenu(btnDashboard); showDashboard(); });
        btnStaff.addActionListener(e ->     { setActiveMenu(btnStaff); showStaffPanel(); });
        btnPackages.addActionListener(e ->  { setActiveMenu(btnPackages); showPackagesPanel(); });
        btnReport.addActionListener(e ->    { setActiveMenu(btnReport); showReportPanel(); });
        btnMembers.addActionListener(e ->   { setActiveMenu(btnMembers); showMembersPanel(); });
        btnSubs.addActionListener(e ->      { setActiveMenu(btnSubs); showSubscriptionsPanel(); });
        btnCheckin.addActionListener(e ->   { setActiveMenu(btnCheckin); showCheckinPanel(); });
        btnChangePwd.addActionListener(e -> showChangePwdDialog());
        btnLogout.addActionListener(e ->    confirmLogout());

        return sidebar;
    }

    private void setActiveMenu(JButton btn) {
        if (activeMenuBtn != null) {
            activeMenuBtn.setBackground(SIDEBAR_BG);
            activeMenuBtn.setForeground(TEXT_GRAY);
        }
        btn.setBackground(new Color(40,40,55));
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
        btn.setOpaque(true); // Quan trọng để tránh lỗi layer
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

    //Tạo 1 label
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

    // ==================== TOP BAR ====================
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(SIDEBAR_BG);
        topBar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, DIVIDER),
            new EmptyBorder(12, 24, 12, 24)
        ));
        topBar.setPreferredSize(new Dimension(0, 56));

        // Tiêu đề trang
        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(FONT_HEADER);
        pageTitle.setForeground(TEXT_WHITE);
        topBar.add(pageTitle, BorderLayout.WEST);

        // Thông tin Admin (góc phải)
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

    // ==================== DASHBOARD ====================
    private void showDashboard() {
        contentPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setBackground(BG_DARK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- Row 1: Thống kê nhanh (4 card) ---
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(BG_DARK);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        statsRow.add(makeStatCard("Hội viên Active", "248", "+12 tháng này", ACCENT_BLUE, "🏃"));
        statsRow.add(makeStatCard("Doanh thu tháng", "32.5M", "+8% so tháng trước", ACCENT_GREEN, "💰"));
        statsRow.add(makeStatCard("Gói sắp hết hạn", "17", "trong 5 ngày tới", ACCENT_ORANGE, "⚠️"));
        statsRow.add(makeStatCard("Nhân viên", "5", "đang hoạt động", ACCENT_RED, "👥"));

        panel.add(statsRow);
        panel.add(Box.createVerticalStrut(20));

        // --- Row 2: Bảng hội viên sắp hết hạn + Check-in hôm nay ---
        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 0));
        row2.setBackground(BG_DARK);

        row2.add(makeExpiringMembersTable());
        row2.add(makeTodayCheckinPanel());
        panel.add(row2);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

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

    //Giả lập dữ liệu
    private JPanel makeExpiringMembersTable() {
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

        // Tạo DefaultTableModel (có thể update dữ liệu từ backend)
        String[] cols = {"Mã HV", "Họ tên", "SĐT", "Hết hạn"};
        expiringMembersModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        //1. clearExpiringMembers(); //Gọi trước khi load dữ liệu mới
        //2. Lấy dữ liệu mới từ backend vd:
        //List<Member> expiringMembers = memberDAO.getExpiringMembers();

        //3. đổ vào model
        //for(Member m : expiringMembers){
        // addExpiringMember(m.getMemberCode(), m.getFullName(), m.getPhone(), m.getExpiryDate());
        //}

        // Dữ liệu giả lập mặc định
        addExpiringMember("MEM26001", "Trần Thị Mai", "0901234567", "05/05/2026");
        addExpiringMember("MEM26002", "Lê Văn Bình", "0912345678", "06/05/2026");
        addExpiringMember("MEM26015", "Phạm Thu Hà", "0923456789", "07/05/2026");
        addExpiringMember("MEM26020", "Nguyễn Minh", "0934567890", "08/05/2026");
        addExpiringMember("MEM26031", "Võ Thị Lan", "0945678901", "09/05/2026");

        JTable table = new JTable(expiringMembersModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Thêm một hàng vào bảng hội viên sắp hết hạn
     * Gọi từ backend khi cần update dữ liệu
     */
    public void addExpiringMember(String memberId, String name, String phone, String expiryDate) {
        expiringMembersModel.addRow(new Object[]{memberId, name, phone, expiryDate});
    }
    
    /**
     * Xóa tất cả dữ liệu trong bảng hội viên sắp hết hạn
     * Gọi trước khi load dữ liệu mới từ backend
     */
    public void clearExpiringMembers() {
        expiringMembersModel.setRowCount(0);
    }

    private JPanel makeTodayCheckinPanel() {
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

        // Tạo DefaultTableModel (có thể update dữ liệu từ backend)
        String[] cols = {"Mã HV", "Họ tên", "Gói tập", "Giờ vào"};
        todayCheckinModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        // Dữ liệu giả lập mặc định
        addTodayCheckin("MEM26005", "Đặng Văn Tú", "Gym 3 tháng", "06:15");
        addTodayCheckin("MEM26009", "Bùi Thị Loan", "Yoga 1 tháng", "07:00");
        addTodayCheckin("MEM26012", "Trương Minh", "Gym VIP", "07:45");
        addTodayCheckin("MEM26019", "Hồ Thu Nga", "Zumba", "08:30");
        addTodayCheckin("MEM26025", "Lý Văn Đức", "Gym 3 tháng", "09:00");

        JTable table = new JTable(todayCheckinModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Thêm một hàng vào bảng check-in hôm nay
     * Gọi từ backend khi cần update dữ liệu
     */
    public void addTodayCheckin(String memberId, String name, String packageName, String checkInTime) {
        todayCheckinModel.addRow(new Object[]{memberId, name, packageName, checkInTime});
    }
    
    /**
     * Xóa tất cả dữ liệu trong bảng check-in hôm nay
     * Gọi trước khi load dữ liệu mới từ backend
     */
    public void clearTodayCheckin() {
        todayCheckinModel.setRowCount(0);
    }

    // ==================== QUẢN LÝ NHÂN VIÊN ====================
    private void showStaffPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG_DARK);

        // Thanh công cụ
        //panel.add(makeToolBar("Quản lý Nhân viên", " Thêm nhân viên", ACCENT_BLUE), BorderLayout.NORTH);

        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Quản lý Nhân viên");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        
        JButton btnAdd = makeActionButton("Thêm nhân viên", ACCENT_BLUE);
        btnAdd.addActionListener(e -> showAddStaffDialog());
        toolBar.add(title, BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        panel.add(toolBar, BorderLayout.NORTH);

        // Bảng nhân viên
        String[] cols = {"ID", "Tên đăng nhập", "Họ tên", "Vai trò", "Trạng thái", "Thao tác"};
        Object[][] data = {
            {"U001", "admin",   "Nguyễn Văn A", "ADMIN", "✅ Active", "Sửa | Khóa"},
            {"U002", "staff01", "Trần Thị B",   "STAFF", "✅ Active", "Sửa | Khóa"},
            {"U003", "staff02", "Lê Văn C",     "STAFF", "✅ Active", "Sửa | Khóa"},
            {"U004", "staff03", "Phạm Thị D",   "STAFF", "🔴 Locked", "Sửa | Mở"},
            {"U005", "staff04", "Võ Minh E",    "STAFF", "✅ Active", "Sửa | Khóa"},
        };

        JTable table = makeStyledTable(cols, data);
        JScrollPane scroll = makeScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAddStaffDialog() {
        JDialog dialog = new JDialog(this, "➕ Thêm Nhân Viên Mới", true);
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Các trường nhập liệu
        JTextField tfId = makeStyledTextField("Nhập mã nhân viên (VD: U006)", 20);
        JTextField tfUser = makeStyledTextField("Tên đăng nhập", 20);
        JTextField tfName = makeStyledTextField("Họ và tên", 20);
        JPasswordField pfPass = new JPasswordField();
        stylePasswordField(pfPass);
        
        String[] roles = {"STAFF", "ADMIN"};
        JComboBox<String> cbRole = makeStyledCombo(roles);

        // Add components to dialog
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++; dialog.add(styledLabel("Mã nhân viên:"), gbc);
        gbc.gridy = row++; dialog.add(tfId, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Tên đăng nhập:"), gbc);
        gbc.gridy = row++; dialog.add(tfUser, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Họ và tên:"), gbc);
        gbc.gridy = row++; dialog.add(tfName, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Mật khẩu:"), gbc);
        gbc.gridy = row++; dialog.add(pfPass, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Vai trò:"), gbc);
        gbc.gridy = row++; dialog.add(cbRole, gbc);

        // Nút Lưu
        JButton btnSave = makeActionButton("💾 Lưu nhân viên", ACCENT_BLUE);
        btnSave.addActionListener(e -> {
            // Logic xử lý lưu dữ liệu ở đây (Gọi DAO)
            String name = tfName.getText();
            if(name.isEmpty() || name.equals("Họ và tên")) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập họ tên!");
                return;
            }
            JOptionPane.showMessageDialog(dialog, "Thêm nhân viên " + name + " thành công!");
            dialog.dispose();
        });

        gbc.gridy = row++;
        gbc.insets = new Insets(20, 15, 10, 15);
        dialog.add(btnSave, gbc);

        dialog.setVisible(true);
    }

    // ==================== QUẢN LÝ GÓI TẬP ====================
    private void showPackagesPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG_DARK);

       // panel.add(makeToolBar("Quản lý Gói tập", " Thêm gói tập", ACCENT_GREEN), BorderLayout.NORTH);
    JPanel toolBar = new JPanel(new BorderLayout());
    toolBar.setBackground(BG_DARK);
    JLabel title = new JLabel("Quản lý Gói tập");
    title.setFont(FONT_HEADER);
    title.setForeground(TEXT_WHITE);
    
    JButton btnAdd = makeActionButton("➕ Thêm gói tập", ACCENT_GREEN);
    btnAdd.addActionListener(e -> showAddPackageDialog()); // Gắn sự kiện ở đây

    toolBar.add(title, BorderLayout.WEST);
    toolBar.add(btnAdd, BorderLayout.EAST);
    panel.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Tên gói", "Thời hạn (ngày)", "Giá (VNĐ)", "Mô tả", "Trạng thái", "Thao tác"};
        Object[][] data = {
            {"P01", "Gym 1 tháng",  "30",  "350,000",   "Tập gym cơ bản",    "✅ Active", "Sửa"},
            {"P02", "Gym 3 tháng",  "90",  "900,000",   "Tiết kiệm 14%",     "✅ Active", "Sửa"},
            {"P03", "Gym 6 tháng",  "180", "1,600,000", "Tiết kiệm 24%",     "✅ Active", "Sửa"},
            {"P04", "Gym VIP",      "30",  "800,000",   "PT + phòng riêng",  "✅ Active", "Sửa"},
            {"P05", "Yoga 1 tháng", "30",  "400,000",   "Lớp Yoga buổi sáng","✅ Active", "Sửa"},
            {"P06", "Zumba",        "30",  "350,000",   "Lớp Zumba Dance",   "✅ Active", "Sửa"},
            {"P07", "Combo VIP",    "365", "5,000,000", "Gym + Yoga trọn năm","🔴 Ẩn",    "Sửa"},
        };

        JTable table = makeStyledTable(cols, data);
        JScrollPane scroll = makeScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAddPackageDialog() {
        JDialog dialog = new JDialog(this, " Thêm Gói Tập Mới", true);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(500, 650));
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Các trường nhập liệu dựa trên thuộc tính yêu cầu
        JTextField tfId = makeStyledTextField("Nhập mã gói (VD: P08)", 20);
        JTextField tfName = makeStyledTextField("Tên gói (1 tháng, VIP...)", 20);
        JTextField tfDuration = makeStyledTextField("Số ngày sử dụng (VD: 30)", 20);
        JTextField tfPrice = makeStyledTextField("Giá niêm yết (VD: 500000)", 20);
        
        //JScrollPane spDesc = makeStyledTextArea("Nhập mô tả chi tiết về gói tập...", 4, 20);
        JPanel descPanel = makeStyledTextArea("Nhập mô tả chi tiết về gói tập...", 4, 20);


        // Thêm các thành phần vào Dialog
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row++; dialog.add(styledLabel("Mã gói tập (ID):"), gbc);
        gbc.gridy = row++; dialog.add(tfId, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Tên gói tập:"), gbc);
        gbc.gridy = row++; dialog.add(tfName, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Thời hạn (ngày):"), gbc);
        gbc.gridy = row++; dialog.add(tfDuration, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Giá hiện tại (VNĐ):"), gbc);
        gbc.gridy = row++; dialog.add(tfPrice, gbc);

        // 👉 ĐỂ TEXTAREA CUỐI
        gbc.gridy = row++; dialog.add(styledLabel("Mô tả thêm:"), gbc);

        gbc.gridy = row++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        //dialog.add(spDesc, gbc);
        dialog.add(descPanel, gbc);

        // reset
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        

        // Nút Lưu
        JButton btnSave = makeActionButton("💾 Lưu gói tập", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            // Kiểm tra dữ liệu sơ bộ
            if(tfId.getText().isEmpty() || tfName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ ID và Tên gói!");
                return;
            }
            
            // Logic: Gọi xuống Database/DAO để lưu
            // Ví dụ: packageDAO.insert(new Package(tfId.getText(), ...));
            
            JOptionPane.showMessageDialog(dialog, "Thêm gói tập mới thành công!");
            dialog.dispose();
        });

        gbc.gridy = row++;
        gbc.insets = new Insets(20, 15, 10, 15);
        dialog.add(btnSave, gbc);

        dialog.setVisible(true);
    }

    // ==================== BÁO CÁO DOANH THU ====================
    private void showReportPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG_DARK);

        // Header + bộ lọc tháng
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("📈  Báo cáo Doanh thu");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JLabel lblThang = new JLabel("Tháng:");
        lblThang.setFont(FONT_MENU);
        lblThang.setForeground(TEXT_GRAY);

        String[] months = {"Tháng 5/2026","Tháng 4/2026","Tháng 3/2026","Tháng 2/2026","Tháng 1/2026"};
        JComboBox<String> cbMonth = makeStyledCombo(months);

        JButton btnLoc = makeActionButton("Lọc", ACCENT_BLUE);

        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(lblThang);
        header.add(cbMonth);
        header.add(btnLoc);
        panel.add(header, BorderLayout.NORTH);

        // Stats summary
        JPanel summaryRow = new JPanel(new GridLayout(1, 3, 16, 0));
        summaryRow.setBackground(BG_DARK);
        summaryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        summaryRow.add(makeStatCard("Tổng doanh thu T5", "32,500,000đ", "↑ +8% so tháng trước", ACCENT_GREEN, "💰"));
        summaryRow.add(makeStatCard("Số gói đã bán", "68 gói", "trong tháng 5/2026", ACCENT_BLUE, "📦"));
        summaryRow.add(makeStatCard("Chưa thu tiền", "4 gói", "cần theo dõi", ACCENT_RED, "⚠️"));

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(BG_DARK);
        center.add(summaryRow, BorderLayout.NORTH);

        // Bảng chi tiết
        String[] cols = {"Ngày mua", "Mã HV", "Hội viên", "Gói tập", "Giá (VNĐ)", "TT Thanh toán"};
        Object[][] data = {
            {"01/05/2026", "MEM26001", "Trần Thị Mai",  "Gym 3 tháng",  "900,000",   "✅ Đã TT"},
            {"02/05/2026", "MEM26008", "Lê Văn Bình",   "Yoga 1 tháng", "400,000",   "✅ Đã TT"},
            {"02/05/2026", "MEM26011", "Nguyễn Minh",   "Gym VIP",      "800,000",   "⏳ Chưa TT"},
            {"03/05/2026", "MEM26019", "Phạm Thu Hà",   "Gym 6 tháng",  "1,600,000", "✅ Đã TT"},
            {"04/05/2026", "MEM26022", "Võ Thị Lan",    "Zumba",        "350,000",   "✅ Đã TT"},
            {"05/05/2026", "MEM26030", "Hồ Văn Tùng",   "Gym 1 tháng",  "350,000",   "✅ Đã TT"},
        };

        JTable table = makeStyledTable(cols, data);
        JScrollPane scroll = makeScrollPane(table);
        center.add(scroll, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==================== QUẢN LÝ HỘI VIÊN ====================
    private void showMembersPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG_DARK);

        // Header + tìm kiếm
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("🏃  Quản lý Hội viên");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBar.setBackground(BG_DARK);
        JTextField searchField = makeStyledTextField("Tìm theo tên hoặc SĐT...", 20);
        JButton btnSearch = makeActionButton(" Tìm kiếm", ACCENT_BLUE);
        JButton btnAdd    = makeActionButton(" Thêm hội viên", ACCENT_GREEN);
        btnAdd.addActionListener(e -> showAddMemberDialog());
        searchBar.add(searchField);
        searchBar.add(btnSearch);
        searchBar.add(btnAdd);

        header.add(title, BorderLayout.WEST);
        header.add(searchBar, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        String[] cols = {"Mã HV", "Họ tên", "SĐT", "Giới tính", "Ngày sinh", "Ngày đăng ký", "Trạng thái", "Thao tác"};
        Object[][] data = {
            {"MEM26001","Trần Thị Mai",  "0901234567","Nữ","15/03/1998","01/01/2026","✅ Active","Sửa | Xóa"},
            {"MEM26002","Lê Văn Bình",   "0912345678","Nam","22/07/1995","05/01/2026","✅ Active","Sửa | Xóa"},
            {"MEM26003","Phạm Thu Hà",   "0923456789","Nữ","08/11/2000","10/01/2026","✅ Active","Sửa | Xóa"},
            {"MEM26004","Nguyễn Văn Minh","0934567890","Nam","30/04/1992","12/01/2026","✅ Active","Sửa | Xóa"},
            {"MEM26005","Võ Thị Lan",    "0945678901","Nữ","14/06/1999","15/01/2026","🔴 Đã khóa","Sửa | Mở"},
        };

        JTable table = makeStyledTable(cols, data);
        JScrollPane scroll = makeScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==================== DIALOG THÊM HỘI VIÊN ====================
    private void showAddMemberDialog() {
        JDialog dialog = new JDialog(this, "➕ Thêm Hội Viên Mới", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Các trường nhập liệu
        JTextField tfMemberCode = makeStyledTextField("MEM2026001", 20);
        JTextField tfFullName = makeStyledTextField("Họ và tên khách hàng", 20);
        JTextField tfPhone = makeStyledTextField("Số điện thoại (09...)", 20);
        
        String[] genders = {"Nam", "Nữ", "Khác"};
        JComboBox<String> cbGender = makeStyledCombo(genders);
        
     // --- Thay đổi ở đây: Dùng JSpinner ---
        JSpinner spBirthday = makeDatePicker();
        JSpinner spCreatedAt = makeDatePicker();

        // Layout
        int row = 0;
        
        gbc.gridy = row++; dialog.add(styledLabel("Mã hội viên:"), gbc);
        gbc.gridy = row++; dialog.add(tfMemberCode, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Họ và tên:"), gbc);
        gbc.gridy = row++; dialog.add(tfFullName, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Số điện thoại:"), gbc);
        gbc.gridy = row++; dialog.add(tfPhone, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Giới tính:"), gbc);
        gbc.gridy = row++; dialog.add(cbGender, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Ngày sinh (dd/mm/yyyy):"), gbc);
        gbc.gridy = row++; dialog.add(spBirthday, gbc);
        
        gbc.gridy = row++; dialog.add(styledLabel("Ngày đăng ký:"), gbc);
        gbc.gridy = row++; dialog.add(spCreatedAt, gbc);

        // Nút Lưu
        JButton btnSave = makeActionButton(" Lưu hội viên", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            // Kiểm tra dữ liệu cơ bản
            if(tfFullName.getText().isEmpty() || tfFullName.getText().contains("Họ và tên")) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên hội viên!");
                return;
            }
            
            java.util.Date selectedDate = (java.util.Date) spBirthday.getValue();
            // Bạn có thể format lại nếu cần lưu xuống DB:
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // String dateStr = sdf.format(selectedDate);
            
            // TODO: Gọi hàm lưu xuống Database ở đây
            JOptionPane.showMessageDialog(dialog, "Thêm hội viên " + tfFullName.getText() + " thành công!" + "Đã chọn ngày sinh: " + selectedDate.toString());
            dialog.dispose();
        });

        gbc.gridy = row++;
        gbc.insets = new Insets(25, 15, 10, 15);
        dialog.add(btnSave, gbc);

        dialog.setVisible(true);
    }

    private JSpinner makeDatePicker() {
        // Tạo model cho ngày tháng
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        
        // Cấu hình định dạng hiển thị là dd/MM/yyyy
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        
        // Style đồng bộ với theme tối
        spinner.setBackground(SIDEBAR_BG);
        spinner.setForeground(Color.WHITE);
        // Bạn có thể thêm border nếu muốn đồng bộ hơn:
        // spinner.setBorder(new RoundedBorder(ACCENT_BLUE, 1, 10)); 
        
        return spinner;
    }

    // ==================== ĐĂNG KÝ GÓI TẬP ====================
    private void showSubscriptionsPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG_DARK);

        //panel.add(makeToolBar("Đăng ký / Gia hạn Gói tập", "➕ Đăng ký gói mới", ACCENT_ORANGE), BorderLayout.NORTH);

     // Toolbar với nút đã được gắn action listener
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Đăng ký / Gia hạn Gói tập");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAddSub = makeActionButton(" Đăng ký gói mới", ACCENT_ORANGE);
        btnAddSub.addActionListener(e -> showAddSubscriptionDialog());
        toolBar.add(title, BorderLayout.WEST);
        toolBar.add(btnAddSub, BorderLayout.EAST);
        panel.add(toolBar, BorderLayout.NORTH);
        
        String[] cols = {"ID", "Mã HV", "Hội viên", "Gói tập", "Ngày bắt đầu", "Ngày hết hạn", "Giá mua (VNĐ)", "TT Gói", "TT Thanh toán", "Ngày tạo"};
        Object[][] data = {
            {"S001","MEM26001","Trần Thị Mai",  "Gym 3 tháng", "01/02/2026","02/05/2026","900,000",   "🔴 Hết hạn", "✅ Đã TT",   "01/02/2026 08:30"},
            {"S002","MEM26001","Trần Thị Mai",  "Gym 3 tháng", "03/05/2026","01/08/2026","900,000",   "✅ Active",  "✅ Đã TT",   "03/05/2026 09:00"},
            {"S003","MEM26002","Lê Văn Bình",   "Yoga 1 tháng","05/04/2026","04/05/2026","400,000",   "✅ Active",  "✅ Đã TT",   "05/04/2026 10:15"},
            {"S004","MEM26003","Phạm Thu Hà",   "Gym VIP",     "01/05/2026","31/05/2026","800,000",   "✅ Active",  "⏳ Chưa TT", "01/05/2026 14:00"},
            {"S005","MEM26004","Nguyễn V Minh", "Gym 6 tháng", "01/01/2026","30/06/2026","1,600,000", "✅ Active",  "✅ Đã TT",   "01/01/2026 11:45"},
        };

        JTable table = makeStyledTable(cols, data);
        JScrollPane scroll = makeScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==================== DIALOG ĐĂNG KÝ GÓI MỚI ====================
    private void showAddSubscriptionDialog() {
        JDialog dialog = new JDialog(this, "➕ Đăng Ký Gói Tập Mới", true);
        dialog.setSize(620, 780);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ── Trường nhập liệu khớp với bảng subscriptions ──
        // member_id
        JTextField tfMemberId = makeStyledTextField("Nhập mã hội viên (VD: MEM26001)", 20);

        // package_id → dùng ComboBox chọn gói
        String[] packages = {
            "P01 - Gym 1 tháng (30 ngày - 350,000đ)",
            "P02 - Gym 3 tháng (90 ngày - 900,000đ)",
            "P03 - Gym 6 tháng (180 ngày - 1,600,000đ)",
            "P04 - Gym VIP     (30 ngày - 800,000đ)",
            "P05 - Yoga 1 tháng(30 ngày - 400,000đ)",
            "P06 - Zumba       (30 ngày - 350,000đ)",
        };
        JComboBox<String> cbPackage = makeStyledCombo(packages);

        // start_date
        JSpinner spStartDate = makeDatePicker();

        // price_at_purchase (giá thực tế, có thể khác giá niêm yết nếu sale)
        JTextField tfPrice = makeStyledTextField("Giá thực tế (VD: 850000)", 20);

        // payment_status
        String[] paymentStatuses = {"1 - Đã thanh toán", "0 - Chưa thanh toán"};
        JComboBox<String> cbPayment = makeStyledCombo(paymentStatuses);

        // ── Ghi chú nhỏ ──
        JLabel noteLabel = new JLabel("* Ngày hết hạn sẽ tự tính dựa theo thời hạn của gói đã chọn.");
        noteLabel.setFont(FONT_SMALL);
        noteLabel.setForeground(ACCENT_ORANGE);

        // ── Layout ──
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row++; dialog.add(styledLabel("Mã hội viên (member_id):"), gbc);
        gbc.gridy = row++;                 dialog.add(tfMemberId, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Gói tập (package_id):"), gbc);
        gbc.gridy = row++; dialog.add(cbPackage, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Ngày bắt đầu (start_date):"), gbc);
        gbc.gridy = row++; dialog.add(spStartDate, gbc);

        gbc.gridy = row++; dialog.add(noteLabel, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Giá thực tế lúc mua - price_at_purchase (VNĐ):"), gbc);
        gbc.gridy = row++; dialog.add(tfPrice, gbc);

        gbc.gridy = row++; dialog.add(styledLabel("Trạng thái thanh toán (payment_status):"), gbc);
        gbc.gridy = row++; dialog.add(cbPayment, gbc);

        // ── Thông tin sẽ tự động điền ──
        JPanel autoFillNote = new JPanel(new GridLayout(0, 1, 0, 4));
        autoFillNote.setBackground(new Color(35, 40, 60));
        autoFillNote.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(10, 14, 10, 14)
        ));
        JLabel autoTitle = new JLabel("Hệ thống tự động điền:");
        autoTitle.setFont(FONT_SMALL);
        autoTitle.setForeground(ACCENT_GREEN);
        JLabel autoEnd   = new JLabel("  • end_date = start_date + duration_days của gói");
        JLabel autoStat  = new JLabel("  • status = 1 (Active)");
        JLabel autoCreate= new JLabel("  • created_at = thời điểm hiện tại");
        for (JLabel l : new JLabel[]{autoTitle, autoEnd, autoStat, autoCreate}) {
            l.setFont(FONT_SMALL);
            l.setForeground(l == autoTitle ? ACCENT_GREEN : TEXT_GRAY);
            autoFillNote.add(l);
        }

        gbc.gridy = row++;
        gbc.insets = new Insets(10, 15, 8, 15);
        dialog.add(autoFillNote, gbc);

        // ── Nút Lưu ──
        JButton btnSave = makeActionButton("💾 Lưu đăng ký", ACCENT_ORANGE);
        btnSave.addActionListener(e -> {
            String memberId = tfMemberId.getText().trim();
            String priceStr = tfPrice.getText().trim();

            if (memberId.isEmpty() || memberId.contains("Nhập mã")) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mã hội viên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (priceStr.isEmpty() || priceStr.contains("Giá thực tế")) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập giá thực tế!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // TODO: 
                // 1. Kiểm tra mã hội viên có tồn tại không (Gọi DAO) (Đã làm ở trên)
                // 2. Lấy thông tin gói tập đã chọn để tính ngày hết hạn 
                    //Lấy spStartDate và duration_days của gói tập đã chọn → tính spEndDate = spStartDate + duration_days
                // 3. Tạo đối tượng Subscription mới và lưu xuống DB (Gọi DAO)
                // 4. Hiển thị thông báo thành công hoặc lỗi nếu có
            // 

            JOptionPane.showMessageDialog(dialog,
                "Đăng ký gói tập thành công!\nHội viên: " + memberId,
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        gbc.gridy = row++;
        gbc.insets = new Insets(12, 15, 15, 15);
        dialog.add(btnSave, gbc);

        dialog.setVisible(true);
    }

    // ==================== CHECK-IN ====================
    private void showCheckinPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG_DARK);

        JLabel title = new JLabel("  Xử lý Check-in");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        panel.add(title, BorderLayout.NORTH);

        // Form Check-in
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(CARD_BG);
        formCard.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(24, 32, 24, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl1 = styledLabel("Nhập mã hội viên hoặc SĐT:");
        JTextField tfSearch = makeStyledTextField("VD: MEM26001 hoặc 09xxxxxxxx", 25);
        JButton btnFind = makeActionButton("🔍 Tìm kiếm", ACCENT_BLUE);

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; formCard.add(lbl1, gbc);
        gbc.gridy=1; gbc.gridwidth=1; formCard.add(tfSearch, gbc);
        gbc.gridx=1; formCard.add(btnFind, gbc);

        // Kết quả tìm kiếm giả lập
        JPanel resultCard = new JPanel(new GridLayout(0, 2, 8, 8));
        resultCard.setBackground(new Color(35, 40, 60));
        resultCard.setBorder(new CompoundBorder(
            new RoundedBorder(ACCENT_GREEN, 1, 8),
            new EmptyBorder(12, 16, 12, 16)
        ));

        resultCard.add(styledLabel("Họ tên:")); resultCard.add(styledValue("Trần Thị Mai"));
        resultCard.add(styledLabel("Gói tập:")); resultCard.add(styledValue("Gym 3 tháng"));
        resultCard.add(styledLabel("Hết hạn:"));  resultCard.add(styledValue("01/08/2026"));
        resultCard.add(styledLabel("Trạng thái:")); resultCard.add(styledValue("✅ ACTIVE - Được phép vào tập"));

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; gbc.insets=new Insets(16,8,8,8);
        formCard.add(resultCard, gbc);

        JButton btnCheckin = makeActionButton("✅  XÁC NHẬN CHECK-IN", ACCENT_GREEN);
        btnCheckin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckin.setPreferredSize(new Dimension(250, 44));
        gbc.gridy=3; gbc.anchor=GridBagConstraints.CENTER; gbc.insets=new Insets(12,8,6,8);
        formCard.add(btnCheckin, gbc);

        panel.add(formCard, BorderLayout.CENTER);

        // Lịch sử check-in hôm nay
        String[] cols = {"STT", "Mã HV", "Họ tên", "Gói tập", "Giờ check-in"};
        Object[][] data = {
            {"1","MEM26005","Đặng Văn Tú",  "Gym 3 tháng",  "06:15"},
            {"2","MEM26009","Bùi Thị Loan", "Yoga 1 tháng", "07:00"},
            {"3","MEM26012","Trương Minh",  "Gym VIP",      "07:45"},
        };
        JTable table = makeStyledTable(cols, data);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER,1,12),
            BorderFactory.createEmptyBorder()
        ));
        scroll.setPreferredSize(new Dimension(0, 160));
        panel.add(scroll, BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==================== ĐỔI MẬT KHẨU ====================
    private void showChangePwdDialog() {
        JDialog dialog = new JDialog(this, " Đổi mật khẩu", true);
        dialog.setSize(540, 600);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField oldPwd = new JPasswordField(20);
        JPasswordField newPwd = new JPasswordField(20);
        JPasswordField confirmPwd = new JPasswordField(20);
        stylePasswordField(oldPwd);
        stylePasswordField(newPwd);
        stylePasswordField(confirmPwd);

        gbc.gridx=0; gbc.gridy=0; dialog.add(styledLabel("Mật khẩu hiện tại:"), gbc);
        gbc.gridy=1; dialog.add(oldPwd, gbc);
        gbc.gridy=2; dialog.add(styledLabel("Mật khẩu mới:"), gbc);
        gbc.gridy=3; dialog.add(newPwd, gbc);
        gbc.gridy=4; dialog.add(styledLabel("Xác nhận mật khẩu mới:"), gbc);
        gbc.gridy=5; dialog.add(confirmPwd, gbc);

        JButton btnSave = makeActionButton("💾 Lưu thay đổi", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        gbc.gridy=6; gbc.insets=new Insets(14,12,8,12);
        dialog.add(btnSave, gbc);
        dialog.setVisible(true);
    }

    // ==================== ĐĂNG XUẤT ====================
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

    // ==================== HELPER UI ====================
    private JPanel makeToolBar(String titleText, String btnText, Color btnColor) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_DARK);
        bar.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel title = new JLabel(titleText);
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JButton btn = makeActionButton(btnText, btnColor);
        bar.add(title, BorderLayout.WEST);
        bar.add(btn, BorderLayout.EAST);
        return bar;
    }

    /**
     * Áp dụng style cho bảng (các bảng không động - không cần update từ backend)
     */
    private JTable makeStyledTable(String[] cols, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTableAppearance(table);
        return table;
    }
    
    /**
     * Áp dụng kiểu dáng (styling) cho JTable
     * Tách riêng để có thể dùng cho cả bảng cũ (makeStyledTable) và bảng mới (động)
     */
    private void styleTableAppearance(JTable table) {
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_WHITE);
        table.setFont(FONT_NORMAL);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(255, 107, 53, 60));
        table.setSelectionForeground(TEXT_WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(35, 40, 60));
        header.setForeground(TEXT_GRAY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(new MatteBorder(0, 0, 1, 0, DIVIDER));
        header.setReorderingAllowed(false);

        // Zebra rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? new Color(255,107,53,50) : (row%2==0 ? CARD_BG : new Color(32,37,56)));
                setForeground(TEXT_WHITE);
                setFont(FONT_NORMAL);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return this;
            }
        });
    }

    private JScrollPane makeScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            BorderFactory.createEmptyBorder()
        ));
        return scroll;
    }

    private JButton makeActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_MENU_B);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    private JTextField makeStyledTextField(String placeholder, int cols) {
        JTextField tf = new JTextField(cols);
        tf.setBackground(new Color(35, 40, 60));
        tf.setForeground(TEXT_GRAY);
        tf.setFont(FONT_NORMAL);
        tf.setCaretColor(TEXT_WHITE);
        tf.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(6, 12, 6, 12)
        ));
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_WHITE); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(TEXT_GRAY); }
            }
        });
        return tf;
    }

    private <T> JComboBox<T> makeStyledCombo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setBackground(new Color(35, 40, 60)); // Màu nền khi đóng
        cb.setForeground(TEXT_WHITE);
        cb.setFont(FONT_NORMAL);
        cb.setFocusable(false); // Bỏ khung viền focus mặc định xấu xí
        
     // 1. Tùy chỉnh danh sách sổ xuống (Popup)
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                // Màu nền và màu chữ trong danh sách
                setBackground(isSelected ? ACCENT_BLUE : new Color(35, 40, 60));
                setForeground(TEXT_WHITE);
                setBorder(new EmptyBorder(6, 12, 6, 12)); // Thêm padding cho item
                return this;
            }
        });
        
        //Ép màu nền cho phần hiển thị chính (fix lỗi màu xám/trắng của Windows)
        cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = super.createArrowButton();
                btn.setBackground(new Color(35, 40, 60));
                btn.setBorder(BorderFactory.createEmptyBorder());
                return btn;
            }
        });

        // 2. Tùy chỉnh viền và background cho phần hiển thị
        cb.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        // Lưu ý: Nếu vẫn thấy nền bị trắng, có thể do LookAndFeel hệ thống, 
        // bạn có thể thử setOpaque(true) hoặc tùy chỉnh UI delegate thêm.
        cb.setOpaque(true);
        
        cb.setEditable(true);
        ((JTextField) cb.getEditor().getEditorComponent()).setBackground(new Color(35,40,60));
        ((JTextField) cb.getEditor().getEditorComponent()).setForeground(TEXT_WHITE);
        return cb;
    }

    private void stylePasswordField(JPasswordField pf) {
        pf.setBackground(new Color(35, 40, 60));
        pf.setForeground(TEXT_WHITE);
        pf.setCaretColor(TEXT_WHITE);
        pf.setFont(FONT_NORMAL);
        pf.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 6),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_MENU);
        l.setForeground(TEXT_GRAY);
        return l;
    }

    private JLabel styledValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_MENU_B);
        l.setForeground(TEXT_WHITE);
        return l;
    }

     private JPanel makeStyledTextArea(String placeholder, int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);

        // ===== STYLE CHÍNH =====
        ta.setBackground(new Color(35, 40, 60));
        ta.setForeground(TEXT_GRAY); // placeholder màu xám
        ta.setCaretColor(TEXT_WHITE);
        ta.setFont(FONT_NORMAL);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new EmptyBorder(10, 12, 10, 12));

        // ===== PLACEHOLDER =====
        ta.setText(placeholder);
        ta.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (ta.getText().equals(placeholder)) {
                    ta.setText("");
                    ta.setForeground(TEXT_WHITE);
                }
            }

            public void focusLost(FocusEvent e) {
                if (ta.getText().isEmpty()) {
                    ta.setText(placeholder);
                    ta.setForeground(TEXT_GRAY);
                }
            }
        });

        // ===== SCROLL =====
        JScrollPane scroll = new JScrollPane(ta);
        scroll.setBorder(new EmptyBorder(0,0,0,0));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(35,40,60));
        wrapper.setBorder(new RoundedBorder(new Color(60,65,85), 1, 12));
        wrapper.add(scroll);

        scroll.getViewport().setBackground(new Color(35, 40, 60));
        scroll.setPreferredSize(new Dimension(0, 120));

        // ===== CUSTOM SCROLLBAR (đẹp hơn) =====
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(70, 80, 110);
                this.trackColor = new Color(35, 40, 60);
            }
        });

        scroll.setPreferredSize(new Dimension(0, 100)); // chiều cao đẹp

        return wrapper;
    }

    // ==================== ROUNDED BORDER ====================
    static class RoundedBorder extends AbstractBorder {
        private Color color;
        private int thickness;
        private int radius;
        RoundedBorder(Color c, int t, int r) { color=c; thickness=t; radius=r; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x+thickness/2.0, y+thickness/2.0,
                    w-thickness, h-thickness, radius, radius));
            g2.dispose();
        }
        public Insets getBorderInsets(Component c) { return new Insets(radius/2,radius/2,radius/2,radius/2); }
    }

    // ==================== MAIN (để test) ====================
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) { /* dùng default */ }

        SwingUtilities.invokeLater(() -> {
            // Giả lập: đăng nhập xong với tên admin
            new AdminDashboard("Nguyễn Văn Admin").setVisible(true);
        });
    }
}