package com.btw.dickies.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.btw.dickies.model.Scan;

import java.util.ArrayList;
import java.util.List;

public class ScanDbh {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ScanDbh(Context c) {
        this.context = c;
    }
    public ScanDbh open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public List<Scan> getAllScanTxn(String fromDate, String toDate) {
        List<Scan> arrList = new ArrayList<>();
        try {

            //scan_transaction
            // scan_id, process_location, process_kanban, status, date_time, scan_by, line_leader

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "select scan_id, process_kanban, process_location," +
                    " status, date_time, scan_by, line_leader" +
                    " from scan_transaction where date_time between ? and ?";
            String[] seleArgs = new String[]{fromDate, toDate};
            Cursor c = db.rawQuery(sql, seleArgs);

            if (c.moveToFirst()) {
                do {
                    Scan s = new Scan();
                    s.setScanID(c.getInt(0));
                    s.setDateTime(c.getString(4));
                    s.setScanBy(c.getString(5));
                    arrList.add(s);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return arrList;
    }

    public Boolean saveTxn(Scan s) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("barcode", s.getBarcode());
            values.put("sales_order_no","");//s.getSalesOrderNo());
            db.insert("scan_transaction", null, values);
            db.close();
            return true;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return false;
    }

    public boolean isBarcodeExists(String barcode) {
            boolean rv = false;
            try {
                open();
                String[] columns = {"barcode"};
                String selection = "barcode = ?";
                String[] selectionArgs = {barcode};
                Cursor cur = database.query("scan_transaction", //Table to query
                        columns, //columns to return
                        selection, //columns for the WHERE clause
                        selectionArgs, //The values for the WHERE clause
                        null, //group the rows
                        null, //filter by row groups
                        null); //The sort order
                if (cur.getCount() > 0){
                    rv = true;
                }

                cur.close();
                close();
            } catch (Exception ex) {
                this.setMessage(ex.getMessage());
            }
            return rv;
    }
}
