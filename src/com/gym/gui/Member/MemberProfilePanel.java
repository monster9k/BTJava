package com.gym.gui.Member;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import com.gym.entity.Member;
import com.gym.entity.User;
import com.gym.service.MemberService;

import static com.gym.gui.AppStyle.*;

public class MemberProfilePanel extends JPanel {

    private final User user;
    private final Member member;
    private final MemberService memberService = new MemberService();

    private JTextField tfName;
    private JTextField tfPhone;

    public MemberProfilePanel(User user, Member member) {
        this.user = user;
        this.member = member;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        JLabel title = new JLabel("Thong tin ca nhan");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new RoundedBorder(DIVIDER, 1, 12),
                new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        gbc.gridy = 0;
        card.add(styledLabel("Ma hoi vien:"), gbc);
        gbc.gridy = 1;
        card.add(styledValue(safe(member.getMemberCode())), gbc);

        gbc.gridy = 2;
        card.add(styledLabel("Ho ten:"), gbc);
        gbc.gridy = 3;
        tfName = makeStyledTextField("", 20);
        tfName.setText(member.getFullName() != null ? member.getFullName() : user.getFullname());
        tfName.setForeground(TEXT_WHITE);
        card.add(tfName, gbc);

        gbc.gridy = 4;
        card.add(styledLabel("Ten dang nhap:"), gbc);
        gbc.gridy = 5;
        card.add(styledValue(safe(user.getUsername())), gbc);

        gbc.gridy = 6;
        card.add(styledLabel("So dien thoai:"), gbc);
        gbc.gridy = 7;
        String phone = member.getPhone() != null ? member.getPhone() : user.getPhone();
        tfPhone = makeStyledTextField("", 20);
        tfPhone.setText(phone != null ? phone : "");
        tfPhone.setForeground(TEXT_WHITE);
        card.add(tfPhone, gbc);

        gbc.gridy = 8;
        card.add(styledLabel("Trang thai:"), gbc);
        gbc.gridy = 9;
        card.add(styledValue(member.isStatus() ? "Active" : "Locked"), gbc);

        JButton btnSave = makeActionButton("Luu thong tin", ACCENT_GREEN);
        btnSave.addActionListener(e -> saveInfo());
        gbc.gridy = 10;
        gbc.insets = new Insets(16, 8, 8, 8);
        card.add(btnSave, gbc);

        add(card, BorderLayout.CENTER);
    }

    private void saveInfo() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ten khong duoc de trong.", "Loi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String phone = tfPhone.getText().trim();
        boolean ok = memberService.updateMember(member.getId(), name, phone,
                member.getGender(), member.getBirthday());
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Cap nhat thong tin that bai.", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Da cap nhat thong tin.", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
    }

    private String safe(String text) {
        return text == null || text.trim().isEmpty() ? "-" : text;
    }
}
