package com.btw.guess.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.btw.guess.utils.singleToneClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBUser {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    private String message;
    public DBUser(Context context) {
        dbHelper = new DBHelper(context);
    }
    public DBUser createDatabase() throws SQLException {
        try {
            dbHelper.createDataBase();
        } catch (IOException ignored) {
        }
        return this;
    }

    public DBUser open() throws SQLException {
        try {
            dbHelper.openDataBase();
            dbHelper.close();
            db = dbHelper.getReadableDatabase();
        } catch (SQLException ignored) {
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isValidUser(String user, String pass){
        String sql = "select user_id, user_name, user_email" +
                " from user" +
                " WHERE user_name = ?" +
                " and user_password = ?";
        String[] seleArgs = {user, pass};
        Cursor cursor = db.rawQuery(sql, seleArgs);
        try {
            if (cursor.moveToFirst()) {
                singleToneClass sc = singleToneClass.getInstance();
                sc.setUserID(cursor.getInt(0));
                return true;
                //do {
                //} while (cursor.moveToNext());
            }
        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return false;
    }

    public ModUser getUser(ModUser u){
        u.setUser_id(0);
        String sql = "select user_id, user_name, user_email, user_type" +
                " from user" +
                " WHERE user_name = ?" +
                " and user_password = ?";
        String[] seleArgs = {u.getUser_name(), u.getUser_password()};
        Cursor c = db.rawQuery(sql, seleArgs);
        try {
            if (c.moveToFirst()) {
                singleToneClass s = singleToneClass.getInstance();
                s.setUserID(c.getInt(0));
                s.setUserType(c.getInt(3));

                u.setUser_id(c.getInt(0));
                u.setUser_email(c.getString(2));
                int usertype = 0;

                u.setUser_type(c.getInt(3));
                return u;
            }


        } catch (Exception ignored) {
        } finally {
            c.close();
        }
        return u;
    }

    public boolean UpdateUser(ModUser u){
        String sql = "UPDATE user" +
                " SET user_name = ?, user_email = ?, user_password = ?" +
                " WHERE user_id = ?";
        String[] args = {u.getUser_name(), u.getUser_email(),
                u.getUser_password(), String.valueOf(u.getUser_id())};
        try{
            db.rawQuery(sql, args);
            return true;
        }catch (SQLException sq){
            this.setMessage(sq.getMessage());
            return false;
        }
    }

    public List<ModUser> getAllUser() {
        List<ModUser> arUsr = new ArrayList<ModUser>();
        String sql = "select u.user_id, u.user_name, u.user_email, u.user_password, t.user_desc" +
                " from user u" +
                " left join user_type t" +
                " on u.user_type = t.user_type_id";


                //"select user_id, user_name, user_email," +
                //" user_password, user_type" +
                //" FROM user";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                ModUser u = new ModUser();
                u.setUser_id(c.getInt(c.getColumnIndex("user_id")));
                u.setUser_name(c.getString(c.getColumnIndex("user_name")));
                u.setUser_email(c.getString(c.getColumnIndex("user_email")));
                u.setUser_password(c.getString(c.getColumnIndex("user_password")));
                //u.setUser_type(c.getInt(c.getColumnIndex("user_type")));
                String userdesc = "";
                if (!(c.getString(c.getColumnIndex("user_desc")) == null)){
                    userdesc =c.getString(c.getColumnIndex("user_desc"));
                }
                u.setUser_type_desc(userdesc);
                arUsr.add(u);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return arUsr;
    }

    public List<String> getUserType(){
        List<String> usertype = new ArrayList<String>();
         String sql = "select user_type_id, user_desc from user_type";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                usertype.add(c.getString(1));
            } while (c.moveToNext());
        }
        // closing connection
        c.close();
        db.close();
        return usertype;
    }

    public int getUserTypeid(String userTypeDesc){
        String sql = "select user_type_id" +
                " from user_type" +
                " where user_desc = ?";
        String[] args = {userTypeDesc};
        Cursor c = db.rawQuery(sql, args);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 1;
    }

    public void xaddUser(ModUser u) {

        String sql = "INSERT INTO user (user_name, user_email, user_password, user_type)" +
                " VALUES (?,?,?,?);";
        String[] args = {u.getUser_name(), u.getUser_email(),
                u.getUser_password(), String.valueOf(u.getUser_type()) };
        try{
            db.rawQuery(sql, args);
            this.setMessage("save sucess!");
        }catch (SQLException sq){
            this.setMessage(sq.getMessage());
        }

    }

    public void addUser(ModUser u) {

        ContentValues values = new ContentValues();
        values.put("user_name", u.getUser_name());
        values.put("user_email", u.getUser_email());
        values.put("user_password", u.getUser_password());
        values.put("user_type", u.getUser_type() );
        // Inserting Row
        try{
            db.insert("user", null, values);
            this.setMessage("save success!");
        }catch (SQLException ex){
            this.setMessage(ex.getMessage());
        }

    }



    public boolean isExists(String username, String email) {
        String sql = "select *" +
                " from user" +
                " where user_email = ?" +
                " or user_name = ?";
        String[] args = {email, username};
        Cursor cursor = db.rawQuery(sql, args);
        int cursorCount = cursor.getCount();
        cursor.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getPlateNo() {
        String sql = "select plate_no" +
                " from plates" +
                " order by plate_no";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<String> plates = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    //ArrayList<Categories> categories = new ArrayList<>();
                    //categories.add(new Categories(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)));
                    //categoriesName.add(cursor.getString(2));
                    plates.add(cursor.getString(0));
                } while (cursor.moveToNext());
                return plates;
            }

        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return null;
    }


    public void addPlateNo(String plateno) {
        ContentValues values = new ContentValues();
        values.put("plate_no", plateno);
        // Inserting Row
        try{
            db.insert("plates", null, values);
            this.setMessage("add success!");
        }catch (SQLException ex){
            this.setMessage(ex.getMessage());
        }
    }

    public void addEmailAdd(String email) {
        ContentValues values = new ContentValues();
        values.put("email_add", email);
        // Inserting Row
        //dbHelper.openDataBase();
        //dbHelper.close();
        try{
            db.insert("email_list", null, values);
            this.setMessage("add success!");
        }catch (SQLException ex){
            this.setMessage(ex.getMessage());
        }
    }

    public void clearEmail(){
        try{
            db.delete("email_list",   null, null);
            this.setMessage("query success!");
        }catch (SQLException sq){
            this.setMessage(sq.getMessage());
        }
    }


    public ArrayList<String> getEmailsString() {
        String sql = "select email_add" +
                " from email_list" +
                " order by email_add";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<String> str = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    str.add(cursor.getString(0));
                } while (cursor.moveToNext());
                return str;
            }
        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return str;
    }

}


