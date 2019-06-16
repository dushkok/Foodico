package com.example.foodico.Model;

import com.google.firebase.database.Exclude;

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

    @Exclude
    public float getTotalPrice() {
        float sum = 0;
        for (Item item : cartItems) {
            sum += (item.getPrice() * item.getQuantity());
        }
        return sum;
    }

    @Exclude
    public String getCartContent() {
        StringBuilder builder = new StringBuilder();
        if (getCartItems().size() == 1) {
            return getCartItems().get(0).getName();
        } else {
            for (int i = 0; i < getCartItems().size() - 1; i++) {
                builder.append(getCartItems().get(i).getName());
                builder.append(", ");
            }
            builder.append(getCartItems().get(getCartItems().size() - 1).getName());
        }
        return builder.toString();
    }
}
