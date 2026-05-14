package com.gym.gui.Admin;

import javax.swing.*;
import java.awt.*;

import com.gym.entity.Subscription;
import com.gym.service.SubscriptionService;
import com.gym.util.AppConstants;

import static com.gym.gui.AppStyle.*;

public class EditSubscriptionDialog extends JDialog {
    private final SubscriptionService subscriptionService = new SubscriptionService();

    public EditSubscriptionDialog(JFrame parent, Subscription sub) {
        super(parent, "Sua trang thai goi", true);
        setSize(480, 420);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm(sub);
    }

    private void buildForm(Subscription sub) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JTextField tfId = makeStyledTextField("S" + sub.getId(), 20);
        tfId.setEditable(false);

        String[] statusItems = {"Pending", "Active", "Expired", "Canceled"};
        JComboBox<String> cbStatus = makeStyledCombo(statusItems);
        cbStatus.setSelectedIndex(mapStatusToIndex(sub.getStatus()));

        String[] paymentItems = {"Unpaid", "Paid"};
        JComboBox<String> cbPayment = makeStyledCombo(paymentItems);
        cbPayment.setSelectedIndex(sub.getPaymentStatus() == AppConstants.PAYMENT_PAID ? 1 : 0);

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Ma goi:"), gbc);
        gbc.gridy = row++; add(tfId, gbc);
        gbc.gridy = row++; add(styledLabel("Trang thai goi:"), gbc);
        gbc.gridy = row++; add(cbStatus, gbc);
        gbc.gridy = row++; add(styledLabel("Trang thai thanh toan:"), gbc);
        gbc.gridy = row++; add(cbPayment, gbc);

        JButton btnSave = makeActionButton("Luu thay doi", ACCENT_ORANGE);
        btnSave.addActionListener(e -> {
            int status = mapIndexToStatus(cbStatus.getSelectedIndex());
            int payment = cbPayment.getSelectedIndex() == 1 ? AppConstants.PAYMENT_PAID : AppConstants.PAYMENT_UNPAID;

            boolean okStatus = subscriptionService.updateStatus(sub.getId(), status);
            boolean okPayment = subscriptionService.updatePaymentStatus(sub.getId(), payment);
            if (!okStatus || !okPayment) {
                JOptionPane.showMessageDialog(this, "Cap nhat that bai.", "Loi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Cap nhat thanh cong.");
            dispose();
        });

        gbc.gridy = row;
        gbc.insets = new Insets(20, 15, 10, 15);
        add(btnSave, gbc);
    }

    private int mapStatusToIndex(int status) {
        if (status == AppConstants.SUBSCRIPTION_PENDING) {
            return 0;
        }
        if (status == AppConstants.SUBSCRIPTION_ACTIVE) {
            return 1;
        }
        if (status == AppConstants.SUBSCRIPTION_EXPIRED) {
            return 2;
        }
        return 3;
    }

    private int mapIndexToStatus(int index) {
        if (index == 0) {
            return AppConstants.SUBSCRIPTION_PENDING;
        }
        if (index == 1) {
            return AppConstants.SUBSCRIPTION_ACTIVE;
        }
        if (index == 2) {
            return AppConstants.SUBSCRIPTION_EXPIRED;
        }
        return AppConstants.SUBSCRIPTION_CANCELED;
    }
}

