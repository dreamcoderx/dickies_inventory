package com.btw.guess.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.btw.guess.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductDbh {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    private String TABLE_ITEM = "item";
    private String item_no = "item_no";
    private String item_name = "item_name";
    private String brand = "brand";
    private String department = "department";
    private String sub_department = "sub_department";
    private String category = "category";
    private String sub_category = "sub_category";
    private String style_code = "style_code";
    private String color = "color";
    private String child_color = "child_color";
    private String itemcode = "itemcode";

    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductDbh(Context c) {
        this.context = c;
    }

    public ProductDbh open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Boolean InsertProduct(Product p) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("item_no", p.getItem_no());
            values.put("item_name", p.getItem_name());
            values.put("brand", p.getBrand());
            values.put("department", p.getDepartment());
            values.put("sub_department", p.getSub_department());
            values.put("category", p.getCategory());
            values.put("sub_category", p.getSub_category());
            values.put("style_code", p.getStyle_code());
            values.put("size", p.getSize());
            values.put("child_size", p.getChild_size());
            values.put("color", p.getColor());
            values.put("child_color", p.getChild_color());
            values.put("itemcode", p.getItemcode());

            db.insert("product", null, values);
            db.close();

            return true;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return false;
    }


    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> productList;
        productList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " LIMIT 100";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("a", cursor.getString(0));
                map.put("b", cursor.getString(1));
                map.put("c", cursor.getString(2));
                productList.add(map);
                Log.e("dataofList", cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return productList;
    }

}
