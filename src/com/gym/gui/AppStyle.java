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
    public static final Color BG_DARK       = new Color(15, 17, 26);
    public static final Color SIDEBAR_BG    = new Color(21, 24, 38);
    public static final Color CARD_BG       = new Color(28, 32, 50);
    public static final Color ACCENT_ORANGE = new Color(255, 107, 53);
    public static final Color ACCENT_BLUE   = new Color(64, 156, 255);
    public static final Color ACCENT_GREEN  = new Color(56, 217, 169);
    public static final Color ACCENT_RED    = new Color(255, 82, 82);
    public static final Color TEXT_WHITE    = new Color(240, 242, 255);
    public static final Color TEXT_GRAY     = new Color(130, 140, 170);
    public static final Color DIVIDER       = new Color(40, 45, 65);

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
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    // ==================== TEXT FIELD ====================

    public static JTextField makeStyledTextField(String placeholder, int cols) {
        JTextField tf = new JTextField(cols);
        tf.setBackground(new Color(35, 40, 60));
        tf.setForeground(TEXT_GRAY);
        tf.setFont(FONT_NORMAL);
        tf.setCaretColor(TEXT_WHITE);
        tf.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(6, 12, 6, 12)
        ));
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_WHITE); }
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
        cb.setBackground(new Color(35, 40, 60));
        cb.setForeground(TEXT_WHITE);
        cb.setFont(FONT_NORMAL);
        cb.setFocusable(false);
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_BLUE : new Color(35, 40, 60));
                setForeground(TEXT_WHITE);
                setBorder(new EmptyBorder(6, 12, 6, 12));
                return this;
            }
        });
        cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = super.createArrowButton();
                btn.setBackground(new Color(35, 40, 60));
                btn.setBorder(BorderFactory.createEmptyBorder());
                return btn;
            }
        });
        cb.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 8),
            new EmptyBorder(5, 8, 5, 8)
        ));
        cb.setOpaque(true);
        cb.setEditable(true);
        ((JTextField) cb.getEditor().getEditorComponent()).setBackground(new Color(35,40,60));
        ((JTextField) cb.getEditor().getEditorComponent()).setForeground(TEXT_WHITE);
        return cb;
    }

    // ==================== PASSWORD FIELD ====================

    public static void stylePasswordField(JPasswordField pf) {
        pf.setBackground(new Color(35, 40, 60));
        pf.setForeground(TEXT_WHITE);
        pf.setCaretColor(TEXT_WHITE);
        pf.setFont(FONT_NORMAL);
        pf.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 6),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    // ==================== DATE PICKER ====================

    public static JSpinner makeDatePicker() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setBackground(SIDEBAR_BG);
        spinner.setForeground(Color.WHITE);
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
        ta.setBackground(new Color(35, 40, 60));
        ta.setForeground(TEXT_GRAY);
        ta.setCaretColor(TEXT_WHITE);
        ta.setFont(FONT_NORMAL);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new EmptyBorder(10, 12, 10, 12));
        ta.setText(placeholder);
        ta.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (ta.getText().equals(placeholder)) { ta.setText(""); ta.setForeground(TEXT_WHITE); }
            }
            public void focusLost(FocusEvent e) {
                if (ta.getText().isEmpty()) { ta.setText(placeholder); ta.setForeground(TEXT_GRAY); }
            }
        });
        JScrollPane scroll = new JScrollPane(ta);
        scroll.setBorder(new EmptyBorder(0,0,0,0));
        scroll.getViewport().setBackground(new Color(35, 40, 60));
        scroll.setPreferredSize(new Dimension(0, 100));
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(70, 80, 110);
                this.trackColor = new Color(35, 40, 60);
            }
        });
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(35,40,60));
        wrapper.setBorder(new RoundedBorder(new Color(60,65,85), 1, 12));
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
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_WHITE);
        table.setFont(FONT_NORMAL);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(255, 107, 53, 60));
        table.setSelectionForeground(TEXT_WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(35, 40, 60));
        header.setForeground(TEXT_GRAY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(new MatteBorder(0, 0, 1, 0, DIVIDER));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? new Color(255,107,53,50) : (row%2==0 ? CARD_BG : new Color(32,37,56)));
                setForeground(TEXT_WHITE);
                setFont(FONT_NORMAL);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return this;
            }
        });
    }

    public static JScrollPane makeScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD_BG);
        scroll.setBorder(new CompoundBorder(
            new RoundedBorder(DIVIDER, 1, 12),
            BorderFactory.createEmptyBorder()
        ));
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
}
