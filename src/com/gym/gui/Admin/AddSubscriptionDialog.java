package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;

import com.gym.gui.AppStyle;
import com.gym.gui.AppStyle.RoundedBorder;
import com.gym.entity.GymPackage;
import com.gym.entity.Member;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;
import com.gym.util.AppConstants;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.gym.gui.AppStyle.*;

/**
 * AddSubscriptionDialog.java
 * Dialog đăng ký gói tập mới cho hội viên.
 * Mở bằng: new AddSubscriptionDialog(parentFrame).setVisible(true);
 */
public class AddSubscriptionDialog extends JDialog {

    public AddSubscriptionDialog(JFrame parent) {
        super(parent, "Đăng Ký Gói Tập Mới", true);
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

        MemberService memberService = new MemberService();
        PackageService packageService = new PackageService();
        SubscriptionService subscriptionService = new SubscriptionService();
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        JTextField tfMemberId = makeStyledTextField("Nhập mã hội viên (VD: GYM26001)", 20);

        List<GymPackage> pkgList = packageService.getActivePackages();
        DefaultComboBoxModel<PackageItem> pkgModel = new DefaultComboBoxModel<>();
        for (GymPackage p : pkgList) {
            pkgModel.addElement(new PackageItem(p));
        }
        JComboBox<PackageItem> cbPackage = new JComboBox<>(pkgModel);

        JSpinner spStartDate = makeDatePicker();
        spStartDate.setEnabled(false);
        spStartDate.setValue(java.sql.Date.valueOf(LocalDate.now()));

        JTextField tfPrice = makeStyledTextField("Giá theo gói", 20);
        tfPrice.setEditable(false);

        String[] paymentStatuses = {"1 - Đã thanh toán", "0 - Chưa thanh toán"};
        JComboBox<String> cbPayment = makeStyledCombo(paymentStatuses);

        if (pkgModel.getSize() > 0) {
            PackageItem item = pkgModel.getElementAt(0);
            tfPrice.setText(currencyFormat.format(item.getPrice()));
        }
        cbPackage.addActionListener(e -> {
            PackageItem item = (PackageItem) cbPackage.getSelectedItem();
            if (item != null) {
                tfPrice.setText(currencyFormat.format(item.getPrice()));
            }
        });

        JLabel noteLabel = new JLabel("* Ngày hết hạn tự tính theo thời hạn của gói đã chọn.");
        noteLabel.setFont(FONT_SMALL);
        noteLabel.setForeground(TEXT_GRAY);

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
        autoFillNote.setBackground(CARD_BG);
        autoFillNote.setBorder(new CompoundBorder(new RoundedBorder(DIVIDER, 1, 8), new EmptyBorder(10, 14, 10, 14)));

        JLabel autoTitle  = new JLabel("Hệ thống tự động điền:");
        JLabel autoEnd    = new JLabel("  • end_date = start_date + duration_days của gói");
        JLabel autoStat   = new JLabel("  • status = 1 (Active)");
        JLabel autoCreate = new JLabel("  • created_at = thời điểm hiện tại");

        for (JLabel l : new JLabel[]{autoTitle, autoEnd, autoStat, autoCreate}) {
            l.setFont(FONT_SMALL);
            l.setForeground(l == autoTitle ? TEXT_WHITE : TEXT_GRAY);
            autoFillNote.add(l);
        }
        gbc.gridy = row++;
        gbc.insets = new Insets(10, 15, 8, 15);
        add(autoFillNote, gbc);

        JButton btnSave = makeActionButton("Lưu đăng ký", ACCENT_ORANGE);
        btnSave.addActionListener(e -> {
            String memberCode = tfMemberId.getText().trim();
            if (memberCode.isEmpty() || memberCode.contains("Nhập mã")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hội viên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PackageItem selected = (PackageItem) cbPackage.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn gói tập!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Member member = memberService.getMemberByCode(memberCode);
            if (member == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hội viên với mã: " + memberCode, "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int paymentStatus = cbPayment.getSelectedIndex() == 0 ? AppConstants.PAYMENT_PAID : AppConstants.PAYMENT_UNPAID;
            boolean ok = subscriptionService.registerPackageWithPayment(member.getId(), selected.getId(), paymentStatus);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Đăng ký gói tập thất bại. Vui lòng kiểm tra dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Đăng ký gói tập thành công!\nHội viên: " + memberCode, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        gbc.gridy  = row;
        gbc.insets = new Insets(12, 15, 15, 15);
        add(btnSave, gbc);
    }

    private static class PackageItem {
        private final int id;
        private final String name;
        private final int duration;
        private final BigDecimal price;

        PackageItem(GymPackage pkg) {
            this.id = pkg.getId();
            this.name = pkg.getPackageName();
            this.duration = pkg.getDurationDays();
            this.price = pkg.getPrice();
        }

        int getId() {
            return id;
        }

        BigDecimal getPrice() {
            return price != null ? price : BigDecimal.ZERO;
        }

        String getDisplay() {
            String priceText = NumberFormat.getInstance(new Locale("vi", "VN")).format(getPrice());
            return String.format("%s - %s (%d ngày - %sđ)", id, name, duration, priceText);
        }

        @Override
        public String toString() {
            return getDisplay();
        }
    }
}