-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 14, 2026 lúc 03:24 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `gym_management`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `check_ins`
--

CREATE TABLE `check_ins` (
  `id` int(11) NOT NULL,
  `subscription_id` int(11) DEFAULT NULL,
  `check_in_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `check_ins`
--

INSERT INTO `check_ins` (`id`, `subscription_id`, `check_in_time`) VALUES
(4, 4, '2026-05-13 13:11:07'),
(5, 7, '2026-05-14 01:23:00');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `members`
--

CREATE TABLE `members` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `member_code` varchar(20) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `members`
--

INSERT INTO `members` (`id`, `user_id`, `member_code`, `full_name`, `phone`, `gender`, `birthday`, `status`, `created_at`) VALUES
(3, 6, 'GYM260003', 'phuc', '1234567890', 'Nam', '2006-05-07', 0, '2026-05-13 12:50:18'),
(4, 7, 'GYM260004', 'Minh Trí', '2132321321213', 'Nam', '2006-02-07', 1, '2026-05-13 12:51:16'),
(5, 8, 'GYM260005', 'phuc cho dien', '0908766788', 'Nam', '2004-03-09', 1, '2026-05-13 13:08:13'),
(6, 11, 'GYM260006', 'Lê Hoài An', '0901000003', 'Nam', '1995-02-14', 1, '2026-05-14 01:18:04'),
(7, 12, 'GYM260007', 'Phạm Thái Bình', '0901000004', 'Nam', '1998-05-20', 1, '2026-05-14 01:18:04'),
(8, 13, 'GYM260008', 'Đỗ Kim Chi', '0901000005', 'Nữ', '2001-08-11', 1, '2026-05-14 01:18:04'),
(9, 14, 'GYM260009', 'Vũ Trí Dũng', '0901000006', 'Nam', '1990-11-30', 1, '2026-05-14 01:18:04'),
(10, 15, 'GYM260010', 'Hồ Hải Én', '0901000007', 'Nữ', '1993-01-05', 1, '2026-05-14 01:18:04'),
(11, 16, 'GYM260011', 'Ngô Thanh Phong', '0901000008', 'Nam', '1988-07-22', 1, '2026-05-14 01:18:04'),
(12, 17, 'GYM260012', 'Bùi Trường Giang', '0901000009', 'Nam', '1999-04-16', 1, '2026-05-14 01:18:04'),
(13, 18, 'GYM260013', 'Mai Cẩm Hoa', '0901000010', 'Nữ', '2002-12-09', 1, '2026-05-14 01:18:04'),
(14, 19, 'GYM260014', 'Phan Thùy Linh', '0901000011', 'Nữ', '1997-03-25', 1, '2026-05-14 01:18:04'),
(15, 20, 'GYM260015', 'Lý Trung Kiên', '0901000012', 'Nam', '1994-06-18', 1, '2026-05-14 01:18:04'),
(16, 21, 'GYM260016', 'Châu Tuyết Mai', '0901000013', 'Nữ', '1996-09-02', 1, '2026-05-14 01:18:04'),
(17, 22, 'GYM260017', 'Lương Hải Nam', '0901000014', 'Nam', '1991-10-28', 1, '2026-05-14 01:18:04'),
(18, 23, 'GYM260018', 'Tô Kiều Oanh', '0901000015', 'Nữ', '2000-02-19', 1, '2026-05-14 01:18:04'),
(19, 24, 'GYM260019', 'Đinh Hồng Quân', '0901000016', 'Nam', '1985-05-12', 1, '2026-05-14 01:18:04'),
(20, 25, 'GYM260020', 'Trịnh Thái Sơn', '0901000017', 'Nam', '1992-08-08', 1, '2026-05-14 01:18:04'),
(21, 26, 'GYM260021', 'Đoàn Thanh Tú', '0901000018', 'Nam', '1998-11-21', 1, '2026-05-14 01:18:04'),
(22, 27, 'GYM260022', 'Võ Hoàng Uyên', '0901000019', 'Nữ', '2003-01-31', 1, '2026-05-14 01:18:04'),
(23, 28, 'GYM260023', 'Đào Thúy Vy', '0901000020', 'Nữ', '1995-04-04', 1, '2026-05-14 01:18:04'),
(24, NULL, 'GYM260024', 'Khách Vãng Lai 01', '0901000098', 'Nam', '1980-12-12', 1, '2026-05-14 01:18:04'),
(25, NULL, 'GYM260025', 'Khách Vãng Lai 02', '0901000099', 'Nữ', '1975-06-06', 1, '2026-05-14 01:18:04');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `packages`
--

CREATE TABLE `packages` (
  `id` int(11) NOT NULL,
  `package_name` varchar(100) NOT NULL,
  `duration_days` int(11) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `description` text DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `packages`
--

INSERT INTO `packages` (`id`, `package_name`, `duration_days`, `price`, `description`, `status`) VALUES
(1, 'Gói Gym 1 Tháng', 30, 350000.00, 'Gói cơ bản', 1),
(2, 'Gói Yoga VIP 3 Tháng', 90, 1200000.00, 'Bao gồm PT', 1),
(3, 'Gói Sinh Viên', 30, 200000.00, 'Cần thẻ sinh viên', 1),
(4, 'Gói gym 1 năm', 365, 12000000.00, 'Đây là gói siu ngon', 1),
(5, 'Gói gym 2 năm', 730, 20000000.00, 'đây là gói siu tiết kiệm', 1),
(6, 'Gói Bơi Lội 1 Tháng', 30, 500000.00, 'Sử dụng hồ bơi không giới hạn', 1),
(7, 'Gói Bơi Lội 3 Tháng', 90, 1350000.00, 'Sử dụng hồ bơi 3 tháng (Tiết kiệm 10%)', 1),
(8, 'Gói Bơi Lội 1 Năm', 365, 5000000.00, 'Bơi lội thỏa thích cả năm', 1),
(9, 'Gói Yoga Cơ Bản', 30, 400000.00, 'Lớp Yoga tuần 3 buổi', 1),
(10, 'Gói Yoga Chuyên Sâu', 90, 1100000.00, 'Lớp Yoga nâng cao, có thảm riêng', 1),
(11, 'Gói Zumba 1 Tháng', 30, 450000.00, 'Lớp Zumba nhảy hiện đại', 1),
(12, 'Gói Kickboxing 1 Tháng', 30, 600000.00, 'Bao gồm găng tay và HLV hướng dẫn', 1),
(13, 'PT 1 Kèm 1 (12 Buổi)', 30, 3600000.00, 'Huấn luyện viên cá nhân 12 buổi', 1),
(14, 'PT 1 Kèm 1 (24 Buổi)', 60, 6500000.00, 'Huấn luyện viên cá nhân 24 buổi', 1),
(15, 'PT 1 Kèm 1 (36 Buổi)', 90, 9000000.00, 'Huấn luyện viên cá nhân 36 buổi (Bao cam kết)', 1),
(16, 'Gói Combo Gym + Bơi', 30, 750000.00, 'Sử dụng cả Gym và Hồ bơi', 1),
(17, 'Gói Combo Gym + Yoga', 30, 650000.00, 'Sử dụng Gym và Lớp Yoga', 1),
(18, 'Gói Cặp Đôi (Couple) 1 Tháng', 30, 600000.00, 'Giá ưu đãi khi đăng ký 2 người', 1),
(19, 'Gói Cặp Đôi (Couple) 3 Tháng', 90, 1600000.00, 'Gym cho 2 người trong 3 tháng', 1),
(20, 'Gói Gia Đình (4 người) 1 Năm', 365, 30000000.00, 'Sử dụng toàn bộ dịch vụ cho 4 người', 1),
(21, 'Gói Sáng Sớm (Morning)', 30, 250000.00, 'Chỉ tập từ 5h00 đến 8h00 sáng', 1),
(22, 'Gói Nghỉ Trưa (Noon)', 30, 250000.00, 'Chỉ tập từ 11h00 đến 14h00', 1),
(23, 'Gói Nửa Năm (6 Tháng)', 180, 6500000.00, 'Tiết kiệm hơn so với mua lẻ', 1),
(24, 'Thẻ Tập Thử (Trial) 7 Ngày', 7, 100000.00, 'Trải nghiệm phòng tập 1 tuần', 1),
(25, 'Gói VIP Toàn Diện', 30, 2000000.00, 'Gym, Bơi, Yoga, Xông hơi, Tủ đồ riêng', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `roles`
--

INSERT INTO `roles` (`id`, `role_name`) VALUES
(1, 'ADMIN'),
(3, 'MEMBER'),
(2, 'STAFF');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `subscriptions`
--

CREATE TABLE `subscriptions` (
  `id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL,
  `package_id` int(11) DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `price_at_purchase` decimal(15,2) NOT NULL,
  `status` int(11) NOT NULL,
  `payment_status` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `subscriptions`
--

INSERT INTO `subscriptions` (`id`, `member_id`, `package_id`, `start_date`, `end_date`, `price_at_purchase`, `status`, `payment_status`, `created_at`) VALUES
(4, 5, 2, '2026-05-13', '2026-08-11', 1200000.00, 1, 1, '2026-05-13 13:08:29'),
(5, 4, 3, '2026-05-13', '2026-06-12', 200000.00, 1, 1, '2026-05-13 13:13:49'),
(7, 7, 8, '2026-05-14', '2027-05-14', 5000000.00, 1, 1, '2026-05-14 01:22:20');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `phone`, `role_id`, `status`) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'Quản lý Khoa', '1231232', 1, 1),
(2, 'staff1', 'e10adc3949ba59abbe56e057f20f883e', 'Lễ tân Phúc', '0903846809', 2, 1),
(3, 'khachhang1', 'e10adc3949ba59abbe56e057f20f883e', 'Nguyễn Văn Khách', '1234566', 3, 1),
(4, 'Phuc', 'e10adc3949ba59abbe56e057f20f883e', 'phuc', '1234567890', 2, 1),
(5, 'nguyen', 'e10adc3949ba59abbe56e057f20f883e', 'nguyen xau trai', '12321321321', 2, 1),
(6, 'sinh', 'e10adc3949ba59abbe56e057f20f883e', 'minh khoa', '090000222', 2, 1),
(7, 'phuctrau', 'e10adc3949ba59abbe56e057f20f883e', 'Minh Trí', '2132321321213', 3, 1),
(8, 'phucnguyen', 'e10adc3949ba59abbe56e057f20f883e', 'phuc cho dien', '0908766788', 3, 1),
(9, 'staff_lan', 'e10adc3949ba59abbe56e057f20f883e', 'Nguyễn Thị Lan', '0901000001', 2, 1),
(10, 'staff_hung', 'e10adc3949ba59abbe56e057f20f883e', 'Trần Việt Hùng', '0901000002', 2, 1),
(11, 'member_an', 'e10adc3949ba59abbe56e057f20f883e', 'Lê Hoài An', '0901000003', 3, 1),
(12, 'member_binh', 'e10adc3949ba59abbe56e057f20f883e', 'Phạm Thái Bình', '0901000004', 3, 1),
(13, 'member_chi', 'e10adc3949ba59abbe56e057f20f883e', 'Đỗ Kim Chi', '0901000005', 3, 1),
(14, 'member_dung', 'e10adc3949ba59abbe56e057f20f883e', 'Vũ Trí Dũng', '0901000006', 3, 1),
(15, 'member_en', 'e10adc3949ba59abbe56e057f20f883e', 'Hồ Hải Én', '0901000007', 3, 1),
(16, 'member_phong', 'e10adc3949ba59abbe56e057f20f883e', 'Ngô Thanh Phong', '0901000008', 3, 1),
(17, 'member_giang', 'e10adc3949ba59abbe56e057f20f883e', 'Bùi Trường Giang', '0901000009', 3, 1),
(18, 'member_hoa', 'e10adc3949ba59abbe56e057f20f883e', 'Mai Cẩm Hoa', '0901000010', 3, 1),
(19, 'member_linh', 'e10adc3949ba59abbe56e057f20f883e', 'Phan Thùy Linh', '0901000011', 3, 1),
(20, 'member_kien', 'e10adc3949ba59abbe56e057f20f883e', 'Lý Trung Kiên', '0901000012', 3, 1),
(21, 'member_mai', 'e10adc3949ba59abbe56e057f20f883e', 'Châu Tuyết Mai', '0901000013', 3, 1),
(22, 'member_nam', 'e10adc3949ba59abbe56e057f20f883e', 'Lương Hải Nam', '0901000014', 3, 1),
(23, 'member_oanh', 'e10adc3949ba59abbe56e057f20f883e', 'Tô Kiều Oanh', '0901000015', 3, 1),
(24, 'member_quan', 'e10adc3949ba59abbe56e057f20f883e', 'Đinh Hồng Quân', '0901000016', 3, 1),
(25, 'member_son', 'e10adc3949ba59abbe56e057f20f883e', 'Trịnh Thái Sơn', '0901000017', 3, 1),
(26, 'member_tu', 'e10adc3949ba59abbe56e057f20f883e', 'Đoàn Thanh Tú', '0901000018', 3, 1),
(27, 'member_uyen', 'e10adc3949ba59abbe56e057f20f883e', 'Võ Hoàng Uyên', '0901000019', 3, 1),
(28, 'member_vy', 'e10adc3949ba59abbe56e057f20f883e', 'Đào Thúy Vy', '0901000020', 3, 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `check_ins`
--
ALTER TABLE `check_ins`
  ADD PRIMARY KEY (`id`),
  ADD KEY `subscription_id` (`subscription_id`);

--
-- Chỉ mục cho bảng `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `member_code` (`member_code`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- Chỉ mục cho bảng `packages`
--
ALTER TABLE `packages`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `role_name` (`role_name`);

--
-- Chỉ mục cho bảng `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `member_id` (`member_id`),
  ADD KEY `package_id` (`package_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `check_ins`
--
ALTER TABLE `check_ins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `members`
--
ALTER TABLE `members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT cho bảng `packages`
--
ALTER TABLE `packages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT cho bảng `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `subscriptions`
--
ALTER TABLE `subscriptions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `check_ins`
--
ALTER TABLE `check_ins`
  ADD CONSTRAINT `check_ins_ibfk_1` FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions` (`id`);

--
-- Các ràng buộc cho bảng `members`
--
ALTER TABLE `members`
  ADD CONSTRAINT `members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD CONSTRAINT `subscriptions_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  ADD CONSTRAINT `subscriptions_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`);

--
-- Các ràng buộc cho bảng `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
