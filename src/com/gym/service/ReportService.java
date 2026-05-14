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






public class ReportService {
    private ISubscriptionDAO subscriptionDAO;
    private ICheckInDAO checkInDAO;
    private IMemberDAO memberDAO;

    public ReportService() {
        this.subscriptionDAO = new SubscriptionDAOImpl();
        this.checkInDAO = new CheckInDAOImpl();
        this.memberDAO = new MemberDAOImpl();
    }

    




    public BigDecimal getMonthlyRevenue(int year, int month) {
        return subscriptionDAO.sumRevenueByMonth(year, month);
    }

    


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

    



    public List<Subscription> getExpiringMembers() {
        return subscriptionDAO.findExpiringSoon(AppConstants.REPORT_EXPIRING_DAYS);
    }

    


    public List<Subscription> getExpiringMembers(int days) {
        return subscriptionDAO.findExpiringSoon(days);
    }

    


    public int getCheckInsToday() {
        return checkInDAO.findByDate(LocalDate.now()).size();
    }

    



    public void getTopPackages() {
        
    }

    



    public void getTopActiveMembers() {
        
    }

    


    public int countExpiringMembers() {
        return getExpiringMembers().size();
    }

    


    public String getWarningMessage() {
        int count = countExpiringMembers();
        if (count > 0) {
            return "Cảnh báo: Có " + count + " hội viên sắp hết hạn trong " + AppConstants.REPORT_EXPIRING_DAYS + " ngày";
        }
        return "";
    }
}

