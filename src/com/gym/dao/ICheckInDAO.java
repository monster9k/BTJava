package com.gym.dao;

import com.gym.entity.CheckIn;
import java.util.List;

public interface ICheckInDAO {
    int log(int subscriptionId); // Ghi nhận khách vào tập
    List<CheckIn> findBySubscription(int subId);
    List<CheckIn> findByDate(java.time.LocalDate date);
}