package com.gym.service;

import com.gym.dao.ICheckInDAO;
import com.gym.dao.IMemberDAO;
import com.gym.dao.impl.CheckInDAOImpl;
import com.gym.dao.impl.MemberDAOImpl;
import com.gym.entity.CheckIn;
import com.gym.entity.Member;
import com.gym.entity.Subscription;

import java.time.LocalDate;
import java.util.List;

/**
 * Business logic cho Check-in
 * Rule:
 * - Chỉ cho check-in nếu subscription ACTIVE, PAID, và ngày nằm trong khoảng hiệu lực
 */
public class CheckInService {
    private ICheckInDAO checkInDAO;
    private MemberService memberService;
    private SubscriptionService subscriptionService;

    public CheckInService() {
        this.checkInDAO = new CheckInDAOImpl();
        this.memberService = new MemberService();
        this.subscriptionService = new SubscriptionService();
    }

    /**
     * Check-in theo mã hội viên
     * Validation:
     * 1. Hội viên tồn tại và active
     * 2. Hội viên có gói đang còn hạn
     * 3. Gói hợp lệ để check-in
     */
    public boolean checkInByMemberCode(String memberCode) {
        if (memberCode == null || memberCode.trim().isEmpty()) {
            System.out.println("Mã hội viên không được rỗng");
            return false;
        }

        Member member = memberService.getMemberByCode(memberCode);
        if (member == null) {
            System.out.println("Mã hội viên không tồn tại: " + memberCode);
            return false;
        }

        return checkInByMemberId(member.getId());
    }

    /**
     * Check-in theo ID hội viên
     */
    public boolean checkInByMemberId(int memberId) {
        // Kiểm tra hội viên tồn tại và active
        if (!memberService.isActiveMember(memberId)) {
            System.out.println("Hội viên không tồn tại hoặc đã bị khóa");
            return false;
        }

        // Kiểm tra hội viên có gói hợp lệ để check-in
        Subscription activeSub = subscriptionService.pickSubscriptionForCheckIn(memberId);
        if (activeSub == null) {
            System.out.println("Hội viên không có gói hợp lệ để check-in");
            return false;
        }

        // Thực hiện check-in
        int result = checkInDAO.log(activeSub.getId());
        return result > 0;
    }

    public boolean checkInBySubscription(int memberId, int subscriptionId) {
        if (!memberService.isActiveMember(memberId)) {
            System.out.println("Hội viên không tồn tại hoặc đã bị khóa");
            return false;
        }
        Subscription sub = subscriptionService.getSubscriptionById(subscriptionId);
        if (sub == null || sub.getMemberId() != memberId) {
            System.out.println("Gói không hợp lệ cho hội viên này");
            return false;
        }
        if (!subscriptionService.isValidForCheckIn(sub)) {
            System.out.println("Gói không hợp lệ: chưa thanh toán hoặc đã hết hạn");
            return false;
        }
        int result = checkInDAO.log(sub.getId());
        return result > 0;
    }

    /**
     * Lấy danh sách check-in của 1 gói
     */
    public List<CheckIn> getCheckInsForSubscription(int subscriptionId) {
        return checkInDAO.findBySubscription(subscriptionId);
    }

    /**
     * Lấy danh sách check-in trong 1 ngày
     */
    public List<CheckIn> getCheckInsForDate(LocalDate date) {
        return checkInDAO.findByDate(date);
    }

    /**
     * Đếm số lần check-in của gói
     */
    public int countCheckIns(int subscriptionId) {
        return getCheckInsForSubscription(subscriptionId).size();
    }

    /**
     * Validate rule check-in mà không ghi nhận
     * Dùng để hiển thị thông báo trước khi check-in
     */
    public String validateCheckIn(int memberId) {
        if (!memberService.isActiveMember(memberId)) {
            return "Hội viên không tồn tại hoặc đã bị khóa";
        }

        List<Subscription> valid = subscriptionService.getValidSubscriptionsForCheckIn(memberId);
        if (!valid.isEmpty()) {
            return null;
        }

        Subscription activeSub = subscriptionService.getActiveSubscription(memberId);
        if (activeSub == null) {
            return "Hội viên không có gói đang còn hạn";
        }

        if (activeSub.getPaymentStatus() != 1) {
            return "Chưa thanh toán, không được check-in";
        }
        if (LocalDate.now().isAfter(activeSub.getEndDate())) {
            return "Gói đã hết hạn";
        }
        if (LocalDate.now().isBefore(activeSub.getStartDate())) {
            return "Gói chưa bắt đầu";
        }
        return "Gói không hợp lệ";
    }
}
