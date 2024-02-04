import { IsString, IsPhoneNumber, IsNotEmpty, IsDate } from 'class-validator';

export class registerDTO {
  @IsPhoneNumber()
  phone_number: string;

  @IsString()
  @IsNotEmpty()
  password: string;

  @IsString()
  retype_password: string;

  @IsDate()
  date_of_birth: Date;

  @IsString()
  fullname: string;

  @IsString()
  @IsNotEmpty()
  address: string;
  facebook_account_id: number = 0;
  google_account_id: number = 0;
  role_id: number = 1;

  constructor(data: any) {
    this.fullname = data.fullname;
    this.phone_number = data.phone_number;
    this.password = data.password;
    this.retype_password = data.retype_password;
    this.date_of_birth = data.date_of_birth;
    this.address = data.address;
    this.google_account_id = data.google_account_id || 0;
    this.facebook_account_id = data.facebook_account_id || 0;
    this.role_id = data.role_id || 1;
  }
}
