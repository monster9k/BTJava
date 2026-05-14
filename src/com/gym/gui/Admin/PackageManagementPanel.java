package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.gym.entity.GymPackage;
import com.gym.service.PackageService;

import static com.gym.gui.AppStyle.*;





public class PackageManagementPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private java.util.List<GymPackage> cachedPackages;
    private final JFrame owner;
    private PackageService packageService;
    private NumberFormat currencyFormat;

    public PackageManagementPanel(JFrame owner) {
        this.owner = owner;
        this.packageService = new PackageService();
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
        loadPackagesFromDB();
    }

    private void build() {
        
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Quản lý Gói tập");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAdd = makeActionButton("Thêm gói tập", ACCENT_GREEN);
        btnAdd.addActionListener(e -> new AddPackageDialog(owner, this::loadPackagesFromDB).setVisible(true));
        toolBar.add(title,  BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        
        String[] cols = {"ID", "Tên gói", "Thời hạn (ngày)", "Giá (VNĐ)", "Mô tả", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        styleTableAppearance(table);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 6) {
                    showActionMenu(row, e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    


    private void loadPackagesFromDB() {
        try {
            clearData();
            cachedPackages = packageService.getAllPackages();

            for (GymPackage pkg : cachedPackages) {
                String status = pkg.isStatus() ? "Active" : "Ẩn";
                String price = currencyFormat.format(pkg.getPrice().doubleValue());

                tableModel.addRow(new Object[]{
                    pkg.getId(),
                    pkg.getPackageName(),
                    pkg.getDurationDays(),
                    price,
                    pkg.getDescription() != null ? pkg.getDescription() : "",
                    status,
                    "Sửa"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi tải dữ liệu gói tập: " + e.getMessage(),
                "Lỗi Database",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("Error loading packages: " + e);
            e.printStackTrace();
        }
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void showActionMenu(int row, Component invoker, int x, int y) {
        if (cachedPackages == null || row >= cachedPackages.size()) {
            return;
        }
        GymPackage selected = cachedPackages.get(row);
        JPopupMenu menu = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Sửa gói tập");
        edit.addActionListener(e -> {
            new EditPackageDialog(owner, selected).setVisible(true);
            loadPackagesFromDB();
        });
        menu.add(edit);
        menu.show(invoker, x, y);
    }
}