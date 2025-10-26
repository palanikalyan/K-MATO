-- Insert test users with different roles
-- Password for all users: password123 (BCrypt encoded)
INSERT INTO users (id, email, password, full_name, phone_number, role, is_active, created_at) VALUES
(1, 'admin@kmato.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', '1234567890', 'ADMIN', true, CURRENT_TIMESTAMP),
(2, 'owner@kmato.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Restaurant Owner', '9876543210', 'RESTAURANT_OWNER', true, CURRENT_TIMESTAMP),
(3, 'user@kmato.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Customer', '5555555555', 'CUSTOMER', true, CURRENT_TIMESTAMP);

-- Insert test restaurants (some approved, some pending)
INSERT INTO restaurants (id, name, description, image_url, address, city, phone_number, rating, total_reviews, is_open, approval_status, owner_id, created_at) VALUES
(1, 'Pizza Palace', 'Authentic Italian pizzas with fresh ingredients', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500', '123 Main St', 'Mumbai', '9988776655', 4.5, 120, true, 'APPROVED', 2, CURRENT_TIMESTAMP),
(2, 'Burger Hub', 'Juicy burgers and crispy fries', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500', '456 Park Ave', 'Mumbai', '8877665544', 4.2, 85, true, 'APPROVED', 2, CURRENT_TIMESTAMP),
(3, 'Sushi Express', 'Fresh sushi and Japanese cuisine', 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500', '789 Ocean Blvd', 'Mumbai', '7766554433', 4.8, 200, true, 'PENDING', 2, CURRENT_TIMESTAMP);

-- Insert menu items for Pizza Palace
INSERT INTO menu_items (id, restaurant_id, name, description, price, image_url, category, is_available, is_vegetarian, created_at) VALUES
(1, 1, 'Margherita Pizza', 'Classic tomato sauce, mozzarella, and basil', 299.00, 'https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?w=500', 'MAIN_COURSE', true, true, CURRENT_TIMESTAMP),
(2, 1, 'Pepperoni Pizza', 'Tomato sauce, mozzarella, and pepperoni', 399.00, 'https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500', 'MAIN_COURSE', true, false, CURRENT_TIMESTAMP),
(3, 1, 'Veggie Supreme', 'Loaded with fresh vegetables', 349.00, 'https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f?w=500', 'MAIN_COURSE', true, true, CURRENT_TIMESTAMP),
(4, 1, 'Garlic Bread', 'Crispy bread with garlic butter', 99.00, 'https://images.unsplash.com/photo-1573140401552-388e7e2f0e8d?w=500', 'SNACKS', true, true, CURRENT_TIMESTAMP);

-- Insert menu items for Burger Hub
INSERT INTO menu_items (id, restaurant_id, name, description, price, image_url, category, is_available, is_vegetarian, created_at) VALUES
(5, 2, 'Classic Burger', 'Beef patty with lettuce, tomato, and sauce', 199.00, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500', 'MAIN_COURSE', true, false, CURRENT_TIMESTAMP),
(6, 2, 'Cheese Burger', 'Double cheese with beef patty', 249.00, 'https://images.unsplash.com/photo-1572802419224-296b0aeee0d9?w=500', 'MAIN_COURSE', true, false, CURRENT_TIMESTAMP),
(7, 2, 'Veggie Burger', 'Plant-based patty with fresh veggies', 179.00, 'https://images.unsplash.com/photo-1520072959219-c595dc870360?w=500', 'MAIN_COURSE', true, true, CURRENT_TIMESTAMP),
(8, 2, 'French Fries', 'Crispy golden fries', 79.00, 'https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500', 'SNACKS', true, true, CURRENT_TIMESTAMP);

-- Insert menu items for Sushi Express (pending restaurant)
INSERT INTO menu_items (id, restaurant_id, name, description, price, image_url, category, is_available, is_vegetarian, created_at) VALUES
(9, 3, 'California Roll', 'Crab, avocado, and cucumber', 349.00, 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500', 'MAIN_COURSE', true, false, CURRENT_TIMESTAMP),
(10, 3, 'Salmon Nigiri', 'Fresh salmon over rice', 299.00, 'https://images.unsplash.com/photo-1564489563601-c53cfc451e93?w=500', 'MAIN_COURSE', true, false, CURRENT_TIMESTAMP),
(11, 3, 'Vegetable Tempura', 'Crispy fried vegetables', 249.00, 'https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=500', 'APPETIZER', true, true, CURRENT_TIMESTAMP);

-- Insert test addresses for customer
INSERT INTO addresses (id, user_id, street, city, state, zip_code, landmark, is_default, created_at) VALUES
(1, 3, '101 Customer Lane', 'Mumbai', 'Maharashtra', '400001', 'Near City Mall', true, CURRENT_TIMESTAMP),
(2, 3, '202 Home Street', 'Mumbai', 'Maharashtra', '400002', 'Near Park', false, CURRENT_TIMESTAMP);

-- Note: Orders and deliveries will be created when users place orders through the application
-- The system will automatically manage order progression and delivery tracking
