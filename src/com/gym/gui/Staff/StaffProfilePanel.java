package com.gym.gui.Staff;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static com.gym.gui.AppStyle.*;

/**
 * StaffProfilePanel.java
 * Panel thông tin cá nhân của Staff.
 *
 * Gồm 2 section:
 *   [1] Thông tin cơ bản  — tên hiển thị, SĐT
 *   [2] Bảo mật           — đổi mật khẩu
 */
public class StaffProfilePanel extends JPanel {

    private final String         username;

    private JTextField     tfDisplayName;
    private JTextField     tfPhone;
    private JPasswordField pfOldPwd;
    private JPasswordField pfNewPwd;
    private JPasswordField pfConfirmPwd;

    public StaffProfilePanel(StaffDashboard owner, String username, String displayName) {
        this.username = username;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build(displayName);
    }

    // ===================================================================
    private void build(String displayName) {
        // Title
        JLabel title = new JLabel("👤  Thông tin cá nhân");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        title.setBorder(new EmptyBorder(0, 0, 4, 0));
        add(title, BorderLayout.NORTH);

        // Container 2 card xếp dọc
        JPanel cards = new JPanel();
        cards.setBackground(BG_DARK);
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.add(buildInfoCard(displayName));
        cards.add(Box.createVerticalStrut(16));
        cards.add(buildPasswordCard());
        cards.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(cards);
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                thumbColor = new Color(70, 80, 110);
                trackColor = BG_DARK;
            }
        });
        add(scroll, BorderLayout.CENTER);
    }

    // ===================================================================
    //  CARD 1 — Thông tin cơ bản
    // ===================================================================
    private JPanel buildInfoCard(String displayName) {
        // wrapper: BoxLayout Y_AXIS — section label trên, inner card dưới
        JPanel wrapper = makeWrapper("✏️  Thông tin cơ bản");

        // inner: GridBagLayout — nơi add các field
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(CARD_BG);
        inner.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(22, 26, 22, 26)
        ));

        GridBagConstraints gbc = defaultGbc();

        // Username (chỉ đọc)
        gbc.gridy = 0; inner.add(styledLabel("Tên đăng nhập:"), gbc);
        gbc.gridy = 1;
        JTextField tfUser = makeStyledTextField(username, 20);
        tfUser.setEditable(false);
        tfUser.setForeground(TEXT_GRAY);
        inner.add(tfUser, gbc);

        // Tên hiển thị
        gbc.gridy = 2; inner.add(styledLabel("Tên hiển thị:"), gbc);
        gbc.gridy = 3;
        tfDisplayName = makeStyledTextField(displayName != null ? displayName : "", 20);
        inner.add(tfDisplayName, gbc);

        // SĐT
        gbc.gridy = 4; inner.add(styledLabel("Số điện thoại:"), gbc);
        gbc.gridy = 5;
        tfPhone = makeStyledTextField("Nhập số điện thoại...", 20);
        inner.add(tfPhone, gbc);

        // Nút lưu
        JButton btnSave = makeActionButton("💾  Lưu thông tin", StaffDashboard.STAFF_ACCENT);
        btnSave.setForeground(new Color(8, 35, 25));
        btnSave.addActionListener(e -> saveInfo());
        gbc.gridy  = 6;
        gbc.insets = new Insets(18, 0, 0, 0);
        inner.add(btnSave, gbc);

        wrapper.add(inner);
        return wrapper;
    }

    // ===================================================================
    //  CARD 2 — Đổi mật khẩu
    // ===================================================================
    private JPanel buildPasswordCard() {
        JPanel wrapper = makeWrapper("🔒  Đổi mật khẩu");

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(CARD_BG);
        inner.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            new EmptyBorder(22, 26, 22, 26)
        ));

        GridBagConstraints gbc = defaultGbc();

        gbc.gridy = 0; inner.add(styledLabel("Mật khẩu hiện tại:"), gbc);
        gbc.gridy = 1;
        pfOldPwd = new JPasswordField(20);
        stylePasswordField(pfOldPwd);
        inner.add(pfOldPwd, gbc);

        gbc.gridy = 2; inner.add(styledLabel("Mật khẩu mới:"), gbc);
        gbc.gridy = 3;
        pfNewPwd = new JPasswordField(20);
        stylePasswordField(pfNewPwd);
        inner.add(pfNewPwd, gbc);

        gbc.gridy = 4; inner.add(styledLabel("Xác nhận mật khẩu mới:"), gbc);
        gbc.gridy = 5;
        pfConfirmPwd = new JPasswordField(20);
        stylePasswordField(pfConfirmPwd);
        inner.add(pfConfirmPwd, gbc);

        JLabel hint = new JLabel("💡 Mật khẩu nên có ít nhất 6 ký tự, bao gồm chữ và số.");
        hint.setFont(FONT_SMALL);
        hint.setForeground(new Color(100, 110, 150));
        gbc.gridy  = 6;
        gbc.insets = new Insets(4, 0, 0, 0);
        inner.add(hint, gbc);

        JButton btnChange = makeActionButton("🔑  Đổi mật khẩu", ACCENT_ORANGE);
        btnChange.addActionListener(e -> savePassword());
        gbc.gridy  = 7;
        gbc.insets = new Insets(18, 0, 0, 0);
        inner.add(btnChange, gbc);

        wrapper.add(inner);
        return wrapper;
    }

    // ===================================================================
    //  HELPERS
    // ===================================================================

    /**
     * Tạo wrapper BoxLayout Y_AXIS:
     *   - section label (teal) ở trên
     *   - caller tự add inner card bên dưới qua wrapper.add(inner)
     * Trả về wrapper để caller gọi wrapper.add(innerCard).
     */
    private JPanel makeWrapper(String sectionTitle) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BG_DARK);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sec = new JLabel(sectionTitle);
        sec.setFont(FONT_MENU_B);
        sec.setForeground(StaffDashboard.STAFF_ACCENT);
        sec.setBorder(new EmptyBorder(0, 2, 8, 0));
        sec.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(sec);

        return wrapper;
    }

    private GridBagConstraints defaultGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx   = 0;
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets  = new Insets(6, 0, 6, 0);
        return g;
    }

    // ===================================================================
    //  SAVE ACTIONS
    // ===================================================================
    private void saveInfo() {
        String newName  = tfDisplayName.getText().trim();
        String newPhone = tfPhone.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên hiển thị không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // TODO: userDAO.updateProfile(username, newName, newPhone)
        JOptionPane.showMessageDialog(this,
            "✅  Đã lưu thông tin!\nTên: " + newName + "\nSĐT: " + newPhone,
            "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void savePassword() {
        String oldP  = new String(pfOldPwd.getPassword());
        String newP  = new String(pfNewPwd.getPassword());
        String confP = new String(pfConfirmPwd.getPassword());
        if (oldP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu hiện tại!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (newP.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newP.equals(confP)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // TODO: userDAO.changePassword(username, oldP, newP)
        pfOldPwd.setText("");
        pfNewPwd.setText("");
        pfConfirmPwd.setText("");
        JOptionPane.showMessageDialog(this,
            "🔑  Đổi mật khẩu thành công!",
            "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}