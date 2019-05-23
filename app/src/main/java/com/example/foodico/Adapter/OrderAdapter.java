package com.example.foodico.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodico.Model.Order;
import com.example.foodico.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderAdapter() {
    }

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_item, viewGroup, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i) {
        orderViewHolder.orderTime.setText("Time of order: " + orders.get(i).getTime());
        orderViewHolder.orderAddress.setText("Address: " + orders.get(i).getAddress());
        orderViewHolder.orderTotalPrice.setText("Total: " + orders.get(i).getCart().getTotalPrice() + "$");
        orderViewHolder.orderItems.setText("Items: " + orders.get(i).getCart().getCartContent());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView orderItems;
        public TextView orderAddress;
        public TextView orderTime;
        public TextView orderTotalPrice;
        public View view;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItems = itemView.findViewById(R.id.orderItems);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderTime = itemView.findViewById(R.id.orderTime);
            orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);
            view = itemView;
        }
    }
}
