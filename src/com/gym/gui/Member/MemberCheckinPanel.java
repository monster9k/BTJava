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
    private final com.gym.service.PackageService packageService = new com.gym.service.PackageService();

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
        java.util.List<Subscription> valid = subscriptionService.getValidSubscriptionsForCheckIn(memberId);
        if (valid.isEmpty()) {
            Subscription latest = subscriptionService.getLatestSubscription(memberId);
            if (latest != null && (latest.getStatus() == AppConstants.SUBSCRIPTION_PENDING
                    || latest.getPaymentStatus() == AppConstants.PAYMENT_UNPAID)) {
                showMessage("Vui long thanh toan tai quay de duoc vao tap.", false);
                return;
            }
            showMessage("Ban chua co goi tap dang hoat dong.", false);
            return;
        }

        Subscription chosen = chooseSubscription(valid);
        if (chosen == null) {
            showMessage("Ban chua chon goi tap de check-in.", false);
            return;
        }

        boolean ok = checkInService.checkInBySubscription(memberId, chosen.getId());
        if (!ok) {
            showMessage("Check-in that bai. Vui long thu lai.", false);
            return;
        }

        String time = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        showMessage("Check-in thanh cong luc " + time + ".", true);
    }

    private Subscription chooseSubscription(java.util.List<Subscription> valid) {
        if (valid.size() == 1) {
            return valid.get(0);
        }
        String[] options = new String[valid.size()];
        for (int i = 0; i < valid.size(); i++) {
            Subscription s = valid.get(i);
            com.gym.entity.GymPackage p = packageService.getPackageById(s.getPackageId());
            String name = p != null ? p.getPackageName() : "";
            String range = s.getStartDate() + " - " + s.getEndDate();
            options[i] = name + " (" + range + ")";
        }
        String picked = (String) JOptionPane.showInputDialog(
                this,
                "Ban co nhieu goi dang hoat dong. Hay chon goi de check-in:",
                "Chon goi",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (picked == null) {
            return null;
        }
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(picked)) {
                return valid.get(i);
            }
        }
        return valid.get(0);
    }

    private void showMessage(String text, boolean success) {
        lblStatus.setText(text);
        lblStatus.setForeground(success ? ACCENT_GREEN : ACCENT_RED);
        JOptionPane.showMessageDialog(this, text, "Thong bao",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }
}
