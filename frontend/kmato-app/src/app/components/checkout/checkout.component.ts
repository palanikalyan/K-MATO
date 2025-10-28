import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { Address, CreateOrderDto } from '../../models';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  addresses: Address[] = [];
  selectedAddress: number | null = null;
  paymentMethod = 'CARD';
  specialInstructions = '';
  loadingAddresses = false;
  placingOrder = false;
  showAddressForm = false;
  
  newAddress: Partial<Address> = {
    street: '',
    city: '',
    state: '',
    zipCode: '',
    addressType: 'HOME'
  };

  deliveryFee = 40;
  platformFee = 5;
  gst = 0.05;

  constructor(
    public cart: CartService, 
    private readonly api: ApiService, 
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadAddresses();
  }

  loadAddresses(): void {
    this.loadingAddresses = true;
    this.api.getUserAddresses().subscribe({
      next: (res: any) => {
        if (res.success && res.data) {
          this.addresses = res.data;
          if (this.addresses.length > 0 && this.addresses[0].id) {
            this.selectedAddress = this.addresses[0].id;
          }
        }
        this.loadingAddresses = false;
      },
      error: (err) => {
        console.error('Failed to load addresses', err);
        this.loadingAddresses = false;
      }
    });
  }

  selectAddress(id: number | undefined): void {
    if (id !== undefined) {
      this.selectedAddress = id;
    }
  }

  saveAddress(): void {
    if (!this.newAddress.street || !this.newAddress.city || !this.newAddress.state || !this.newAddress.zipCode) {
      alert('Please fill all required fields');
      return;
    }

    this.api.createAddress(this.newAddress as Address).subscribe({
      next: (res: any) => {
        if (res.success && res.data) {
          this.addresses.push(res.data);
          if (res.data.id) {
            this.selectedAddress = res.data.id;
          }
          this.cancelAddressForm();
        }
      },
      error: (err) => {
        console.error('Failed to save address', err);
        alert('Failed to save address. Please try again.');
      }
    });
  }

  cancelAddressForm(): void {
    this.showAddressForm = false;
    this.newAddress = {
      street: '',
      city: '',
      state: '',
      zipCode: '',
      addressType: 'HOME'
    };
  }

  get subtotal(): number {
    return this.cart.total();
  }

  get tax(): number {
    return this.subtotal * this.gst;
  }

  get totalAmount(): number {
    return this.subtotal + this.deliveryFee + this.platformFee + this.tax;
  }

  placeOrder(): void {
    if (!this.selectedAddress) {
      alert('Please select a delivery address');
      return;
    }

    if (this.cart.getItems().length === 0) {
      alert('Your cart is empty');
      return;
    }

    this.placingOrder = true;

    // Assume single-restaurant cart
    const restaurantId = this.cart.getItems()[0].restaurantId;
    if (!restaurantId) {
      alert('Invalid restaurant information');
      this.placingOrder = false;
      return;
    }

    const items = this.cart.getItems().map(item => ({
      menuItemId: item.id,
      quantity: item.quantity
    }));

    const orderData: CreateOrderDto = {
      restaurantId,
      deliveryAddressId: this.selectedAddress,
      items,
      paymentMethod: this.paymentMethod as 'CARD' | 'CASH',
      specialInstructions: this.specialInstructions
    };

    this.api.createOrder(orderData).subscribe({
      next: (res: any) => {
        if (res.success && res.data) {
          // Order placed successfully - no payment processing needed
          this.cart.clear();
          this.placingOrder = false;
          alert('Order placed successfully!');
          this.router.navigate(['/orders']);
        } else {
          alert('Failed to create order');
          this.placingOrder = false;
        }
      },
      error: (err) => {
        console.error('Order creation failed', err);
        alert('Failed to place order. Please try again.');
        this.placingOrder = false;
      }
    });
  }
}
