package com.gym.gui.Admin;

import javax.swing.*;
import java.awt.*;

import com.gym.entity.Member;
import com.gym.service.MemberService;
import com.gym.service.UserService;

import static com.gym.gui.AppStyle.*;

public class EditMemberDialog extends JDialog {
    private final MemberService memberService = new MemberService();
    private final UserService userService = new UserService();

    public EditMemberDialog(JFrame parent, Member member) {
        super(parent, "Sua hoi vien", true);
        setSize(520, 620);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(CARD_BG);
        setLayout(new GridBagLayout());
        buildForm(member);
    }

    private void buildForm(Member member) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JTextField tfCode = makeStyledTextField(member.getMemberCode(), 20);
        tfCode.setEditable(false);
        JTextField tfFullName = makeStyledTextField(member.getFullName() != null ? member.getFullName() : "", 20);
        JTextField tfPhone = makeStyledTextField(member.getPhone() != null ? member.getPhone() : "", 20);
        String[] genders = {"Nam", "Nữ", "Khác"};
        JComboBox<String> cbGender = makeStyledCombo(genders);
        if (member.getGender() != null) {
            cbGender.setSelectedItem(member.getGender());
        }
        JSpinner spBirthday = makeDatePicker();
        if (member.getBirthday() != null) {
            spBirthday.setValue(java.sql.Date.valueOf(member.getBirthday()));
        }

        int row = 0;
        gbc.gridy = row++; add(styledLabel("Ma hoi vien:"), gbc);
        gbc.gridy = row++; add(tfCode, gbc);
        gbc.gridy = row++; add(styledLabel("Ho va ten:"), gbc);
        gbc.gridy = row++; add(tfFullName, gbc);
        gbc.gridy = row++; add(styledLabel("So dien thoai:"), gbc);
        gbc.gridy = row++; add(tfPhone, gbc);
        gbc.gridy = row++; add(styledLabel("Gioi tinh:"), gbc);
        gbc.gridy = row++; add(cbGender, gbc);
        gbc.gridy = row++; add(styledLabel("Ngay sinh:"), gbc);
        gbc.gridy = row++; add(spBirthday, gbc);

        JButton btnSave = makeActionButton("Luu thay doi", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            String name = tfFullName.getText().trim();
            String phone = tfPhone.getText().trim();
            String gender = (String) cbGender.getSelectedItem();
            java.util.Date dob = (java.util.Date) spBirthday.getValue();
            java.time.LocalDate birthday = dob != null ? new java.sql.Date(dob.getTime()).toLocalDate() : null;

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long nhap ho ten.");
                return;
            }

            boolean ok = memberService.updateMember(member.getId(), name, phone, gender, birthday);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Cap nhat that bai.", "Loi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (member.getUserId() != null) {
                userService.updateProfileById(member.getUserId(), name, phone);
            }

            JOptionPane.showMessageDialog(this, "Cap nhat thanh cong.");
            dispose();
        });

        gbc.gridy = row;
        gbc.insets = new Insets(18, 15, 10, 15);
        add(btnSave, gbc);
    }
}

