package com.gym.dao;
import com.gym.entity.Member;
import java.util.List;

public interface IMemberDAO {
    List<Member> findAll();
    Member findById(int id);
    Member findByCode(String code); // Tìm kiếm nhanh khi check-in
    Member findByUserId(int userId);
    List<Member> searchByNameOrPhone(String keyword);

    int insert(Member member);
    int update(Member member);
    int deleteSoft(int id); // Update status = false
}
