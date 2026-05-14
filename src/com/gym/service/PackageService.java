package com.gym.service;

import com.gym.dao.IPackageDAO;
import com.gym.dao.impl.PackageDAOImpl;
import com.gym.entity.GymPackage;
import com.gym.util.AppConstants;

import java.math.BigDecimal;
import java.util.List;




public class PackageService {
    private IPackageDAO packageDAO;

    public PackageService() {
        this.packageDAO = new PackageDAOImpl();
    }

    


    public List<GymPackage> getActivePackages() {
        return packageDAO.findAllActive();
    }

    


    public List<GymPackage> getAllPackages() {
        return packageDAO.findAll();
    }

    


    public GymPackage getPackageById(int id) {
        return packageDAO.findById(id);
    }

    


    public boolean addPackage(String packageName, int durationDays, BigDecimal price, String description) {
        if (packageName == null || packageName.trim().isEmpty()) {
            System.out.println("Tên gói tập không được rỗng");
            return false;
        }

        if (durationDays <= 0) {
            System.out.println("Thời hạn phải > 0 ngày");
            return false;
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Giá phải > 0");
            return false;
        }

        GymPackage pkg = new GymPackage();
        pkg.setPackageName(packageName);
        pkg.setDurationDays(durationDays);
        pkg.setPrice(price);
        pkg.setDescription(description);
        pkg.setStatus(AppConstants.PACKAGE_ACTIVE);

        int result = packageDAO.insert(pkg);
        return result > 0;
    }

    


    public boolean updatePackage(int id, String packageName, int durationDays, BigDecimal price, String description) {
        if (packageName == null || packageName.trim().isEmpty()) {
            System.out.println("Tên gói tập không được rỗng");
            return false;
        }

        if (durationDays <= 0) {
            System.out.println("Thời hạn phải > 0 ngày");
            return false;
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Giá phải > 0");
            return false;
        }

        GymPackage pkg = packageDAO.findById(id);
        if (pkg == null) {
            System.out.println("Không tìm thấy gói ID: " + id);
            return false;
        }

        pkg.setPackageName(packageName);
        pkg.setDurationDays(durationDays);
        pkg.setPrice(price);
        pkg.setDescription(description);

        int result = packageDAO.update(pkg);
        return result > 0;
    }

    


    public boolean togglePackageStatus(int id, boolean status) {
        GymPackage pkg = packageDAO.findById(id);
        if (pkg == null) {
            System.out.println("Không tìm thấy gói ID: " + id);
            return false;
        }

        int result = packageDAO.toggleStatus(id, status);
        return result > 0;
    }

    


    public boolean isActivePackage(int packageId) {
        GymPackage pkg = packageDAO.findById(packageId);
        return pkg != null && pkg.isStatus();
    }

    


    public BigDecimal getPackagePrice(int packageId) {
        GymPackage pkg = packageDAO.findById(packageId);
        return pkg != null ? pkg.getPrice() : BigDecimal.ZERO;
    }

    


    public int getPackageDuration(int packageId) {
        GymPackage pkg = packageDAO.findById(packageId);
        return pkg != null ? pkg.getDurationDays() : 0;
    }
}

