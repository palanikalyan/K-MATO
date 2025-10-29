# K-MATO Food Delivery Database Schema

## Entity Relationship Diagram (ERD)

### Copy this code to https://dbdiagram.io/d to visualize

```dbdiagram
// K-MATO Food Delivery System Database Schema
// Use DBML (Database Markup Language) syntax
// Visit: https://dbdiagram.io/d

Table users {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  email varchar(255) [unique, not null, note: 'Unique email address']
  password varchar(255) [not null, note: 'BCrypt hashed password']
  full_name varchar(255) [not null, note: 'User full name']
  phone_number varchar(20) [null, note: 'Contact number']
  role varchar(50) [not null, note: 'ENUM: ADMIN, RESTAURANT_OWNER, CUSTOMER']
  is_active boolean [not null, default: true, note: 'Account active status']
  created_at timestamp [not null, default: `now()`, note: 'Account creation time']
  updated_at timestamp [null, note: 'Last update time']
  
  indexes {
    email [unique, name: 'idx_users_email']
    role [name: 'idx_users_role']
  }
  
  note: 'Stores user accounts for customers, restaurant owners, and admins'
}

Table addresses {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  street varchar(255) [not null, note: 'Street address']
  city varchar(100) [not null, note: 'City name']
  state varchar(100) [not null, note: 'State name']
  zip_code varchar(20) [not null, note: 'Postal code']
  landmark varchar(255) [null, note: 'Optional landmark']
  is_default boolean [not null, default: false, note: 'Default address flag']
  user_id bigint [not null, ref: > users.id, note: 'Foreign key to users']
  
  indexes {
    user_id [name: 'idx_addresses_user_id']
  }
  
  note: 'Stores delivery addresses for users'
}

Table restaurants {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  name varchar(255) [not null, note: 'Restaurant name']
  description text [not null, note: 'Restaurant description']
  image_url varchar(500) [null, note: 'Restaurant cover image URL']
  address varchar(255) [not null, note: 'Physical address']
  city varchar(100) [not null, note: 'City location']
  phone_number varchar(20) [null, note: 'Contact number']
  rating double [not null, default: 0.0, note: 'Average rating (0.0-5.0)']
  total_reviews int [not null, default: 0, note: 'Number of reviews']
  is_open boolean [not null, default: true, note: 'Open/Closed status']
  approval_status varchar(50) [not null, default: 'PENDING', note: 'ENUM: PENDING, APPROVED, REJECTED']
  owner_id bigint [not null, ref: > users.id, note: 'Foreign key to users (owner)']
  created_at timestamp [not null, default: `now()`, note: 'Restaurant creation time']
  updated_at timestamp [null, note: 'Last update time']
  
  indexes {
    city [name: 'idx_restaurants_city']
    owner_id [name: 'idx_restaurants_owner_id']
    approval_status [name: 'idx_restaurants_approval_status']
  }
  
  note: 'Stores restaurant information'
}

Table menu_items {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  name varchar(255) [not null, note: 'Menu item name']
  description text [not null, note: 'Item description']
  price double [not null, note: 'Item price']
  image_url varchar(500) [null, note: 'Item image URL']
  category varchar(50) [not null, note: 'ENUM: APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE, SNACKS, BREAKFAST, LUNCH, DINNER']
  is_available boolean [not null, default: true, note: 'Availability status']
  is_vegetarian boolean [not null, default: false, note: 'Vegetarian flag']
  restaurant_id bigint [not null, ref: > restaurants.id, note: 'Foreign key to restaurants']
  created_at timestamp [not null, default: `now()`, note: 'Item creation time']
  updated_at timestamp [null, note: 'Last update time']
  
  indexes {
    restaurant_id [name: 'idx_menu_items_restaurant_id']
    category [name: 'idx_menu_items_category']
  }
  
  note: 'Stores menu items for each restaurant'
}

Table orders {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  customer_id bigint [not null, ref: > users.id, note: 'Foreign key to users (customer)']
  restaurant_id bigint [not null, ref: > restaurants.id, note: 'Foreign key to restaurants']
  delivery_address_id bigint [not null, ref: > addresses.id, note: 'Foreign key to addresses']
  total_amount double [not null, note: 'Order total amount']
  delivery_fee double [not null, default: 0.0, note: 'Delivery charges']
  tax_amount double [not null, default: 0.0, note: 'Tax amount']
  status varchar(50) [not null, default: 'PENDING', note: 'ENUM: PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED']
  payment_method varchar(50) [not null, note: 'ENUM: CASH, CARD, UPI']
  payment_status varchar(50) [not null, default: 'PENDING', note: 'ENUM: PENDING, COMPLETED, FAILED, REFUNDED']
  special_instructions text [null, note: 'Customer instructions']
  created_at timestamp [not null, default: `now()`, note: 'Order creation time']
  updated_at timestamp [null, note: 'Last update time']
  delivered_at timestamp [null, note: 'Delivery completion time']
  
  indexes {
    customer_id [name: 'idx_orders_customer_id']
    restaurant_id [name: 'idx_orders_restaurant_id']
    status [name: 'idx_orders_status']
    created_at [name: 'idx_orders_created_at']
  }
  
  note: 'Stores customer orders'
}

Table order_items {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  order_id bigint [not null, ref: > orders.id, note: 'Foreign key to orders']
  menu_item_id bigint [not null, ref: > menu_items.id, note: 'Foreign key to menu_items']
  quantity int [not null, note: 'Number of items ordered']
  price double [not null, note: 'Price per item at time of order']
  subtotal double [not null, note: 'Quantity × Price']
  
  indexes {
    order_id [name: 'idx_order_items_order_id']
    menu_item_id [name: 'idx_order_items_menu_item_id']
  }
  
  note: 'Stores individual items within an order (junction table)'
}

Table deliveries {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  order_id bigint [unique, not null, ref: - orders.id, note: 'One-to-one with orders']
  status varchar(50) [not null, default: 'SCHEDULED', note: 'ENUM: SCHEDULED, IN_TRANSIT, DELIVERED, FAILED']
  assigned_driver varchar(255) [null, note: 'Driver name/ID']
  eta_seconds int [null, note: 'Estimated time of arrival in seconds']
  scheduled_at timestamp [null, note: 'Scheduled delivery time']
  updated_at timestamp [null, note: 'Last update time']
  
  indexes {
    order_id [unique, name: 'idx_deliveries_order_id']
  }
  
  note: 'Stores delivery information for orders'
}

Table payments {
  id bigint [pk, increment, note: 'Auto-increment primary key']
  order_id bigint [not null, ref: > orders.id, note: 'Foreign key to orders']
  amount double [not null, note: 'Payment amount']
  payment_method varchar(50) [not null, note: 'ENUM: CASH, CARD, UPI']
  status varchar(50) [not null, default: 'PENDING', note: 'ENUM: PENDING, COMPLETED, FAILED, REFUNDED']
  transaction_id varchar(255) [null, note: 'Payment gateway transaction ID']
  created_at timestamp [not null, default: `now()`, note: 'Payment creation time']
  updated_at timestamp [null, note: 'Last update time']
  
  indexes {
    order_id [name: 'idx_payments_order_id']
    transaction_id [name: 'idx_payments_transaction_id']
  }
  
  note: 'Stores payment transactions for orders'
}

// Relationship documentation
// users 1--* addresses (One user has many addresses)
// users 1--* orders (One customer places many orders)
// users 1--* restaurants (One owner owns many restaurants)
// restaurants 1--* menu_items (One restaurant has many menu items)
// restaurants 1--* orders (One restaurant receives many orders)
// orders 1--* order_items (One order contains many items)
// orders *--1 addresses (Many orders delivered to one address)
// orders 1--1 deliveries (One order has one delivery)
// orders 1--* payments (One order can have multiple payment attempts)
// order_items *--1 menu_items (Many order items reference one menu item)
```

## Database Schema Details

### 1. USERS Table
**Purpose**: Stores user accounts (Customers, Restaurant Owners, Admins)

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `email` (VARCHAR, UNIQUE): User's email address
- `password` (VARCHAR): BCrypt hashed password
- `full_name` (VARCHAR): User's full name
- `phone_number` (VARCHAR): Contact number
- `role` (ENUM): User role - ADMIN, RESTAURANT_OWNER, CUSTOMER
- `is_active` (BOOLEAN): Account status
- `created_at` (TIMESTAMP): Account creation time
- `updated_at` (TIMESTAMP): Last update time

**Relationships**:
- One-to-Many with ADDRESSES (A user can have multiple addresses)
- One-to-Many with ORDERS (A user can place multiple orders)
- One-to-Many with RESTAURANTS (A restaurant owner can own multiple restaurants)

**Indexes**:
- Primary Key: `id`
- Unique Index: `email`

---

### 2. ADDRESSES Table
**Purpose**: Stores delivery addresses for users

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `street` (VARCHAR): Street address
- `city` (VARCHAR): City name
- `state` (VARCHAR): State name
- `zip_code` (VARCHAR): Postal code
- `landmark` (VARCHAR): Optional landmark
- `is_default` (BOOLEAN): Default address flag
- `user_id` (BIGINT, FK): Reference to USERS table

**Relationships**:
- Many-to-One with USERS (Multiple addresses belong to one user)
- One-to-Many with ORDERS (An address can be used for multiple orders)

**Indexes**:
- Primary Key: `id`
- Foreign Key: `user_id` → USERS(id)

---

### 3. RESTAURANTS Table
**Purpose**: Stores restaurant information

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `name` (VARCHAR): Restaurant name
- `description` (VARCHAR): Restaurant description
- `image_url` (VARCHAR): Cover image URL
- `address` (VARCHAR): Physical address
- `city` (VARCHAR): City location
- `phone_number` (VARCHAR): Contact number
- `rating` (DOUBLE): Average rating (0.0-5.0)
- `total_reviews` (INTEGER): Number of reviews
- `is_open` (BOOLEAN): Open/Closed status
- `approval_status` (ENUM): PENDING, APPROVED, REJECTED
- `owner_id` (BIGINT, FK): Reference to USERS table
- `created_at` (TIMESTAMP): Restaurant creation time
- `updated_at` (TIMESTAMP): Last update time

**Relationships**:
- Many-to-One with USERS (Multiple restaurants owned by one owner)
- One-to-Many with MENU_ITEMS (A restaurant has multiple menu items)
- One-to-Many with ORDERS (A restaurant receives multiple orders)

**Indexes**:
- Primary Key: `id`
- Foreign Key: `owner_id` → USERS(id)
- Index: `city` (for location-based searches)

---

### 4. MENU_ITEMS Table
**Purpose**: Stores menu items for each restaurant

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `name` (VARCHAR): Item name
- `description` (VARCHAR): Item description
- `price` (DOUBLE): Item price
- `image_url` (VARCHAR): Item image URL
- `category` (ENUM): APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE, SNACKS, etc.
- `is_available` (BOOLEAN): Availability status
- `is_vegetarian` (BOOLEAN): Vegetarian flag
- `restaurant_id` (BIGINT, FK): Reference to RESTAURANTS table
- `created_at` (TIMESTAMP): Item creation time
- `updated_at` (TIMESTAMP): Last update time

**Relationships**:
- Many-to-One with RESTAURANTS (Multiple menu items belong to one restaurant)
- One-to-Many with ORDER_ITEMS (A menu item can be in multiple orders)

**Indexes**:
- Primary Key: `id`
- Foreign Key: `restaurant_id` → RESTAURANTS(id)
- Index: `category` (for filtering by category)

---

### 5. ORDERS Table
**Purpose**: Stores customer orders

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `customer_id` (BIGINT, FK): Reference to USERS table
- `restaurant_id` (BIGINT, FK): Reference to RESTAURANTS table
- `delivery_address_id` (BIGINT, FK): Reference to ADDRESSES table
- `total_amount` (DOUBLE): Order total amount
- `delivery_fee` (DOUBLE): Delivery charges
- `tax_amount` (DOUBLE): Tax amount
- `status` (ENUM): PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
- `payment_method` (ENUM): CASH, CARD, UPI
- `payment_status` (ENUM): PENDING, COMPLETED, FAILED, REFUNDED
- `special_instructions` (VARCHAR): Customer instructions
- `created_at` (TIMESTAMP): Order creation time
- `updated_at` (TIMESTAMP): Last update time
- `delivered_at` (TIMESTAMP): Delivery completion time

**Relationships**:
- Many-to-One with USERS (Multiple orders placed by one customer)
- Many-to-One with RESTAURANTS (Multiple orders for one restaurant)
- Many-to-One with ADDRESSES (Multiple orders delivered to one address)
- One-to-Many with ORDER_ITEMS (An order contains multiple items)
- One-to-One with DELIVERIES (An order has one delivery)
- One-to-One with PAYMENTS (An order has one payment)

**Indexes**:
- Primary Key: `id`
- Foreign Key: `customer_id` → USERS(id)
- Foreign Key: `restaurant_id` → RESTAURANTS(id)
- Foreign Key: `delivery_address_id` → ADDRESSES(id)
- Index: `status` (for filtering by order status)
- Index: `created_at` (for date-based queries)

---

### 6. ORDER_ITEMS Table
**Purpose**: Stores individual items within an order (junction table)

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `order_id` (BIGINT, FK): Reference to ORDERS table
- `menu_item_id` (BIGINT, FK): Reference to MENU_ITEMS table
- `quantity` (INTEGER): Number of items ordered
- `price` (DOUBLE): Price per item at time of order
- `subtotal` (DOUBLE): Quantity × Price

**Relationships**:
- Many-to-One with ORDERS (Multiple items belong to one order)
- Many-to-One with MENU_ITEMS (Multiple order items reference one menu item)

**Indexes**:
- Primary Key: `id`
- Foreign Key: `order_id` → ORDERS(id)
- Foreign Key: `menu_item_id` → MENU_ITEMS(id)

---

### 7. DELIVERIES Table
**Purpose**: Stores delivery information for orders

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `order_id` (BIGINT, FK, UNIQUE): Reference to ORDERS table
- `status` (ENUM): SCHEDULED, IN_TRANSIT, DELIVERED, FAILED
- `assigned_driver` (VARCHAR): Driver name/ID
- `eta_seconds` (INTEGER): Estimated time of arrival in seconds
- `scheduled_at` (TIMESTAMP): Scheduled delivery time
- `updated_at` (TIMESTAMP): Last update time

**Relationships**:
- One-to-One with ORDERS (Each delivery is for one order)

**Indexes**:
- Primary Key: `id`
- Unique Foreign Key: `order_id` → ORDERS(id)

---

### 8. PAYMENTS Table
**Purpose**: Stores payment transactions for orders

**Columns**:
- `id` (BIGINT, PK): Unique identifier
- `order_id` (BIGINT, FK): Reference to ORDERS table
- `amount` (DOUBLE): Payment amount
- `payment_method` (ENUM): CASH, CARD, UPI
- `status` (ENUM): PENDING, COMPLETED, FAILED, REFUNDED
- `transaction_id` (VARCHAR): Payment gateway transaction ID
- `created_at` (TIMESTAMP): Payment creation time
- `updated_at` (TIMESTAMP): Last update time

**Relationships**:
- One-to-One with ORDERS (Each payment is for one order)

**Indexes**:
- Primary Key: `id`
- Foreign Key: `order_id` → ORDERS(id)
- Index: `transaction_id` (for payment tracking)

---

## Enum Types

### Role (User roles)
```java
public enum Role {
    ADMIN,           // System administrator
    RESTAURANT_OWNER, // Restaurant owner/manager
    CUSTOMER         // Regular customer
}
```

### ApprovalStatus (Restaurant approval)
```java
public enum ApprovalStatus {
    PENDING,   // Awaiting admin approval
    APPROVED,  // Restaurant is active
    REJECTED   // Restaurant application rejected
}
```

### Category (Menu item categories)
```java
public enum Category {
    APPETIZER,      // Starters
    MAIN_COURSE,    // Main dishes
    DESSERT,        // Desserts
    BEVERAGE,       // Drinks
    SNACKS,         // Light snacks
    BREAKFAST,      // Breakfast items
    LUNCH,          // Lunch specials
    DINNER          // Dinner specials
}
```

### OrderStatus (Order lifecycle)
```java
public enum OrderStatus {
    PENDING,           // Order placed, awaiting confirmation
    CONFIRMED,         // Restaurant confirmed the order
    PREPARING,         // Food is being prepared
    OUT_FOR_DELIVERY,  // Order dispatched
    DELIVERED,         // Order delivered successfully
    CANCELLED          // Order cancelled
}
```

### PaymentMethod (Payment types)
```java
public enum PaymentMethod {
    CASH,  // Cash on delivery
    CARD,  // Credit/Debit card
    UPI    // UPI payment
}
```

### PaymentStatus (Payment states)
```java
public enum PaymentStatus {
    PENDING,    // Payment not yet made
    COMPLETED,  // Payment successful
    FAILED,     // Payment failed
    REFUNDED    // Payment refunded
}
```

### DeliveryStatus (Delivery states)
```java
public enum DeliveryStatus {
    SCHEDULED,   // Delivery scheduled
    IN_TRANSIT,  // Out for delivery
    DELIVERED,   // Delivered successfully
    FAILED       // Delivery failed
}
```

---

## Relationship Summary

### One-to-Many Relationships:
1. **USERS → ADDRESSES**: One user can have multiple delivery addresses
2. **USERS → ORDERS**: One customer can place multiple orders
3. **USERS → RESTAURANTS**: One owner can own multiple restaurants
4. **RESTAURANTS → MENU_ITEMS**: One restaurant has multiple menu items
5. **RESTAURANTS → ORDERS**: One restaurant receives multiple orders
6. **ORDERS → ORDER_ITEMS**: One order contains multiple items

### Many-to-One Relationships:
1. **ADDRESSES → USERS**: Many addresses belong to one user
2. **ORDERS → USERS**: Many orders placed by one customer
3. **RESTAURANTS → USERS**: Many restaurants owned by one owner
4. **MENU_ITEMS → RESTAURANTS**: Many menu items belong to one restaurant
5. **ORDERS → RESTAURANTS**: Many orders for one restaurant
6. **ORDERS → ADDRESSES**: Many orders delivered to one address
7. **ORDER_ITEMS → ORDERS**: Many items in one order
8. **ORDER_ITEMS → MENU_ITEMS**: Many order items reference one menu item

### One-to-One Relationships:
1. **ORDERS ↔ DELIVERIES**: Each order has one delivery record
2. **ORDERS ↔ PAYMENTS**: Each order has one payment record

---

## Database Constraints

### Primary Keys:
- All tables have auto-increment BIGINT primary keys named `id`

### Foreign Keys:
- `addresses.user_id` → `users.id` (CASCADE on DELETE)
- `restaurants.owner_id` → `users.id` (CASCADE on DELETE)
- `menu_items.restaurant_id` → `restaurants.id` (CASCADE on DELETE)
- `orders.customer_id` → `users.id` (RESTRICT on DELETE)
- `orders.restaurant_id` → `restaurants.id` (RESTRICT on DELETE)
- `orders.delivery_address_id` → `addresses.id` (RESTRICT on DELETE)
- `order_items.order_id` → `orders.id` (CASCADE on DELETE)
- `order_items.menu_item_id` → `menu_items.id` (RESTRICT on DELETE)
- `deliveries.order_id` → `orders.id` (CASCADE on DELETE)
- `payments.order_id` → `orders.id` (CASCADE on DELETE)

### Unique Constraints:
- `users.email` - Each email must be unique
- `deliveries.order_id` - Each order can have only one delivery

### Not Null Constraints:
- All `id` columns (Primary Keys)
- All foreign key columns
- Essential fields like names, amounts, timestamps
- Status and enum fields with default values

---

## Sample Data Flow

### User Registration Flow:
```
1. New record in USERS table (role: CUSTOMER)
2. Optional: Add records in ADDRESSES table
```

### Restaurant Onboarding Flow:
```
1. New record in USERS table (role: RESTAURANT_OWNER)
2. New record in RESTAURANTS table (approval_status: PENDING)
3. Admin approves: approval_status → APPROVED
4. Add records in MENU_ITEMS table
```

### Order Placement Flow:
```
1. New record in ORDERS table (status: PENDING)
2. Multiple records in ORDER_ITEMS table
3. New record in PAYMENTS table (status: PENDING)
4. Payment completed: payments.status → COMPLETED
5. Order confirmed: orders.status → CONFIRMED
6. New record in DELIVERIES table (status: SCHEDULED)
7. Order lifecycle: PREPARING → OUT_FOR_DELIVERY → DELIVERED
8. Delivery status: SCHEDULED → IN_TRANSIT → DELIVERED
```

---

## Indexes for Performance

### Recommended Indexes:
```sql
-- Users table
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Restaurants table
CREATE INDEX idx_restaurants_city ON restaurants(city);
CREATE INDEX idx_restaurants_owner_id ON restaurants(owner_id);
CREATE INDEX idx_restaurants_approval_status ON restaurants(approval_status);

-- Menu Items table
CREATE INDEX idx_menu_items_restaurant_id ON menu_items(restaurant_id);
CREATE INDEX idx_menu_items_category ON menu_items(category);

-- Orders table
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_restaurant_id ON orders(restaurant_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- Order Items table
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_menu_item_id ON order_items(menu_item_id);

-- Addresses table
CREATE INDEX idx_addresses_user_id ON addresses(user_id);

-- Deliveries table
CREATE UNIQUE INDEX idx_deliveries_order_id ON deliveries(order_id);

-- Payments table
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
```

---

## Database Size Estimates (Sample Data)

Based on current initialization:
- **USERS**: 3 rows (admin, owner, customer)
- **ADDRESSES**: 0 rows (to be added by users)
- **RESTAURANTS**: 3 rows (Pizza Palace, Burger Hub, Sushi Express)
- **MENU_ITEMS**: 11 rows (4 + 4 + 3 across restaurants)
- **ORDERS**: 0 rows (to be created by customers)
- **ORDER_ITEMS**: 0 rows (dependent on orders)
- **DELIVERIES**: 0 rows (dependent on orders)
- **PAYMENTS**: 0 rows (dependent on orders)

**Total Initial Records**: 17 rows across all tables

---

## Technologies Used

- **ORM**: Hibernate (JPA)
- **Database**: H2 (Development), PostgreSQL/MySQL (Production)
- **Migration Tool**: Liquibase/Flyway (optional)
- **Connection Pool**: HikariCP (Spring Boot default)

---

## Viewing the Diagram

This Mermaid diagram can be viewed in:
1. **GitHub**: Automatic rendering in GitHub Markdown
2. **VS Code**: Install "Markdown Preview Mermaid Support" extension
3. **Online**: https://mermaid.live/
4. **IntelliJ IDEA**: Built-in Mermaid support
5. **Documentation Sites**: GitBook, Confluence, etc.

---

*Last Updated: October 29, 2025*
*Database Version: 1.0*
*Application: K-MATO Food Delivery System*
