import {Injectable} from '@angular/core';
import {Product} from "../model/Product";
import {OrderItem} from "../model/OrderItem";
import * as CryptoJS from 'crypto-js';


@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {
  private cartItems: OrderItem[] = [];

  private key = CryptoJS.enc.Utf8.parse("1203");
  private iv = CryptoJS.enc.Utf8.parse("1203");

  constructor() {
    this.loadCartItems();
  }

  addToCart(product: Product, notIngredients: String): boolean {
    const existingItem = this.cartItems.find(item => item.productId === product.id);
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      if (this.cartItems.length >= 8) {
        return false;
      }
      const newItem: OrderItem = {
        productId: product.id,
        productName: product.name,
        price: product.price,
        productImage: product.image,
        description: product.description,
        quantity: 1,
        notIngredients: notIngredients,
        categoryName: product.category.name,
        pizzaNumber: product.pizzaNumber
      };
      this.cartItems.push(newItem);
    }
    this.saveCartItems();
    return true;
  }

  removeFromCart(orderItem: OrderItem): OrderItem[] {
    const index = this.cartItems.findIndex(item => item.productId === orderItem.productId);
    if (index !== -1) {
      const existingItem = this.cartItems[index];
      existingItem.quantity -= 1;
      if (existingItem.quantity === 0) {
        this.cartItems.splice(index, 1);
      }
      if (existingItem.quantity < 0) {
        this.cartItems.splice(index, 1);
      }
      this.saveCartItems();
    }
    return this.cartItems;
  }

  getNumberOfCartItems() {
    return this.cartItems.length;
  }

  getCartItems(): OrderItem[] {
    this.loadCartItems();
    return this.cartItems;
  }

  private saveCartItems(): void {
    localStorage.setItem('cartItems', this.encrypt(JSON.stringify(this.cartItems)) as string)
  }

  private loadCartItems(): void {
    const storedItems = localStorage.getItem('cartItems');
    if (storedItems) {
      this.cartItems = JSON.parse(this.decrypt(storedItems));
    }

  }

  calculateTotal(): number {
    return this.getCartItems().map(a => a.price * a.quantity).reduce((a, b) => a + b, 0);
  }

  encrypt(message: string): String {
    const encrypted = CryptoJS.AES.encrypt(message, this.key, {
      keySize: 8 / 2,
      iv: this.iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
  }

  decrypt(message: string) {
    const decrypted = CryptoJS.AES.decrypt(message, this.key, {
      keySize: 8 / 2,
      iv: this.iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
  }
}

