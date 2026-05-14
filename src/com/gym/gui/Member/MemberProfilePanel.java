package com.gym.gui.Member;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import com.gym.entity.Member;
import com.gym.entity.User;

import static com.gym.gui.AppStyle.*;

public class MemberProfilePanel extends JPanel {

    private final User user;
    private final Member member;

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

        JPanel card = new JPanel(new GridLayout(0, 2, 12, 12));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new RoundedBorder(DIVIDER, 1, 12),
                new EmptyBorder(20, 24, 20, 24)
        ));

        addRow(card, "Ma hoi vien", safe(member.getMemberCode()));
        addRow(card, "Ho ten", safe(member.getFullName() != null ? member.getFullName() : user.getFullname()));
        addRow(card, "Ten dang nhap", safe(user.getUsername()));
        String phone = member.getPhone() != null ? member.getPhone() : user.getPhone();
        addRow(card, "So dien thoai", safe(phone));
        addRow(card, "Trang thai", member.isStatus() ? "Active" : "Locked");

        add(card, BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(FONT_MENU);
        lbl.setForeground(TEXT_GRAY);
        JLabel val = new JLabel(value);
        val.setFont(FONT_MENU_B);
        val.setForeground(TEXT_WHITE);
        panel.add(lbl);
        panel.add(val);
    }

    private String safe(String text) {
        return text == null || text.trim().isEmpty() ? "-" : text;
    }
}

