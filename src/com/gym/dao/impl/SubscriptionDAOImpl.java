package com.gym.dao.impl;

import com.gym.dao.BaseDAO;
import com.gym.dao.ISubscriptionDAO;
import com.gym.entity.Subscription;
import com.gym.util.AppConstants;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionDAOImpl extends BaseDAO implements ISubscriptionDAO {

    private Subscription mapSubscription(java.sql.ResultSet rs) throws java.sql.SQLException {
        Subscription s = new Subscription();
        s.setId(rs.getInt("id"));
        s.setMemberId(rs.getInt("member_id"));
        s.setPackageId(rs.getInt("package_id"));
        s.setStartDate(rs.getDate("start_date").toLocalDate());
        s.setEndDate(rs.getDate("end_date").toLocalDate());
        s.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
        s.setStatus(rs.getInt("status"));
        s.setPaymentStatus(rs.getInt("payment_status"));
        java.sql.Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            s.setCreatedAt(created.toLocalDateTime());
        }
        return s;
    }

    @Override
    public List<Subscription> findByMemberId(int memberId) {
        String sql = "SELECT * FROM subscriptions WHERE member_id = ? ORDER BY start_date DESC";
        return executeQuery(sql, this::mapSubscription, memberId);
    }

    @Override
    public List<Subscription> findAll() {
        String sql = "SELECT * FROM subscriptions ORDER BY start_date DESC";
        return executeQuery(sql, this::mapSubscription);
    }

    @Override
    public Subscription findById(int id) {
        String sql = "SELECT * FROM subscriptions WHERE id = ?";
        List<Subscription> list = executeQuery(sql, this::mapSubscription, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Subscription findActiveSubscription(int memberId) {
        String sql = "SELECT * FROM subscriptions WHERE member_id = ? AND status = ? AND end_date >= ? ORDER BY end_date DESC LIMIT 1";
        List<Subscription> list = executeQuery(sql, this::mapSubscription,
                memberId,
                AppConstants.SUBSCRIPTION_ACTIVE,
                LocalDate.now());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Subscription findLatestByMemberId(int memberId) {
        String sql = "SELECT * FROM subscriptions WHERE member_id = ? ORDER BY start_date DESC, id DESC LIMIT 1";
        List<Subscription> list = executeQuery(sql, this::mapSubscription, memberId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int register(Subscription sub) {
        String sql = "INSERT INTO subscriptions (member_id, package_id, start_date, end_date, price_at_purchase, status, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                sub.getMemberId(),
                sub.getPackageId(),
                java.sql.Date.valueOf(sub.getStartDate()),
                java.sql.Date.valueOf(sub.getEndDate()),
                sub.getPriceAtPurchase(),
                sub.getStatus(),
                sub.getPaymentStatus());
    }

    @Override
    public int updatePaymentStatus(int subId, int paymentStatus) {
        String sql = "UPDATE subscriptions SET payment_status = ? WHERE id = ?";
        return executeUpdate(sql, paymentStatus, subId);
    }

    @Override
    public int updateStatus(int subId, int status) {
        String sql = "UPDATE subscriptions SET status = ? WHERE id = ?";
        return executeUpdate(sql, status, subId);
    }

    @Override
    public int cancelSubscription(int subId) {
        String sql = "UPDATE subscriptions SET status = ? WHERE id = ?";
        return executeUpdate(sql, AppConstants.SUBSCRIPTION_CANCELED, subId);
    }

    @Override
    public List<Subscription> findExpiringSoon(int days) {
        String sql = "SELECT * FROM subscriptions WHERE status = ? AND payment_status = ? " +
                "AND end_date > ? AND end_date <= ? ORDER BY end_date";
        LocalDate today = LocalDate.now();
        LocalDate expireDate = today.plusDays(days);

        return executeQuery(sql, this::mapSubscription,
                AppConstants.SUBSCRIPTION_ACTIVE,
                AppConstants.PAYMENT_PAID,
                today,
                expireDate);
    }

    @Override
    public List<Subscription> findByMonth(int year, int month) {
        String sql = "SELECT * FROM subscriptions WHERE YEAR(start_date) = ? AND MONTH(start_date) = ? ORDER BY start_date";
        return executeQuery(sql, this::mapSubscription, year, month);
    }

    @Override
    public java.math.BigDecimal sumRevenueByMonth(int year, int month) {
        String sql = "SELECT COALESCE(SUM(price_at_purchase), 0) AS total " +
                "FROM subscriptions WHERE YEAR(start_date) = ? AND MONTH(start_date) = ? " +
                "AND payment_status = ?";
        List<java.math.BigDecimal> list = executeQuery(sql, rs -> rs.getBigDecimal("total"), year, month, AppConstants.PAYMENT_PAID);
        return list.isEmpty() ? java.math.BigDecimal.ZERO : list.get(0);
    }

    @Override
    public int countPaidByMonth(int year, int month) {
        String sql = "SELECT COUNT(*) AS total FROM subscriptions WHERE YEAR(start_date) = ? AND MONTH(start_date) = ? AND payment_status = ?";
        List<Integer> list = executeQuery(sql, rs -> rs.getInt("total"), year, month, AppConstants.PAYMENT_PAID);
        return list.isEmpty() ? 0 : list.get(0);
    }

    @Override
    public int countUnpaidByMonth(int year, int month) {
        String sql = "SELECT COUNT(*) AS total FROM subscriptions WHERE YEAR(start_date) = ? AND MONTH(start_date) = ? AND payment_status = ?";
        List<Integer> list = executeQuery(sql, rs -> rs.getInt("total"), year, month, AppConstants.PAYMENT_UNPAID);
        return list.isEmpty() ? 0 : list.get(0);
    }
}
