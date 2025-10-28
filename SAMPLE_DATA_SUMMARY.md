# Sample Data Initialization Summary

## Overview
The `DataInitializer.java` has been enhanced to automatically create sample restaurants and menu items along with default user accounts when the application starts up. This provides immediate data for testing and demonstration purposes.

## What Gets Created

### 1. Default Users (3 accounts)
- **Admin User**
  - Email: `admin@kmato.com`
  - Password: `Admin@123`
  - Role: ADMIN
  - Phone: 9999999999

- **Restaurant Owner**
  - Email: `owner@kmato.com`
  - Password: `Owner@123`
  - Role: RESTAURANT_OWNER
  - Phone: 8888888888

- **Sample Customer**
  - Email: `customer@kmato.com`
  - Password: `Customer@123`
  - Role: CUSTOMER
  - Phone: 7777777777

### 2. Sample Restaurants (3 establishments)

#### 🍕 Pizza Palace
- **Description**: Authentic Italian pizzas with fresh ingredients
- **Location**: 123 Main St, Mumbai
- **Phone**: 9988776655
- **Rating**: 4.5 ⭐ (120 reviews)
- **Status**: APPROVED, Open
- **Owner**: owner@kmato.com

#### 🍔 Burger Hub
- **Description**: Juicy burgers and crispy fries
- **Location**: 456 Park Ave, Mumbai
- **Phone**: 8877665544
- **Rating**: 4.2 ⭐ (85 reviews)
- **Status**: APPROVED, Open
- **Owner**: owner@kmato.com

#### 🍣 Sushi Express
- **Description**: Fresh sushi and Japanese cuisine
- **Location**: 789 Ocean Blvd, Mumbai
- **Phone**: 7766554433
- **Rating**: 4.8 ⭐ (200 reviews)
- **Status**: APPROVED, Open
- **Owner**: owner@kmato.com

### 3. Menu Items (11 items)

#### Pizza Palace Menu Items (4 items)
| Item | Price | Category | Vegetarian |
|------|-------|----------|------------|
| Margherita Pizza | ₹299 | MAIN_COURSE | ✅ Yes |
| Pepperoni Pizza | ₹399 | MAIN_COURSE | ❌ No |
| Veggie Supreme | ₹349 | MAIN_COURSE | ✅ Yes |
| Garlic Bread | ₹99 | SNACKS | ✅ Yes |

#### Burger Hub Menu Items (4 items)
| Item | Price | Category | Vegetarian |
|------|-------|----------|------------|
| Classic Burger | ₹199 | MAIN_COURSE | ❌ No |
| Cheese Burger | ₹249 | MAIN_COURSE | ❌ No |
| Veggie Burger | ₹179 | MAIN_COURSE | ✅ Yes |
| French Fries | ₹79 | SNACKS | ✅ Yes |

#### Sushi Express Menu Items (3 items)
| Item | Price | Category | Vegetarian |
|------|-------|----------|------------|
| California Roll | ₹349 | MAIN_COURSE | ❌ No |
| Salmon Nigiri | ₹299 | MAIN_COURSE | ❌ No |
| Vegetable Tempura | ₹249 | APPETIZER | ✅ Yes |

## Database Schema

### Users Table
```sql
ID | EMAIL | PASSWORD | FULL_NAME | PHONE_NUMBER | ROLE | IS_ACTIVE | CREATED_AT
```

### Restaurants Table
```sql
ID | ADDRESS | APPROVAL_STATUS | CITY | CREATED_AT | DESCRIPTION | IMAGE_URL | IS_OPEN | NAME | PHONE_NUMBER | RATING | TOTAL_REVIEWS | OWNER_ID
```

### Menu Items Table
```sql
ID | CATEGORY | CREATED_AT | DESCRIPTION | IMAGE_URL | IS_AVAILABLE | IS_VEGETARIAN | NAME | PRICE | RESTAURANT_ID
```

## Key Features

### Intelligent Initialization
- **Idempotent**: Checks if data already exists before creating (won't duplicate on restart)
- **Linked Relationships**: Properly associates menu items with their restaurants
- **Approved Status**: All restaurants are pre-approved for immediate use
- **Realistic Data**: Uses actual Unsplash image URLs for professional appearance

### Entity Relationships
```
User (RESTAURANT_OWNER)
  └── Restaurant (3 restaurants)
       └── MenuItem (11 menu items total)
```

### Enums Used
- **Role**: ADMIN, RESTAURANT_OWNER, CUSTOMER
- **ApprovalStatus**: APPROVED (all restaurants)
- **Category**: MAIN_COURSE, SNACKS, APPETIZER

## Technical Implementation

### File Location
```
Backend/src/main/java/com/foodordering/config/DataInitializer.java
```

### Dependencies Injected
- `UserRepository` - For user management
- `RestaurantRepository` - For restaurant management
- `MenuItemRepository` - For menu item management
- `PasswordEncoder` - For secure password hashing (BCrypt)

### Builder Pattern Usage
All entities use Lombok's `@Builder` annotation for clean, readable object creation:

```java
Restaurant pizzaPalace = Restaurant.builder()
    .name("Pizza Palace")
    .description("Authentic Italian pizzas with fresh ingredients")
    .rating(4.5)
    .totalReviews(120)
    .approvalStatus(ApprovalStatus.APPROVED)
    .owner(owner)
    .build();
```

### Logging
The initializer provides detailed console logging:
- ✅ Success messages for each created entity
- ℹ️  Info messages when data already exists
- ⚠️  Warning messages if any issues occur
- 🚀 Final startup confirmation

## Console Output Example

```
========================================
STEP 1: Create Default Users
========================================
✅ Default admin user created successfully!
📧 Email: admin@kmato.com
🔑 Password: Admin@123
✅ Sample restaurant owner created!
📧 Email: owner@kmato.com
🔑 Password: Owner@123
✅ Sample customer created!
📧 Email: customer@kmato.com
🔑 Password: Customer@123
========================================
🍽️  Creating sample restaurants...
========================================
✅ Created: Pizza Palace
✅ Created: Burger Hub
✅ Created: Sushi Express
========================================
🍕 Creating menu items for Pizza Palace...
========================================
  ✅ Margherita Pizza - ₹299
  ✅ Pepperoni Pizza - ₹399
  ✅ Veggie Supreme - ₹349
  ✅ Garlic Bread - ₹99
========================================
🍔 Creating menu items for Burger Hub...
========================================
  ✅ Classic Burger - ₹199
  ✅ Cheese Burger - ₹249
  ✅ Veggie Burger - ₹179
  ✅ French Fries - ₹79
========================================
🍣 Creating menu items for Sushi Express...
========================================
  ✅ California Roll - ₹349
  ✅ Salmon Nigiri - ₹299
  ✅ Vegetable Tempura - ₹249
========================================
✅ Successfully created 11 menu items!
========================================
🚀 Application ready with sample data!
========================================
```

## Testing the Sample Data

### 1. Login as Admin
```
URL: http://localhost:8081/login
Email: admin@kmato.com
Password: Admin@123
```
- View all restaurants
- Approve/reject restaurant applications
- Manage users

### 2. Login as Restaurant Owner
```
URL: http://localhost:8081/login
Email: owner@kmato.com
Password: Owner@123
```
- View owned restaurants (3 restaurants)
- Manage menu items (11 existing items)
- Update restaurant details

### 3. Login as Customer
```
URL: http://localhost:8081/login
Email: customer@kmato.com
Password: Customer@123
```
- Browse restaurants (3 available)
- View menu items (11 items across restaurants)
- Place orders

### 4. API Testing with Postman

**Get All Restaurants:**
```http
GET http://localhost:8081/api/restaurants
Authorization: Bearer <your_jwt_token>
```

**Get Menu Items by Restaurant:**
```http
GET http://localhost:8081/api/menu-items/restaurant/1
Authorization: Bearer <your_jwt_token>
```

**Get All Menu Items:**
```http
GET http://localhost:8081/api/menu-items
Authorization: Bearer <your_jwt_token>
```

## Benefits

### For Development
- ✅ **Immediate Testing**: No need to manually create data through UI/API
- ✅ **Consistent Data**: Same data on every fresh deployment
- ✅ **Relationship Testing**: Properly linked entities for testing joins
- ✅ **Demo Ready**: Professional-looking data with real images

### For Deployment
- ✅ **Fresh Environments**: Automatically populates new databases
- ✅ **Docker Compatible**: Works seamlessly in containerized deployments
- ✅ **Cloud Ready**: Perfect for AWS/Azure/GCP deployments

### For CI/CD
- ✅ **Integration Tests**: Predictable data for automated tests
- ✅ **E2E Testing**: Complete user journeys possible immediately
- ✅ **Performance Testing**: Can test with known dataset

## Deployment Impact

When you redeploy the application:

1. **GitHub Actions** will build the new Docker image with updated code
2. **Docker Image** will be pushed to Docker Hub
3. **EC2 Instance** will pull and run the new image
4. **On Startup**, DataInitializer will:
   - Create 3 users (if not exist)
   - Create 3 restaurants (if not exist)
   - Create 11 menu items (if not exist)
5. **Application Ready** with full sample data

## Current Deployment

Your application is deployed at:
- **Frontend**: http://54.91.72.53
- **Backend**: http://54.91.72.53:8081

After Docker containers start (5-7 minutes), all sample data will be available automatically!

## Git Commit History

```bash
commit be724e8
Author: Your Name
Date: Today

    Add sample restaurants and menu items to DataInitializer
    
    - Added 3 sample restaurants (Pizza Palace, Burger Hub, Sushi Express)
    - Added 11 menu items across all restaurants
    - Properly linked menu items to restaurants
    - All restaurants pre-approved and open
    - Used realistic images from Unsplash
    - Implemented idempotent initialization
```

## Future Enhancements

Potential additions to DataInitializer:

- 🔄 **Sample Orders**: Pre-create some completed orders for dashboard
- 📍 **Sample Addresses**: Add delivery addresses for the customer
- ⭐ **Sample Reviews**: Add reviews and ratings from customers
- 🚗 **Sample Deliveries**: Create delivery records for testing
- 🏷️ **Categories**: More diverse menu categories (DESSERT, BEVERAGE)
- 🌍 **Multiple Cities**: Restaurants in different cities
- 💰 **Promotions**: Sample discount codes and offers

---

**Note**: This sample data is perfect for development, testing, and demonstration. For production, you may want to disable automatic data initialization or use a different mechanism for seeding production data.
