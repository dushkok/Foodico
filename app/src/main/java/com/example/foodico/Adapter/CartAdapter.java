package com.example.foodico.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.foodico.Activity.CartActivity;
import com.example.foodico.Helper.DatabaseHelper;
import com.example.foodico.Model.Item;
import com.example.foodico.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Item> cartItemList;
    private CartActivity cartActivity;
    private DatabaseHelper databaseHelper;

    public CartAdapter() {
    }

    public CartAdapter(List<Item> cartItemList, CartActivity cartActivity) {
        this.cartItemList = cartItemList;
        this.cartActivity = cartActivity;
        databaseHelper = new DatabaseHelper(cartActivity.getApplicationContext());
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_item, viewGroup, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int i) {
        cartViewHolder.quantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Item temp = cartItemList.get(i);
                temp.setQuantity(newVal);
                databaseHelper.addOrUpdateItem(temp);
                cartActivity.update();
            }
        });

        Picasso.get()
                .load(cartItemList.get(i).getImage())
                .into(cartViewHolder.image);

        cartViewHolder.quantity.setValue(cartItemList.get(i).getQuantity());
        cartViewHolder.name.setText(cartItemList.get(i).getName());

        cartViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.removeItemFromCart(cartItemList.get(i));
                cartActivity.onDelete();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;
        public NumberPicker quantity;
        public ImageButton button;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.cartItemName);
            this.image = itemView.findViewById(R.id.cartItemImage);
            this.quantity = itemView.findViewById(R.id.cartItemQuantity);
            this.button = itemView.findViewById(R.id.removeImageButton);
            quantity.setMinValue(1);
            quantity.setMaxValue(20);
            quantity.setWrapSelectorWheel(true);
            quantity.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }
}
