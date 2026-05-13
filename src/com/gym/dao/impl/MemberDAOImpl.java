package com.gym.dao.impl;

import com.gym.dao.BaseDAO;
import com.gym.dao.IMemberDAO;
import com.gym.entity.Member;
import com.gym.util.AppConstants;

import java.util.List;

public class MemberDAOImpl extends BaseDAO implements IMemberDAO {

    private Member mapMember(java.sql.ResultSet rs) throws java.sql.SQLException {
        Member m = new Member();
        m.setId(rs.getInt("id"));
        m.setMemberCode(rs.getString("member_code"));
        m.setFullName(rs.getString("full_name"));
        m.setPhone(rs.getString("phone"));
        m.setGender(rs.getString("gender"));
        java.sql.Date bd = rs.getDate("birthday");
        if (bd != null) {
            m.setBirthday(bd.toLocalDate());
        }
        java.sql.Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            m.setCreatedAt(created.toLocalDateTime());
        }
        m.setStatus(rs.getBoolean("status"));
        return m;
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM members";
        return executeQuery(sql, this::mapMember);
    }

    @Override
    public Member findById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        List<Member> list = executeQuery(sql, this::mapMember, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Member findByCode(String code) {
        String sql = "SELECT * FROM members WHERE member_code = ? AND status = true";
        List<Member> list = executeQuery(sql, this::mapMember, code);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Member> searchByNameOrPhone(String keyword) {
        String searchTerm = "%" + keyword + "%";
        String sql = "SELECT * FROM members WHERE (full_name LIKE ? OR phone LIKE ?) AND status = true ORDER BY full_name";
        return executeQuery(sql, this::mapMember, searchTerm, searchTerm);
    }

    @Override
    public int insert(Member member) {
        String sql = "INSERT INTO members (member_code, full_name, phone, gender, birthday, status) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                member.getMemberCode(),
                member.getFullName(),
                member.getPhone(),
                member.getGender(),
                member.getBirthday() != null ? java.sql.Date.valueOf(member.getBirthday()) : null,
                member.isStatus());
    }

    @Override
    public int update(Member member) {
        String sql = "UPDATE members SET full_name = ?, phone = ?, gender = ?, birthday = ? WHERE id = ?";
        return executeUpdate(sql,
                member.getFullName(),
                member.getPhone(),
                member.getGender(),
                java.sql.Date.valueOf(member.getBirthday()),
                member.getId());
    }

    @Override
    public int deleteSoft(int id) {
        String sql = "UPDATE members SET status = false WHERE id = ?";
        return executeUpdate(sql, id);
    }
}

