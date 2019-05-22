package com.example.foodico.Model;

import java.util.List;

public class Cart {

    private List<Item> cartItems;

    public Cart() {
    }

    public Cart(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Item> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    public boolean addItem(Item item) {
        return cartItems.add(item);
    }

    public Item removeItem(int i) {
        return cartItems.remove(i);
    }

    public int getTotalPrice() {
        int sum = 0;
        for (Item item : cartItems) {
            sum += (item.getPrice() * item.getQuantity());
        }
        return sum;
    }
}
