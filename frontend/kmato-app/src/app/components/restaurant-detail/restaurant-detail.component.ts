import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CartService } from '../../services/cart.service';
import { Restaurant, MenuItem } from '../../models';

@Component({
  selector: 'app-restaurant-detail',
  templateUrl: './restaurant-detail.component.html',
  styleUrls: ['./restaurant-detail.component.css']
})
export class RestaurantDetailComponent implements OnInit {
  restaurant: Restaurant | null = null;
  menu: MenuItem[] = [];
  loading = true;
  error = '';
  selectedCategory = 'all';
  categories: string[] = [];
  quantities: { [key: number]: number } = {};

  constructor(
    private route: ActivatedRoute, 
    private router: Router,
    private api: ApiService, 
    private cart: CartService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.error = 'Invalid restaurant ID';
      this.loading = false;
      return;
    }

    this.loadRestaurant(id);
    this.loadMenu(id);
  }

  loadRestaurant(id: number): void {
    this.api.getRestaurantById(id).subscribe({
      next: (res: any) => {
        if (res.success && res.data) {
          this.restaurant = res.data;
        } else {
          this.error = 'Restaurant not found';
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load restaurant';
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadMenu(id: number): void {
    this.api.getMenuByRestaurant(id).subscribe({
      next: (res: any) => {
        if (res.success && res.data) {
          this.menu = res.data;
          this.extractCategories();
          this.menu.forEach(item => this.quantities[item.id] = 1);
        }
      },
      error: (err) => {
        console.error('Failed to load menu', err);
      }
    });
  }

  extractCategories(): void {
    const uniqueCategories = Array.from(new Set(this.menu.map(item => item.category).filter(c => c)));
    this.categories = ['all', ...uniqueCategories];
  }

  get filteredMenu(): MenuItem[] {
    if (this.selectedCategory === 'all') return this.menu;
    return this.menu.filter(item => item.category === this.selectedCategory);
  }

  filterByCategory(category: string): void {
    this.selectedCategory = category;
  }

  incrementQuantity(itemId: number): void {
    this.quantities[itemId] = (this.quantities[itemId] || 1) + 1;
  }

  decrementQuantity(itemId: number): void {
    if (this.quantities[itemId] > 1) {
      this.quantities[itemId]--;
    }
  }

  addToCart(item: MenuItem): void {
    const quantity = this.quantities[item.id] || 1;
    this.cart.addItem({ ...item, quantity });
    // Reset quantity after adding
    this.quantities[item.id] = 1;
  }

  goBack(): void {
    this.router.navigate(['/restaurants']);
  }
}
