package com.gym.dao;

import com.gym.entity.GymPackage;
import java.util.List;

public interface IPackageDAO {
    List<GymPackage> findAllActive(); 
    List<GymPackage> findAll();       
    GymPackage findById(int id);

    int insert(GymPackage pkg);
    int update(GymPackage pkg);
    int toggleStatus(int id, boolean status);
}
