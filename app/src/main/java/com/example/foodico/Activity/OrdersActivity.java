package com.example.foodico.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.foodico.Adapter.OrderAdapter;
import com.example.foodico.Helper.DatabaseHelper;
import com.example.foodico.Model.Order;
import com.example.foodico.Model.User;
import com.example.foodico.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DatabaseHelper databaseHelper;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");
        databaseHelper = new DatabaseHelper(this);
        User currentUser = databaseHelper.getLoggedUser();
        Query query = databaseReference.orderByChild("userEmail").equalTo(currentUser.getEmail());
        query.addListenerForSingleValueEvent(valueEventListener());

    }

    public ValueEventListener valueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    orderList = new ArrayList<>();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        orderList.add(item.getValue(Order.class));
                    }
                    loadRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public void loadRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ordersRecyclerView);
        OrderAdapter orderAdapter = new OrderAdapter(orderList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
    }
}
