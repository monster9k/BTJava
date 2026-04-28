package com.gym.dao;

import com.gym.entity.GymPackage;
import java.util.List;

public interface IPackageDAO {
    List<GymPackage> findAllActive(); // Chỉ lấy gói đang kinh doanh (status=true)
    List<GymPackage> findAll();       // Lấy hết (kể cả gói đã ngừng bán)
    GymPackage findById(int id);

    int insert(GymPackage pkg);
    int update(GymPackage pkg);
    int toggleStatus(int id, boolean status);
}
