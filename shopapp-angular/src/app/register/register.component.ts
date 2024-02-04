import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { registerDTO } from '../dtos/user/register.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  phone: string;
  password: string;
  retypePassword: string;
  address: string;
  fullName: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor(private route: Router, private userService: UserService) {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
  }

  onPhoneChange() {
    console.log('>>> Check phone change' + this.phone);
  }
  onPasswordChange() {
    console.log('>>> Check password change' + this.password);
  }
  onRetypePasswordChange() {
    console.log('>>> Check  retype password change' + this.retypePassword);
  }
  onFullNameChange() {
    console.log('>>> Check fullName change' + this.fullName);
  }

  onAddressesChanged() {
    console.log('>>> Check address change' + this.address);
  }
  onChecked() {
    this.isAccepted = true;
  }

  register() {
    const registerData: registerDTO = {
      phone_number: this.phone,
      password: this.password,
      retype_password: this.retypePassword,
      date_of_birth: this.dateOfBirth,
      fullname: this.fullName,
      address: this.address,
      facebook_account_id: 0,
      google_account_id: 0,
      role_id: 1,
    };

    this.userService.register(registerData).subscribe({
      next: (response: any) => {
        debugger;
        this.route.navigate(['login']);
      },
      complete: () => {
        debugger;
      },
      error: (err) => {
        alert('Cant register user: ' + err.error);
      },
    });
  }

  checkPasswordMatch() {
    console.log(this.password === this.retypePassword);
    if (this.password != this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({
        passwordMismatch: true,
      });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }
  checkAge() {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      if (
        monthDiff < 0 ||
        (monthDiff === 0 && today.getDay() < birthDate.getDay())
      ) {
        age--;
      }

      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({
          invalidAge: true,
        });
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}
