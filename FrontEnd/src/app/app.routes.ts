import { Routes } from '@angular/router';
import { Welcome } from './welcome/welcome';
import { Login } from './login/login';
import { Signup } from './signup/signup';
import { ForgotPasswordComponent } from './forgot-password/forgot-password';
import { TransactionComponent } from './transaction/transaction';
import { HomeComponent } from './home/home'; 
import { ContactUs } from './contect-us/contect-us'; 
import { RecurringTransactionComponent } from './recurring-transaction/recurring-transaction';
import { About } from './about/about';
import { AdminGuard } from './services/admin.guard';
import { Dashboard } from './dashboard/dashboard';

export const routes: Routes = [
  { path: 'welcome', component: Welcome },
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'signup', component: Signup },
  { path: '', redirectTo: 'signup', pathMatch: 'full' },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: '', redirectTo: 'forgot-password', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard, canActivate: [AdminGuard] },
  { path: 'home', component: HomeComponent },  
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'transaction', component: TransactionComponent },
  { path: '', redirectTo: 'transaction', pathMatch: 'full' },
  { path: 'contect-us', component: ContactUs },
  { path: '', redirectTo: 'contect-us', pathMatch: 'full' },
  { path: 'recurring-transaction', component: RecurringTransactionComponent},
  { path: '', redirectTo: 'recurring-transaction', pathMatch: 'full' },
  { path: 'about', component: About},
  { path: '', redirectTo: 'about', pathMatch: 'full' },
];


