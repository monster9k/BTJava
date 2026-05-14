package com.gym.service;

import com.gym.dao.IMemberDAO;
import com.gym.dao.impl.MemberDAOImpl;
import com.gym.entity.Member;
import com.gym.util.AppConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;




public class MemberService {
    private IMemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAOImpl();
    }

    


    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    


    public Member getMemberById(int id) {
        return memberDAO.findById(id);
    }

    public Member getMemberByUserId(int userId) {
        if (userId <= 0) {
            return null;
        }
        return memberDAO.findByUserId(userId);
    }

    


    public Member getMemberByCode(String code) {
        return memberDAO.findByCode(code);
    }

    


    public List<Member> searchMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMembers();
        }
        return memberDAO.searchByNameOrPhone(keyword);
    }

    




    public boolean addMember(String fullName, String phone, String gender, LocalDate birthday) {
        if (fullName == null || fullName.trim().isEmpty()) {
            System.out.println("Tên hội viên không được rỗng");
            return false;
        }

        Member member = new Member();
        member.setUserId(null);
        member.setMemberCode(generateMemberCode());
        member.setFullName(fullName);
        member.setPhone(phone);
        member.setGender(gender);
        member.setBirthday(birthday);
        member.setStatus(AppConstants.MEMBER_ACTIVE);

        int result = memberDAO.insert(member);
        return result > 0;
    }

    public boolean addMemberForUser(int userId, String fullName, String phone) {
        if (userId <= 0) {
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }

        Member member = new Member();
        member.setUserId(userId);
        member.setMemberCode(generateMemberCode());
        member.setFullName(fullName.trim());
        member.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
        member.setGender(null);
        member.setBirthday(null);
        member.setStatus(AppConstants.MEMBER_ACTIVE);

        return memberDAO.insert(member) > 0;
    }

    public boolean addMemberForUser(int userId, String fullName, String phone, String gender, LocalDate birthday) {
        if (userId <= 0) {
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }

        Member member = new Member();
        member.setUserId(userId);
        member.setMemberCode(generateMemberCode());
        member.setFullName(fullName.trim());
        member.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
        member.setGender(gender);
        member.setBirthday(birthday);
        member.setStatus(AppConstants.MEMBER_ACTIVE);

        return memberDAO.insert(member) > 0;
    }

    


    public boolean updateMember(int id, String fullName, String phone, String gender, LocalDate birthday) {
        if (fullName == null || fullName.trim().isEmpty()) {
            System.out.println("Tên hội viên không được rỗng");
            return false;
        }

        Member member = memberDAO.findById(id);
        if (member == null) {
            System.out.println("Không tìm thấy hội viên ID: " + id);
            return false;
        }

        member.setFullName(fullName);
        member.setPhone(phone);
        member.setGender(gender);
        member.setBirthday(birthday);

        int result = memberDAO.update(member);
        return result > 0;
    }

    


    public boolean updateMemberStatus(int id, boolean status) {
        Member member = memberDAO.findById(id);
        if (member == null) {
            System.out.println("Không tìm thấy hội viên ID: " + id);
            return false;
        }
        int result = memberDAO.updateStatus(id, status);
        return result > 0;
    }

    


    public boolean deactivateMember(int id) {
        Member member = memberDAO.findById(id);
        if (member == null) {
            System.out.println("Không tìm thấy hội viên ID: " + id);
            return false;
        }

        int result = memberDAO.deleteSoft(id);
        return result > 0;
    }

    



    private String generateMemberCode() {
        LocalDate today = LocalDate.now();
        int year = today.getYear() % 100; 
        List<Member> allMembers = memberDAO.findAll();
        int maxSeq = 0;
        for (Member m : allMembers) {
            String code = m.getMemberCode();
            if (code != null && code.startsWith(AppConstants.MEMBER_CODE_PREFIX)) {
                String digits = code.replace(AppConstants.MEMBER_CODE_PREFIX, "");
                if (digits.length() >= 2) {
                    String seqPart = digits.substring(2); 
                    try {
                        int seq = Integer.parseInt(seqPart);
                        if (seq > maxSeq) {
                            maxSeq = seq;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        int nextSeq = maxSeq + 1;
        return String.format("%s%02d%04d", AppConstants.MEMBER_CODE_PREFIX, year, nextSeq);
    }

    


    public String getNextMemberCode() {
        return generateMemberCode();
    }

    


    public Member findByCodeOrPhone(String query) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }
        String q = query.trim();
        Member byCode = memberDAO.findByCode(q);
        if (byCode != null) {
            return byCode;
        }
        List<Member> list = memberDAO.searchByNameOrPhone(q);
        if (list.isEmpty()) {
            return null;
        }
        
        for (Member m : list) {
            if (m.getPhone() != null && m.getPhone().equals(q)) {
                return m;
            }
        }
        return list.get(0);
    }

    


    public boolean isActiveMember(int memberId) {
        Member member = memberDAO.findById(memberId);
        return member != null && member.isStatus();
    }

    public String resolveDisplayName(Member member) {
        if (member == null) {
            return "";
        }
        if (member.getFullName() != null && !member.getFullName().trim().isEmpty()) {
            return member.getFullName();
        }
        if (member.getUserId() != null) {
            UserService userService = new UserService();
            com.gym.entity.User user = userService.getById(member.getUserId());
            if (user != null) {
                if (user.getFullname() != null && !user.getFullname().trim().isEmpty()) {
                    return user.getFullname();
                }
                return user.getUsername();
            }
        }
        return "";
    }

    public String resolvePhone(Member member) {
        if (member == null) {
            return "";
        }
        if (member.getPhone() != null && !member.getPhone().trim().isEmpty()) {
            return member.getPhone();
        }
        if (member.getUserId() != null) {
            UserService userService = new UserService();
            com.gym.entity.User user = userService.getById(member.getUserId());
            if (user != null && user.getPhone() != null) {
                return user.getPhone();
            }
        }
        return "";
    }
}
