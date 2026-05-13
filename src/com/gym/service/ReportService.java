package com.gym.service;

import com.gym.dao.ICheckInDAO;
import com.gym.dao.IMemberDAO;
import com.gym.dao.ISubscriptionDAO;
import com.gym.dao.impl.CheckInDAOImpl;
import com.gym.dao.impl.MemberDAOImpl;
import com.gym.dao.impl.SubscriptionDAOImpl;
import com.gym.entity.Subscription;
import com.gym.util.AppConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Business logic cho báo cáo
 * - Báo cáo doanh thu
 * - Báo cáo hội viên sắp hết hạn
 */
public class ReportService {
    private ISubscriptionDAO subscriptionDAO;
    private ICheckInDAO checkInDAO;
    private IMemberDAO memberDAO;

    public ReportService() {
        this.subscriptionDAO = new SubscriptionDAOImpl();
        this.checkInDAO = new CheckInDAOImpl();
        this.memberDAO = new MemberDAOImpl();
    }

    /**
     * Lấy doanh thu trong 1 tháng
     * Công thức: sum(price_at_purchase) của các subscription được đăng ký/gia hạn trong tháng
     * Điều kiện: status active, payment paid
     */
    public BigDecimal getMonthlyRevenue(int year, int month) {
        return subscriptionDAO.sumRevenueByMonth(year, month);
    }

    /**
     * Lấy doanh thu tháng hiện tại
     */
    public BigDecimal getCurrentMonthRevenue() {
        LocalDate today = LocalDate.now();
        return getMonthlyRevenue(today.getYear(), today.getMonthValue());
    }

    public int getPaidCountByMonth(int year, int month) {
        return subscriptionDAO.countPaidByMonth(year, month);
    }

    public int getUnpaidCountByMonth(int year, int month) {
        return subscriptionDAO.countUnpaidByMonth(year, month);
    }

    public List<Subscription> getSubscriptionsByMonth(int year, int month) {
        return subscriptionDAO.findByMonth(year, month);
    }

    /**
     * Lấy danh sách hội viên sắp hết hạn
     * Mặc định: trong vòng 5 ngày tới (theo REPORT_EXPIRING_DAYS)
     */
    public List<Subscription> getExpiringMembers() {
        return subscriptionDAO.findExpiringSoon(AppConstants.REPORT_EXPIRING_DAYS);
    }

    /**
     * Lấy danh sách hội viên sắp hết hạn trong N ngày
     */
    public List<Subscription> getExpiringMembers(int days) {
        return subscriptionDAO.findExpiringSoon(days);
    }

    /**
     * Đếm số lần check-in trong ngày
     */
    public int getCheckInsToday() {
        return checkInDAO.findByDate(LocalDate.now()).size();
    }

    /**
     * Lấy thống kê gói tập được bán nhiều nhất (top)
     * Tạm thời không implement vì cần thêm query phức tạp
     */
    public void getTopPackages() {
        // Not implemented: requires aggregate query by package
    }

    /**
     * Lấy thống kê hội viên hoạt động nhất
     * Tạm thời không implement vì cần thêm query phức tạp
     */
    public void getTopActiveMembers() {
        // Not implemented: requires aggregate query by member
    }

    /**
     * Kiểm tra gói sắp hết hạn có bao nhiêu
     */
    public int countExpiringMembers() {
        return getExpiringMembers().size();
    }

    /**
     * Lấy thông báo cảnh báo nếu có hội viên sắp hết hạn
     */
    public String getWarningMessage() {
        int count = countExpiringMembers();
        if (count > 0) {
            return "Cảnh báo: Có " + count + " hội viên sắp hết hạn trong " + AppConstants.REPORT_EXPIRING_DAYS + " ngày";
        }
        return "";
    }
}

