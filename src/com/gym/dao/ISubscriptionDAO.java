package com.gym.dao;
import com.gym.entity.Subscription;
import java.util.List;

public interface ISubscriptionDAO {
    List<Subscription> findByMemberId(int memberId);
    Subscription findActiveSubscription(int memberId); // Tìm gói đang còn hạn của khách

    int register(Subscription sub); // Đăng ký mới/Gia hạn
    int updatePaymentStatus(int subId, int paymentStatus);
    int cancelSubscription(int subId);

    // Phục vụ báo cáo
    List<Subscription> findExpiringSoon(int days);
}
