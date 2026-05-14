package com.gym.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * AppStyle.java
 * Tập trung toàn bộ màu sắc, font chữ và các helper UI dùng chung.
 * Tất cả thành viên đều là public static để dùng thoải mái từ bất kỳ class nào.
 */
public final class AppStyle {

    private AppStyle() {} // Không cho phép khởi tạo

    // ===== MÀU SẮC CHỦ ĐẠO =====
    public static final Color BG_DARK       = uiColor("Panel.background", Color.LIGHT_GRAY);
    public static final Color SIDEBAR_BG    = uiColor("Panel.background", Color.LIGHT_GRAY);
    public static final Color CARD_BG       = uiColor("Panel.background", Color.LIGHT_GRAY);
    public static final Color ACCENT_ORANGE = uiColor("Button.background", new Color(220, 220, 220));
    public static final Color ACCENT_BLUE   = uiColor("Button.background", new Color(220, 220, 220));
    public static final Color ACCENT_GREEN  = uiColor("Button.background", new Color(220, 220, 220));
    public static final Color ACCENT_RED    = uiColor("Button.background", new Color(220, 220, 220));
    public static final Color TEXT_WHITE    = uiColor("Label.foreground", Color.BLACK);
    public static final Color TEXT_GRAY     = uiColor("Label.disabledForeground", Color.GRAY);
    public static final Color DIVIDER       = uiColor("Separator.foreground", Color.GRAY);
    public static final Color SIDEBAR_TEXT  = new Color(60, 60, 60);
    public static final Color SIDEBAR_HOVER = new Color(220, 220, 220);
    public static final Color SIDEBAR_ACTIVE = new Color(210, 210, 210);

    // ===== FONT =====
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_MENU    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_MENU_B  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_CARD_N  = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_CARD_L  = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_NORMAL  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);

    // ==================== BUTTON ====================

    public static JButton makeActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_MENU_B);
        btn.setForeground(uiColor("Button.foreground", Color.BLACK));
        btn.setBackground(uiColor("Button.background", color));
        Border border = UIManager.getBorder("Button.border");
        if (border != null) {
            btn.setBorder(border);
        }
        btn.setFocusPainted(true);
        return btn;
    }

    // ==================== TEXT FIELD ====================

    public static JTextField makeStyledTextField(String placeholder, int cols) {
        JTextField tf = new JTextField(cols);
        tf.setBackground(uiColor("TextField.background", Color.WHITE));
        tf.setForeground(uiColor("TextField.foreground", Color.BLACK));
        tf.setFont(FONT_NORMAL);
        tf.setCaretColor(uiColor("TextField.caretForeground", Color.BLACK));
        Border border = UIManager.getBorder("TextField.border");
        if (border != null) {
            tf.setBorder(border);
        }
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(uiColor("TextField.foreground", Color.BLACK)); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(TEXT_GRAY); }
            }
        });
        return tf;
    }

    // ==================== COMBO BOX ====================

    public static <T> JComboBox<T> makeStyledCombo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setFont(FONT_NORMAL);
        cb.setFocusable(false);
        return cb;
    }

    // ==================== PASSWORD FIELD ====================

    public static void stylePasswordField(JPasswordField pf) {
        pf.setBackground(uiColor("TextField.background", Color.WHITE));
        pf.setForeground(uiColor("TextField.foreground", Color.BLACK));
        pf.setCaretColor(uiColor("TextField.caretForeground", Color.BLACK));
        pf.setFont(FONT_NORMAL);
        Border border = UIManager.getBorder("TextField.border");
        if (border != null) {
            pf.setBorder(border);
        }
    }

    // ==================== DATE PICKER ====================

    public static JSpinner makeDatePicker() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        return spinner;
    }

    // ==================== LABEL HELPERS ====================

    public static JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_MENU);
        l.setForeground(TEXT_GRAY);
        return l;
    }

    public static JLabel styledValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_MENU_B);
        l.setForeground(TEXT_WHITE);
        return l;
    }

    // ==================== TEXT AREA ====================

    public static JPanel makeStyledTextArea(String placeholder, int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setBackground(uiColor("TextArea.background", Color.WHITE));
        ta.setForeground(uiColor("TextArea.foreground", Color.BLACK));
        ta.setCaretColor(uiColor("TextArea.caretForeground", Color.BLACK));
        ta.setFont(FONT_NORMAL);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        Border border = UIManager.getBorder("TextArea.border");
        if (border != null) {
            ta.setBorder(border);
        }
        ta.setText(placeholder);
        ta.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (ta.getText().equals(placeholder)) { ta.setText(""); ta.setForeground(uiColor("TextArea.foreground", Color.BLACK)); }
            }
            public void focusLost(FocusEvent e) {
                if (ta.getText().isEmpty()) { ta.setText(placeholder); ta.setForeground(TEXT_GRAY); }
            }
        });
        JScrollPane scroll = new JScrollPane(ta);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(uiColor("Panel.background", Color.LIGHT_GRAY));
        wrapper.add(scroll);
        return wrapper;
    }

    // ==================== TABLE HELPERS ====================

    public static JTable makeStyledTable(String[] cols, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTableAppearance(table);
        return table;
    }

    public static void styleTableAppearance(JTable table) {
        table.setBackground(uiColor("Table.background", Color.WHITE));
        table.setForeground(uiColor("Table.foreground", Color.BLACK));
        table.setFont(FONT_NORMAL);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(uiColor("Table.gridColor", Color.LIGHT_GRAY));
        table.setSelectionBackground(uiColor("Table.selectionBackground", new Color(200, 200, 200)));
        table.setSelectionForeground(uiColor("Table.selectionForeground", Color.BLACK));

        JTableHeader header = table.getTableHeader();
        header.setBackground(uiColor("TableHeader.background", Color.LIGHT_GRAY));
        header.setForeground(uiColor("TableHeader.foreground", Color.BLACK));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
    }

    public static JScrollPane makeScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(uiColor("Table.background", Color.WHITE));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    // ==================== ROUNDED BORDER ====================

    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedBorder(Color c, int t, int r) { color=c; thickness=t; radius=r; }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x + thickness/2.0, y + thickness/2.0,
                    w - thickness, h - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }

    private static Color uiColor(String key, Color fallback) {
        Color c = UIManager.getColor(key);
        return c != null ? c : fallback;
    }
}
