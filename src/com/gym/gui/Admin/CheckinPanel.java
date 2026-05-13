package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.entity.Member;
import com.gym.entity.GymPackage;
import com.gym.entity.Subscription;
import com.gym.service.CheckInService;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;

import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * CheckinPanel.java
 * Panel xử lý check-in: form tìm kiếm hội viên, hiển thị thông tin và
 * lịch sử check-in trong ngày.
 */
public class CheckinPanel extends JPanel {

    private DefaultTableModel historyModel;
    private JTextField tfSearch;
    private JLabel lblName;
    private JLabel lblPackage;
    private JLabel lblExpiry;
    private JLabel lblStatus;
    private JButton btnCheckin;
    private final MemberService memberService = new MemberService();
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final PackageService packageService = new PackageService();
    private final CheckInService checkInService = new CheckInService();

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
        tfSearch = makeStyledTextField("VD: GYM26001 hoặc 09xxxxxxxx", 25);
        JButton btnFind   = makeActionButton("🔍 Tìm kiếm", ACCENT_BLUE);

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; formCard.add(lbl1, gbc);
        gbc.gridy=1; gbc.gridwidth=1;               formCard.add(tfSearch, gbc);
        gbc.gridx=1;                                formCard.add(btnFind, gbc);

        // Kết quả tìm kiếm
        JPanel resultCard = new JPanel(new GridLayout(0, 2, 8, 8));
        resultCard.setBackground(new Color(35, 40, 60));
        resultCard.setBorder(new CompoundBorder(
            new RoundedBorder(ACCENT_GREEN, 1, 8),
            new EmptyBorder(12, 16, 12, 16)
        ));
        resultCard.add(styledLabel("Họ tên:"));       lblName = styledValue("-"); resultCard.add(lblName);
        resultCard.add(styledLabel("Gói tập:"));      lblPackage = styledValue("-"); resultCard.add(lblPackage);
        resultCard.add(styledLabel("Hết hạn:"));      lblExpiry = styledValue("-"); resultCard.add(lblExpiry);
        resultCard.add(styledLabel("Trạng thái:"));   lblStatus = styledValue("-"); resultCard.add(lblStatus);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; gbc.insets=new Insets(16, 8, 8, 8);
        formCard.add(resultCard, gbc);

        btnCheckin = makeActionButton("✅  XÁC NHẬN CHECK-IN", ACCENT_GREEN);
        btnCheckin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckin.setPreferredSize(new Dimension(250, 44));
        btnCheckin.setEnabled(false);
        btnFind.addActionListener(e -> doSearch());
        btnCheckin.addActionListener(e -> confirmCheckin());

        gbc.gridy=3; gbc.anchor=GridBagConstraints.CENTER; gbc.insets=new Insets(12, 8, 6, 8);
        formCard.add(btnCheckin, gbc);

        add(formCard, BorderLayout.CENTER);

        // --- Lịch sử check-in hôm nay ---
        String[] cols = {"STT","Mã HV","Họ tên","Gói tập","Giờ check-in"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // Lịch sử sẽ được load động từ DB (mặc định trống)

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

    private void doSearch() {
        String query = tfSearch.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hội viên hoặc SĐT!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Member member = memberService.findByCodeOrPhone(query);
        if (member == null) {
            lblName.setText("Không tìm thấy");
            lblPackage.setText("-");
            lblExpiry.setText("-");
            lblStatus.setText("❌ Không hợp lệ");
            btnCheckin.setEnabled(false);
            return;
        }

        Subscription sub = subscriptionService.getActiveSubscription(member.getId());
        GymPackage pkg = sub != null ? packageService.getPackageById(sub.getPackageId()) : null;
        boolean allowed = subscriptionService.isValidForCheckIn(sub);

        lblName.setText(member.getFullName());
        lblPackage.setText(pkg != null ? pkg.getPackageName() : "-");
        lblExpiry.setText(sub != null && sub.getEndDate() != null ? sub.getEndDate().toString() : "-");
        lblStatus.setText(allowed ? "✅ ACTIVE - Được phép" : "⛔ Không đủ điều kiện");
        btnCheckin.setEnabled(allowed);
    }

    private void confirmCheckin() {
        String query = tfSearch.getText().trim();
        Member member = memberService.findByCodeOrPhone(query);
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hội viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean ok = checkInService.checkInByMemberId(member.getId());
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Check-in thất bại. Vui lòng kiểm tra trạng thái gói.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        addCheckinRecord(String.valueOf(historyModel.getRowCount() + 1), member.getMemberCode(), member.getFullName(), lblPackage.getText(), java.time.LocalTime.now().toString());
        JOptionPane.showMessageDialog(this, "✅ Check-in thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}