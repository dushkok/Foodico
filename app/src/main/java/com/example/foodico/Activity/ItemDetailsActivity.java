package com.example.foodico.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodico.Helper.DatabaseHelper;
import com.example.foodico.Model.Item;
import com.example.foodico.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemDetailsActivity extends AppCompatActivity {

    @BindView(R.id.itemDetailImage)
    ImageView itemDetailImage;

    @BindView(R.id.itemDetailName)
    TextView itemDetailName;

    @BindView(R.id.itemDetailPrice)
    TextView itemDetailPrice;

    @BindView(R.id.itemDetailDescription)
    TextView itemDetailDescription;

    private DatabaseHelper databaseHelper;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        databaseHelper = new DatabaseHelper(this);
        item = (Item) getIntent().getSerializableExtra("item");

        Picasso.get()
                .load(item.getImage())
                .into(itemDetailImage);

        itemDetailName.setText(item.getName());
        itemDetailName.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        itemDetailPrice.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        itemDetailPrice.setText(item.getPrice() + "$");
        itemDetailDescription.setText(item.getDescription());
    }

    @OnClick(R.id.addToCartButton)
    public void onAddToCartButtonClick() {
        item.setQuantity(1);
        databaseHelper.addOrUpdateItem(item);
        Snackbar.make(findViewById(R.id.activityItemDetailView), "Added to cart", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @OnClick(R.id.fabItemDetail)
    public void onFabItemDetailClick() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
}
