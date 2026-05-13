package com.gym.dao.impl;

import com.gym.dao.BaseDAO;
import com.gym.dao.IPackageDAO;
import com.gym.entity.GymPackage;

import java.util.List;

public class PackageDAOImpl extends BaseDAO implements IPackageDAO {

    private GymPackage mapPackage(java.sql.ResultSet rs) throws java.sql.SQLException {
        GymPackage p = new GymPackage();
        p.setId(rs.getInt("id"));
        p.setPackageName(rs.getString("package_name"));
        p.setDurationDays(rs.getInt("duration_days"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setDescription(rs.getString("description"));
        p.setStatus(rs.getBoolean("status"));
        return p;
    }

    @Override
    public List<GymPackage> findAllActive() {
        String sql = "SELECT * FROM packages WHERE status = true ORDER BY package_name";
        return executeQuery(sql, this::mapPackage);
    }

    @Override
    public List<GymPackage> findAll() {
        String sql = "SELECT * FROM packages ORDER BY package_name";
        return executeQuery(sql, this::mapPackage);
    }

    @Override
    public GymPackage findById(int id) {
        String sql = "SELECT * FROM packages WHERE id = ?";
        List<GymPackage> list = executeQuery(sql, this::mapPackage, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int insert(GymPackage pkg) {
        String sql = "INSERT INTO packages (package_name, duration_days, price, description, status) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                pkg.getPackageName(),
                pkg.getDurationDays(),
                pkg.getPrice(),
                pkg.getDescription(),
                pkg.isStatus());
    }

    @Override
    public int update(GymPackage pkg) {
        String sql = "UPDATE packages SET package_name = ?, duration_days = ?, price = ?, description = ? WHERE id = ?";
        return executeUpdate(sql,
                pkg.getPackageName(),
                pkg.getDurationDays(),
                pkg.getPrice(),
                pkg.getDescription(),
                pkg.getId());
    }

    @Override
    public int toggleStatus(int id, boolean status) {
        String sql = "UPDATE packages SET status = ? WHERE id = ?";
        return executeUpdate(sql, status, id);
    }
}

