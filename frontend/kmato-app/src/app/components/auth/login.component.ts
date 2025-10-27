import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'] // optional if you want to add extra CSS
})
export class LoginComponent {
  email = '';
  password = '';
  error: string | null = null;
  loading = false;

  constructor(private api: ApiService, private auth: AuthService, private router: Router) {}

  onSubmit() {
    this.error = null;
    this.loading = true;
    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: (res: any) => {
        this.loading = false;
        console.log('Login response:', res);
        
        // Extract from ApiResponse wrapper: { success: true, message: "...", data: { token, id, email, ... } }
        const responseData = res?.data || res;
        const token = responseData?.token;
        const userInfo = {
          id: responseData?.id,
          email: responseData?.email,
          fullName: responseData?.fullName,
          role: responseData?.role,
          phone: responseData?.phone
        };

        console.log('Extracted token:', token);
        console.log('Extracted user:', userInfo);

        if (!token) {
          this.error = 'No token received';
          return;
        }

        // Save token and user
        this.auth.saveToken(token);
        if (userInfo.id) {
          this.auth.saveUser(userInfo as any);
        }

        // Redirect based on role
        const role = (userInfo?.role || '').toString().toUpperCase();
        console.log('User role:', role);
        
        if (role === 'ADMIN') {
          console.log('Redirecting to /admin');
          this.router.navigate(['/admin']);
        } else if (role === 'RESTAURANT_OWNER' || role === 'OWNER') {
          console.log('Redirecting to /owner');
          this.router.navigate(['/owner']);
        } else {
          console.log('Redirecting to /home');
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Login failed';
      }
    });
  }
}
