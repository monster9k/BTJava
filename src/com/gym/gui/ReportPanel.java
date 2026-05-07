package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * ReportPanel.java
 * Panel báo cáo doanh thu. Hiển thị bộ lọc tháng, 3 stat card tóm tắt
 * và bảng chi tiết giao dịch.
 */
public class ReportPanel extends JPanel {

    private DefaultTableModel tableModel;

    public ReportPanel() {
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Header + bộ lọc tháng ---
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
        // TODO: btnLoc.addActionListener(e -> filterByMonth(cbMonth.getSelectedItem()));

        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(lblThang);
        header.add(cbMonth);
        header.add(btnLoc);
        add(header, BorderLayout.NORTH);

        // --- Summary row (3 stat cards) ---
        JPanel summaryRow = new JPanel(new GridLayout(1, 3, 16, 0));
        summaryRow.setBackground(BG_DARK);
        summaryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        summaryRow.add(makeStatCard("Tổng doanh thu T5", "32,500,000đ", "↑ +8% so tháng trước", ACCENT_GREEN, "💰"));
        summaryRow.add(makeStatCard("Số gói đã bán",     "68 gói",      "trong tháng 5/2026",   ACCENT_BLUE,  "📦"));
        summaryRow.add(makeStatCard("Chưa thu tiền",     "4 gói",       "cần theo dõi",         ACCENT_RED,   "⚠️"));

        // --- Detail table ---
        String[] cols = {"Ngày mua", "Mã HV", "Hội viên", "Gói tập", "Giá (VNĐ)", "TT Thanh toán"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableModel.addRow(new Object[]{"01/05/2026","MEM26001","Trần Thị Mai", "Gym 3 tháng",  "900,000",   "✅ Đã TT"});
        tableModel.addRow(new Object[]{"02/05/2026","MEM26008","Lê Văn Bình",  "Yoga 1 tháng", "400,000",   "✅ Đã TT"});
        tableModel.addRow(new Object[]{"02/05/2026","MEM26011","Nguyễn Minh",  "Gym VIP",      "800,000",   "⏳ Chưa TT"});
        tableModel.addRow(new Object[]{"03/05/2026","MEM26019","Phạm Thu Hà",  "Gym 6 tháng",  "1,600,000", "✅ Đã TT"});
        tableModel.addRow(new Object[]{"04/05/2026","MEM26022","Võ Thị Lan",   "Zumba",        "350,000",   "✅ Đã TT"});
        tableModel.addRow(new Object[]{"05/05/2026","MEM26030","Hồ Văn Tùng",  "Gym 1 tháng",  "350,000",   "✅ Đã TT"});

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(BG_DARK);
        center.add(summaryRow, BorderLayout.NORTH);
        center.add(makeScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel makeStatCard(String label, String value, String sub, Color accent, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new RoundedBorder(accent.darker(), 1, 12),
            new EmptyBorder(16, 18, 16, 18)
        ));
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40));
        iconPanel.setPreferredSize(new Dimension(44, 44));
        iconPanel.setBorder(new RoundedBorder(new Color(60, 65, 85), 1, 10));
        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconPanel.add(emojiLbl);
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(FONT_CARD_N);
        valueLbl.setForeground(TEXT_WHITE);
        top.add(iconPanel, BorderLayout.WEST);
        top.add(valueLbl, BorderLayout.EAST);
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(FONT_MENU_B);
        labelLbl.setForeground(TEXT_WHITE);
        labelLbl.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(accent);
        card.add(top,      BorderLayout.NORTH);
        card.add(labelLbl, BorderLayout.CENTER);
        card.add(subLbl,   BorderLayout.SOUTH);
        return card;
    }

    /** Xóa bảng chi tiết để load dữ liệu tháng mới. */
    public void clearData() { tableModel.setRowCount(0); }

    /** Thêm một giao dịch vào bảng. */
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }
}