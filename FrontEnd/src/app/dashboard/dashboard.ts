import { Component, Inject, PLATFORM_ID, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements AfterViewInit {
  isBrowser: boolean;

  @ViewChild('transactionsChart') transactionsChart!: ElementRef<HTMLCanvasElement>;
  @ViewChild('recurringChart') recurringChart!: ElementRef<HTMLCanvasElement>;
  @ViewChild('lineChart') lineChart!: ElementRef<HTMLCanvasElement>;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: Auth,
    private router: Router
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  /** ===== NAVBAR METHODS ===== */
  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
  goHome() { this.router.navigate(['/home']); }
  goToTransaction() { this.router.navigate(['/transaction']); }
  goToRecurring() { this.router.navigate(['/recurring-transaction']); }
  goToDashboard() { this.router.navigate(['/dashboard']); }
  goToContact() { this.router.navigate(['/contact-us']); } // corrected spelling
  goToAbout() { this.router.navigate(['/about']); }
  goToLogin() { this.router.navigate(['/login']); }
  goToRegister() { this.router.navigate(['/welcome']); }

  /** ===== DRAW CHARTS AFTER VIEW INIT ===== */
  ngAfterViewInit(): void {
    if (!this.isBrowser) return;

    // Transactions Bar Chart
    new Chart(this.transactionsChart.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          label: 'Transactions',
          data: [500, 800, 650, 900, 700, 950],
          backgroundColor: '#e74c3c'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: { y: { beginAtZero: true } }
      }
    });

    // Recurring Expense Doughnut
    new Chart(this.recurringChart.nativeElement, {
      type: 'doughnut',
      data: {
        labels: ['Rent', 'Utilities', 'Subscriptions', 'Others'],
        datasets: [{
          data: [40, 25, 20, 15],
          backgroundColor: ['#e74c3c', '#f39c12', '#3498db', '#2ecc71']
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });

    // Monthly Savings Line Chart
    new Chart(this.lineChart.nativeElement, {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          data: [400, 600, 550, 750, 700, 850],
          label: 'Monthly Savings',
          fill: true,
          tension: 0.4,
          borderColor: '#3498db',
          backgroundColor: 'rgba(52,152,219,0.2)'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
}
