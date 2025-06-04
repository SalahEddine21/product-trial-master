import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { CartService } from 'app/cart/data-access/cart.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [NgIf, NgFor, CommonModule, ButtonModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  private readonly cartService = inject(CartService);

  get cartItems() {
    return this.cartService.cart();
  }

  get totalPrice() {
    return this.cartItems.reduce(
      (total, item) => total + item.product.price * (item.product.quantity || 1),
      0
    );
  }

  removeProduct(itemId: string) {
    this.cartService.removeProduct(itemId);
  }

  clearCart() {
    this.cartService.clearCart();
  }
}
