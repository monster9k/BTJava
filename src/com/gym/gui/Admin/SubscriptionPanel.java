package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

import com.gym.entity.Member;
import com.gym.entity.GymPackage;
import com.gym.entity.Subscription;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;
import com.gym.util.AppConstants;

import static com.gym.gui.AppStyle.*;

/**
 * SubscriptionPanel.java
 * Panel đăng ký / gia hạn gói tập. Bảng danh sách subscription + nút đăng ký mới.
 */
public class SubscriptionPanel extends JPanel {

    private DefaultTableModel tableModel;
    private final JFrame owner;
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final MemberService memberService = new MemberService();
    private final PackageService packageService = new PackageService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    public SubscriptionPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        // --- Toolbar ---
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Đăng ký / Gia hạn Gói tập");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAdd = makeActionButton("➕ Đăng ký gói mới", ACCENT_ORANGE);
        btnAdd.addActionListener(e -> {
            new AddSubscriptionDialog(owner).setVisible(true);
            loadSubscriptions();
        });
        toolBar.add(title,  BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        // --- Table ---
        String[] cols = {"ID","Mã HV","Hội viên","Gói tập","Ngày bắt đầu","Ngày hết hạn","Giá mua (VNĐ)","TT Gói","TT Thanh toán","Ngày tạo"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        loadSubscriptions();

        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void loadSubscriptions() {
        clearData();
        for (Subscription s : subscriptionService.getAllSubscriptions()) {
            Member m = memberService.getMemberById(s.getMemberId());
            GymPackage p = packageService.getPackageById(s.getPackageId());

            String memberCode = m != null ? m.getMemberCode() : "";
            String memberName = m != null ? m.getFullName() : "";
            String packageName = p != null ? p.getPackageName() : "";
            String start = s.getStartDate() != null ? s.getStartDate().format(dateFmt) : "";
            String end = s.getEndDate() != null ? s.getEndDate().format(dateFmt) : "";
            String price = currencyFormat.format(s.getPriceAtPurchase()) + "";

            String status = s.getStatus() == AppConstants.SUBSCRIPTION_ACTIVE ? "✅ Active" : (s.getStatus() == AppConstants.SUBSCRIPTION_EXPIRED ? "🔴 Hết hạn" : "❌ Hủy");
            String payment = s.getPaymentStatus() == AppConstants.PAYMENT_PAID ? "✅ Đã TT" : "⏳ Chưa TT";

            addRow(new Object[]{
                    "S" + s.getId(),
                    memberCode,
                    memberName,
                    packageName,
                    start,
                    end,
                    price,
                    status,
                    payment,
                    start
            });
        }
    }
}