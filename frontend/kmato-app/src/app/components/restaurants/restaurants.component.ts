import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-restaurants',
  templateUrl: './restaurants.component.html',
  styleUrls: ['./restaurants.component.css']
})
export class RestaurantsComponent implements OnInit {
  restaurants: any[] = [];
  filteredRestaurants: any[] = [];
  loading = true;
  error: string | null = null;
  selectedFilter: string = 'all';
  viewMode: 'grid' | 'list' = 'grid';
  searchQuery: string = '';

  constructor(
    private api: ApiService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadRestaurants();
    
    // Watch for search query params
    this.route.queryParams.subscribe(params => {
      if (params['search']) {
        this.searchQuery = params['search'];
        this.applySearch();
      }
    });
  }

  loadRestaurants(): void {
    this.api.getRestaurants().subscribe({
      next: (res: any) => {
        this.restaurants = res?.data || [];
        this.filteredRestaurants = [...this.restaurants];
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Failed to load restaurants';
        this.loading = false;
        console.error('Error loading restaurants:', err);
      }
    });
  }

  filterBy(filter: string): void {
    this.selectedFilter = filter;
    this.applyFilters();
  }

  applyFilters(): void {
    let filtered = [...this.restaurants];

    // Apply search
    if (this.searchQuery) {
      const query = this.searchQuery.toLowerCase();
      filtered = filtered.filter(r =>
        r.name.toLowerCase().includes(query) ||
        r.description?.toLowerCase().includes(query) ||
        r.city?.toLowerCase().includes(query)
      );
    }

    // Apply category filter
    switch (this.selectedFilter) {
      case 'rating':
        filtered = filtered.filter(r => (r.rating || 0) >= 4.0);
        filtered.sort((a, b) => (b.rating || 0) - (a.rating || 0));
        break;
      case 'delivery':
        // Sort by delivery time (would need backend support, using rating as proxy)
        filtered.sort((a, b) => (b.rating || 0) - (a.rating || 0));
        break;
      case 'new':
        filtered.sort((a, b) => {
          const dateA = new Date(a.createdAt || 0).getTime();
          const dateB = new Date(b.createdAt || 0).getTime();
          return dateB - dateA;
        });
        break;
    }

    this.filteredRestaurants = filtered;
  }

  applySearch(): void {
    this.applyFilters();
  }

  resetFilters(): void {
    this.selectedFilter = 'all';
    this.searchQuery = '';
    this.filteredRestaurants = [...this.restaurants];
  }

  goToRestaurant(id: number): void {
    this.router.navigate(['/restaurant', id]);
  }

  addToFavorites(restaurant: any, event: Event): void {
    event.stopPropagation();
    // TODO: Implement favorites functionality
    console.log('Add to favorites:', restaurant);
  }
}
