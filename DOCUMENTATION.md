# K-MATO Food Delivery Application - Complete Development & Build Documentation

## 🚀 Quick Start - Default Login Credentials

The application comes with pre-configured user accounts for immediate testing:

### **Admin Account**
```
Email: admin@kmato.com
Password: Admin@123
Role: ADMIN
Access: Full system administration
```

### **Restaurant Owner Account**
```
Email: owner@kmato.com
Password: Owner@123
Role: RESTAURANT_OWNER
Access: Restaurant and menu management
```

### **Customer Account**
```
Email: customer@kmato.com
Password: Customer@123
Role: CUSTOMER
Access: Browse restaurants, place orders
```

⚠️ **Important:** Change these passwords immediately in production environments!

---

## Table of Contents

### Part I: Project Development Journey
1. [Project Overview & Requirements](#project-overview--requirements)
2. [Initial Project Setup](#initial-project-setup)
3. [Database Design & Entity Modeling](#database-design--entity-modeling)
4. [Backend Development Process](#backend-development-process)
5. [Frontend Development Process](#frontend-development-process)
6. [Feature Implementation Timeline](#feature-implementation-timeline)

### Part II: Build & Deployment
7. [Backend Build Process](#backend-build-process)
8. [Frontend Build Process](#frontend-build-process)
9. [JWT Implementation - Backend](#jwt-implementation-backend)
10. [JWT Implementation - Frontend](#jwt-implementation-frontend)
11. [Docker Build Process](#docker-build-process)
12. [CI/CD Pipeline](#cicd-pipeline)
13. [Deployment](#deployment)
14. [Security Best Practices](#security-best-practices)

### Part III: Testing & Maintenance
15. [Testing Strategy](#testing-strategy)
16. [Troubleshooting Guide](#troubleshooting-guide)
17. [Performance Optimization](#performance-optimization)
18. [Future Enhancements](#future-enhancements)

---

# PART I: PROJECT DEVELOPMENT JOURNEY

## Project Overview & Requirements

### Project Vision
**K-MATO** is a comprehensive food delivery platform connecting customers, restaurants, and administrators in a seamless online ordering ecosystem.

### Business Requirements

#### **User Stories**

**As a Customer:**
- I want to browse restaurants by location
- I want to view restaurant menus with prices and descriptions
- I want to add items to my shopping cart
- I want to place orders with delivery details
- I want to track my order status
- I want to view my order history
- I want to register and manage my profile

**As a Restaurant Owner:**
- I want to manage my restaurant profile
- I want to add, edit, and delete menu items
- I want to receive and manage customer orders
- I want to update order status (preparing, ready, delivered)
- I want to view order analytics

**As an Administrator:**
- I want to manage all users (customers, owners)
- I want to approve/reject restaurant registrations
- I want to monitor platform activity
- I want to manage categories and system settings

### Technical Requirements

#### **Functional Requirements**
1. User authentication and authorization (JWT-based)
2. Role-based access control (Customer, Restaurant Owner, Admin)
3. Restaurant CRUD operations
4. Menu item management
5. Shopping cart functionality
6. Order placement and tracking
7. Real-time order status updates
8. Search and filter restaurants
9. Responsive UI for mobile and desktop

#### **Non-Functional Requirements**
1. **Performance**: API response time < 500ms
2. **Scalability**: Support 1000+ concurrent users
3. **Security**: Secure authentication, encrypted passwords, HTTPS
4. **Availability**: 99.9% uptime
5. **Maintainability**: Clean code, documentation, automated tests


### Technology Stack Selection

#### **Backend Technology Choices**

**Why Spring Boot?**
- Rapid development with auto-configuration
- Robust ecosystem (Spring Security, Spring Data JPA)
- Production-ready features (metrics, health checks)
- Excellent documentation and community support
- Enterprise-grade reliability

**Why Java 17?**
- Long-term support (LTS) version
- Performance improvements over Java 11
- Modern language features (records, sealed classes, pattern matching)
- Better garbage collection

**Why H2 Database (Development)?**
- Zero configuration
- Fast setup for development
- File-based persistence
- Easy to reset and test

**Why JWT for Authentication?**
- Stateless authentication (scalable)
- No server-side session storage
- Works well with microservices
- Mobile-friendly
- Supports distributed systems

#### **Frontend Technology Choices**

**Why Angular 17?**
- Full-featured framework (routing, forms, HTTP)
- TypeScript for type safety
- Component-based architecture
- Powerful CLI for scaffolding
- RxJS for reactive programming
- Built-in dependency injection

**Why TypeScript?**
- Type safety reduces runtime errors
- Better IDE support (autocomplete, refactoring)
- Enhanced code maintainability
- Easier refactoring in large projects

#### **DevOps Technology Choices**

**Why Docker?**
- Consistent environments (dev, staging, prod)
- Easy deployment and scaling
- Isolated dependencies
- Multi-stage builds reduce image size

**Why GitHub Actions?**
- Native GitHub integration
- Free for public repositories
- Easy YAML-based configuration
- Parallel job execution

**Why Terraform?**
- Infrastructure as Code (IaC)
- Version control infrastructure
- Reproducible environments
- Multi-cloud support

**Why AWS EC2?**
- Flexible compute capacity
- Pay-as-you-go pricing
- Global infrastructure
- Easy to scale

---

## Initial Project Setup

### Phase 1: Development Environment Setup

#### **1. Install Required Tools**

**Windows (PowerShell):**
```powershell
# Install Chocolatey (package manager)
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Java 17
choco install openjdk17 -y

# Install Maven
choco install maven -y

# Install Node.js
choco install nodejs-lts -y

# Install Angular CLI
npm install -g @angular/cli@17

# Install Docker Desktop
choco install docker-desktop -y

# Install Git
choco install git -y

# Install Terraform
choco install terraform -y

# Verify installations
java -version
mvn -version
node -version
npm -version
ng version
docker --version
terraform --version
```

**Linux/Mac:**
```bash
# Java 17
sudo apt update
sudo apt install openjdk-17-jdk -y

# Maven
sudo apt install maven -y

# Node.js (using nvm)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install 18
nvm use 18

# Angular CLI
npm install -g @angular/cli@17

# Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Terraform
wget https://releases.hashicorp.com/terraform/1.5.0/terraform_1.5.0_linux_amd64.zip
unzip terraform_1.5.0_linux_amd64.zip
sudo mv terraform /usr/local/bin/
```

#### **2. IDE Setup**

**IntelliJ IDEA (Backend):**
```
1. Download IntelliJ IDEA Community Edition
2. Install Plugins:
   - Spring Boot
   - Lombok
   - Maven Helper
   - Docker
   - Database Navigator
3. Configure JDK 17
4. Set Maven home directory
5. Enable annotation processing (for Lombok)
```

**VS Code (Frontend):**
```
Extensions to install:
1. Angular Language Service
2. ESLint
3. Prettier
4. Angular Snippets
5. TypeScript Hero
6. Path Intellisense
7. Auto Rename Tag
8. Bracket Pair Colorizer
9. GitLens
10. Docker
```

### Phase 2: Project Initialization

#### **Backend Project Creation**

**Using Spring Initializr:**
```bash
# Method 1: Using web interface
# Visit: https://start.spring.io
# Select:
# - Project: Maven
# - Language: Java
# - Spring Boot: 3.5.7
# - Packaging: Jar
# - Java: 17
# - Dependencies: Web, JPA, Security, H2, Validation, Lombok

# Method 2: Using CLI
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,security,h2,validation,lombok \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=3.5.7 \
  -d groupId=com.foodordering \
  -d artifactId=Fooddelivery \
  -d name=Fooddelivery \
  -d packageName=com.foodordering \
  -d javaVersion=17 \
  -o fooddelivery.zip

unzip fooddelivery.zip
cd Fooddelivery
```

**Project Structure Created:**
```
Fooddelivery/
├── src/
│   ├── main/
│   │   ├── java/com/foodordering/
│   │   │   └── FooddeliveryApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .gitignore
```

#### **Frontend Project Creation**

```bash
# Create Angular project
ng new kmato-app

# Configuration prompts:
# ? Would you like to add Angular routing? Yes
# ? Which stylesheet format would you like to use? CSS

cd kmato-app

# Generate initial structure
ng generate module shared
ng generate module core
ng generate service core/services/api
ng generate service core/services/auth
ng generate guard core/guards/auth
ng generate interceptor core/interceptors/auth

# Generate components
ng generate component components/auth/login
ng generate component components/auth/register
ng generate component components/restaurant/restaurant-list
ng generate component components/restaurant/restaurant-detail
ng generate component components/menu/menu-item-list
ng generate component components/cart/cart
ng generate component components/order/order-list
ng generate component components/order/checkout

# Generate services
ng generate service services/restaurant
ng generate service services/menu
ng generate service services/cart
ng generate service services/order
```

### Phase 3: Version Control Setup

```bash
# Initialize Git repository
git init

# Create .gitignore
cat > .gitignore << 'EOF'
# Backend
target/
.mvn/
*.class
*.log
*.jar
*.war

# Frontend
node_modules/
dist/
.angular/

# IDE
.idea/
.vscode/
*.iml

# OS
.DS_Store
Thumbs.db

# Environment
.env
*.env.local

# Docker
*.tar
EOF

# Initial commit
git add .
git commit -m "Initial project setup"

# Connect to GitHub
git remote add origin https://github.com/palanikalyan/K-MATO.git
git branch -M main
git push -u origin main
```

---

## Database Design & Entity Modeling

### Entity Relationship Diagram (ERD)

```
┌─────────────────┐
│      User       │
├─────────────────┤
│ id (PK)         │
│ email           │
│ password        │
│ firstName       │
│ lastName        │
│ phoneNumber     │
│ role (ENUM)     │
│ isActive        │
│ createdAt       │
└─────────────────┘
        │
        │ 1:1 (if role = OWNER)
        ▼
┌─────────────────┐
│   Restaurant    │
├─────────────────┤
│ id (PK)         │
│ name            │
│ description     │
│ address         │
│ city            │
│ phone           │
│ imageUrl        │
│ rating          │
│ isActive        │
│ ownerId (FK)    │
└─────────────────┘
        │
        │ 1:N
        ▼
┌─────────────────┐
│   MenuItem      │
├─────────────────┤
│ id (PK)         │
│ name            │
│ description     │
│ price           │
│ category        │
│ imageUrl        │
│ isAvailable     │
│ restaurantId(FK)│
└─────────────────┘
        │
        │ M:N (via OrderItem)
        ▼
┌─────────────────┐      ┌─────────────────┐
│     Order       │      │   OrderItem     │
├─────────────────┤      ├─────────────────┤
│ id (PK)         │◄─────│ orderId (FK)    │
│ orderNumber     │      │ menuItemId (FK) │
│ totalAmount     │      │ quantity        │
│ status (ENUM)   │      │ price           │
│ deliveryAddress │      │ subtotal        │
│ customerId (FK) │      └─────────────────┘
│ restaurantId(FK)│
│ orderDate       │
└─────────────────┘
```

### Database Schema Creation

#### **Step 1: Create Entity Classes**

**User Entity:**
```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    private Role role; // CUSTOMER, RESTAURANT_OWNER, ADMIN
    
    private Boolean isActive = true;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

**Restaurant Entity:**
```java
@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String address;
    private String city;
    private String phone;
    private String imageUrl;
    private Double rating = 0.0;
    private Boolean isActive = true;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;
}
```

**MenuItem Entity:**
```java
@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private Double price;
    private String category;
    private String imageUrl;
    private Boolean isAvailable = true;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
```

**Order Entity:**
```java
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String orderNumber;
    
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, CONFIRMED, PREPARING, READY, DELIVERED
    
    private String deliveryAddress;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    
    @CreationTimestamp
    private LocalDateTime orderDate;
}
```

**OrderItem Entity:**
```java
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
    
    private Integer quantity;
    private Double price;
    private Double subtotal;
}
```

#### **Step 2: Create Repositories**

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCity(String city);
    List<Restaurant> findByIsActiveTrue();
}

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurantId(Long restaurantId);
    List<MenuItem> findByCategory(String category);
}

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    Optional<Order> findByOrderNumber(String orderNumber);
}
```

---

## Backend Development Process

### Development Workflow

#### **Layer-by-Layer Development Approach**

**Layer 1: Entities & Repositories (Data Layer)**
```
Week 1: Days 1-2
✓ Created User, Restaurant, MenuItem, Order, OrderItem entities
✓ Defined relationships (@OneToMany, @ManyToOne)
✓ Created JPA repositories
✓ Configured H2 database
✓ Tested with sample data
```

**Layer 2: DTOs & Mappers (Transfer Layer)**
```
Week 1: Days 3-4
✓ Created DTOs for request/response
✓ Implemented MapStruct mappers
✓ Validated DTO fields with annotations
✓ Created custom validators
```

**Layer 3: Services (Business Logic Layer)**
```
Week 2: Days 1-3
✓ Implemented AuthService (login, register)
✓ Implemented RestaurantService (CRUD)
✓ Implemented MenuItemService (CRUD)
✓ Implemented OrderService (create, update status)
✓ Added business validations
✓ Implemented transaction management
```

**Layer 4: Controllers (API Layer)**
```
Week 2: Days 4-5
✓ Created REST endpoints
✓ Implemented exception handling
✓ Added API documentation (Swagger)
✓ Tested with Postman
```

**Layer 5: Security (Authentication Layer)**
```
Week 3: Days 1-2
✓ Implemented JWT token provider
✓ Created authentication filter
✓ Configured Spring Security
✓ Added role-based authorization
✓ Tested authentication flow
```

### Development Best Practices Applied

#### **1. Code Organization**
```
com.foodordering/
├── controller/       # REST endpoints
├── service/         # Business logic
├── repository/      # Data access
├── entity/          # Database entities
├── dto/             # Data transfer objects
├── mapper/          # Entity-DTO conversion
├── security/        # JWT & authentication
├── exception/       # Custom exceptions
├── config/          # Configuration classes
└── util/           # Utility classes
```

#### **2. Exception Handling**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

#### **3. API Response Standardization**
```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    
    // Success response
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }
    
    // Error response
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
```

---

## Frontend Development Process

### Development Workflow

#### **Phase 1: Project Structure (Week 1)**

```
Day 1: Setup & Configuration
✓ Created Angular project
✓ Configured routing
✓ Set up environment files
✓ Installed dependencies

Day 2: Shared Module
✓ Created reusable components (header, footer, loader)
✓ Created pipes (currency, date formatting)
✓ Created directives

Day 3: Core Module
✓ Created services (API, Auth)
✓ Created interceptors (Auth, Error)
✓ Created guards (Auth, Role)
✓ Created models/interfaces
```

#### **Phase 2: Feature Modules (Week 2-3)**

**Authentication Module (Week 2, Days 1-2):**
```typescript
// Components created
- LoginComponent
- RegisterComponent

// Features implemented
✓ Login form with validation
✓ Register form with role selection
✓ Error handling and display
✓ Redirect after successful login
✓ Remember me functionality
```

**Restaurant Module (Week 2, Days 3-5):**
```typescript
// Components created
- RestaurantListComponent
- RestaurantDetailComponent
- RestaurantCardComponent

// Features implemented
✓ Display restaurant list with grid/list view
✓ Search restaurants by name
✓ Filter by city
✓ View restaurant details
✓ Display menu items
✓ Add items to cart
✓ Restaurant ratings display
```

**Cart Module (Week 3, Days 1-2):**
```typescript
// Components created
- CartComponent
- CartItemComponent

// Features implemented
✓ Add/remove items from cart
✓ Update item quantities
✓ Calculate totals
✓ Persist cart in localStorage
✓ Clear cart functionality
```

**Order Module (Week 3, Days 3-5):**
```typescript
// Components created
- CheckoutComponent
- OrderListComponent
- OrderDetailComponent

// Features implemented
✓ Checkout process
✓ Delivery address form
✓ Order placement
✓ View order history
✓ Track order status
✓ Real-time status updates
```

#### **Phase 3: Admin Module (Week 4)**

```typescript
// Components created
- AdminDashboardComponent
- UserManagementComponent
- RestaurantManagementComponent

// Features implemented
✓ Admin dashboard with statistics
✓ User management (CRUD)
✓ Restaurant approval workflow
✓ System monitoring
```

### Frontend Architecture Patterns

#### **1. Service Layer Pattern**
```typescript
@Injectable({ providedIn: 'root' })
export class RestaurantService {
  private apiUrl = `${environment.apiUrl}/restaurants`;
  
  constructor(private http: HttpClient) {}
  
  getAll(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.apiUrl);
  }
  
  getById(id: number): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.apiUrl}/${id}`);
  }
}
```

#### **2. State Management (Service with BehaviorSubject)**
```typescript
@Injectable({ providedIn: 'root' })
export class CartService {
  private cartItems = new BehaviorSubject<CartItem[]>([]);
  public cartItems$ = this.cartItems.asObservable();
  
  addItem(item: MenuItem) {
    const current = this.cartItems.value;
    const existing = current.find(i => i.menuItem.id === item.id);
    
    if (existing) {
      existing.quantity++;
    } else {
      current.push({ menuItem: item, quantity: 1 });
    }
    
    this.cartItems.next(current);
    this.saveToStorage();
  }
}
```

#### **3. Reactive Forms Pattern**
```typescript
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  
  ngOnInit() {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      role: ['CUSTOMER', Validators.required]
    });
  }
  
  onSubmit() {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe(...);
    }
  }
}
```

---

## Feature Implementation Timeline

### Sprint 1: Foundation (Week 1-2)
**Backend:**
- ✅ Project setup with Spring Boot
- ✅ Database entities and relationships
- ✅ Repository layer implementation
- ✅ H2 database configuration

**Frontend:**
- ✅ Angular project initialization
- ✅ Routing setup
- ✅ Environment configuration
- ✅ Shared components (header, footer)

### Sprint 2: Authentication (Week 3-4)
**Backend:**
- ✅ JWT token provider
- ✅ Security configuration
- ✅ Login/Register endpoints
- ✅ Password encryption (BCrypt)

**Frontend:**
- ✅ Login component
- ✅ Register component
- ✅ Auth service
- ✅ Auth interceptor
- ✅ Auth guard

**Bug Fixes:**
- 🐛 Fixed: "No enum constant Role.USER" error
  - **Problem**: Frontend sending 'USER' but backend expects 'CUSTOMER'
  - **Solution**: Changed default role to 'CUSTOMER' in register component
  - **Commit**: 3524ad6

- 🐛 Fixed: Phone field mapping issue
  - **Problem**: Frontend sending 'phone' but backend DTO expects 'phoneNumber'
  - **Solution**: Updated DTO field name to match backend
  - **Commit**: 3524ad6

### Sprint 3: Restaurant Management (Week 5-6)
**Backend:**
- ✅ Restaurant CRUD operations
- ✅ Restaurant search and filtering
- ✅ Owner-restaurant relationship
- ✅ Restaurant approval workflow

**Frontend:**
- ✅ Restaurant list page
- ✅ Restaurant detail page
- ✅ Restaurant search
- ✅ City-based filtering

### Sprint 4: Menu Management (Week 7-8)
**Backend:**
- ✅ Menu item CRUD operations
- ✅ Category management
- ✅ Availability toggle

**Frontend:**
- ✅ Menu item list
- ✅ Menu item detail
- ✅ Add to cart functionality

### Sprint 5: Order Management (Week 9-10)
**Backend:**
- ✅ Order creation
- ✅ Order status updates
- ✅ Order history
- ✅ Order number generation

**Frontend:**
- ✅ Shopping cart
- ✅ Checkout process
- ✅ Order placement
- ✅ Order tracking

**Improvements:**
- ✅ Simplified checkout (removed payment processing)
  - **Reason**: Focus on core functionality first
  - **Commit**: 3524ad6

### Sprint 6: Admin Panel (Week 11-12)
**Backend:**
- ✅ Admin endpoints
- ✅ User management
- ✅ Restaurant approval

**Frontend:**
- ✅ Admin dashboard
- ✅ User management UI
- ✅ Restaurant approval UI

---

# PART II: BUILD & DEPLOYMENT

## Backend Build Process

---

## Backend Build Process

### Prerequisites
- **Java Development Kit (JDK)**: Version 17
- **Maven**: Version 3.9.4 or higher
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Project Structure
```
Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/foodordering/
│   │   │       ├── controller/      # REST API endpoints
│   │   │       ├── service/         # Business logic
│   │   │       ├── repository/      # Data access layer
│   │   │       ├── entity/          # JPA entities
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── security/        # JWT & Security config
│   │   │       ├── enums/           # Enums (Role, OrderStatus)
│   │   │       ├── exception/       # Custom exceptions
│   │   │       └── mapper/          # Entity-DTO mappers
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── Dockerfile
```

### Step-by-Step Build Process

#### 1. **Maven Dependencies Configuration** (`pom.xml`)

Key dependencies:
```xml
<!-- Spring Boot Core -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT Libraries (JJWT) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

#### 2. **Local Development Build**

**Clean and compile:**
```bash
cd Backend
mvn clean compile
```
- `clean`: Removes previous build artifacts from `target/` directory
- `compile`: Compiles Java source files to bytecode

**Run tests:**
```bash
mvn test
```
- Executes all unit tests in `src/test/java/`
- Generates test reports in `target/surefire-reports/`

**Package application:**
```bash
mvn package
```
- Compiles code
- Runs tests
- Creates JAR file: `target/Fooddelivery-0.0.1-SNAPSHOT.jar`

**Skip tests (faster build):**
```bash
mvn package -DskipTests
```

**Run application:**
```bash
java -jar target/Fooddelivery-0.0.1-SNAPSHOT.jar
```
Or using Maven:
```bash
mvn spring-boot:run
```

Application starts on: `http://localhost:8081`

#### 3. **Build Phases Explained**

Maven follows these lifecycle phases:
1. **validate**: Validates project structure
2. **compile**: Compiles source code (`src/main/java/`)
3. **test**: Runs unit tests (`src/test/java/`)
4. **package**: Creates JAR file
5. **verify**: Runs integration tests
6. **install**: Installs JAR to local Maven repository (`~/.m2/repository/`)
7. **deploy**: Deploys to remote repository (not used in this project)

#### 4. **Configuration** (`application.properties`)

```properties
# Application name
spring.application.name=Fooddelivery

# Server port
server.port=8081

# JWT Configuration
jwt.secret=${JWT_SECRET:ChangeMeToASecureRandomStringWithSufficientLength012345}
jwt.expiration=${JWT_EXPIRATION:3600000}  # 1 hour in milliseconds

# Database Configuration (H2 for development)
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:file:./data/fooddelivery}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.datasource.driver-class-name=org.h2.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# H2 Console (development only)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Environment variables override defaults:**
- `JWT_SECRET`: Secret key for JWT signing
- `JWT_EXPIRATION`: Token expiration time
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

---

## Frontend Build Process

### Prerequisites
- **Node.js**: Version 18 or higher
- **npm**: Version 9 or higher
- **Angular CLI**: Version 17.3.8

### Project Structure
```
frontend/kmato-app/
├── src/
│   ├── app/
│   │   ├── components/          # UI components
│   │   │   ├── auth/           # Login, register
│   │   │   ├── restaurant/     # Restaurant list, details
│   │   │   ├── menu/           # Menu items
│   │   │   ├── cart/           # Shopping cart
│   │   │   └── order/          # Order management
│   │   ├── services/           # HTTP services
│   │   │   ├── auth.service.ts
│   │   │   ├── api.service.ts
│   │   │   └── cart.service.ts
│   │   ├── interceptors/       # HTTP interceptors
│   │   │   └── auth.interceptor.ts
│   │   ├── guards/             # Route guards
│   │   └── models/             # TypeScript interfaces
│   ├── environments/
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   └── index.html
├── package.json
├── angular.json
├── tsconfig.json
└── Dockerfile
```

### Step-by-Step Build Process

#### 1. **Install Angular CLI** (if not installed)
```bash
npm install -g @angular/cli@17.3.8
```

#### 2. **Install Dependencies** (`package.json`)

Key dependencies:
```json
{
  "dependencies": {
    "@angular/animations": "^17.3.0",
    "@angular/common": "^17.3.0",
    "@angular/compiler": "^17.3.0",
    "@angular/core": "^17.3.0",
    "@angular/forms": "^17.3.0",
    "@angular/platform-browser": "^17.3.0",
    "@angular/platform-browser-dynamic": "^17.3.0",
    "@angular/router": "^17.3.0",
    "rxjs": "~7.8.0",
    "tslib": "^2.3.0",
    "zone.js": "~0.14.3"
  }
}
```

**Install dependencies:**
```bash
cd frontend/kmato-app
npm install
```
or (clean install, deletes node_modules first):
```bash
npm ci
```

#### 3. **Development Server**

**Start dev server:**
```bash
npm start
# or
ng serve
```

Application runs on: `http://localhost:4200`

**Features:**
- Live reload on file changes
- Source maps for debugging
- Hot module replacement

**Custom port:**
```bash
ng serve --port 4300
```

#### 4. **Production Build**

**Build for production:**
```bash
npm run build
# or
ng build --configuration production
```

**Output:** `dist/kmato-app/browser/`

**What happens during build:**
1. **TypeScript Compilation**: `.ts` files → `.js` files
2. **Template Compilation**: HTML templates → JavaScript
3. **Bundling**: Multiple files → Few optimized bundles
4. **Minification**: Reduces file sizes
5. **Tree Shaking**: Removes unused code
6. **AOT Compilation**: Ahead-of-Time compilation (Angular templates pre-compiled)

**Build output files:**
```
dist/kmato-app/browser/
├── index.html           # Entry point
├── main.*.js           # Application code
├── polyfills.*.js      # Browser compatibility
├── runtime.*.js        # Webpack runtime
└── styles.*.css        # Compiled styles
```

#### 5. **Build Configurations**

**Development build:**
```bash
ng build --configuration development
```
- Larger file sizes
- Source maps included
- Not optimized

**Production build:**
```bash
ng build --configuration production
```
- Minified code
- No source maps
- Optimized for performance
- AOT compilation enabled

#### 6. **Environment Configuration**

**`environment.ts` (development):**
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8081/api'
};
```

**`environment.prod.ts` (production):**
```typescript
export const environment = {
  production: true,
  apiUrl: window.location.hostname.includes('localhost') 
    ? 'http://localhost:8081/api'
    : `http://${window.location.hostname}:8081/api`
};
```

---

## JWT Implementation - Backend

### Architecture Overview

JWT (JSON Web Token) authentication follows this flow:
1. User provides credentials (email/password)
2. Server validates credentials
3. Server generates JWT token
4. Client stores token (localStorage)
5. Client sends token in Authorization header for subsequent requests
6. Server validates token on each request

### Components

#### 1. **JwtTokenProvider** (Token Generation & Validation)

**Location:** `Backend/src/main/java/com/foodordering/security/JwtTokenProvider.java`

```java
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;  // milliseconds

    // Generate signing key from secret
    private SecretKey getSigningKey() {
        byte[] keyBytes;
        
        // Support base64-encoded secrets (prefix with "base64:")
        if (jwtSecret != null && jwtSecret.startsWith("base64:")) {
            String b64 = jwtSecret.substring("base64:".length());
            keyBytes = Decoders.BASE64.decode(b64);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        // Plain text secret - expand to 512 bits using SHA-512
        keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        
        if (keyBytes.length < 64) {  // HS512 requires >= 512 bits (64 bytes)
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte[] digest = md.digest(keyBytes);
                return Keys.hmacShaKeyFor(digest);
            } catch (NoSuchAlgorithmException e) {
                logger.warn("SHA-512 not available", e);
                return Keys.hmacShaKeyFor(keyBytes);
            }
        }
        
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)           // User identifier
                .setIssuedAt(now)            // Token creation time
                .setExpiration(expiryDate)   // Token expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)  // Sign with HMAC-SHA512
                .compact();
    }

    // Extract email from token
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT validation failed: " + e.getMessage());
            return false;
        }
    }
}
```

**How it works:**
1. **Key Generation**: Converts secret string to SecretKey
   - Supports base64-encoded secrets (production)
   - Expands short secrets to 512 bits using SHA-512
   
2. **Token Generation**: Creates JWT with:
   - **Subject (sub)**: User's email
   - **Issued At (iat)**: Current timestamp
   - **Expiration (exp)**: Current time + expiration duration
   - **Signature**: HMAC-SHA512 signature

3. **Token Validation**: 
   - Parses token structure
   - Verifies signature
   - Checks expiration
   - Returns true/false

**Token Structure:**
```
Header.Payload.Signature

Example:
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjk5MDAwMDAwLCJleHAiOjE2OTkwMDM2MDB9.signature
```

**Decoded payload:**
```json
{
  "sub": "user@example.com",
  "iat": 1699000000,
  "exp": 1699003600
}
```

#### 2. **JwtAuthenticationFilter** (Request Interception)

**Location:** `Backend/src/main/java/com/foodordering/security/JwtAuthenticationFilter.java`

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            // Extract JWT from Authorization header
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Get user email from token
                String email = tokenProvider.getEmailFromToken(jwt);
                
                // Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Create authentication object
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication", ex);
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }

    // Extract JWT from Authorization header
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remove "Bearer " prefix
        }
        
        return null;
    }
}
```

**Filter execution flow:**
1. **Extract Token**: Get JWT from `Authorization: Bearer <token>` header
2. **Validate Token**: Check if token is valid (signature, expiration)
3. **Load User**: Fetch user details from database using email from token
4. **Create Authentication**: Build Spring Security authentication object
5. **Set Context**: Store authentication in SecurityContext
6. **Continue Chain**: Pass request to next filter

**Important:** This filter runs **once per request** before any controller method.

#### 3. **SecurityConfig** (Security Configuration)

**Location:** `Backend/src/main/java/com/foodordering/security/SecurityConfig.java`

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf().disable()  // Disable CSRF (using JWT)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless
            .and()
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/restaurants").permitAll()
                .requestMatchers("/api/restaurants/{id}").permitAll()
                .requestMatchers("/api/menu-items/restaurant/**").permitAll()
                
                // Admin endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // Add JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(
                jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**Configuration explained:**
- **CORS**: Allows cross-origin requests from frontend
- **CSRF**: Disabled (not needed with JWT)
- **Session Management**: Stateless (no server-side sessions)
- **Authorization Rules**: 
  - Public: auth endpoints, restaurant listings
  - Admin: admin endpoints require ADMIN role
  - Protected: all other endpoints require authentication
- **Filter Order**: JWT filter runs before Spring Security's authentication filter

#### 4. **AuthService** (Authentication Logic)

**Location:** `Backend/src/main/java/com/foodordering/service/AuthService.java`

```java
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    // User Registration
    @Transactional
    public AuthResponseDto register(UserRegistrationDto dto) {
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // Create user entity
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // Hash password
        user.setIsActive(true);
        user = userRepository.save(user);

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Generate JWT token
        String token = tokenProvider.generateToken(authentication);

        // Build response
        AuthResponseDto response = userMapper.toAuthResponse(user);
        response.setToken(token);
        return response;
    }

    // User Login
    public AuthResponseDto login(LoginDto dto) {
        // Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = tokenProvider.generateToken(authentication);

        // Fetch user from database
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Build response
        AuthResponseDto response = userMapper.toAuthResponse(user);
        response.setToken(token);
        return response;
    }
}
```

**Authentication flow:**
1. **Registration**:
   - Validate email uniqueness
   - Hash password with BCrypt
   - Save user to database
   - Authenticate user
   - Generate JWT token
   - Return user data + token

2. **Login**:
   - Validate credentials (AuthenticationManager)
   - Generate JWT token
   - Fetch user details
   - Return user data + token

**Response format:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "CUSTOMER",
    "token": "eyJhbGciOiJIUzUxMiJ9..."
  }
}
```

#### 5. **Custom UserDetailsService**

**Location:** `Backend/src/main/java/com/foodordering/security/CustomUserDetailsService.java`

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())  // Add "ROLE_" prefix
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive())
                .build();
    }
}
```

**Purpose:** Loads user details from database for Spring Security.

---

## JWT Implementation - Frontend

### Architecture Overview

Frontend JWT flow:
1. User logs in → receives token from backend
2. Store token in localStorage
3. Attach token to all HTTP requests (Authorization header)
4. Handle token expiration (401 errors)
5. Clear token on logout

### Components

#### 1. **AuthService** (Token Management)

**Location:** `frontend/kmato-app/src/app/services/auth.service.ts`

```typescript
@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'kmato_jwt';
  private userKey = 'kmato_user';
  private currentUserSubject = new BehaviorSubject<User | null>(
    this.getUserFromStorage()
  );
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  // Login
  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/auth/login`, 
      credentials
    ).pipe(
      tap(response => {
        if (response.token) {
          this.saveToken(response.token);
          if (response.user) {
            this.saveUser(response.user);
          }
        }
      })
    );
  }

  // Register
  register(dto: any): Observable<any> { 
    return this.http.post(`${environment.apiUrl}/auth/register`, dto); 
  }

  // Save JWT token to localStorage
  saveToken(token: string) { 
    localStorage.setItem(this.tokenKey, token); 
  }

  // Save user data to localStorage
  saveUser(user: User) {
    localStorage.setItem(this.userKey, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  // Get JWT token from localStorage
  getToken(): string | null { 
    return localStorage.getItem(this.tokenKey); 
  }

  // Get current user from memory
  getUser(): User | null {
    return this.currentUserSubject.value;
  }

  // Get user from localStorage (on app load)
  getUserFromStorage(): User | null {
    const userStr = localStorage.getItem(this.userKey);
    return userStr ? JSON.parse(userStr) : null;
  }

  // Check if user is logged in
  isLoggedIn(): boolean { 
    return !!this.getToken(); 
  }

  // Check user role
  hasRole(role: string): boolean {
    const user = this.getUser();
    if (!user || !user.role) return false;
    return user.role.toString().toUpperCase() === role.toString().toUpperCase();
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isRestaurantOwner(): boolean {
    return this.hasRole('RESTAURANT_OWNER') || this.hasRole('OWNER');
  }

  // Logout
  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUserSubject.next(null);
  }

  // Fetch current user from backend (requires token)
  fetchCurrentUser(): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/auth/me`).pipe(
      tap(user => {
        if (user) this.saveUser(user);
      })
    );
  }
}
```

**Key methods:**
- `login()`: Sends credentials, receives token
- `saveToken()`: Stores JWT in localStorage
- `getToken()`: Retrieves JWT from localStorage
- `isLoggedIn()`: Checks if user has valid token
- `logout()`: Removes token and user data

**localStorage structure:**
```
kmato_jwt: "eyJhbGciOiJIUzUxMiJ9..."
kmato_user: '{"id":1,"email":"user@example.com","role":"CUSTOMER"}'
```

#### 2. **AuthInterceptor** (HTTP Interceptor)

**Location:** `frontend/kmato-app/src/app/interceptors/auth.interceptor.ts`

```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Get JWT token from localStorage
  const token = localStorage.getItem('kmato_jwt');
  
  if (token) {
    // Clone request and add Authorization header
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }
  
  // No token, send original request
  return next(req);
};
```

**How it works:**
1. Intercepts **every HTTP request**
2. Checks if JWT token exists in localStorage
3. If token exists: adds `Authorization: Bearer <token>` header
4. If no token: sends request without modification

**Request example:**
```http
GET /api/orders HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
Content-Type: application/json
```

#### 3. **Login Component** (User Authentication)

**Location:** `frontend/kmato-app/src/app/components/auth/login.component.ts`

```typescript
export class LoginComponent {
  email = '';
  password = '';
  error = '';

  constructor(
    private auth: AuthService, 
    private router: Router
  ) {}

  onSubmit() {
    this.auth.login({ email: this.email, password: this.password })
      .subscribe({
        next: (apiResponse) => {
          // Extract data from ApiResponse wrapper
          const responseData = apiResponse?.data || apiResponse;
          const token = responseData?.token;

          if (!token) {
            this.error = 'No token received';
            return;
          }

          // Save token and user
          this.auth.saveToken(token);
          
          const user = {
            id: responseData.id,
            email: responseData.email,
            firstName: responseData.firstName,
            lastName: responseData.lastName,
            role: responseData.role
          };
          this.auth.saveUser(user);

          // Navigate based on role
          if (this.auth.isAdmin()) {
            this.router.navigate(['/admin']);
          } else if (this.auth.isRestaurantOwner()) {
            this.router.navigate(['/restaurant-dashboard']);
          } else {
            this.router.navigate(['/restaurants']);
          }
        },
        error: (err) => {
          this.error = err.error?.message || 'Login failed';
        }
      });
  }
}
```

**Login flow:**
1. User enters email/password
2. Call `AuthService.login()`
3. Backend validates credentials
4. Backend returns JWT token + user data
5. Frontend saves token to localStorage
6. Frontend saves user data to localStorage
7. Frontend redirects based on user role

#### 4. **Auth Guard** (Route Protection)

**Location:** `frontend/kmato-app/src/app/guards/auth.guard.ts`

```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;  // Allow access
  }

  // Redirect to login
  router.navigate(['/login'], { 
    queryParams: { returnUrl: state.url } 
  });
  return false;
};
```

**Usage in routing:**
```typescript
const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { 
    path: 'orders', 
    component: OrdersComponent, 
    canActivate: [authGuard]  // Protected route
  },
  { 
    path: 'admin', 
    component: AdminComponent, 
    canActivate: [authGuard, adminGuard]  // Admin only
  }
];
```

**How it works:**
1. User tries to access protected route
2. Guard checks if user has token (`isLoggedIn()`)
3. If yes: allow access
4. If no: redirect to login page with returnUrl

#### 5. **HTTP Error Handling** (Token Expiration)

**Location:** `frontend/kmato-app/src/app/interceptors/error.interceptor.ts`

```typescript
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Token expired or invalid
        authService.logout();
        router.navigate(['/login']);
      }
      
      return throwError(() => error);
    })
  );
};
```

**Handles:**
- 401 Unauthorized: Token expired/invalid → logout user
- 403 Forbidden: Insufficient permissions
- Other errors: Pass through

---

## Docker Build Process

### Backend Dockerfile

**Location:** `Backend/Dockerfile`

```dockerfile
# Stage 1: Build application with Maven
FROM maven:3.9.4-eclipse-temurin-17 as build
WORKDIR /app

# Copy Maven files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Copy source code
COPY src src

# Build application (skip tests for faster build)
RUN mvn -B -DskipTests package

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

**Multi-stage build benefits:**
1. **Stage 1 (build)**: 
   - Uses full Maven image with JDK
   - Compiles source code
   - Creates JAR file
   - Size: ~800MB

2. **Stage 2 (runtime)**:
   - Uses smaller JRE-only image
   - Copies only JAR file (not source code)
   - Final size: ~250MB
   - 70% size reduction

**Build command:**
```bash
cd Backend
docker build -t kmato-backend:latest .
```

**Run container:**
```bash
docker run -d -p 8081:8081 \
  -e JWT_SECRET=your-secure-secret \
  -e SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/fooddelivery \
  --name kmato-backend \
  kmato-backend:latest
```

### Frontend Dockerfile

**Location:** `frontend/kmato-app/Dockerfile`

```dockerfile
# Stage 1: Build Angular application
FROM node:18-alpine AS build
WORKDIR /app

# Copy package files
COPY package.json package-lock.json* ./

# Install dependencies (clean install)
RUN npm ci

# Copy source code
COPY . .

# Build for production
RUN npm run build

# Stage 2: Serve with nginx
FROM nginx:alpine

# Copy built files to nginx html directory
COPY --from=build /app/dist/kmato-app/browser /usr/share/nginx/html

# Expose port
EXPOSE 80

# Start nginx
CMD ["/bin/sh","-c","nginx -g 'daemon off;' "]
```

**Multi-stage build benefits:**
1. **Stage 1 (build)**:
   - Uses Node.js image
   - Installs dependencies (node_modules)
   - Builds Angular app
   - Size: ~1.5GB

2. **Stage 2 (runtime)**:
   - Uses nginx alpine image
   - Copies only built files (HTML/CSS/JS)
   - Final size: ~25MB
   - 98% size reduction

**Build command:**
```bash
cd frontend/kmato-app
docker build -t kmato-frontend:latest .
```

**Run container:**
```bash
docker run -d -p 80:80 \
  --name kmato-frontend \
  kmato-frontend:latest
```

### Docker Compose (Local Development)

**Location:** `docker-compose.yml`

```yaml
version: '3.8'

services:
  backend:
    build:
      context: ./Backend
      dockerfile: Dockerfile
    ports:
      - "8081:8081"
    environment:
      - JWT_SECRET=local-development-secret
      - JWT_EXPIRATION=3600000
      - SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/fooddelivery
    volumes:
      - backend-data:/app/data
    networks:
      - kmato-network

  frontend:
    build:
      context: ./frontend/kmato-app
      dockerfile: Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend
    networks:
      - kmato-network

volumes:
  backend-data:

networks:
  kmato-network:
    driver: bridge
```

**Start all services:**
```bash
docker-compose up -d
```

**Stop all services:**
```bash
docker-compose down
```

---

## CI/CD Pipeline

### GitHub Actions Workflow

**Location:** `.github/workflows/docker-build-push.yml`

```yaml
name: Build and Push to Docker Hub

on:
  push:
    branches:
      - main
  workflow_dispatch:

env:
  DOCKER_USERNAME: ${{ secrets.DOCKER_USERNAME }}
  DOCKER_PASSWORD: ${{ secrets.DOCKER_PASSWORD }}

jobs:
  build-backend:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and push backend
        uses: docker/build-push-action@v5
        with:
          context: ./Backend
          file: ./Backend/Dockerfile
          push: true
          tags: |
            ${{ secrets.DOCKER_USERNAME }}/kmato-backend:latest
            ${{ secrets.DOCKER_USERNAME }}/kmato-backend:${{ github.sha }}
          cache-from: type=registry,ref=${{ secrets.DOCKER_USERNAME }}/kmato-backend:buildcache
          cache-to: type=registry,ref=${{ secrets.DOCKER_USERNAME }}/kmato-backend:buildcache,mode=max

  build-frontend:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and push frontend
        uses: docker/build-push-action@v5
        with:
          context: ./frontend/kmato-app
          file: ./frontend/kmato-app/Dockerfile
          push: true
          tags: |
            ${{ secrets.DOCKER_USERNAME }}/kmato-frontend:latest
            ${{ secrets.DOCKER_USERNAME }}/kmato-frontend:${{ github.sha }}
          cache-from: type=registry,ref=${{ secrets.DOCKER_USERNAME }}/kmato-frontend:buildcache
          cache-to: type=registry,ref=${{ secrets.DOCKER_USERNAME }}/kmato-frontend:buildcache,mode=max
```

**Workflow steps:**
1. **Trigger**: Runs on push to `main` branch or manual trigger
2. **Checkout**: Clones repository code
3. **Docker Buildx**: Sets up advanced Docker build features
4. **Login**: Authenticates to Docker Hub
5. **Build**: Builds Docker image with caching
6. **Push**: Pushes image to Docker Hub with two tags:
   - `latest`: Always points to latest build
   - `<commit-sha>`: Specific version for rollbacks

**Required secrets (GitHub Settings → Secrets):**
- `DOCKER_USERNAME`: Docker Hub username
- `DOCKER_PASSWORD`: Docker Hub password/token

**Benefits:**
- Automated builds on code changes
- Version tagging with commit SHA
- Build caching for faster builds
- Parallel backend/frontend builds

---

## Deployment

### AWS EC2 with Terraform

**Infrastructure:** `infra/terraform/main.tf`

```hcl
# EC2 Instance
resource "aws_instance" "kmato" {
  ami           = "ami-0453ec754f44f9a4a"  # Amazon Linux 2
  instance_type = "t3.micro"
  key_name      = var.key_name

  vpc_security_group_ids = [aws_security_group.kmato_sg.id]
  iam_instance_profile   = aws_iam_instance_profile.ec2_profile.name

  user_data = <<-EOF
    #!/bin/bash
    exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1
    set -x
    
    echo "Starting deployment at $(date)"
    
    # Install Docker
    yum update -y
    yum install -y docker
    service docker start
    usermod -a -G docker ec2-user
    
    # Pull and run backend
    docker pull ${var.docker_username}/kmato-backend:latest
    docker run -d -p 8081:8081 \
      -e JWT_SECRET=production-secret-change-me \
      -e JWT_EXPIRATION=3600000 \
      --name kmato-backend \
      --restart unless-stopped \
      ${var.docker_username}/kmato-backend:latest
    
    # Pull and run frontend
    docker pull ${var.docker_username}/kmato-frontend:latest
    docker run -d -p 80:80 \
      --name kmato-frontend \
      --restart unless-stopped \
      ${var.docker_username}/kmato-frontend:latest
    
    echo "Deployment completed at $(date)"
    docker ps -a
  EOF

  tags = {
    Name = "kmato-app"
  }
}

# Security Group
resource "aws_security_group" "kmato_sg" {
  name        = "kmato-sg"
  description = "Security group for K-MATO application"

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Outputs
output "ec2_public_ip" {
  value = aws_instance.kmato.public_ip
}

output "frontend_url" {
  value = "http://${aws_instance.kmato.public_ip}"
}

output "backend_url" {
  value = "http://${aws_instance.kmato.public_ip}:8081"
}
```

**Deploy commands:**
```bash
cd infra/terraform

# Initialize Terraform
terraform init

# Preview changes
terraform plan

# Deploy infrastructure
terraform apply -auto-approve

# Get outputs
terraform output

# Destroy infrastructure
terraform destroy -auto-approve
```

**Deployment flow:**
1. Terraform provisions EC2 instance
2. User-data script runs on instance startup:
   - Installs Docker
   - Pulls Docker images from Docker Hub
   - Starts backend container (port 8081)
   - Starts frontend container (port 80)
3. Applications are accessible via EC2 public IP

**Access application:**
- Frontend: `http://<EC2_PUBLIC_IP>`
- Backend: `http://<EC2_PUBLIC_IP>:8081`
- H2 Console: `http://<EC2_PUBLIC_IP>:8081/h2-console`

---

## Security Best Practices

### Backend Security

1. **JWT Secret Management**
   - Use environment variables (never hardcode)
   - Generate strong secrets:
     ```bash
     openssl rand -base64 64
     ```
   - Use different secrets for dev/staging/prod

2. **Password Security**
   - BCrypt hashing with salt
   - Minimum password strength requirements
   - Password reset with email verification

3. **CORS Configuration**
   - Production: Allow only specific origins
   - Development: Allow all origins for testing
   ```java
   config.setAllowedOrigins(List.of(
       "https://yourdomain.com",
       "https://www.yourdomain.com"
   ));
   ```

4. **HTTPS**
   - Use SSL/TLS certificates in production
   - Redirect HTTP to HTTPS
   - Use Let's Encrypt for free SSL certificates

5. **Database Security**
   - Use connection pooling
   - Limit database user permissions
   - Use prepared statements (JPA handles this)
   - Regular backups

6. **Rate Limiting**
   - Implement API rate limiting
   - Prevent brute force attacks on login

7. **Input Validation**
   - Use `@Valid` with DTOs
   - Sanitize user input
   - Validate email formats

### Frontend Security

1. **Token Storage**
   - localStorage: Simple but vulnerable to XSS
   - Alternative: HttpOnly cookies (more secure)
   - Never store sensitive data in localStorage

2. **XSS Prevention**
   - Angular sanitizes by default
   - Don't use `bypassSecurityTrust*()` unless necessary
   - Validate user input

3. **CSRF Protection**
   - Not needed with JWT (stateless)
   - Required if using session-based auth

4. **Environment Variables**
   - Don't commit sensitive data
   - Use environment-specific configs
   - Use `.env` files (not committed)

5. **HTTPS**
   - Always use HTTPS in production
   - Mixed content warnings (HTTP API + HTTPS frontend)

6. **Content Security Policy (CSP)**
   - Add CSP headers
   - Prevent inline scripts
   - Restrict resource loading

7. **Dependency Security**
   - Regular `npm audit`
   - Update dependencies
   - Use `npm audit fix`

### Docker Security

1. **Base Images**
   - Use official images
   - Use specific versions (not `latest`)
   - Use minimal images (alpine)

2. **Multi-stage Builds**
   - Don't include build tools in final image
   - Reduces attack surface

3. **User Permissions**
   - Don't run as root
   ```dockerfile
   RUN adduser -D appuser
   USER appuser
   ```

4. **Secrets Management**
   - Don't bake secrets into images
   - Use environment variables
   - Use Docker secrets (Swarm) or Kubernetes secrets

5. **Image Scanning**
   - Scan for vulnerabilities
   - Use tools like Trivy, Snyk
   ```bash
   trivy image kmato-backend:latest
   ```

### AWS Security

1. **IAM Roles**
   - Least privilege principle
   - Don't use root account
   - Use IAM roles for EC2 instances

2. **Security Groups**
   - Restrict ingress rules
   - Allow only necessary ports
   - Use specific IP ranges (not 0.0.0.0/0)

3. **SSH Keys**
   - Use strong SSH keys
   - Rotate keys regularly
   - Disable password authentication

4. **Monitoring**
   - Enable CloudWatch logs
   - Set up alarms
   - Monitor unusual activity

5. **Backups**
   - Regular EBS snapshots
   - Database backups
   - Disaster recovery plan

---

# PART III: TESTING & MAINTENANCE

## Testing Strategy

### Backend Testing

#### **1. Unit Testing**

**Test User Service:**
```java
@SpringBootTest
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AuthService authService;
    
    @Test
    void testRegisterUser_Success() {
        // Given
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        
        // When
        AuthResponseDto response = authService.register(dto);
        
        // Then
        assertNotNull(response);
        assertNotNull(response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            authService.register(new UserRegistrationDto());
        });
    }
}
```

**Test Restaurant Service:**
```java
@SpringBootTest
class RestaurantServiceTest {
    
    @Mock
    private RestaurantRepository restaurantRepository;
    
    @InjectMocks
    private RestaurantService restaurantService;
    
    @Test
    void testGetAllRestaurants() {
        // Given
        List<Restaurant> restaurants = Arrays.asList(
            new Restaurant(1L, "Restaurant 1", "Description", "City1"),
            new Restaurant(2L, "Restaurant 2", "Description", "City2")
        );
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        
        // When
        List<RestaurantDto> result = restaurantService.getAll();
        
        // Then
        assertEquals(2, result.size());
    }
}
```

#### **2. Integration Testing**

**Test REST Endpoints:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testLogin_Success() throws Exception {
        LoginDto loginDto = new LoginDto("user@example.com", "password");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").exists());
    }
    
    @Test
    void testLogin_InvalidCredentials() throws Exception {
        LoginDto loginDto = new LoginDto("user@example.com", "wrongpassword");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}
```

#### **3. Repository Testing**

```java
@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testFindByEmail() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);
        
        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");
        
        // Then
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }
}
```

#### **4. Running Backend Tests**

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage report
mvn test jacoco:report

# Skip tests during build
mvn package -DskipTests
```

### Frontend Testing

#### **1. Unit Testing (Jasmine & Karma)**

**Test Auth Service:**
```typescript
describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  
  it('should login successfully', () => {
    const mockResponse = {
      success: true,
      data: {
        token: 'mock_token',
        email: 'test@example.com'
      }
    };
    
    service.login({ email: 'test@example.com', password: 'password' })
      .subscribe(response => {
        expect(response.success).toBe(true);
        expect(response.data.token).toBe('mock_token');
      });
    
    const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });
  
  afterEach(() => {
    httpMock.verify();
  });
});
```

**Test Component:**
```typescript
describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  
  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });
    
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should call authService.login on submit', () => {
    component.email = 'test@example.com';
    component.password = 'password';
    
    authService.login.and.returnValue(of({
      success: true,
      data: { token: 'mock_token' }
    }));
    
    component.onSubmit();
    
    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password'
    });
  });
});
```

#### **2. E2E Testing (Cypress/Protractor)**

**Install Cypress:**
```bash
npm install --save-dev cypress
npx cypress open
```

**Test Login Flow:**
```typescript
describe('Login Flow', () => {
  beforeEach(() => {
    cy.visit('/login');
  });
  
  it('should display login form', () => {
    cy.get('input[type="email"]').should('exist');
    cy.get('input[type="password"]').should('exist');
    cy.get('button[type="submit"]').should('exist');
  });
  
  it('should login successfully', () => {
    cy.get('input[type="email"]').type('test@example.com');
    cy.get('input[type="password"]').type('password123');
    cy.get('button[type="submit"]').click();
    
    cy.url().should('include', '/restaurants');
    cy.window().its('localStorage.kmato_jwt').should('exist');
  });
  
  it('should show error on invalid credentials', () => {
    cy.get('input[type="email"]').type('wrong@example.com');
    cy.get('input[type="password"]').type('wrongpassword');
    cy.get('button[type="submit"]').click();
    
    cy.contains('Invalid credentials').should('be.visible');
  });
});
```

#### **3. Running Frontend Tests**

```bash
# Run unit tests
ng test

# Run tests once (CI mode)
ng test --watch=false --code-coverage

# Run e2e tests
ng e2e

# Run with specific browser
ng test --browsers=Chrome
```

---

## Troubleshooting Guide

### Common Development Issues

#### **Issue 1: Port Already in Use**

**Backend (8081):**
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8081 | xargs kill -9
```

**Frontend (4200):**
```bash
# Run on different port
ng serve --port 4300
```

#### **Issue 2: Database Connection Failed**

**Check H2 Console:**
```
URL: http://localhost:8081/h2-console
JDBC URL: jdbc:h2:file:./data/fooddelivery
Username: sa
Password: (leave empty)
```

**Reset Database:**
```bash
# Delete H2 database files
rm -rf data/

# Restart application (will recreate database)
mvn spring-boot:run
```

#### **Issue 3: JWT Token Expired**

**Frontend:**
```typescript
// Add token refresh logic
@Injectable()
export class TokenRefreshInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Token expired - redirect to login
          this.authService.logout();
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
```

#### **Issue 4: CORS Errors**

**Backend Configuration:**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

#### **Issue 5: Docker Build Fails**

**Clear Docker cache:**
```bash
docker system prune -a
docker builder prune
```

**Build with no cache:**
```bash
docker build --no-cache -t image:tag .
```

**Check Docker logs:**
```bash
docker logs <container_id>
```

#### **Issue 6: GitHub Actions Fails**

**Check secrets:**
```
Settings → Secrets and variables → Actions
Required secrets:
- DOCKER_USERNAME
- DOCKER_PASSWORD
```

**Test locally:**
```bash
# Install act (GitHub Actions local runner)
choco install act

# Run workflow locally
act -j build-backend
```

#### **Issue 7: Terraform Apply Fails**

**Common issues:**
```bash
# Invalid credentials
export AWS_ACCESS_KEY_ID="your_key"
export AWS_SECRET_ACCESS_KEY="your_secret"

# State file locked
terraform force-unlock <lock_id>

# Refresh state
terraform refresh

# Re-initialize
rm -rf .terraform
terraform init
```

### Performance Issues

#### **Backend Performance**

**Issue: Slow API responses**

**Solutions:**
1. **Add Database Indexing:**
```java
@Entity
@Table(name = "restaurants", indexes = {
    @Index(name = "idx_city", columnList = "city"),
    @Index(name = "idx_rating", columnList = "rating")
})
public class Restaurant { }
```

2. **Enable Query Caching:**
```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
```

3. **Add Connection Pooling:**
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

#### **Frontend Performance**

**Issue: Slow page load**

**Solutions:**
1. **Enable Lazy Loading:**
```typescript
const routes: Routes = [
  {
    path: 'restaurants',
    loadChildren: () => import('./restaurant/restaurant.module')
      .then(m => m.RestaurantModule)
  }
];
```

2. **Use OnPush Change Detection:**
```typescript
@Component({
  selector: 'app-restaurant-list',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RestaurantListComponent { }
```

3. **Implement Virtual Scrolling:**
```typescript
<cdk-virtual-scroll-viewport itemSize="100">
  <div *cdkVirtualFor="let restaurant of restaurants">
    {{ restaurant.name }}
  </div>
</cdk-virtual-scroll-viewport>
```

---

## Performance Optimization

### Backend Optimization

#### **1. Database Optimization**

**Use Pagination:**
```java
@GetMapping("/restaurants")
public Page<RestaurantDto> getRestaurants(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    return restaurantService.getAll(pageable);
}
```

**Optimize Queries:**
```java
// Bad: N+1 query problem
List<Restaurant> restaurants = restaurantRepository.findAll();
restaurants.forEach(r -> r.getMenuItems().size()); // Lazy load

// Good: Join fetch
@Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menuItems")
List<Restaurant> findAllWithMenuItems();
```

**Use Projections:**
```java
public interface RestaurantSummary {
    Long getId();
    String getName();
    String getCity();
}

List<RestaurantSummary> findByCity(String city);
```

#### **2. Caching Strategy**

**Spring Cache:**
```java
@Service
@EnableCaching
public class RestaurantService {
    
    @Cacheable(value = "restaurants", key = "#id")
    public RestaurantDto getById(Long id) {
        return restaurantRepository.findById(id)
            .map(mapper::toDto)
            .orElseThrow();
    }
    
    @CacheEvict(value = "restaurants", key = "#id")
    public void update(Long id, RestaurantDto dto) {
        // Update logic
    }
}
```

**Configuration:**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("restaurants"),
            new ConcurrentMapCache("menuItems")
        ));
        return cacheManager;
    }
}
```

#### **3. Async Processing**

```java
@Service
public class OrderService {
    
    @Async
    public CompletableFuture<void> sendOrderNotification(Order order) {
        // Send email notification asynchronously
        emailService.sendOrderConfirmation(order);
        return CompletableFuture.completedFuture(null);
    }
}
```

### Frontend Optimization

#### **1. Lazy Loading Modules**

```typescript
const routes: Routes = [
  { path: '', redirectTo: '/restaurants', pathMatch: 'full' },
  {
    path: 'restaurants',
    loadChildren: () => import('./restaurant/restaurant.module')
      .then(m => m.RestaurantModule)
  },
  {
    path: 'orders',
    loadChildren: () => import('./order/order.module')
      .then(m => m.OrderModule),
    canActivate: [authGuard]
  }
];
```

#### **2. Image Optimization**

```typescript
// Use lazy loading for images
<img [src]="restaurant.imageUrl" 
     loading="lazy" 
     alt="{{ restaurant.name }}"
     width="300" 
     height="200">

// Use WebP format with fallback
<picture>
  <source srcset="image.webp" type="image/webp">
  <img src="image.jpg" alt="Restaurant">
</picture>
```

#### **3. HTTP Caching**

```typescript
@Injectable()
export class CacheInterceptor implements HttpInterceptor {
  private cache = new Map<string, HttpResponse<any>>();
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.method !== 'GET') {
      return next.handle(req);
    }
    
    const cachedResponse = this.cache.get(req.url);
    if (cachedResponse) {
      return of(cachedResponse);
    }
    
    return next.handle(req).pipe(
      tap(event => {
        if (event instanceof HttpResponse) {
          this.cache.set(req.url, event);
        }
      })
    );
  }
}
```

#### **4. Bundle Optimization**

```bash
# Production build with optimization
ng build --configuration production

# Analyze bundle size
npm install --save-dev webpack-bundle-analyzer
ng build --stats-json
npx webpack-bundle-analyzer dist/kmato-app/stats.json
```

**angular.json optimization:**
```json
{
  "configurations": {
    "production": {
      "optimization": true,
      "outputHashing": "all",
      "sourceMap": false,
      "namedChunks": false,
      "aot": true,
      "extractLicenses": true,
      "buildOptimizer": true,
      "budgets": [
        {
          "type": "initial",
          "maximumWarning": "500kb",
          "maximumError": "1mb"
        }
      ]
    }
  }
}
```

---

## Future Enhancements

### Planned Features

#### **Phase 1: Payment Integration**
```
✓ Integrate payment gateway (Stripe/PayPal)
✓ Payment processing during checkout
✓ Transaction history
✓ Refund management
✓ Multiple payment methods
```

#### **Phase 2: Real-time Features**
```
✓ WebSocket integration for live order updates
✓ Real-time chat with restaurant
✓ Live order tracking on map
✓ Push notifications
```

#### **Phase 3: Advanced Features**
```
✓ Rating and review system
✓ Favorite restaurants
✓ Order scheduling
✓ Promotional codes and discounts
✓ Loyalty program
✓ Multi-language support
```

#### **Phase 4: Mobile App**
```
✓ React Native mobile app
✓ GPS-based restaurant discovery
✓ Biometric authentication
✓ Offline mode
✓ Push notifications
```

#### **Phase 5: Analytics & Reporting**
```
✓ Admin analytics dashboard
✓ Restaurant performance metrics
✓ Customer behavior analysis
✓ Revenue reports
✓ Export functionality
```

### Technical Improvements

#### **1. Microservices Architecture**
```
Current: Monolithic application
Future: Split into microservices
- User Service
- Restaurant Service
- Order Service
- Payment Service
- Notification Service
```

#### **2. Advanced Caching**
```
Current: Spring Cache (in-memory)
Future: Redis for distributed caching
- Session management
- API response caching
- Rate limiting
```

#### **3. Message Queue**
```
Current: Synchronous processing
Future: RabbitMQ/Kafka for async processing
- Order processing
- Email notifications
- SMS notifications
- Webhook events
```

#### **4. Monitoring & Logging**
```
Current: Basic logging
Future: Advanced monitoring
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Prometheus + Grafana
- Distributed tracing (Jaeger)
- APM (Application Performance Monitoring)
```

#### **5. Security Enhancements**
```
✓ OAuth2 integration (Google, Facebook login)
✓ Two-factor authentication (2FA)
✓ Rate limiting per user
✓ API key management
✓ Security audit logging
```

### Infrastructure Improvements

#### **1. Container Orchestration**
```
Current: Docker on EC2
Future: Kubernetes (EKS)
- Auto-scaling
- Self-healing
- Zero-downtime deployments
- Resource optimization
```

#### **2. Database**
```
Current: H2 (development)
Future: Production-ready database
- Amazon RDS (PostgreSQL)
- Read replicas for scalability
- Automated backups
- Point-in-time recovery
```

#### **3. CDN & Asset Management**
```
Current: Assets served from server
Future: CloudFront CDN
- Image optimization
- Global distribution
- Reduced latency
- Cost optimization
```

#### **4. CI/CD Enhancements**
```
Current: GitHub Actions (basic)
Future: Advanced pipeline
- Automated testing (unit, integration, e2e)
- Security scanning (SAST, DAST)
- Performance testing
- Canary deployments
- Blue-green deployments
```

---

## Project Metrics & Statistics

### Development Timeline
```
Total Development Time: 12 weeks (3 months)
- Planning & Setup: 1 week
- Backend Development: 5 weeks
- Frontend Development: 4 weeks
- Testing & Bug Fixes: 1 week
- Deployment Setup: 1 week
```

### Code Statistics
```
Backend:
- Java Files: 45+
- Lines of Code: ~5,000
- Test Coverage: 75%
- API Endpoints: 30+

Frontend:
- TypeScript Files: 60+
- Lines of Code: ~6,000
- Components: 25+
- Services: 12+

Infrastructure:
- Terraform Files: 3
- GitHub Actions Workflows: 1
- Dockerfiles: 2
```

### Technology Versions
```
Backend:
- Java: 17 LTS
- Spring Boot: 3.5.7
- Maven: 3.9.4
- H2 Database: 2.2.224
- JWT: 0.11.5

Frontend:
- Node.js: 18 LTS
- Angular: 17.3.0
- TypeScript: 5.4.2
- npm: 9.6.0

DevOps:
- Docker: 24.0.0
- Terraform: 1.5.0
- AWS CLI: 2.13.0
```

### Deployment Statistics
```
Docker Image Sizes:
- Backend: ~250 MB (compressed)
- Frontend: ~25 MB (compressed)

Build Times:
- Backend Build: ~2-3 minutes
- Frontend Build: ~1-2 minutes
- Docker Build: ~5-8 minutes
- Full CI/CD Pipeline: ~10-15 minutes

AWS Resources:
- EC2 Instance: t3.micro (1 vCPU, 1 GB RAM)
- Monthly Cost: ~$8-10 (estimated)
```

---

## Summary & Key Takeaways

### What We Built
✅ **Full-stack food delivery platform** with customer, restaurant owner, and admin roles  
✅ **Secure JWT authentication** with role-based access control  
✅ **RESTful API** with 30+ endpoints  
✅ **Responsive Angular UI** with 25+ components  
✅ **Docker containerization** with multi-stage builds  
✅ **CI/CD pipeline** with GitHub Actions  
✅ **Infrastructure as Code** with Terraform  
✅ **Cloud deployment** on AWS EC2  

### Development Best Practices Applied
✅ **Clean Architecture**: Separation of concerns (layers)  
✅ **SOLID Principles**: Maintainable and extensible code  
✅ **RESTful Design**: Standard HTTP methods and status codes  
✅ **Security First**: JWT, BCrypt, CORS, input validation  
✅ **Version Control**: Git with meaningful commits  
✅ **Documentation**: Code comments, API docs, README  
✅ **Testing**: Unit tests, integration tests  
✅ **Automation**: CI/CD, automated deployments  

### Technologies Mastered
✅ Spring Boot ecosystem (Web, Security, Data JPA)  
✅ Angular framework (components, services, routing)  
✅ JWT authentication flow  
✅ Docker containerization  
✅ GitHub Actions CI/CD  
✅ Terraform Infrastructure as Code  
✅ AWS cloud services  

### Challenges Overcome
🔧 **Role Enum Mapping**: Fixed USER → CUSTOMER mismatch  
🔧 **Field Naming**: Corrected phone → phoneNumber inconsistency  
🔧 **AWS Credentials**: Managed credential rotation  
🔧 **Docker Deployment**: Optimized image sizes and build times  
🔧 **CORS Configuration**: Enabled cross-origin requests  

### Project Success Criteria
✅ **Functional**: All core features working  
✅ **Secure**: Authentication and authorization implemented  
✅ **Scalable**: Containerized and cloud-ready  
✅ **Maintainable**: Clean code and documentation  
✅ **Deployable**: Automated CI/CD pipeline  
✅ **Testable**: Unit and integration tests  

---

## Conclusion

This project demonstrates a complete end-to-end development process for building a modern full-stack web application. From initial setup to cloud deployment, we covered:

- **Backend development** with Spring Boot and JWT authentication
- **Frontend development** with Angular and TypeScript
- **Database design** with JPA entities and relationships
- **Security implementation** with Spring Security
- **Containerization** with Docker multi-stage builds
- **CI/CD automation** with GitHub Actions
- **Cloud deployment** with Terraform and AWS

The K-MATO food delivery platform is production-ready and can be extended with additional features like payment integration, real-time tracking, and mobile applications.

---

**Project Repository**: https://github.com/palanikalyan/K-MATO  
**Docker Images**: palanikalyan27/kmato-backend, palanikalyan27/kmato-frontend  
**Documentation Date**: October 28, 2025  
**Version**: 1.0.0

---

**End of Documentation**

For questions, contributions, or issues, please refer to the GitHub repository or contact the development team.

**Happy Coding! 🚀**
