import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) { }

  // Restaurants
  getRestaurants() {
    return this.http.get(`${environment.apiUrl}/restaurants`);
  }

  getRestaurantById(id: number) {
    return this.http.get(`${environment.apiUrl}/restaurants/${id}`);
  }

  getRestaurantsByCity(city: string) {
    return this.http.get(`${environment.apiUrl}/restaurants/city/${city}`);
  }

  getOwnerRestaurants() {
    return this.http.get(`${environment.apiUrl}/restaurants/owner`);
  }

  // Menu Items
  getMenuByRestaurant(restaurantId: number) {
    return this.http.get(`${environment.apiUrl}/menu-items/restaurant/${restaurantId}`);
  }

  getAllMenuItems() {
    return this.http.get(`${environment.apiUrl}/menu-items`);
  }

  getMenuItemById(id: number) {
    return this.http.get(`${environment.apiUrl}/menu-items/${id}`);
  }

  // Orders
  createOrder(payload: any) { return this.http.post(`${environment.apiUrl}/orders`, payload); }
  getCustomerOrders() { return this.http.get(`${environment.apiUrl}/orders/customer`); }
  getOrderById(id: number) { return this.http.get(`${environment.apiUrl}/orders/${id}`); }
  getCurrentOrder() { return this.http.get(`${environment.apiUrl}/orders/current`); }
  cancelOrder(id: number) { return this.http.put(`${environment.apiUrl}/orders/${id}/cancel`, {}); }

  // User addresses
  getUserAddresses() { return this.http.get(`${environment.apiUrl}/addresses/user`); }
  createAddress(payload: any) { return this.http.post(`${environment.apiUrl}/addresses`, payload); }
  updateAddress(id: number, payload: any) { return this.http.put(`${environment.apiUrl}/addresses/${id}`, payload); }
  deleteAddress(id: number) { return this.http.delete(`${environment.apiUrl}/addresses/${id}`); }

  // Payments (pay for current order)
  payForCurrentOrder(payload: any) { return this.http.post(`${environment.apiUrl}/orders/current/pay`, payload); }
  payForOrder(orderId: number, payload: any) { return this.http.post(`${environment.apiUrl}/orders/${orderId}/pay`, payload); }

  // Delivery
  getCurrentDelivery() { return this.http.get(`${environment.apiUrl}/orders/current/delivery`); }
  updateDeliveryStatus(id: number, status: string) {
    return this.http.put(`${environment.apiUrl}/deliveries/${id}/status`, null, { params: { status } });
  }

  // Auth
  register(dto: any) { return this.http.post(`${environment.apiUrl}/auth/register`, dto); }
  login(dto: any) { return this.http.post(`${environment.apiUrl}/auth/login`, dto); }

  // User
  getCurrentUser() { return this.http.get(`${environment.apiUrl}/user/me`); }

  // Restaurant Owner
  createRestaurant(data: any) { return this.http.post(`${environment.apiUrl}/restaurant-owner/restaurants`, data); }
  updateRestaurant(id: number, data: any) { return this.http.put(`${environment.apiUrl}/restaurant-owner/restaurants/${id}`, data); }
  deleteRestaurant(id: number) { return this.http.delete(`${environment.apiUrl}/restaurant-owner/restaurants/${id}`); }
  createMenuItem(data: any) { return this.http.post(`${environment.apiUrl}/restaurant-owner/menu-items`, data); }
  updateMenuItem(id: number, data: any) { return this.http.put(`${environment.apiUrl}/restaurant-owner/menu-items/${id}`, data); }
  deleteMenuItem(id: number) { return this.http.delete(`${environment.apiUrl}/restaurant-owner/menu-items/${id}`); }
  getRestaurantOrders(restaurantId: number) { return this.http.get(`${environment.apiUrl}/restaurant-owner/orders/${restaurantId}`); }
  updateOrderStatus(orderId: number, status: string) {
    return this.http.put(`${environment.apiUrl}/restaurant-owner/orders/${orderId}/status`, null, { params: { status } });
  }

  // Admin
  getAllUsers() { return this.http.get(`${environment.apiUrl}/admin/users`); }
  getUserById(id: number) { return this.http.get(`${environment.apiUrl}/admin/users/${id}`); }
  getAllRestaurantsAdmin() { return this.http.get(`${environment.apiUrl}/admin/restaurants`); }
  getRestaurantByIdAdmin(id: number) { return this.http.get(`${environment.apiUrl}/admin/restaurants/${id}`); }
  getAllOrders() { return this.http.get(`${environment.apiUrl}/admin/orders`); }
  getOrderByIdAdmin(id: number) { return this.http.get(`${environment.apiUrl}/admin/orders/${id}`); }
  getAllDeliveries() { return this.http.get(`${environment.apiUrl}/admin/deliveries`); }
  getDeliveryById(id: number) { return this.http.get(`${environment.apiUrl}/admin/deliveries/${id}`); }
}
