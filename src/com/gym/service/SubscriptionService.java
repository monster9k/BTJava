package com.gym.service;

import com.gym.dao.ISubscriptionDAO;
import com.gym.dao.impl.SubscriptionDAOImpl;
import com.gym.entity.Subscription;
import com.gym.util.AppConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Business logic cho Subscription (đăng ký gói, gia hạn)
 * Quy tắc:
 * - Đăng ký mới: tạo dòng subscription mới
 * - Gia hạn: cũng tạo dòng mới (không overwrite cũ)
 * - Giữ lịch sử để audit
 */
public class SubscriptionService {
    private ISubscriptionDAO subscriptionDAO;
    private PackageService packageService;

    public SubscriptionService() {
        this.subscriptionDAO = new SubscriptionDAOImpl();
        this.packageService = new PackageService();
    }

    /**
     * Lấy tất cả gói đăng ký của thành viên
     */
    public List<Subscription> getSubscriptionsForMember(int memberId) {
        return subscriptionDAO.findByMemberId(memberId);
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionDAO.findAll();
    }

    public Subscription getSubscriptionById(int id) {
        return subscriptionDAO.findById(id);
    }

    /**
     * Lấy gói đang còn hạn của thành viên (status = ACTIVE)
     */
    public Subscription getActiveSubscription(int memberId) {
        return subscriptionDAO.findActiveSubscription(memberId);
    }

    public Subscription getLatestSubscription(int memberId) {
        return subscriptionDAO.findLatestByMemberId(memberId);
    }

    /**
     * Đăng ký gói mới cho thành viên
     * Rule:
     * - start_date = hôm nay
     * - end_date = start_date + duration_days
     * - price_at_purchase = giá tại thời điểm đăng ký (snapshot)
     * - status = ACTIVE
     * - payment_status = UNPAID (mặc định)
     */
    public boolean registerPackage(int memberId, int packageId) {
        return registerPackageInternal(memberId, packageId, AppConstants.SUBSCRIPTION_ACTIVE, AppConstants.PAYMENT_UNPAID);
    }

    public boolean registerPackagePending(int memberId, int packageId) {
        return registerPackageInternal(memberId, packageId, AppConstants.SUBSCRIPTION_PENDING, AppConstants.PAYMENT_UNPAID);
    }

    public boolean registerPackageWithPayment(int memberId, int packageId, int paymentStatus) {
        return registerPackageInternal(memberId, packageId, AppConstants.SUBSCRIPTION_ACTIVE, paymentStatus);
    }

    private boolean registerPackageInternal(int memberId, int packageId, int status, int paymentStatus) {
        // Validate
        if (packageId <= 0 || memberId <= 0) {
            System.out.println("ID không hợp lệ");
            return false;
        }

        // Kiểm tra gói có tồn tại không
        if (!packageService.isActivePackage(packageId)) {
            System.out.println("Gói tập không tồn tại hoặc không active");
            return false;
        }

        // Tạo subscription
        Subscription sub = new Subscription();
        sub.setMemberId(memberId);
        sub.setPackageId(packageId);

        LocalDate startDate = LocalDate.now();
        int duration = packageService.getPackageDuration(packageId);
        LocalDate endDate = startDate.plusDays(duration);

        sub.setStartDate(startDate);
        sub.setEndDate(endDate);
        sub.setPriceAtPurchase(packageService.getPackagePrice(packageId));
        sub.setStatus(status);
        sub.setPaymentStatus(paymentStatus);

        int result = subscriptionDAO.register(sub);
        return result > 0;
    }

    /**
     * Gia hạn gói cho thành viên
     * - Tạo subscription mới, không overwrite cũ
     * - Tính end_date từ ngày gia hạn + duration mới
     */
    public boolean renewPackage(int memberId, int packageId) {
        return renewPackageWithPayment(memberId, packageId, AppConstants.PAYMENT_UNPAID);
    }

    /**
     * Gia hạn gói với trạng thái thanh toán
     */
    public boolean renewPackageWithPayment(int memberId, int packageId, int paymentStatus) {
        // Validate
        if (packageId <= 0 || memberId <= 0) {
            System.out.println("ID không hợp lệ");
            return false;
        }

        if (!packageService.isActivePackage(packageId)) {
            System.out.println("Gói tập không tồn tại hoặc không active");
            return false;
        }

        // Lấy gói cũ để check
        Subscription oldSub = subscriptionDAO.findActiveSubscription(memberId);
        LocalDate startDate;
        
        // Nếu gói cũ còn hạn, gia hạn từ ngày hết hạn
        // Nếu gói cũ đã hết, gia hạn từ hôm nay
        if (oldSub != null && oldSub.getEndDate().isAfter(LocalDate.now())) {
            startDate = oldSub.getEndDate();
        } else {
            startDate = LocalDate.now();
        }

        int duration = packageService.getPackageDuration(packageId);
        LocalDate endDate = startDate.plusDays(duration);

        Subscription newSub = new Subscription();
        newSub.setMemberId(memberId);
        newSub.setPackageId(packageId);
        newSub.setStartDate(startDate);
        newSub.setEndDate(endDate);
        newSub.setPriceAtPurchase(packageService.getPackagePrice(packageId));
        newSub.setStatus(AppConstants.SUBSCRIPTION_ACTIVE);
        newSub.setPaymentStatus(paymentStatus);

        int result = subscriptionDAO.register(newSub);
        return result > 0;
    }

    /**
     * Cập nhật trạng thái thanh toán
     */
    public boolean updatePaymentStatus(int subscriptionId, int paymentStatus) {
        int result = subscriptionDAO.updatePaymentStatus(subscriptionId, paymentStatus);
        return result > 0;
    }

    public boolean updateStatus(int subscriptionId, int status) {
        int result = subscriptionDAO.updateStatus(subscriptionId, status);
        return result > 0;
    }

    /**
     * Đánh dấu subscription là PAID
     */
    public boolean markAsPaid(int subscriptionId) {
        return updatePaymentStatus(subscriptionId, AppConstants.PAYMENT_PAID);
    }

    /**
     * Hủy gói đăng ký
     */
    public boolean cancelSubscription(int subscriptionId) {
        int result = subscriptionDAO.cancelSubscription(subscriptionId);
        return result > 0;
    }

    /**
     * Kiểm tra xem subscription có hợp lệ để check-in không
     * Điều kiện:
     * 1. subscription đang ACTIVE
     * 2. đã PAID
     * 3. ngày hiện tại nằm giữa start_date và end_date
     */
    public boolean isValidForCheckIn(Subscription sub) {
        if (sub == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        return sub.getStatus() == AppConstants.SUBSCRIPTION_ACTIVE
                && sub.getPaymentStatus() == AppConstants.PAYMENT_PAID
                && !today.isBefore(sub.getStartDate())
                && !today.isAfter(sub.getEndDate());
    }

    /**
     * Lấy danh sách gói sắp hết hạn trong N ngày tới
     */
    public List<Subscription> getExpiringSubscriptions(int days) {
        return subscriptionDAO.findExpiringSoon(days);
    }

    /**
     * Lấy danh sách gói sắp hết hạn theo cấu hình mặc định
     */
    public List<Subscription> getExpiringSubscriptionsDefault() {
        return getExpiringSubscriptions(AppConstants.REPORT_EXPIRING_DAYS);
    }
}
