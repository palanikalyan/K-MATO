import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./login.component.css'] // Reuse login CSS
})
export class RegisterComponent {
  firstName = '';
  lastName = '';
  email = '';
  phone = '';
  password = '';
  role = 'USER'; // Default role
  error: string | null = null;

  constructor(private readonly api: ApiService, private readonly router: Router) {}

  onSubmit(): void {
    this.error = null;

    // Validate inputs
    if (!this.firstName || !this.lastName || !this.email || !this.phone || !this.password) {
      this.error = 'All fields are required';
      return;
    }

    const fullName = `${this.firstName} ${this.lastName}`;
    const dto = {
      fullName,
      email: this.email,
      password: this.password,
      phone: this.phone,
      role: this.role
    };

    this.api.register(dto).subscribe({
      next: () => {
        // Navigate to login after successful registration
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.error = err?.error?.message || 'Registration failed. Please try again.';
        console.error('Registration error:', err);
      }
    });
  }
}
