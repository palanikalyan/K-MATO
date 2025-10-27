import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { MenuItem } from '../../models';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
  deliveryFee = 40;
  platformFee = 5;
  gst = 0.05; // 5% GST

  constructor(public cart: CartService, private router: Router) {}

  get items() {
    return this.cart.getItems();
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

  updateQuantity(item: MenuItem & { quantity: number }, newQuantity: number): void {
    if (newQuantity <= 0) {
      this.removeItem(item);
    } else {
      const updatedItem = { ...item, quantity: newQuantity };
      this.cart.removeItem(item.id);
      this.cart.addItem(updatedItem);
    }
  }

  incrementQuantity(item: MenuItem & { quantity: number }): void {
    this.updateQuantity(item, item.quantity + 1);
  }

  decrementQuantity(item: MenuItem & { quantity: number }): void {
    this.updateQuantity(item, item.quantity - 1);
  }

  removeItem(item: MenuItem & { quantity: number }): void {
    this.cart.removeItem(item.id);
  }

  clearCart(): void {
    if (confirm('Are you sure you want to clear your cart?')) {
      this.cart.clear();
    }
  }

  goToCheckout(): void {
    this.router.navigate(['/checkout']);
  }

  continueShopping(): void {
    this.router.navigate(['/restaurants']);
  }
}
