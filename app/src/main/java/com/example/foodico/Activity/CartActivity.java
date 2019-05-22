package com.example.foodico.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodico.Adapter.CartAdapter;
import com.example.foodico.Helper.DatabaseHelper;
import com.example.foodico.Model.Cart;
import com.example.foodico.Model.Item;
import com.example.foodico.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.cartRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.cartTotal)
    TextView cartTotal;
    @BindView(R.id.placeOrderButton)
    Button placeOrderButton;
    private DatabaseHelper databaseHelper;
    private List<Item> cartItems;
    private CartAdapter cartAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        databaseHelper = new DatabaseHelper(this);
        cartItems = databaseHelper.getItemsInCart();
        cartAdapter = new CartAdapter(cartItems, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");
    }

    public void update() {
        cartTotal.setText("Total sum: " + new Cart(cartItems).getTotalPrice() + "$");
    }

    public void onDelete() {
        cartItems = databaseHelper.getItemsInCart();
        recyclerView.setAdapter(new CartAdapter(cartItems, this));
        recyclerView.invalidate();
        cartTotal.setText("Total sum: " + new Cart(cartItems).getTotalPrice() + "$");
    }
}