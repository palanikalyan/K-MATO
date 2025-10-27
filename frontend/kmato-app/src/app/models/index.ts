// Restaurant Model
export interface Restaurant {
  id: number;
  name: string;
  description: string;
  imageUrl?: string;
  address: string;
  city: string;
  phoneNumber?: string;
  rating: number;
  totalReviews: number;
  isOpen: boolean;
  createdAt?: string;
}

// MenuItem Model
export interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl?: string;
  category: string;
  isAvailable: boolean;
  isVegetarian: boolean;
  restaurantId: number;
}

// Order Models
export interface OrderItem {
  menuItemId: number;
  menuItemName?: string;
  quantity: number;
  price?: number;
  subtotal?: number;
}

export interface Order {
  id: number;
  customerId: number;
  customerName: string;
  restaurantId: number;
  restaurantName: string;
  items: OrderItem[];
  deliveryAddress: Address;
  totalAmount: number;
  deliveryFee: number;
  taxAmount: number;
  status: string;
  paymentMethod: string;
  paymentStatus: string;
  specialInstructions?: string;
  createdAt: string;
  deliveredAt?: string;
  delivery?: Delivery;
}

export interface CreateOrderDto {
  restaurantId: number;
  deliveryAddressId: number;
  items: { menuItemId: number; quantity: number }[];
  paymentMethod: string;
  specialInstructions?: string;
}

// Address Model
export interface Address {
  id?: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  addressType?: string;
  landmark?: string;
  isDefault?: boolean;
}

// Delivery Model
export interface Delivery {
  id: number;
  orderId: number;
  status: string;
  assignedDriver?: string;
  etaSeconds?: number;
  scheduledAt: string;
  updatedAt?: string;
}

// Payment Model
export interface PaymentRequest {
  paymentMethod: string;
  cardNumber?: string;
  cvv?: string;
  expiryDate?: string;
}

// User Model
export interface User {
  id: number;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: string;
  addresses?: Address[];
  orders?: Order[];
  restaurants?: Restaurant[];
  createdAt: string;
  isActive: boolean;
}

// API Response
export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data?: T;
}

// Auth Response
export interface AuthResponse {
  token: string;
  user: {
    id: number;
    email: string;
    fullName: string;
    role: string;
  };
}
