package com.example.foodico.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.foodico.Model.Item;
import com.example.foodico.Model.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Local.db";
    private static final String USER_TABLE = "user";
    private static final String CART_TABLE = "cart";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        init(db);
    }

    public void init(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CART_TABLE +
                "(id integer primary key autoincrement, " +
                "name text," +
                "quantity integer," +
                "price real," +
                "image text," +
                "description text)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE +
                "(id integer primary key autoincrement, " +
                "name text," +
                "email text," +
                "password text)");
    }

    public User getLoggedUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE, null);
        if (cursor.getCount() <= 0) {
            return null;
        }
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        db.close();
        return new User(name, email, password);
    }

    public boolean logInUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        db.insert(USER_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public boolean logOutUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + USER_TABLE);
        db.close();
        return true;
    }

    public boolean addOrUpdateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item.getName());
        contentValues.put("quantity", item.getQuantity());
        contentValues.put("price", item.getPrice());
        contentValues.put("image", item.getImage());
        contentValues.put("description", item.getDescription());

        int n = db.update(CART_TABLE, contentValues, "name=?", new String[]{item.getName()});
        if (n == 0) {
            db.insertWithOnConflict(CART_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return true;
    }

    public void emptyCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }

    public boolean removeItemFromCart(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CART_TABLE + " where name=?", new String[]{item.getName()});
        return true;
    }

    public ArrayList<Item> getItemsInCart() {
        ArrayList<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CART_TABLE, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String name = res.getString(res.getColumnIndex("name"));
            float price = res.getFloat(res.getColumnIndex("price"));
            int quantity = res.getInt(res.getColumnIndex("quantity"));
            String image = res.getString(res.getColumnIndex("image"));
            String description = res.getString(res.getColumnIndex("description"));
            items.add(new Item(name, image, description, price, quantity));
            res.moveToNext();
        }
        return items;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CART_TABLE);
        return numRows;
    }
}
