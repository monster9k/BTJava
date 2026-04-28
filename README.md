# GymManagement

Implementation-first project guide for a small gym management system.

## 1) Project Goal
Build a desktop Java system (IntelliJ module) to manage:
- users and roles (ADMIN, STAFF)
- members
- packages
- subscriptions and renewals
- check-ins
- revenue and expiry reports

This README is optimized for fast team implementation.

## 2) Current Workspace Context
Known from current project structure:
- IntelliJ Java module exists: `GymManagement.iml`
- JDBC driver exists: `lib/mysql-connector-j-9.5.0.jar`
- Source root: `src/`
- Package base: `src/com/gym/`
- Existing folders: `bus`, `dao`, `entity`, `gui`, `util`

## 3) Recommended Package Architecture (Avoid logic overload in bus)
Keep current structure, but split business logic into dedicated packages.

Recommended package map under `src/com/gym/`:
- `entity`: POJO/entity classes mapped to tables
- `dao`: DB access (CRUD, query, transaction)
- `service`: business rules and use cases
- `auth`: login, password change, current user/role context
- `report`: revenue and expiry report queries/services
- `gui`: AWT/Swing forms and screen flow
- `util`: DB connection, date helpers, code generators, constants
- `bus`: optional transition layer only (do not add new core logic here)

Suggested migration rule:
- New logic goes to `service/auth/report`.
- `bus` 

## 4) RBAC (Role-Based Access Control)
### Roles
- `ADMIN`: owner/manager
- `STAFF`: reception/staff

### Permission Matrix
| Feature | ADMIN | STAFF |
|---|---:|---:|
| Manage staff accounts (`users`) | Yes | No |
| Manage packages (`packages`) | Yes | No |
| View revenue report | Yes | No |
| Create/update member (`members`) | Yes | Yes |
| Register/renew subscription (`subscriptions`) | Yes | Yes |
| Check-in member (`check_ins`) | Yes | Yes |
| Update own profile/password | Yes | Yes |

### UI Rule
After login, build menu by `users.role_id`:
- If role = ADMIN: show all menus
- If role = STAFF: hide admin-only menus

## 5) Database Design (DBML)
Copy this block to dbdiagram.io.

```dbml
// Gym Management Database - Team Khoa (optimized)

Table roles {
  id int [pk, increment]
  role_name varchar(50) [not null, unique] // ADMIN, STAFF
}

Table users {
  id int [pk, increment]
  username varchar(50) [not null, unique]
  password varchar(255) [not null]
  full_name varchar(100)
  role_id int [ref: > roles.id]
  status boolean [default: true] // true: active, false: locked
}

Table members {
  id int [pk, increment]
  member_code varchar(20) [unique, not null] // ex: GYM26001
  full_name varchar(100) [not null]
  phone varchar(15)
  gender varchar(10) // Male, Female, Other
  birthday date
  status boolean [default: true] // soft delete flag
  created_at timestamp [default: `now()`]
}

Table packages {
  id int [pk, increment]
  package_name varchar(100) [not null]
  duration_days int [not null] // 30, 90, 365
  price decimal(15,2) [not null]
  description text
  status boolean [default: true]
}

Table subscriptions {
  id int [pk, increment]
  member_id int [ref: > members.id]
  package_id int [ref: > packages.id]

  start_date date [not null]
  end_date date [not null]

  price_at_purchase decimal(15,2) [not null] // price snapshot when sold

  status int [not null] // 1 Active, 2 Expired, 3 Canceled
  payment_status int [not null] // 1 Paid, 0 Unpaid

  created_at timestamp [default: `now()`]
}

Table check_ins {
  id int [pk, increment]
  subscription_id int [ref: > subscriptions.id]
  check_in_time timestamp [default: `now()`]
}
```

## 6) Enum and Status Standard (Team-wide)
Use a single source of truth in code (constants or Java enum + mapper).

### `users.status` (boolean)
- `true`: ACTIVE
- `false`: LOCKED

### `members.status` (boolean)
- `true`: ACTIVE
- `false`: INACTIVE (soft deleted)

### `packages.status` (boolean)
- `true`: ACTIVE
- `false`: INACTIVE

### `subscriptions.status` (int)
- `1`: ACTIVE
- `2`: EXPIRED
- `3`: CANCELED

### `subscriptions.payment_status` (int)
- `1`: PAID
- `0`: UNPAID

Recommended Java enum names:
- `UserStatus`
- `SubscriptionStatus`
- `PaymentStatus`

## 7) Core Business Rules (Must implement exactly)
### 7.1 Register Subscription
- `start_date = current_date`
- `end_date = start_date + packages.duration_days`
- `price_at_purchase = packages.price` at transaction time (immutable snapshot)
- default `subscriptions.status = 1`
- default `payment_status` depends on payment action

### 7.2 Renew Subscription
- Create a NEW row in `subscriptions` (do not overwrite old subscription)
- Keep history for revenue and attendance audit

### 7.3 Check-in Validation
Allow insert to `check_ins` only when all conditions are true:
1. `subscriptions.status = 1` (ACTIVE)
2. `subscriptions.payment_status = 1` (PAID)
3. current date between `start_date` and `end_date`

### 7.4 Soft Delete Policy
- Do not hard delete from `users`, `members`, `packages`
- Use status flags (`false` or `CANCELED`) to preserve history

### 7.5 Member Code Generator
Generate professional `member_code` format, example:
- `GYM` + 2-digit year + 3-5 digit sequence
- ex: `GYM26001`

## 8) Functional Modules
### A. System Management
- login/logout
- change password
- staff account lock/unlock (admin only)
- package setup and maintenance (admin only)

### B. Member Management
- create/update member profile
- search by `member_code`, `phone`, `full_name`

### C. Business Operations
- register package
- renew package
- update payment status
- check-in flow

### D. Reporting
- monthly revenue (sum `price_at_purchase`)
- expiring soon members (within next 5 days)

## 9) Typical Workflow
1. Staff creates member profile.
2. Staff registers package for member.
3. System calculates dates and saves subscription snapshot.
4. Member arrives for training.
5. Staff enters member code/phone, system validates active paid subscription.
6. System stores check-in record.

## 10) 2-Week Implementation Plan
### Week 1 (Core)
- Day 1-2: DB schema + seed roles/users/packages
- Day 3-4: Auth module (`auth`) + role-based menu
- Day 5-7: Member CRUD + search (`service` + `dao` + `gui`)

### Week 2 (Gym Operations + Reports)
- Day 8-10: Subscription register/renew + payment status
- Day 11-12: Check-in validation + check-in history
- Day 13: Revenue and expiring-soon reports (`report`)
- Day 14: Integration test, bug fix, release demo

## 11) Suggested Initial Milestones
- M1: Login + RBAC menu done
- M2: Member CRUD + search done
- M3: Subscription and check-in done
- M4: Reporting done

## 12) Quick Setup Notes
Database notes (for Vietnamese text safety):
- use MySQL collation `utf8mb4_unicode_ci`
- keep timezone consistent between app and DB

Project notes:
- ensure `lib/mysql-connector-j-9.5.0.jar` is attached in module dependencies
- keep DB credentials outside source code if possible (`config.properties`)

## 13) Definition of Done
A feature is done when:
- business rule is implemented in service layer
- role permission is enforced in UI and service
- DB write/read is tested with real data
- no hard delete for business tables
- logs and error messages are understandable for support

