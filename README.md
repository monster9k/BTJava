# Gym Management System

## Overview

Gym Management System is a practice project developed to learn and apply Entity Framework Core in building a management application.

The system helps gym administrators manage members, subscription plans, and memberships efficiently. It demonstrates database design, CRUD operations, and entity relationships using Entity Framework Core.

## Features

- Member Management
  - Add new members
  - Update member information
  - Delete members
  - View member details

- Subscription Management
  - Register subscriptions for members
  - Renew subscriptions
  - Cancel subscriptions
  - View subscription history

- Membership Plan Management
  - Create gym plans
  - Update plan information
  - Delete plans
  - Set pricing and duration

## Technologies

- C# .NET
- Entity Framework 
- SQL Server
- Windows Forms

### Main Entities

- User
- Subscription
- MembershipPlan

### Relationships

- One User can have multiple Subscriptions.
- One Membership Plan can be assigned to multiple Subscriptions.
- Each Subscription belongs to one User and one Membership Plan.

## Learning Objectives

This project was developed to practice:

- Entity Framework
- Code First approach
- Database migrations
- CRUD operations
- LINQ queries
- One-to-Many relationships

## Author

Lê Xuân Phúc
