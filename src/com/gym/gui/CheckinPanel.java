package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * CheckinPanel.java
 * Panel xử lý check-in: form tìm kiếm hội viên, hiển thị thông tin và
 * lịch sử check-in trong ngày.
 */
public class CheckinPanel extends JPanel {

    private DefaultTableModel historyModel;

    public CheckinPanel() {
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Title ---
        JLabel title = new JLabel("✅  Xử lý Check-in");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        // --- Form Card ---
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(CARD_BG);
        formCard.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(24, 32, 24, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 8, 6, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        JLabel lbl1       = styledLabel("Nhập mã hội viên hoặc SĐT:");
        JTextField tfSearch = makeStyledTextField("VD: MEM26001 hoặc 09xxxxxxxx", 25);
        JButton btnFind   = makeActionButton("🔍 Tìm kiếm", ACCENT_BLUE);

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; formCard.add(lbl1, gbc);
        gbc.gridy=1; gbc.gridwidth=1;               formCard.add(tfSearch, gbc);
        gbc.gridx=1;                                formCard.add(btnFind, gbc);

        // Kết quả tìm kiếm (giả lập)
        JPanel resultCard = new JPanel(new GridLayout(0, 2, 8, 8));
        resultCard.setBackground(new Color(35, 40, 60));
        resultCard.setBorder(new CompoundBorder(
            new RoundedBorder(ACCENT_GREEN, 1, 8),
            new EmptyBorder(12, 16, 12, 16)
        ));
        resultCard.add(styledLabel("Họ tên:"));       resultCard.add(styledValue("Trần Thị Mai"));
        resultCard.add(styledLabel("Gói tập:"));      resultCard.add(styledValue("Gym 3 tháng"));
        resultCard.add(styledLabel("Hết hạn:"));      resultCard.add(styledValue("01/08/2026"));
        resultCard.add(styledLabel("Trạng thái:"));   resultCard.add(styledValue("✅ ACTIVE - Được phép vào tập"));

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; gbc.insets=new Insets(16, 8, 8, 8);
        formCard.add(resultCard, gbc);

        JButton btnCheckin = makeActionButton("✅  XÁC NHẬN CHECK-IN", ACCENT_GREEN);
        btnCheckin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckin.setPreferredSize(new Dimension(250, 44));
        // TODO: btnCheckin.addActionListener(e -> confirmCheckin(tfSearch.getText()));

        gbc.gridy=3; gbc.anchor=GridBagConstraints.CENTER; gbc.insets=new Insets(12, 8, 6, 8);
        formCard.add(btnCheckin, gbc);

        add(formCard, BorderLayout.CENTER);

        // --- Lịch sử check-in hôm nay ---
        String[] cols = {"STT","Mã HV","Họ tên","Gói tập","Giờ check-in"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        historyModel.addRow(new Object[]{"1","MEM26005","Đặng Văn Tú", "Gym 3 tháng", "06:15"});
        historyModel.addRow(new Object[]{"2","MEM26009","Bùi Thị Loan","Yoga 1 tháng","07:00"});
        historyModel.addRow(new Object[]{"3","MEM26012","Trương Minh", "Gym VIP",     "07:45"});

        JTable table = new JTable(historyModel);
        styleTableAppearance(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(new CompoundBorder(new RoundedBorder(DIVIDER,1,12), BorderFactory.createEmptyBorder()));
        scroll.setPreferredSize(new Dimension(0, 160));
        add(scroll, BorderLayout.SOUTH);
    }

    /** Thêm bản ghi check-in vào bảng lịch sử. */
    public void addCheckinRecord(String stt, String memberId, String name, String pkg, String time) {
        historyModel.addRow(new Object[]{stt, memberId, name, pkg, time});
    }

    /** Xóa lịch sử để load lại từ đầu ngày. */
    public void clearHistory() { historyModel.setRowCount(0); }
}