package com.btw.dickies.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.btw.dickies.model.Scan;
import com.btw.dickies.model.ScanTxn;
import com.btw.dickies.model.Search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScanTxnDb {
    private DatabaseHelper dh;
    private Context context;
    private SQLiteDatabase database;
    private String message = "";

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ScanTxnDb() {
    }
    public ScanTxnDb(Context c) {
        this.context = c;
    }
    public ScanTxnDb open() throws SQLException {
        dh = new DatabaseHelper(context);
        database = dh.getWritableDatabase();
        return this;
    }

    public void close() {
        dh.close();
    }

    //scan_transaction >scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by
    public boolean scanTxnExists(Scan scan) {
        open();
        //  database.execSQL("UPDATE scan_transaction SET qty = qty + ?, date_time = ?, scan_by = ? " +
        //                    "WHERE rack = ? and row_scan = ? and barcode = ? and line_no = ?", args);
        String[] columns = {"scan_id"};
        String selection = "rack = ? and row_scan = ? and barcode = ?";
        String[] selectionArgs = { scan.getRack(), scan.getRowScan(), scan.getBarcode()};
        Cursor cursor = database.query("scan_transaction", //Table to query
                columns, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null, //group the rows
                null, //filter by row groups
                null); //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean updSert(Scan scan){
        try{
            if (scanTxnExists(scan)){
                updateTxn(scan);
                return true;
            }
            insertTxn(scan);
            return true;
        }catch (Exception ex) {
            setMessage(ex.getMessage());
            return false;
        }
    }

    //scan_transaction > scan_id, rack, row_scan, barcode, line_no, qty, scan_by, date_time
    public void insertTxn(Scan scan) {
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rack", scan.getRack());
        values.put("row_scan", scan.getRowScan());
        values.put("barcode", scan.getBarcode());
        values.put("line_no", scan.getLineNo());
        values.put("scan_by", scan.getScanBy());
        values.put("qty", scan.getQty());

        setMessage(scan + "-insert");
        db.insert("scan_transaction", null, values);
        db.close();
    }

    //scan_transaction > scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by
    public boolean updateTxn(Scan scan) {
        try {
            open();
            String[] args = new String[]{String.valueOf(scan.getQty()), getSysDate(), scan.getScanBy(),
                                                        scan.getRack(), scan.getRowScan(), scan.getBarcode() };
            database.execSQL("UPDATE scan_transaction SET qty = qty + ?, date_time = ?, scan_by = ? " +
                    "WHERE rack = ? and row_scan = ? and barcode = ?", args);

            close();
            setMessage(scan + "- update success!");
            return true;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return false;
    }
    public String getSysDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return  dateFormat.format(date);
    }
    //scan_transaction > scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by
    public List<ScanTxn> getAllScanx(String sales_order_no) {
        List<ScanTxn> arrList = new ArrayList<>();
        try {
            open();
            String[] columns = {"barcode"};
            String selection = "sales_order_no = ?";
            String[] selectionArgs = {sales_order_no};
            Cursor cur = database.query("scan_transaction", //Table to query
                    columns, //columns to return
                    selection, //columns for the WHERE clause
                    selectionArgs, //The values for the WHERE clause
                    null, //group the rows
                    null, //filter by row groups
                    null); //The sort order

            if (cur.moveToFirst()) {
                do {
                    ScanTxn st = new ScanTxn();
                    st.setBarcode(cur.getString(0));
                    arrList.add(st);
                } while (cur.moveToNext());
            }
            cur.close();
            close();
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return arrList;
    }

    public boolean deleteScan(Integer scanID){
        try{
            open();
            database.delete("scan_transaction", "scan_id = ?",
                    new String[]{String.valueOf(scanID)});
            close();
            return true;
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean updateDelete(Scan scan) {
        try {
            open();
            //Toast.makeText(context, scan.toString(), Toast.LENGTH_LONG).show();
            String[] args = new String[]{scan.getRack(), String.valueOf(scan.getLineNo())};
            database.execSQL("UPDATE scan_transaction SET line_no = line_no - 1 " +
                    " WHERE rack = ? and line_no > ?", args);

            close();
            setMessage(scan + "- update success!");
            return true;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return false;
    }

    public boolean updateLineNoSlip(Scan scan) {
        try {
            open();
            String[] args = new String[]{scan.getRack(), scan.getBarcode(), scan.getRowScan(), String.valueOf(scan.getLineNo())};
            database.execSQL("UPDATE scan_transaction SET line_no = line_no + 1 " +
                    "WHERE rack = ? and line_no > ?", args);

            close();
            setMessage(scan + "- update success!");
            return true;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return false;
    }

    //scan_transaction > scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by
    public boolean updateScan(Scan scan) {
        try {
            open();
            //                //rack, row, barcode, qty
            String[] args = new String[]{String.valueOf(scan.getQty()), getSysDate(), scan.getScanBy(),
                    scan.getRack(), scan.getRowScan(), scan.getBarcode(),
                    String.valueOf(scan.getScanID()) };
            database.execSQL("UPDATE scan_transaction SET qty = ?, date_time = ?, scan_by = ?, " +
                    " rack = ?, row_scan = ?, barcode = ?" +
                    " WHERE scan_id = ?", args);

            close();
            setMessage(scan + "- update success!");
            return true;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return false;
    }


    public boolean isBarcodeExists(String barcode) {
        List<ScanTxn> arrList = new ArrayList<>();
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

    private String sqlWhereConcat(String s, String whereAdd){
        if (s.isEmpty() || s == ""){
            return whereAdd;
        }
        return s + " and " + whereAdd;
    }
    public List<Scan> getScanStatic(Search search) {
        List<Scan> arrList = new ArrayList<>();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Calendar cal = Calendar.getInstance();
        String fromDate = search.getDateFrom();
        String toDate = search.getDateTo();
        String rack = search.getRack();
        String row = search.getRow();
        String barcode = search.getBarcode();
        String sqlWhere = "";


        if (!fromDate.isEmpty() && !toDate.isEmpty()){
            sqlWhere = sqlWhereConcat(sqlWhere, " date_time between '" + fromDate + "' and '" + toDate + "'");
        }

        if (!rack.isEmpty()){
            sqlWhere = sqlWhereConcat(sqlWhere, " rack = '" + rack + "'");
        }

        if (!row.isEmpty()){
            sqlWhere = sqlWhereConcat(sqlWhere, " row_scan = '" + row + "'");
        }

        if (!barcode.isEmpty()){
            sqlWhere = sqlWhereConcat(sqlWhere, " barcode = '" + barcode + "'");
        }

        String selectQuery = "select scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by from scan_transaction";

        if (!sqlWhere.isEmpty()){
            selectQuery += " WHERE " + sqlWhere;
        }
        open();
        Cursor cur = database.rawQuery(selectQuery, null);


            if (cur.moveToFirst()) {
                do {
                    Scan scan = new Scan();
                    //scan_id-0, rack-1, row_scan-2, barcode-3, line_no-4, date_time-5, qty-6, scan_by-7
                    scan.setScanID(cur.getInt(0));
                    scan.setRack(cur.getString(1));
                    scan.setRowScan(cur.getString(2));
                    scan.setBarcode(cur.getString(3));
                    scan.setLineNo(cur.getInt(4));
                    scan.setDateTime(cur.getString(5));
                    scan.setQty(cur.getInt(6));
                    scan.setScanBy(cur.getString(7));
                    arrList.add(scan);
                } while (cur.moveToNext());
            }
            cur.close();
            close();

        return arrList;
    }




    public List<Scan> getScanByDate(String fromDate, String toDate) {
        List<Scan> arrList = new ArrayList<>();
        try {
            open();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(toDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            c.add(Calendar.DATE, 1);
            SimpleDateFormat sdf_to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String betweenDateTo = sdf_to.format(c.getTime());

            String sql = "select scan_id, rack, row_scan, barcode, line_no, date_time, qty, scan_by" +
                    " from scan_transaction where date_time between ? and ?";
            String[] seleArgs = new String[]{fromDate, betweenDateTo};
            Cursor cur = database.rawQuery(sql, seleArgs);

            if (cur.moveToFirst()) {
                do {
                    Scan scan = new Scan();
                    //scan_id-0, rack-1, row_scan-2, barcode-3, line_no-4, date_time-5, qty-6, scan_by-7
                    scan.setScanID(cur.getInt(0));
                    scan.setRack(cur.getString(1));
                    scan.setRowScan(cur.getString(2));
                    scan.setBarcode(cur.getString(3));
                    scan.setLineNo(cur.getInt(4));
                    scan.setDateTime(cur.getString(5));
                    scan.setQty(cur.getInt(6));
                    scan.setScanBy(cur.getString(7));
                    arrList.add(scan);
                } while (cur.moveToNext());
            }
            cur.close();
            close();
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return arrList;
    }


    public int getTotalRack(String rack) {
        int rv = 0;
        try {
            open();
            String sql = "select sum(qty) qty" +
                    " from scan_transaction where rack = ?";
            String[] seleArgs = new String[]{rack};
            Cursor c = database.rawQuery(sql, seleArgs);
            c.moveToFirst();
            rv = c.getInt(0);
            c.close();
            close();
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return rv;
    }

    public int getMaxLineNo(String rack) {
        int rv = 0;
        try {
            open();
            String sql = "select IFNULL(MAX(line_no), 0) line_no" +
                    " from scan_transaction where rack = ?";
            String[] seleArgs = new String[]{rack};
            Cursor c = database.rawQuery(sql, seleArgs);
            c.moveToFirst();
            rv = c.getInt(0);
            c.close();
            close();
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
        return rv;
    }



}
