package com.example.foodico.Activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodico.Adapter.CartAdapter;
import com.example.foodico.Helper.DatabaseHelper;
import com.example.foodico.Model.Cart;
import com.example.foodico.Model.Item;
import com.example.foodico.Model.Order;
import com.example.foodico.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.cartRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.cartTotal)
    TextView cartTotal;
    @BindView(R.id.placeOrderButton)
    Button placeOrderButton;
    @BindView(R.id.emptyCart)
    TextView emptyCart;
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
        if (cartItems.size() == 0) {
            placeOrderButton.setVisibility(View.INVISIBLE);
            emptyCart.setVisibility(View.VISIBLE);
        } else {
            update();
        }
    }

    public void update() {
        cartTotal.setText("Total sum: " + new Cart(cartItems).getTotalPrice() + "$");
        cartTotal.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
    }

    public void onDelete() {
        cartItems = databaseHelper.getItemsInCart();
        cartTotal.setText("Total sum: " + new Cart(cartItems).getTotalPrice() + "$");
        cartTotal.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        if (cartItems.size() == 0) {
            placeOrderButton.setVisibility(View.INVISIBLE);
            cartTotal.setText("");
            emptyCart.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(new CartAdapter(cartItems, this));
        recyclerView.invalidate();
    }

    @OnClick(R.id.placeOrderButton)
    public void onPlaceOrderButtonClick() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
        dialog.setTitle("Enter your shipping address");
        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Address");
        input.setTextColor(Color.BLACK);
        dialog.setView(input);
        dialog.setPositiveButton("Place order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().length() != 0) {
                    placeOrder(input.getText().toString());
                } else {
                    Toast.makeText(CartActivity.this, "Address can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void placeOrder(String address) {
        Cart cart = new Cart(databaseHelper.getItemsInCart());
        String userEmail = databaseHelper.getLoggedUser().getEmail();
        String orderId = databaseReference.push().getKey();
        String userToken = FirebaseInstanceId.getInstance().getToken();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MMM.yyyy HH:mm:ss");
        String time = simpleDateFormat.format(Calendar.getInstance().getTime());
        Order order = new Order(cart, address, userEmail, userToken, time);

        databaseReference.child(orderId).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                databaseHelper.emptyCart();
                onDelete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartActivity.this, "Order not placed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}