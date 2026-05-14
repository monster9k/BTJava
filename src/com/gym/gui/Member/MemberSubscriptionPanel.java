package com.gym.gui.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.gym.entity.GymPackage;
import com.gym.entity.Subscription;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;
import com.gym.util.AppConstants;

import static com.gym.gui.AppStyle.*;

public class MemberSubscriptionPanel extends JPanel {

    private final int memberId;
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final PackageService packageService = new PackageService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    private DefaultTableModel tableModel;

    public MemberSubscriptionPanel(int memberId) {
        this.memberId = memberId;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        JLabel title = new JLabel("Goi tap cua toi");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Goi tap", "Bat dau", "Ket thuc", "Gia", "Trang thai", "Thanh toan", "Ngay tao"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        styleTableAppearance(table);
        add(makeScrollPane(table), BorderLayout.CENTER);

        loadSubscriptions();
    }

    private void loadSubscriptions() {
        tableModel.setRowCount(0);
        List<Subscription> subs = subscriptionService.getSubscriptionsForMember(memberId);
        for (Subscription s : subs) {
            GymPackage p = packageService.getPackageById(s.getPackageId());
            String pkgName = p != null ? p.getPackageName() : "";
            String start = s.getStartDate() != null ? s.getStartDate().format(dateFmt) : "";
            String end = s.getEndDate() != null ? s.getEndDate().format(dateFmt) : "";
            String price = currencyFormat.format(s.getPriceAtPurchase());
            String created = s.getCreatedAt() != null ? s.getCreatedAt().toLocalDate().format(dateFmt) : start;

            String status;
            if (s.getStatus() == AppConstants.SUBSCRIPTION_PENDING) {
                status = "Pending";
            } else if (s.getStatus() == AppConstants.SUBSCRIPTION_ACTIVE) {
                status = "Active";
            } else if (s.getStatus() == AppConstants.SUBSCRIPTION_EXPIRED) {
                status = "Expired";
            } else {
                status = "Canceled";
            }
            String payment = s.getPaymentStatus() == AppConstants.PAYMENT_PAID ? "Paid" : "Unpaid";

            tableModel.addRow(new Object[]{
                    "S" + s.getId(),
                    pkgName,
                    start,
                    end,
                    price,
                    status,
                    payment,
                    created
            });
        }
    }
}

