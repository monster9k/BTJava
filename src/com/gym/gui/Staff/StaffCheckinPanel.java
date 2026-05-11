package com.gym.gui.Staff;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.gym.gui.AppStyle.*;

/**
 * StaffCheckinPanel.java
 * Màn hình check-in khách — chức năng trọng tâm của Staff.
 *
 * Layout:
 *  ┌─────────────────────────────────────────────────────────────┐
 *  │  [Thanh tìm kiếm rộng — nhập mã HV hoặc SĐT + nút Tìm]    │
 *  ├──────────────────────────────┬──────────────────────────────┤
 *  │  Card thông tin hội viên     │  Bảng lịch sử check-in       │
 *  │  + badge trạng thái          │  hôm nay                     │
 *  │  + nút XÁC NHẬN lớn         │                              │
 *  └──────────────────────────────┴──────────────────────────────┘
 */
public class StaffCheckinPanel extends JPanel {

    private final StaffDashboard owner;
    private JTextField           searchField;
    private JPanel               memberCard;
    private JLabel               checkinCountLabel;
    private DefaultTableModel    historyModel;
    private int                  checkinCount = 5; // Giả lập đã có 5 lượt

    public StaffCheckinPanel(StaffDashboard owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 14));
        build();
    }

    // ===================================================================
    private void build() {
        add(buildTitleRow(),  BorderLayout.NORTH);
        add(buildBody(),      BorderLayout.CENTER);
    }

    // ===================================================================
    //  TITLE ROW
    // ===================================================================
    private JPanel buildTitleRow() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel lbl = new JLabel("✅  Check-in Khách");
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

    // ===================================================================
    //  BODY = search + 2 cột
    // ===================================================================
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

    // ===================================================================
    //  SEARCH BAR
    // ===================================================================
    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setBackground(CARD_BG);
        bar.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 10),
            new EmptyBorder(10, 14, 10, 10)
        ));

        JLabel ico = new JLabel("🔎");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ico.setBorder(new EmptyBorder(0, 0, 0, 8));

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
        searchField.addActionListener(e -> doSearch()); // Enter key

        JButton btnSearch = makeActionButton("🔍  Tìm kiếm", StaffDashboard.STAFF_ACCENT);
        btnSearch.setForeground(new Color(10, 40, 30));
        btnSearch.addActionListener(e -> doSearch());

        bar.add(ico,         BorderLayout.WEST);
        bar.add(searchField, BorderLayout.CENTER);
        bar.add(btnSearch,   BorderLayout.EAST);
        return bar;
    }

    // ===================================================================
    //  MEMBER CARD (cột trái)
    // ===================================================================
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

    /** Trạng thái chờ — chưa tìm kiếm */
    private void renderEmptyState() {
        memberCard.removeAll();
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(CARD_BG);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel ico  = new JLabel("🔍");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel h    = new JLabel("Tìm hội viên để check-in");
        h.setFont(FONT_MENU_B);
        h.setForeground(TEXT_GRAY);
        h.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub  = new JLabel("Nhập mã HV hoặc SĐT ở thanh tìm kiếm");
        sub.setFont(FONT_SMALL);
        sub.setForeground(new Color(90, 100, 130));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(ico);
        inner.add(Box.createVerticalStrut(10));
        inner.add(h);
        inner.add(Box.createVerticalStrut(5));
        inner.add(sub);
        center.add(inner);
        memberCard.add(center, BorderLayout.CENTER);
        memberCard.revalidate();
        memberCard.repaint();
    }

    /** Render thông tin hội viên sau khi tìm thấy */
    private void renderMemberFound(String memberId, String name, String phone,
                                    String pkg, String expiry, boolean allowed) {
        memberCard.removeAll();
        Color ac = allowed ? StaffDashboard.STAFF_ACCENT : ACCENT_RED;
        String statusTxt = allowed ? "✅  ĐƯỢC PHÉP VÀO TẬP" : "❌  GÓI HẾT HẠN / BỊ KHÓA";

        // --- Badge trạng thái ---
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

        // --- Avatar + Tên ---
        JLabel av = new JLabel("👤") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(ac.getRed(), ac.getGreen(), ac.getBlue(), 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(ac);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(1, 1, getWidth()-2, getHeight()-2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        av.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
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

        // --- Chi tiết gói ---
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

        // --- Nút xác nhận ---
        JButton btnConfirm = new JButton(allowed ? "✅   XÁC NHẬN CHECK-IN" : "⚠️   LIÊN HỆ QUẢN LÝ");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setForeground(allowed ? new Color(8, 35, 25) : Color.WHITE);
        btnConfirm.setBackground(allowed ? StaffDashboard.STAFF_ACCENT : ACCENT_ORANGE);
        btnConfirm.setBorderPainted(false);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirm.setEnabled(allowed);
        btnConfirm.setPreferredSize(new Dimension(0, 44));
        btnConfirm.addActionListener(e -> doConfirmCheckin(memberId, name, pkg));

        // --- Ghép layout ---
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

    // ===================================================================
    //  HISTORY PANEL (cột phải)
    // ===================================================================
    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel title = new JLabel("📋  Lịch sử check-in hôm nay");
        title.setFont(FONT_MENU_B);
        title.setForeground(TEXT_WHITE);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã HV", "Họ tên", "Gói tập", "Giờ vào"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // Dữ liệu mẫu
        historyModel.addRow(new Object[]{"1", "MEM26005", "Đặng Văn Tú",  "Gym 3 tháng",  "06:15"});
        historyModel.addRow(new Object[]{"2", "MEM26009", "Bùi Thị Loan", "Yoga 1 tháng", "07:00"});
        historyModel.addRow(new Object[]{"3", "MEM26012", "Trương Minh",  "Gym VIP",      "07:45"});
        historyModel.addRow(new Object[]{"4", "MEM26019", "Hồ Thu Nga",   "Zumba",        "08:30"});
        historyModel.addRow(new Object[]{"5", "MEM26025", "Lý Văn Đức",  "Gym 3 tháng",  "09:00"});

        JTable table = new JTable(historyModel);
        styleTableAppearance(table);

        // Tô màu teal cho hàng mới nhất (hàng cuối)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                boolean isLast = (row == t.getRowCount() - 1);
                if (sel) {
                    setBackground(new Color(56, 217, 169, 50));
                } else if (isLast) {
                    setBackground(new Color(56, 217, 169, 18));
                } else {
                    setBackground(row % 2 == 0 ? CARD_BG : new Color(32, 37, 56));
                }
                setForeground(TEXT_WHITE);
                setFont(FONT_NORMAL);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ===================================================================
    //  LOGIC TÌM KIẾM (giả lập — thay bằng DAO thật)
    // ===================================================================
    private void doSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty() || query.startsWith("Nhập mã")) return;

        // --- Giả lập lookup ---
        if (query.equalsIgnoreCase("MEM26001") || query.equals("0901234567")) {
            renderMemberFound("MEM26001", "Trần Thị Mai", "0901234567",
                "Gym 3 tháng", "01/08/2026", true);
        } else if (query.equalsIgnoreCase("MEM26005") || query.equals("0945678901")) {
            renderMemberFound("MEM26005", "Võ Thị Lan", "0945678901",
                "Gym 1 tháng", "30/04/2026", false); // đã hết hạn
        } else {
            // Không tìm thấy
            memberCard.removeAll();
            JPanel center = new JPanel(new GridBagLayout());
            center.setBackground(CARD_BG);
            JPanel inner = new JPanel();
            inner.setOpaque(false);
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            JLabel ico = new JLabel("❓");
            ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
            ico.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel msg = new JLabel("Không tìm thấy hội viên");
            msg.setFont(FONT_MENU_B);
            msg.setForeground(ACCENT_RED);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel sub = new JLabel("Kiểm tra lại mã HV hoặc số điện thoại");
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
    }

    // ===================================================================
    //  LOGIC XÁC NHẬN CHECK-IN
    // ===================================================================
    private void doConfirmCheckin(String memberId, String name, String pkg) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        checkinCount++;
        int stt = historyModel.getRowCount() + 1;
        historyModel.addRow(new Object[]{String.valueOf(stt), memberId, name, pkg, time});
        checkinCountLabel.setText("Hôm nay: " + checkinCount + " lượt");

        // TODO: checkinDAO.insert(memberId, LocalDate.now(), LocalTime.now())
        JOptionPane.showMessageDialog(this,
            "✅  Check-in thành công!\n" + name + " — " + time,
            "Check-in", JOptionPane.INFORMATION_MESSAGE
        );
        renderEmptyState();
        searchField.setText("Nhập mã hội viên hoặc số điện thoại...");
        searchField.setForeground(TEXT_GRAY);
    }
}