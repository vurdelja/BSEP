export interface User {
  id: number;
  email: string;
  password: string;
  passwordConfirm: string;
  address: string;
  city: string;
  country: string;
  phoneNumber: string;
  userType: string;
  firstName: string;
  lastName: string;
  companyName?: string;
  pib?: string;
  packageType: string;
  role: string;
}
