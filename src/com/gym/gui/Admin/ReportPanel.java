package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.entity.Member;
import com.gym.entity.GymPackage;
import com.gym.entity.Subscription;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.ReportService;
import com.gym.util.AppConstants;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.gym.gui.AppStyle.*;

/**
 * ReportPanel.java
 * Panel báo cáo doanh thu. Hiển thị bộ lọc tháng, 3 stat card tóm tắt
 * và bảng chi tiết giao dịch.
 */
public class ReportPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final ReportService reportService = new ReportService();
    private final MemberService memberService = new MemberService();
    private final PackageService packageService = new PackageService();
    private JLabel revenueValue;
    private JLabel packagesValue;
    private JLabel unpaidValue;
    private JComboBox<String> cbMonth;
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    public ReportPanel() {
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Header + bộ lọc tháng ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("Báo cáo Doanh thu");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);

        JLabel lblThang = new JLabel("Tháng:");
        lblThang.setFont(FONT_MENU);
        lblThang.setForeground(TEXT_GRAY);

        cbMonth = makeStyledCombo(buildMonthOptions());

        JButton btnLoc = makeActionButton("Lọc", ACCENT_BLUE);
        btnLoc.addActionListener(e -> {
            YearMonth ym = parseMonth((String) cbMonth.getSelectedItem());
            if (ym != null) {
                loadMonth(ym.getYear(), ym.getMonthValue());
            }
        });

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
        revenueValue = new JLabel("0đ");
        packagesValue = new JLabel("0 gói");
        unpaidValue = new JLabel("0 gói");
        summaryRow.add(makeStatCard("Tổng doanh thu", revenueValue, "theo tháng đã chọn", ACCENT_GREEN, ""));
        summaryRow.add(makeStatCard("Số gói đã bán", packagesValue, "trong tháng", ACCENT_BLUE, ""));
        summaryRow.add(makeStatCard("Chưa thu tiền", unpaidValue, "cần theo dõi", ACCENT_RED, ""));

        // --- Detail table ---
        String[] cols = {"Ngày mua", "Mã HV", "Hội viên", "Gói tập", "Giá (VNĐ)", "TT Thanh toán"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        YearMonth current = YearMonth.now();
        loadMonth(current.getYear(), current.getMonthValue());

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(BG_DARK);
        center.add(summaryRow, BorderLayout.NORTH);
        center.add(makeScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel makeStatCard(String label, JLabel valueLbl, String sub, Color accent, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new RoundedBorder(accent.darker(), 1, 12),
            new EmptyBorder(16, 18, 16, 18)
        ));

        valueLbl.setFont(FONT_CARD_N);
        valueLbl.setForeground(TEXT_WHITE);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(FONT_MENU_B);
        labelLbl.setForeground(TEXT_WHITE);
        labelLbl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(accent);

        card.add(valueLbl, BorderLayout.NORTH);
        card.add(labelLbl, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);
        return card;
    }

    /** Xóa bảng chi tiết để load dữ liệu tháng mới. */
    public void clearData() { tableModel.setRowCount(0); }

    /** Thêm một giao dịch vào bảng. */
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private String[] buildMonthOptions() {
        YearMonth now = YearMonth.now();
        String[] months = new String[6];
        for (int i = 0; i < 6; i++) {
            YearMonth ym = now.minusMonths(i);
            months[i] = String.format("Tháng %d/%d", ym.getMonthValue(), ym.getYear());
        }
        return months;
    }

    private YearMonth parseMonth(String text) {
        if (text == null) {
            return null;
        }
        try {
            String[] parts = text.replace("Tháng", "").trim().split("/");
            int month = Integer.parseInt(parts[0].trim());
            int year = Integer.parseInt(parts[1].trim());
            return YearMonth.of(year, month);
        } catch (Exception e) {
            return null;
        }
    }

    private void loadMonth(int year, int month) {
        BigDecimal revenue = reportService.getMonthlyRevenue(year, month);
        int paidCount = reportService.getPaidCountByMonth(year, month);
        int unpaidCount = reportService.getUnpaidCountByMonth(year, month);

        revenueValue.setText(currencyFormat.format(revenue) + "đ");
        packagesValue.setText(paidCount + " gói");
        unpaidValue.setText(unpaidCount + " gói");

        clearData();
        for (Subscription s : reportService.getSubscriptionsByMonth(year, month)) {
            Member m = memberService.getMemberById(s.getMemberId());
            GymPackage p = packageService.getPackageById(s.getPackageId());

            String date = s.getStartDate() != null ? s.getStartDate().format(dateFmt) : "";
            String memberCode = m != null ? m.getMemberCode() : "";
            String memberName = m != null ? m.getFullName() : "";
            String packageName = p != null ? p.getPackageName() : "";
            String price = currencyFormat.format(s.getPriceAtPurchase()) + "đ";
            String payment = s.getPaymentStatus() == AppConstants.PAYMENT_PAID ? "Đã TT" : "Chưa TT";

            addRow(new Object[]{date, memberCode, memberName, packageName, price, payment});
        }
    }
}