import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

interface Restaurant {
  id?: number;
  name: string;
  description: string;
  address: string;
  city: string;
  cuisineType: string;
  phoneNumber: string;
  openingHours: string;
  approvalStatus?: 'PENDING' | 'APPROVED' | 'REJECTED';
  rejectionReason?: string;
  createdAt?: string;
}

interface MenuItem {
  id?: number;
  name: string;
  description: string;
  price: number;
  category: string;
  available: boolean;
  restaurantId?: number;
}

@Component({
  selector: 'app-owner-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './owner-dashboard.component.html',
  styleUrls: ['./owner-dashboard.component.css']
})
export class OwnerDashboardComponent implements OnInit {
  activeTab: 'restaurants' | 'menu' | 'orders' = 'restaurants';
  
  myRestaurants: Restaurant[] = [];
  selectedRestaurant: Restaurant | null = null;
  
  showCreateForm = false;
  newRestaurant: Restaurant = this.getEmptyRestaurant();
  
  menuItems: MenuItem[] = [];
  showMenuForm = false;
  newMenuItem: MenuItem = this.getEmptyMenuItem();
  
  orders: any[] = [];

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (!this.authService.isRestaurantOwner() && !this.authService.isAdmin()) {
      this.router.navigate(['/']);
      return;
    }
    this.loadMyRestaurants();
  }

  getEmptyRestaurant(): Restaurant {
    return {
      name: '',
      description: '',
      address: '',
      city: '',
      cuisineType: '',
      phoneNumber: '',
      openingHours: '9:00 AM - 10:00 PM'
    };
  }

  getEmptyMenuItem(): MenuItem {
    return {
      name: '',
      description: '',
      price: 0,
      category: '',
      available: true
    };
  }

  loadMyRestaurants() {
    this.http.get<any>(`${environment.apiUrl}/restaurants/owner`)
      .subscribe({
        next: (response) => {
          const data = response.data || response || [];
          this.myRestaurants = data;
          if (data.length > 0 && !this.selectedRestaurant) {
            this.selectedRestaurant = data[0];
            this.loadMenuItems(data[0].id!);
          }
        },
        error: (err) => console.error('Error loading restaurants:', err)
      });
  }

  loadMenuItems(restaurantId: number) {
    this.http.get<any>(`${environment.apiUrl}/menu-items/restaurant/${restaurantId}`)
      .subscribe({
        next: (response) => {
          this.menuItems = response.data || response || [];
        },
        error: (err) => console.error('Error loading menu:', err)
      });
  }

  loadOrders() {
    // Load all orders for the owner's restaurants
    this.http.get<any>(`${environment.apiUrl}/restaurant-owner/restaurants/orders`)
      .subscribe({
        next: (response) => {
          this.orders = response.data || response || [];
        },
        error: (err) => console.error('Error loading orders:', err)
      });
  }

  submitRestaurant() {
    this.http.post<any>(`${environment.apiUrl}/restaurants`, this.newRestaurant)
      .subscribe({
        next: (response) => {
          const restaurant = response.data || response;
          alert('Restaurant submitted for approval! Admin will review it soon.');
          this.myRestaurants.push(restaurant);
          this.showCreateForm = false;
          this.newRestaurant = this.getEmptyRestaurant();
        },
        error: (err) => alert('Error submitting restaurant: ' + (err.error?.message || err.message))
      });
  }

  selectRestaurant(restaurant: Restaurant) {
    this.selectedRestaurant = restaurant;
    if (restaurant.id) {
      this.loadMenuItems(restaurant.id);
    }
  }

  addMenuItem() {
    if (!this.selectedRestaurant?.id) {
      alert('Please select a restaurant first');
      return;
    }

    this.newMenuItem.restaurantId = this.selectedRestaurant.id;
    
    this.http.post<any>(`${environment.apiUrl}/menu-items`, this.newMenuItem)
      .subscribe({
        next: (response) => {
          const item = response.data || response;
          alert('Menu item added successfully!');
          this.menuItems.push(item);
          this.showMenuForm = false;
          this.newMenuItem = this.getEmptyMenuItem();
        },
        error: (err) => alert('Error adding menu item: ' + (err.error?.message || err.message))
      });
  }

  toggleMenuItemAvailability(item: MenuItem) {
    if (!item.id) return;
    
    this.http.put(`${environment.apiUrl}/menu-items/${item.id}`, { ...item, available: !item.available })
      .subscribe({
        next: () => {
          item.available = !item.available;
        },
        error: (err) => alert('Error updating item: ' + err.message)
      });
  }

  deleteMenuItem(itemId: number) {
    if (!confirm('Are you sure you want to delete this menu item?')) return;
    
    this.http.delete(`${environment.apiUrl}/menu-items/${itemId}`)
      .subscribe({
        next: () => {
          this.menuItems = this.menuItems.filter(i => i.id !== itemId);
          alert('Menu item deleted');
        },
        error: (err) => alert('Error deleting item: ' + err.message)
      });
  }

  getPendingRestaurantsCount(): number {
    return this.myRestaurants.filter(r => r.approvalStatus === 'PENDING').length;
  }

  updateOrderStatus(orderId: number, newStatus: string) {
    this.http.put<any>(`${environment.apiUrl}/restaurant-owner/orders/${orderId}/status?status=${newStatus}`, {})
      .subscribe({
        next: (response) => {
          // Update local order
          const order = this.orders.find(o => o.id === orderId);
          if (order) {
            order.status = newStatus;
          }
          alert(`Order #${orderId} status updated to ${newStatus}`);
        },
        error: (err) => alert('Error updating order status: ' + err.message)
      });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
