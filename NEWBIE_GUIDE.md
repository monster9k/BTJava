# Newbie Guide - GymManagement (Senior ke`m 1)

Tai lieu nay danh cho ban moi vao project. Muc tieu: doc code co huong, khong bi ngop, lam feature an toan.

## 1. Ban do nhanh du an

Project hien tai la Java Swing + JDBC (khong dung Spring).

- Entry point: `src/com/gym/gui/App.java`
- UI Login: `src/com/gym/gui/LoginJFram.java`
- Dashboard Admin: `src/com/gym/gui/Admin/AdminDashboard.java`
- Dashboard Staff: `src/com/gym/gui/Staff/StaffDashboard.java`
- Auth service: `src/com/gym/auth/AuthService.java`
- DAO base: `src/com/gym/dao/BaseDAO.java`
- Ket noi DB: `src/com/gym/util/DatabaseConnection.java`
- SQL scripts: `sql/packages.sql`
- Driver: `lib/mysql-connector-j-9.5.0.jar`

## 2. Kien truc dang chay (de hieu dung)

### Luong tong quat

`GUI -> DAO -> MySQL`

- GUI la noi goi su kien (button click, form submit)
- DAO chay SQL qua `BaseDAO`
- `DatabaseConnection` mo ket noi MySQL

### Vai tro tung package

- `com.gym.entity`: model/POJO map voi bang DB
- `com.gym.dao`: interface va base truy van SQL
- `com.gym.dao.impl`: implement SQL cu the
- `com.gym.auth`: dang nhap, session nguoi dung
- `com.gym.gui`: toan bo man hinh Swing
- `com.gym.util`: hang so, bao mat, DB connection

### Luu y quan trong ve hien trang

- `LoginJFram` dang co `Authenticate()` hard-code `admin/123`, `staff/123`
- `AuthService` da co logic login/register voi DB, nhung chua noi vao login UI
- Nghia la luong auth hien tai **chua thong nhat** (newbie can biet de khong hieu nham)

## 3. Setup toi thieu de chay du an

1. Cai JDK (khuyen nghi 17+)
2. Cai MySQL va tao schema `gym_management`
3. Chay script SQL trong thu muc `sql/` de tao bang/du lieu mau
4. Kiem tra `lib/mysql-connector-j-9.5.0.jar` da add vao module dependencies
5. Sua thong tin DB trong `src/com/gym/util/DatabaseConnection.java`

Lenh build/chay nhanh (PowerShell):

```powershell
javac -encoding UTF-8 -d ./bin -cp "./lib/mysql-connector-j-9.5.0.jar" (Get-ChildItem -Recurse -Filter "*.java" ./src | ForEach-Object { $_.FullName })
java "-Dfile.encoding=UTF-8" -cp "./bin;./lib/mysql-connector-j-9.5.0.jar" com.gym.gui.App
```

## 4. Cach doc code cho nguoi moi (90 phut dau)

### Buoc 1 - 15 phut: nhin luong vao app

- Doc `App.main()` trong `src/com/gym/gui/App.java`
- Xac nhan app mo `LoginJFram`

### Buoc 2 - 25 phut: doc login

- Doc `handleLogin()` trong `src/com/gym/gui/LoginJFram.java`
- Hieu ro 2 role dang duoc route:
  - ADMIN -> `AdminDashboard`
  - STAFF -> `StaffDashboard`

### Buoc 3 - 25 phut: doc dashboard

- `src/com/gym/gui/Admin/AdminDashboard.java`
- `src/com/gym/gui/Staff/StaffDashboard.java`
- Hieu quy tac `showPanel(...)`: menu ben trai -> swap panel vao `contentPanel`

### Buoc 4 - 25 phut: doc DAO + DB

- `src/com/gym/dao/BaseDAO.java`: `executeQuery`, `executeUpdate`
- `src/com/gym/dao/impl/UserDAOImpl.java`: login, insert, toggle status
- `src/com/gym/util/DatabaseConnection.java`: URL, USER, PASSWORD

## 5. 3 luong nghiep vu can nam truoc

### A. Dang nhap

- UI lay username/password
- Hien tai check hard-code
- Dinh huong dung: goi `AuthService.login()` + `UserSession`

### B. Quan ly hoi vien/goi tap/goi dang ky

- Menu admin/staff se mo panel nghiep vu
- Moi panel xu ly input + goi DAO de thao tac DB

### C. Check-in

- Check-in phai dua tren subscription hop le
- Rule chuan nam trong `README.md` (active + paid + trong khoang ngay)

## 6. Nguyen tac code de newbie khong pha he thong

- Khong hard-code role/status lung tung, dung constant/enum
- Khong hard delete bang business (`users`, `members`, `packages`)
- Logic nghiep vu dat o service/auth, GUI chi xu ly view/event
- Moi SQL moi: viet ro rang, co dieu kien status
- Moi form submit: validate input truoc khi goi DAO

## 7. Quy trinh them 1 feature (safe path)

Vi du: them "khoa/mo khoa staff".

1. Xac dinh rule va role duoc phep
2. Them method interface DAO
3. Implement SQL trong `dao.impl`
4. Neu co rule nghiep vu -> tao service method
5. Goi tu panel GUI (button/action)
6. Test voi DB that (case dung + case loi)
7. Review lai RBAC (staff khong duoc thay/manh admin-only)

## 8. Checklist debug truoc khi hoi senior

- [ ] Da check DB co dang chay chua?
- [ ] URL/user/password trong `DatabaseConnection` dung chua?
- [ ] SQL co dung ten cot, ten bang khong?
- [ ] Co bi null/empty input truoc khi submit khong?
- [ ] Co bi route sai role (ADMIN/STAFF) khong?
- [ ] Co stacktrace trong console khong? (copy day du)

## 9. Ke hoach 7 ngay cho newbie

### Ngay 1

- Clone project, chay duoc app, hieu luong login -> dashboard

### Ngay 2

- Doc entity + DAO user
- Viet note rieng: bang nao map voi class nao

### Ngay 3

- Doc panel Member/Package/Subscription
- Ve so do event -> method -> SQL

### Ngay 4

- Lam 1 bug nho UI (validation/message)

### Ngay 5

- Lam 1 task DAO nho (them filter/search)

### Ngay 6

- Noi login UI vao `AuthService` (bo hard-code)

### Ngay 7

- Demo lai luong dang nhap + 1 nghiep vu chinh + viet note hoc duoc gi

## 10. Nhiem vu dau tien senior giao cho ban

Lam theo thu tu nay de len tay nhanh va an toan:

1. Refactor login: `LoginJFram` goi `AuthService` thay vi hard-code
2. Dung `UserSession` de route dashboard theo role_id
3. Them thong bao loi than thien khi DB fail
4. Viet 1 file note mapping "Man hinh nao goi DAO nao"

---

Neu ban bi ket o dau, gui senior 3 thu sau de duoc ho tro nhanh:

- Context: dang lam man hinh/feature nao
- Stacktrace/day du thong bao loi
- Da thu nhung buoc nao trong checklist debug

