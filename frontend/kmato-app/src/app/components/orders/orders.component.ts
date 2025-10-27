import { Component, OnInit, OnDestroy } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { WebsocketService } from '../../services/websocket.service';
import { Router } from '@angular/router';
import { Order } from '../../models';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit, OnDestroy {
  orders: Order[] = [];
  loading = true;
  error = '';
  selectedTab: 'active' | 'history' = 'active';
  private wsSubscription?: Subscription;

  constructor(
    private readonly api: ApiService,
    private readonly ws: WebsocketService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadOrders();
    this.subscribeToOrderUpdates();
  }

  ngOnDestroy(): void {
    this.wsSubscription?.unsubscribe();
  }

  loadOrders(): void {
    this.loading = true;
    this.error = '';
    
    this.api.getCustomerOrders().subscribe({
      next: (res: any) => {
        if (res.success && res.data) {
          this.orders = res.data;
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load orders';
        this.loading = false;
        console.error('Load orders error:', err);
      }
    });
  }

  subscribeToOrderUpdates(): void {
    // Subscribe to WebSocket for real-time order updates
    this.wsSubscription = this.ws.messages().subscribe((message: any) => {
      if (message.type === 'ORDER_UPDATE' && message.data) {
        this.updateOrder(message.data);
      } else if (message.type === 'DELIVERY_UPDATE' && message.data) {
        this.updateDelivery(message.data);
      }
    });
  }

  updateOrder(updatedOrder: Order): void {
    const index = this.orders.findIndex(o => o.id === updatedOrder.id);
    if (index !== -1) {
      this.orders[index] = { ...this.orders[index], ...updatedOrder };
    }
  }

  updateDelivery(deliveryData: any): void {
    const orderId = deliveryData.orderId;
    const index = this.orders.findIndex(o => o.id === orderId);
    if (index !== -1) {
      this.orders[index] = { 
        ...this.orders[index], 
        delivery: deliveryData 
      };
    }
  }

  get activeOrders(): Order[] {
    return this.orders.filter(order => 
      ['PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY'].includes(order.status)
    );
  }

  get orderHistory(): Order[] {
    return this.orders.filter(order => 
      ['DELIVERED', 'CANCELLED'].includes(order.status)
    );
  }

  get displayedOrders(): Order[] {
    return this.selectedTab === 'active' ? this.activeOrders : this.orderHistory;
  }

  selectTab(tab: 'active' | 'history'): void {
    this.selectedTab = tab;
  }

  getStatusClass(status: string): string {
    const statusMap: { [key: string]: string } = {
      'PENDING': 'badge-warning',
      'CONFIRMED': 'badge-info',
      'PREPARING': 'badge-info',
      'OUT_FOR_DELIVERY': 'badge-primary',
      'DELIVERED': 'badge-success',
      'CANCELLED': 'badge-error'
    };
    return statusMap[status] || 'badge-secondary';
  }

  getStatusProgress(status: string): number {
    const progressMap: { [key: string]: number } = {
      'PENDING': 25,
      'CONFIRMED': 40,
      'PREPARING': 60,
      'OUT_FOR_DELIVERY': 80,
      'DELIVERED': 100,
      'CANCELLED': 0
    };
    return progressMap[status] || 0;
  }

  cancelOrder(orderId: number): void {
    if (!confirm('Are you sure you want to cancel this order?')) {
      return;
    }

    this.api.cancelOrder(orderId).subscribe({
      next: (res: any) => {
        if (res.success) {
          this.loadOrders();
        }
      },
      error: (err) => {
        alert('Failed to cancel order');
        console.error('Cancel order error:', err);
      }
    });
  }

  reorder(order: Order): void {
    // Navigate to restaurant with order items
    if (order.restaurantId) {
      this.router.navigate(['/restaurant', order.restaurantId]);
    }
  }

  trackOrder(orderId: number): void {
    // Could open a modal or navigate to detailed tracking page
    console.log('Tracking order:', orderId);
  }

  goToRestaurant(restaurantId?: number): void {
    if (restaurantId) {
      this.router.navigate(['/restaurant', restaurantId]);
    }
  }

  getEstimatedTime(order: Order): string {
    if (order.status === 'DELIVERED') {
      return 'Delivered';
    }
    if (order.status === 'CANCELLED') {
      return 'Cancelled';
    }
    
    // Use actual delivery ETA if available
    if (order.delivery?.etaSeconds) {
      const minutes = Math.ceil(order.delivery.etaSeconds / 60);
      return `${minutes} min`;
    }
    
    // Fallback to mock ETA calculation
    const statusTimes: { [key: string]: number } = {
      'PENDING': 30,
      'CONFIRMED': 25,
      'PREPARING': 15,
      'OUT_FOR_DELIVERY': 10
    };
    
    const minutes = statusTimes[order.status] || 20;
    return `${minutes} min`;
  }

  getDeliveryStatus(order: Order): string {
    if (!order.delivery) {
      return 'Preparing your order';
    }
    
    const statusMessages: { [key: string]: string } = {
      'SCHEDULED': 'Delivery scheduled',
      'PICKED_UP': `${order.delivery.assignedDriver || 'Driver'} picked up your order`,
      'IN_TRANSIT': `${order.delivery.assignedDriver || 'Driver'} is on the way`,
      'DELIVERED': 'Order delivered successfully'
    };
    
    return statusMessages[order.delivery.status] || 'Processing delivery';
  }
}
