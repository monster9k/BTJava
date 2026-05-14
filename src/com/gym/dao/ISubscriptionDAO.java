package com.gym.dao;
import com.gym.entity.Subscription;
import java.util.List;

public interface ISubscriptionDAO {
    List<Subscription> findAll();
    Subscription findById(int id);
    List<Subscription> findByMemberId(int memberId);
    Subscription findActiveSubscription(int memberId); 
    Subscription findLatestByMemberId(int memberId);
    List<Subscription> findValidForCheckIn(int memberId);

    int register(Subscription sub); 
    int updatePaymentStatus(int subId, int paymentStatus);
    int updateStatus(int subId, int status);
    int cancelSubscription(int subId);

    
    List<Subscription> findExpiringSoon(int days);
    List<Subscription> findByMonth(int year, int month);
    java.math.BigDecimal sumRevenueByMonth(int year, int month);
    int countPaidByMonth(int year, int month);
    int countUnpaidByMonth(int year, int month);
}
