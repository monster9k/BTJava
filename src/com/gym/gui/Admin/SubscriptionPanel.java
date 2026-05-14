package com.gym.gui.Admin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

import com.gym.entity.Member;
import com.gym.entity.GymPackage;
import com.gym.entity.Subscription;
import com.gym.service.MemberService;
import com.gym.service.PackageService;
import com.gym.service.SubscriptionService;
import com.gym.util.AppConstants;

import static com.gym.gui.AppStyle.*;





public class SubscriptionPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private java.util.List<Subscription> cachedSubscriptions;
    private final JFrame owner;
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final MemberService memberService = new MemberService();
    private final PackageService packageService = new PackageService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    public SubscriptionPanel(JFrame owner) {
        this.owner = owner;
        setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 16));
        build();
    }

    private void build() {
        
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setBackground(BG_DARK);
        JLabel title = new JLabel("Đăng ký / Gia hạn Gói tập");
        title.setFont(FONT_HEADER);
        title.setForeground(TEXT_WHITE);
        JButton btnAdd = makeActionButton("Đăng ký gói mới", ACCENT_ORANGE);
        btnAdd.addActionListener(e -> {
            new AddSubscriptionDialog(owner).setVisible(true);
            loadSubscriptions();
        });
        toolBar.add(title,  BorderLayout.WEST);
        toolBar.add(btnAdd, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        
        String[] cols = {"ID","Mã HV","Hội viên","Gói tập","Ngày bắt đầu","Ngày hết hạn","Giá mua (VNĐ)","TT Gói","TT Thanh toán","Ngày tạo","Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        loadSubscriptions();

        table = new JTable(tableModel);
        styleTableAppearance(table);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 10) {
                    showActionMenu(row, e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        add(makeScrollPane(table), BorderLayout.CENTER);
    }

    public void clearData() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    private void loadSubscriptions() {
        clearData();
        cachedSubscriptions = subscriptionService.getAllSubscriptions();
        for (Subscription s : cachedSubscriptions) {
            Member m = memberService.getMemberById(s.getMemberId());
            GymPackage p = packageService.getPackageById(s.getPackageId());

            String memberCode = m != null ? m.getMemberCode() : "";
            String memberName = m != null ? memberService.resolveDisplayName(m) : "";
            String packageName = p != null ? p.getPackageName() : "";
            String start = s.getStartDate() != null ? s.getStartDate().format(dateFmt) : "";
            String end = s.getEndDate() != null ? s.getEndDate().format(dateFmt) : "";
            String price = currencyFormat.format(s.getPriceAtPurchase()) + "";
            String created = s.getCreatedAt() != null ? s.getCreatedAt().toLocalDate().format(dateFmt) : start;

            String status;
            if (s.getStatus() == AppConstants.SUBSCRIPTION_PENDING) {
                status = "Pending";
            } else if (s.getStatus() == AppConstants.SUBSCRIPTION_ACTIVE) {
                status = "Active";
            } else if (s.getStatus() == AppConstants.SUBSCRIPTION_EXPIRED) {
                status = "Hết hạn";
            } else {
                status = "Hủy";
            }
            String payment = s.getPaymentStatus() == AppConstants.PAYMENT_PAID ? "Đã TT" : "Chưa TT";

            addRow(new Object[]{
                    "S" + s.getId(),
                    memberCode,
                    memberName,
                    packageName,
                    start,
                    end,
                    price,
                    status,
                    payment,
                    created,
                    "Sửa | Hủy"
            });
        }
    }

    private void showActionMenu(int row, Component invoker, int x, int y) {
        if (cachedSubscriptions == null || row >= cachedSubscriptions.size()) {
            return;
        }
        Subscription selected = cachedSubscriptions.get(row);
        JPopupMenu menu = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Sửa trạng thái");
        edit.addActionListener(e -> {
            new EditSubscriptionDialog(owner, selected).setVisible(true);
            loadSubscriptions();
        });
        if (selected.getStatus() == AppConstants.SUBSCRIPTION_CANCELED) {
            JMenuItem restore = new JMenuItem("Mở khóa gói");
            restore.addActionListener(e -> {
                boolean ok = subscriptionService.updateStatus(selected.getId(), AppConstants.SUBSCRIPTION_ACTIVE);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Mở khóa gói thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                loadSubscriptions();
            });
            menu.add(edit);
            menu.add(restore);
        } else {
            JMenuItem cancel = new JMenuItem("Hủy gói (khóa)");
            cancel.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có chắc muốn hủy gói này?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                boolean ok = subscriptionService.cancelSubscription(selected.getId());
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Hủy gói thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                loadSubscriptions();
            });
            menu.add(edit);
            menu.add(cancel);
        }
        menu.show(invoker, x, y);
    }
}