package com.gym.dao;

import com.gym.entity.CheckIn;
import java.util.List;

public interface ICheckInDAO {
    int log(int subscriptionId); 
    List<CheckIn> findBySubscription(int subId);
    List<CheckIn> findByDate(java.time.LocalDate date);
}