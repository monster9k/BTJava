package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * AddSubscriptionDialog.java
 * Dialog đăng ký gói tập mới cho hội viên.
 * Mở bằng: new AddSubscriptionDialog(parentFrame).setVisible(true);
 */
public class AddSubscriptionDialog extends JDialog {

    public AddSubscriptionDialog(JFrame parent) {
        super(parent, "➕ Đăng Ký Gói Tập Mới", true);
        setSize(620, 780);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 15, 8, 15);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx   = 0;

        JTextField tfMemberId = makeStyledTextField("Nhập mã hội viên (VD: MEM26001)", 20);

        String[] packages = {
            "P01 - Gym 1 tháng  (30 ngày  - 350,000đ)",
            "P02 - Gym 3 tháng  (90 ngày  - 900,000đ)",
            "P03 - Gym 6 tháng  (180 ngày - 1,600,000đ)",
            "P04 - Gym VIP      (30 ngày  - 800,000đ)",
            "P05 - Yoga 1 tháng (30 ngày  - 400,000đ)",
            "P06 - Zumba        (30 ngày  - 350,000đ)",
        };
        JComboBox<String> cbPackage = makeStyledCombo(packages);

        JSpinner  spStartDate   = makeDatePicker();
        JTextField tfPrice      = makeStyledTextField("Giá thực tế (VD: 850000)", 20);

        String[] paymentStatuses = {"1 - Đã thanh toán", "0 - Chưa thanh toán"};
        JComboBox<String> cbPayment = makeStyledCombo(paymentStatuses);

        JLabel noteLabel = new JLabel("* Ngày hết hạn tự tính theo thời hạn của gói đã chọn.");
        noteLabel.setFont(FONT_SMALL);
        noteLabel.setForeground(ACCENT_ORANGE);

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Mã hội viên (member_id):"), gbc);
        gbc.gridy = row++; add(tfMemberId, gbc);
        gbc.gridy = row++; add(styledLabel("Gói tập (package_id):"), gbc);
        gbc.gridy = row++; add(cbPackage, gbc);
        gbc.gridy = row++; add(styledLabel("Ngày bắt đầu (start_date):"), gbc);
        gbc.gridy = row++; add(spStartDate, gbc);
        gbc.gridy = row++; add(noteLabel, gbc);
        gbc.gridy = row++; add(styledLabel("Giá thực tế - price_at_purchase (VNĐ):"), gbc);
        gbc.gridy = row++; add(tfPrice, gbc);
        gbc.gridy = row++; add(styledLabel("Trạng thái thanh toán (payment_status):"), gbc);
        gbc.gridy = row++; add(cbPayment, gbc);

        // Thông tin tự động điền
        JPanel autoFillNote = new JPanel(new GridLayout(0, 1, 0, 4));
        autoFillNote.setBackground(new Color(35, 40, 60));
        autoFillNote.setBorder(new CompoundBorder(new RoundedBorder(DIVIDER, 1, 8), new EmptyBorder(10, 14, 10, 14)));

        JLabel autoTitle  = new JLabel("Hệ thống tự động điền:");
        JLabel autoEnd    = new JLabel("  • end_date = start_date + duration_days của gói");
        JLabel autoStat   = new JLabel("  • status = 1 (Active)");
        JLabel autoCreate = new JLabel("  • created_at = thời điểm hiện tại");

        for (JLabel l : new JLabel[]{autoTitle, autoEnd, autoStat, autoCreate}) {
            l.setFont(FONT_SMALL);
            l.setForeground(l == autoTitle ? ACCENT_GREEN : TEXT_GRAY);
            autoFillNote.add(l);
        }
        gbc.gridy = row++;
        gbc.insets = new Insets(10, 15, 8, 15);
        add(autoFillNote, gbc);

        JButton btnSave = makeActionButton("💾 Lưu đăng ký", ACCENT_ORANGE);
        btnSave.addActionListener(e -> {
            String memberId = tfMemberId.getText().trim();
            String price    = tfPrice.getText().trim();
            if (memberId.isEmpty() || memberId.contains("Nhập mã")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hội viên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (price.isEmpty() || price.contains("Giá thực tế")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập giá thực tế!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // TODO: Gọi subscriptionDAO.insert(...)
            JOptionPane.showMessageDialog(this, "Đăng ký gói tập thành công!\nHội viên: " + memberId, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        gbc.gridy  = row;
        gbc.insets = new Insets(12, 15, 15, 15);
        add(btnSave, gbc);
    }
}