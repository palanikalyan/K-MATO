import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

interface Restaurant {
  id: number;
  name: string;
  description: string;
  address: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  ownerId: number;
  ownerName?: string;
  createdAt: string;
}

interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  phone: string;
}

interface Order {
  id: number;
  userId: number;
  restaurantId: number;
  status: string;
  totalAmount: number;
  createdAt: string;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  activeTab: 'restaurants' | 'users' | 'orders' | 'stats' = 'restaurants';
  
  pendingRestaurants: Restaurant[] = [];
  approvedRestaurants: Restaurant[] = [];
  rejectedRestaurants: Restaurant[] = [];
  
  users: User[] = [];
  orders: Order[] = [];
  
  stats = {
    totalUsers: 0,
    totalRestaurants: 0,
    totalOrders: 0,
    pendingApprovals: 0,
    totalRevenue: 0
  };

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/']);
      return;
    }
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.loadPendingRestaurants();
    this.loadApprovedRestaurants();
    this.loadUsers();
    this.loadOrders();
    this.loadStats();
  }

  loadPendingRestaurants() {
    this.http.get<any>(`${environment.apiUrl}/admin/restaurants/pending`)
      .subscribe({
        next: (response) => {
          this.pendingRestaurants = response.data || response || [];
        },
        error: (err) => console.error('Error loading pending restaurants:', err)
      });
  }

  loadApprovedRestaurants() {
    this.http.get<any>(`${environment.apiUrl}/admin/restaurants`)
      .subscribe({
        next: (response) => {
          const allRestaurants = response.data || response || [];
          this.approvedRestaurants = allRestaurants.filter((r: any) => r.approvalStatus === 'APPROVED');
        },
        error: (err) => console.error('Error loading approved restaurants:', err)
      });
  }

  loadUsers() {
    this.http.get<any>(`${environment.apiUrl}/admin/users`)
      .subscribe({
        next: (response) => {
          this.users = response.data || response || [];
        },
        error: (err) => console.error('Error loading users:', err)
      });
  }

  loadOrders() {
    this.http.get<any>(`${environment.apiUrl}/admin/orders`)
      .subscribe({
        next: (response) => {
          this.orders = response.data || response || [];
        },
        error: (err) => console.error('Error loading orders:', err)
      });
  }

  loadStats() {
    // Calculate stats from loaded data
    this.stats = {
      totalUsers: this.users.length,
      totalRestaurants: this.pendingRestaurants.length + this.approvedRestaurants.length,
      totalOrders: this.orders.length,
      pendingApprovals: this.pendingRestaurants.length,
      totalRevenue: this.orders.reduce((sum, o) => sum + (o.totalAmount || 0), 0)
    };
  }

  approveRestaurant(restaurantId: number) {
    this.http.post(`${environment.apiUrl}/admin/restaurants/${restaurantId}/approve`, {})
      .subscribe({
        next: () => {
          alert('Restaurant approved successfully!');
          this.loadDashboardData();
        },
        error: (err) => alert('Error approving restaurant: ' + err.message)
      });
  }

  rejectRestaurant(restaurantId: number) {
    const reason = prompt('Enter rejection reason (optional):');
    this.http.post(`${environment.apiUrl}/admin/restaurants/${restaurantId}/reject`, { reason })
      .subscribe({
        next: () => {
          alert('Restaurant rejected');
          this.loadDashboardData();
        },
        error: (err) => alert('Error rejecting restaurant: ' + err.message)
      });
  }

  deleteRestaurant(restaurantId: number) {
    if (!confirm('Are you sure you want to delete this restaurant?')) return;
    
    this.http.delete(`${environment.apiUrl}/admin/restaurants/${restaurantId}`)
      .subscribe({
        next: () => {
          alert('Restaurant deleted');
          this.loadDashboardData();
        },
        error: (err) => alert('Error deleting restaurant: ' + err.message)
      });
  }

  updateUserRole(userId: number, newRole: string) {
    this.http.put(`${environment.apiUrl}/admin/users/${userId}/role`, { role: newRole })
      .subscribe({
        next: () => {
          alert('User role updated');
          this.loadUsers();
        },
        error: (err) => alert('Error updating role: ' + err.message)
      });
  }

  deleteUser(userId: number) {
    if (!confirm('Are you sure you want to delete this user?')) return;
    
    this.http.delete(`${environment.apiUrl}/admin/users/${userId}`)
      .subscribe({
        next: () => {
          alert('User deleted');
          this.loadUsers();
        },
        error: (err) => alert('Error deleting user: ' + err.message)
      });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
