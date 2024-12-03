import { Injectable, inject, signal } from "@angular/core";
import { Product } from "../../products/data-access/product.model";
import { Cart } from "./cart.model";

@Injectable({
  providedIn: "root",
})
export class CartService {
  private readonly _cart = signal<Cart[]>([]);

  public readonly cart = this._cart.asReadonly();

  addProduct(product: Product): void {
    const cartItems = this._cart();
    const newItem : Cart = { id: `${product.id}-${Date.now()}`, product: product };
    this._cart.set([...cartItems, newItem]);
  }

  removeProduct(itemId: string): void {
    this._cart.set(this._cart().filter((item) => item.id !== itemId));
  }

  clearCart(): void {
    this._cart.set([]);
  }
}