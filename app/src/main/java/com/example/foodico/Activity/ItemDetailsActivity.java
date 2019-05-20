package com.example.foodico.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodico.Model.Item;
import com.example.foodico.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailsActivity extends AppCompatActivity {

    @BindView(R.id.itemDetailImage)
    ImageView itemDetailImage;

    @BindView(R.id.itemDetailName)
    TextView itemDetailName;

    @BindView(R.id.itemDetailPrice)
    TextView itemDetailPrice;

    @BindView(R.id.itemDetailDescription)
    TextView itemDetailDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        Item item = (Item) getIntent().getSerializableExtra("item");

        Picasso.get()
                .load(item.getImage())
                .into(itemDetailImage);

        itemDetailName.setText(item.getName());
        itemDetailName.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        itemDetailPrice.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        itemDetailPrice.setText(item.getPrice() + "$");
        itemDetailDescription.setText(item.getDescription());
    }
}
