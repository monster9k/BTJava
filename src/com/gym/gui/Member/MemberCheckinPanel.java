package com.gym.gui.Member;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

import com.gym.entity.Subscription;
import com.gym.service.CheckInService;
import com.gym.service.SubscriptionService;
import com.gym.util.AppConstants;

import static com.gym.gui.AppStyle.*;

public class MemberCheckinPanel extends JPanel {

    private final int memberId;
    private final String memberCode;
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final CheckInService checkInService = new CheckInService();

    private JLabel lblStatus;

    public MemberCheckinPanel(int memberId, String memberCode) {
        this.memberId = memberId;
        this.memberCode = memberCode;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        JLabel title = new JLabel("Check-in hom nay");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new RoundedBorder(DIVIDER, 1, 12),
                new EmptyBorder(20, 24, 20, 24)
        ));

        JLabel code = new JLabel("Ma hoi vien: " + memberCode);
        code.setFont(FONT_MENU_B);
        code.setForeground(TEXT_WHITE);

        lblStatus = new JLabel("Nhan nut de check-in hom nay.");
        lblStatus.setFont(FONT_NORMAL);
        lblStatus.setForeground(TEXT_GRAY);

        JButton btnCheckin = makeActionButton("Check-in hom nay", ACCENT_GREEN);
        btnCheckin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckin.setPreferredSize(new Dimension(220, 44));
        btnCheckin.addActionListener(e -> handleCheckin());

        card.add(code, BorderLayout.NORTH);
        card.add(lblStatus, BorderLayout.CENTER);
        card.add(btnCheckin, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    private void handleCheckin() {
        Subscription activeSub = subscriptionService.getActiveSubscription(memberId);
        if (activeSub == null) {
            Subscription latest = subscriptionService.getLatestSubscription(memberId);
            if (latest != null && (latest.getStatus() == AppConstants.SUBSCRIPTION_PENDING
                    || latest.getPaymentStatus() == AppConstants.PAYMENT_UNPAID)) {
                showMessage("Vui long thanh toan tai quay de duoc vao tap.", false);
                return;
            }
            showMessage("Ban chua co goi tap dang hoat dong.", false);
            return;
        }

        if (activeSub.getPaymentStatus() != AppConstants.PAYMENT_PAID) {
            showMessage("Vui long thanh toan tai quay de duoc vao tap.", false);
            return;
        }

        boolean ok = checkInService.checkInByMemberId(memberId);
        if (!ok) {
            showMessage("Check-in that bai. Vui long thu lai.", false);
            return;
        }

        String time = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        showMessage("Check-in thanh cong luc " + time + ".", true);
    }

    private void showMessage(String text, boolean success) {
        lblStatus.setText(text);
        lblStatus.setForeground(success ? ACCENT_GREEN : ACCENT_RED);
        JOptionPane.showMessageDialog(this, text, "Thong bao",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }
}

