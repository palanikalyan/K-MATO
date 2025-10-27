import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RestaurantsComponent } from './components/restaurants/restaurants.component';
import { RestaurantDetailComponent } from './components/restaurant-detail/restaurant-detail.component';
import { CartComponent } from './components/cart/cart.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { OrdersComponent } from './components/orders/orders.component';
import { LoginComponent } from './components/auth/login.component';
import { RegisterComponent } from './components/auth/register.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard.component';
import { OwnerDashboardComponent } from './components/owner/owner-dashboard.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard, restaurantOwnerGuard } from './guards/role.guard';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: RestaurantsComponent, canActivate: [authGuard] },
  { path: 'restaurants', component: RestaurantsComponent, canActivate: [authGuard] },
  { path: 'restaurant/:id', component: RestaurantDetailComponent, canActivate: [authGuard] },
  { path: 'cart', component: CartComponent, canActivate: [authGuard] },
  { path: 'checkout', component: CheckoutComponent, canActivate: [authGuard] },
  { path: 'orders', component: OrdersComponent, canActivate: [authGuard] },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard, adminGuard] },
  { path: 'owner', component: OwnerDashboardComponent, canActivate: [authGuard, restaurantOwnerGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
