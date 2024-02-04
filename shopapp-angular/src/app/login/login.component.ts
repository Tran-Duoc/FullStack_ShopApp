import { Component, ViewChild } from '@angular/core';
import { LoginDTO } from '../dtos/user/login.dto';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  @ViewChild('loginForm') registerForm!: NgForm;
  phoneNumber: string;
  password: string;
  constructor(private route: Router, private userService: UserService) {
    this.phoneNumber = '';
    this.password = '';
  }

  onPhoneChange() {
    console.log(`>> the phone number has been changed to ${this.phoneNumber}`);
  }

  login() {
    const loginData: LoginDTO = {
      phone_number: this.phoneNumber,
      password: this.password,
    };
    console.log(loginData);
    this.userService.login(loginData).subscribe({
      next: (response: any) => {
        console.log(response);
        debugger;
        // this.route.navigate(['login']);
      },
      complete: () => {
        debugger;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
