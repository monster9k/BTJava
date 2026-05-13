-- ===================================
-- GYM MANAGEMENT DATABASE - CLEAN SETUP
-- ===================================

-- DROP EXISTING TABLES (if any)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS check_ins;
DROP TABLE IF EXISTS subscriptions;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS packages;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Table: roles (CREATE FIRST - no dependencies)
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT 'ADMIN, STAFF'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Insert roles IMMEDIATELY after creating table
INSERT INTO roles (id, role_name) VALUES
(1, 'ADMIN'),
(2, 'STAFF');

-- 2. Table: users (references roles)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL COMMENT 'Hashed password',
    full_name VARCHAR(100),
    phone VARCHAR(15),
    role_id INT NOT NULL,
    status BOOLEAN DEFAULT true COMMENT 'true: active, false: locked',
    FOREIGN KEY (role_id) REFERENCES roles(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Test data: admin/123 (MD5), staff/123 (MD5)
INSERT INTO users (username, password, full_name, role_id, status) VALUES
('admin', '202cb962ac59075b964b07152d234b70', 'Admin Gym', 1, true),
('staff', '202cb962ac59075b964b07152d234b70', 'Staff Gym', 2, true);

-- 3. Table: members
CREATE TABLE members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    member_code VARCHAR(20) NOT NULL UNIQUE COMMENT 'ex: GYM26001',
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    gender VARCHAR(10) COMMENT 'Male, Female, Other',
    birthday DATE,
    status BOOLEAN DEFAULT true COMMENT 'true: active, false: inactive',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO members (member_code, full_name, phone, gender, birthday, status) VALUES
('GYM26001', 'Nguyễn Văn A', '0912345678', 'Male', '1990-05-15', true),
('GYM26002', 'Trần Thị B', '0987654321', 'Female', '1995-08-20', true),
('GYM26003', 'Lê Văn C', '0909999999', 'Male', '1992-03-10', true);

-- 4. Table: packages
CREATE TABLE packages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    package_name VARCHAR(100) NOT NULL,
    duration_days INT NOT NULL COMMENT '30, 90, 365',
    price DECIMAL(15, 2) NOT NULL,
    description TEXT,
    status BOOLEAN DEFAULT true COMMENT 'true: active, false: inactive'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO packages (package_name, duration_days, price, description, status) VALUES
('Gói 1 Tháng', 30, 350000, 'Tập gym cơ bản 30 ngày', true),
('Gói 3 Tháng', 90, 900000, 'Tập gym 90 ngày - Giảm giá', true),
('Gói 1 Năm', 365, 3000000, 'Tập gym cả năm - VIP', true),
('Yoga 1 Tháng', 30, 250000, 'Lớp yoga 30 ngày', true);

-- 5. Table: subscriptions
CREATE TABLE subscriptions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    package_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    price_at_purchase DECIMAL(15, 2) NOT NULL COMMENT 'Giá snapshot khi bán',
    status INT NOT NULL DEFAULT 1 COMMENT '1: Active, 2: Expired, 3: Canceled',
    payment_status INT NOT NULL DEFAULT 0 COMMENT '1: Paid, 0: Unpaid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (package_id) REFERENCES packages(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO subscriptions (member_id, package_id, start_date, end_date, price_at_purchase, status, payment_status) VALUES
(1, 1, '2026-05-01', '2026-05-31', 350000, 1, 1),
(1, 2, '2026-06-01', '2026-08-29', 900000, 1, 0),
(2, 3, '2026-01-01', '2026-12-31', 3000000, 1, 1),
(3, 1, '2026-04-01', '2026-04-30', 350000, 2, 1);

-- 6. Table: check_ins
CREATE TABLE check_ins (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subscription_id INT NOT NULL,
    check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO check_ins (subscription_id, check_in_time) VALUES
(1, '2026-05-10 07:00:00'),
(1, '2026-05-11 08:30:00'),
(1, '2026-05-12 09:15:00'),
(3, '2026-05-12 10:00:00');

-- ===================================
-- INDEX FOR PERFORMANCE
-- ===================================
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_members_code ON members(member_code);
CREATE INDEX idx_members_phone ON members(phone);
CREATE INDEX idx_subscriptions_member_status ON subscriptions(member_id, status);
CREATE INDEX idx_checkins_subscription ON check_ins(subscription_id);

-- Verify
SELECT 'Roles:' as check_point;
SELECT * FROM roles;

SELECT 'Users:' as check_point;
SELECT id, username, role_id FROM users LIMIT 5;

SELECT 'Setup Complete!' as result;

