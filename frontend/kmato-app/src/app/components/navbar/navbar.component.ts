import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';
import { Subscription } from 'rxjs';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  searchQuery: string = '';
  showUserMenu: boolean = false;
  showMobileMenu: boolean = false;
  currentUser: User | null = null;
  private sub: Subscription | null = null;

  constructor(
    public auth: AuthService,
    public cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // subscribe to auth currentUser so navbar updates immediately after login/logout
    this.sub = this.auth.currentUser$.subscribe(u => {
      this.currentUser = u;
    });
  }

  ngOnDestroy(): void {
    if (this.sub) this.sub.unsubscribe();
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      // Navigate to search results or filter restaurants
      this.router.navigate(['/'], { queryParams: { search: this.searchQuery } });
    }
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  toggleMobileMenu(): void {
    this.showMobileMenu = !this.showMobileMenu;
  }

  closeMobileMenu(): void {
    this.showMobileMenu = false;
  }

  logout(): void {
    this.auth.logout();
    this.showUserMenu = false;
    this.showMobileMenu = false;
    this.router.navigate(['/login']);
  }

  // Close dropdowns when clicking outside
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-menu')) {
      this.showUserMenu = false;
    }
    if (!target.closest('.mobile-menu') && !target.closest('.mobile-menu-btn')) {
      this.showMobileMenu = false;
    }
  }
}
