package com.example.foodico.Model;

public class Order {

    private Cart cart;
    private String address;
    private String userEmail;
    private String userToken;

    private String time;

    public Order() {
    }

    public Order(Cart cart, String address, String userEmail, String userToken, String time) {
        this.cart = cart;
        this.address = address;
        this.userEmail = userEmail;
        this.userToken = userToken;
        this.time = time;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
