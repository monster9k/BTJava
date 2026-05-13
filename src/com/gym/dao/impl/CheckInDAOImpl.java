package com.gym.dao.impl;

import com.gym.dao.BaseDAO;
import com.gym.dao.ICheckInDAO;
import com.gym.entity.CheckIn;

import java.time.LocalDate;
import java.util.List;

public class CheckInDAOImpl extends BaseDAO implements ICheckInDAO {

    private CheckIn mapCheckIn(java.sql.ResultSet rs) throws java.sql.SQLException {
        CheckIn c = new CheckIn();
        c.setId(rs.getInt("id"));
        c.setSubscriptionId(rs.getInt("subscription_id"));
        c.setCheckInTime(rs.getTimestamp("check_in_time"));
        return c;
    }

    @Override
    public int log(int subscriptionId) {
        String sql = "INSERT INTO check_ins (subscription_id, check_in_time) VALUES (?, NOW())";
        return executeUpdate(sql, subscriptionId);
    }

    @Override
    public List<CheckIn> findBySubscription(int subId) {
        String sql = "SELECT * FROM check_ins WHERE subscription_id = ? ORDER BY check_in_time DESC";
        return executeQuery(sql, this::mapCheckIn, subId);
    }

    @Override
    public List<CheckIn> findByDate(LocalDate date) {
        String sql = "SELECT * FROM check_ins WHERE DATE(check_in_time) = ? ORDER BY check_in_time DESC";
        return executeQuery(sql, this::mapCheckIn, java.sql.Date.valueOf(date));
    }
}

