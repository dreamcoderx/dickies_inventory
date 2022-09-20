package com.btw.guess.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.btw.guess.model.ModelScan;

import java.util.ArrayList;
import java.util.List;

public class ModelDbh {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    private int modelID;
    private String modelNo;
    private String location;
    private String kanban;
    private String tblName = "model";
    private String cModelID = "model_id";
    private String cModelNo = "model_no";
    private String cProcessLoc = "process_location";
    private String cProcessKanban = "process_kanban";
    private String message = "";




    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKanban() {
        return kanban;
    }

    public void setKanban(String kanban) {
        this.kanban = kanban;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelDbh(Context c) {
        this.context = c;
    }

    public ModelDbh open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public List<ModelScan> getAllModel() {
        String[] columns = { cModelID, cModelNo, cProcessLoc, cProcessKanban };
        String sortOrder = cModelID +  " ASC";
        List<ModelScan> modelList = new ArrayList<ModelScan>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(tblName, //Table to query
                columns, //columns to return
                null, //columns for the WHERE clause
                null, //The values for the WHERE clause
                null, //group the rows
                null, //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ModelScan mod = new ModelScan();
                mod.setModelID(c.getInt(0));
                mod.setModelNo(c.getString(1));
                mod.setProcessLocation(c.getString(2));
                mod.setProcessKanban(c.getString(3));
                modelList.add(mod);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return modelList;
    }

    //model_id INTEGER
    //model_no TEXT
    //process_location TEXT
    //process_kanban TEXT

    public void addModel(ModelScan mod) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(cModelNo, mod.getModelNo());
        values.put(cProcessLoc, mod.getProcessLocation());
        values.put(cProcessKanban, mod.getProcessKanban());
        db.insert(tblName, null, values);
        db.close();
    }

    public Boolean isValidModel(){
        ModelScan ms = new ModelScan();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select model_id, model_no, process_location, process_kanban" +
                     " from model where model_no = ?";
        String[] seleArgs = new String[]{ this.getModelNo()};
        Cursor cur = db.rawQuery(sql, seleArgs);
        if (cur.getCount() > 0){
            return true;
        }
        this.setMessage("Invalid Model!");
        return false;
    }

    public Boolean isValidLocation(){
        ModelScan ms = new ModelScan();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select model_id, model_no, process_location, process_kanban" +
                " from model where model_no = ? and process_location = ?";
        String[] seleArgs = new String[]{ this.getModelNo(), this.getLocation()};
        Cursor cur = db.rawQuery(sql, seleArgs);
        if (cur.getCount() > 0){
            return true;
        }
        this.setMessage("Invalid Location!");
        return false;
    }

    public Boolean isValidKanban(){
        ModelScan ms = new ModelScan();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select model_id, model_no, process_location, process_kanban" +
                " from model where model_no = ? and process_location = ? and process_kanban = ?";
        String[] seleArgs = new String[]{ this.getModelNo(), this.getLocation(), this.getKanban()};
        Cursor cur = db.rawQuery(sql, seleArgs);
        if (cur.getCount() > 0){
            return true;
        }
        this.setMessage("Invalid Kanban!");
        return false;
    }
}
