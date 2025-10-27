import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class CartService {
  private key = 'kmato_cart';
  private items: any[] = [];

  constructor() {
    const raw = localStorage.getItem(this.key);
    if (raw) { try { this.items = JSON.parse(raw); } catch { this.items = []; } }
  }

  getItems() { return this.items; }
  
  getItemCount(): number {
    return this.items.reduce((total, item) => total + (item.quantity || 1), 0);
  }
  
  addItem(item: any) {
    const found = this.items.find(i => i.id === item.id);
    if (found) found.quantity = (found.quantity || 1) + (item.quantity || 1);
    else this.items.push(item);
    this.save();
  }
  removeItem(id: number) { this.items = this.items.filter(i => i.id !== id); this.save(); }
  clear() { this.items = []; this.save(); }
  total() { return this.items.reduce((s, i) => s + (i.price || 0) * (i.quantity || 1), 0); }

  private save() { localStorage.setItem(this.key, JSON.stringify(this.items)); }
}
