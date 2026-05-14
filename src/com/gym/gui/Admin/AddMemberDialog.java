package com.gym.gui.Admin;

import javax.swing.*;
import java.awt.*;

import com.gym.service.MemberService;
import com.gym.service.UserService;

import static com.gym.gui.AppStyle.*;

/**
 * AddMemberDialog.java
 * Dialog thêm hội viên mới.
 * Mở bằng: new AddMemberDialog(parentFrame).setVisible(true);
 */
public class AddMemberDialog extends JDialog {
    private final MemberService memberService = new MemberService();
    private final UserService userService = new UserService();

    public AddMemberDialog(JFrame parent) {
        super(parent, "➕ Thêm Hội Viên Mới", true);
        // Đã điều chỉnh: Tăng chiều rộng lên 800, Giảm chiều cao xuống 600 an toàn cho mọi màn hình
        setSize(800, 600);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm();
    }

    private void buildForm() {
        JTextField tfMemberCode = makeStyledTextField(memberService.getNextMemberCode(), 20);
        tfMemberCode.setEditable(false);
        JTextField tfFullName   = makeStyledTextField("Họ và tên khách hàng", 20);
        JTextField tfPhone      = makeStyledTextField("Số điện thoại (09...)", 20);

        JCheckBox cbCreateAccount = new JCheckBox("Tạo tài khoản đăng nhập");
        cbCreateAccount.setForeground(TEXT_WHITE);
        cbCreateAccount.setBackground(CARD_BG);
        JTextField tfUsername = makeStyledTextField("Tên đăng nhập", 20);
        JPasswordField pfPassword = new JPasswordField();
        stylePasswordField(pfPassword);
        tfUsername.setEnabled(false);
        pfPassword.setEnabled(false);

        JCheckBox cbLinkAccount = new JCheckBox("Liên kết tài khoản có sẵn");
        cbLinkAccount.setForeground(TEXT_WHITE);
        cbLinkAccount.setBackground(CARD_BG);
        JTextField tfLinkUsername = makeStyledTextField("Username đã đăng ký", 20);
        tfLinkUsername.setEnabled(false);

        cbCreateAccount.addActionListener(e -> {
            boolean enabled = cbCreateAccount.isSelected();
            tfUsername.setEnabled(enabled);
            pfPassword.setEnabled(enabled);
            if (enabled) {
                cbLinkAccount.setSelected(false);
                tfLinkUsername.setEnabled(false);
            }
        });

        cbLinkAccount.addActionListener(e -> {
            boolean enabled = cbLinkAccount.isSelected();
            tfLinkUsername.setEnabled(enabled);
            if (enabled) {
                cbCreateAccount.setSelected(false);
                tfUsername.setEnabled(false);
                pfPassword.setEnabled(false);
            }
        });

        String[] genders = {"Nam", "Nữ", "Khác"};
        JComboBox<String> cbGender = makeStyledCombo(genders);

        JSpinner spBirthday  = makeDatePicker();
        JSpinner spCreatedAt = makeDatePicker();

        // --- SẮP XẾP LAYOUT 2 CỘT ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 15, 8, 15);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5; // Dàn đều 50% mỗi cột

        int row = 0;

        // Dòng 1
        gbc.gridy = row; gbc.gridx = 0; add(styledLabel("Mã hội viên:"), gbc);
        gbc.gridx = 1; add(styledLabel("Ngày đăng ký:"), gbc);

        // Dòng 2
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(tfMemberCode, gbc);
        gbc.gridx = 1; add(spCreatedAt, gbc);

        // Dòng 3
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(styledLabel("Họ và tên:"), gbc);
        gbc.gridx = 1; add(cbCreateAccount, gbc);

        // Dòng 4
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(tfFullName, gbc);
        gbc.gridx = 1; add(styledLabel("Tên đăng nhập:"), gbc);

        // Dòng 5
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(styledLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; add(tfUsername, gbc);

        // Dòng 6
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(tfPhone, gbc);
        gbc.gridx = 1; add(styledLabel("Mật khẩu:"), gbc);

        // Dòng 7
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(styledLabel("Giới tính:"), gbc);
        gbc.gridx = 1; add(pfPassword, gbc);

        // Dòng 8
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(cbGender, gbc);
        gbc.gridx = 1; add(cbLinkAccount, gbc);

        // Dòng 9
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(styledLabel("Ngày sinh (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; add(styledLabel("Username liên kết:"), gbc);

        // Dòng 10
        row++;
        gbc.gridy = row; gbc.gridx = 0; add(spBirthday, gbc);
        gbc.gridx = 1; add(tfLinkUsername, gbc);

        // --- NÚT LƯU CHIẾM 2 CỘT ---
        JButton btnSave = makeActionButton("💾 Lưu hội viên", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            String name = tfFullName.getText().trim();
            if (name.isEmpty() || name.contains("Họ và tên")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên hội viên!");
                return;
            }
            java.util.Date dob = (java.util.Date) spBirthday.getValue();
            java.time.LocalDate birthday = dob != null ? new java.sql.Date(dob.getTime()).toLocalDate() : null;
            String phone = tfPhone.getText().trim();
            String gender = (String) cbGender.getSelectedItem();

            if (cbCreateAccount.isSelected()) {
                String username = tfUsername.getText().trim();
                String password = new String(pfPassword.getPassword());
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!");
                    return;
                }
                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!");
                    return;
                }
                com.gym.entity.User created = userService.createMemberUser(username, password, name, phone);
                if (created == null) {
                    JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại. Tên đăng nhập có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean ok = memberService.addMemberForUser(created.getId(), name, phone, gender, birthday);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Thêm hội viên thất bại sau khi tạo tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (cbLinkAccount.isSelected()) {
                String linkUsername = tfLinkUsername.getText().trim();
                if (linkUsername.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập username để liên kết!");
                    return;
                }
                com.gym.entity.User existing = userService.getByUsername(linkUsername);
                if (existing == null || existing.getRoleId() != com.gym.util.AppConstants.ROLE_MEMBER) {
                    JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại hoặc không phải hội viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (memberService.getMemberByUserId(existing.getId()) != null) {
                    JOptionPane.showMessageDialog(this, "Tài khoản này đã được liên kết với hội viên khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean ok = memberService.addMemberForUser(existing.getId(), name, phone, gender, birthday);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Thêm hội viên thất bại khi liên kết tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng tạo hoặc liên kết tài khoản hội viên.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Thêm hội viên \"" + name + "\" thành công!\nMã hội viên: " + tfMemberCode.getText());
            dispose();
        });

        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Gộp 2 cột lại để chứa nút Lưu
        gbc.insets = new Insets(25, 15, 20, 15);
        add(btnSave, gbc);
    }
}