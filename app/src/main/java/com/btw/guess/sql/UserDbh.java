package com.btw.guess.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.btw.guess.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDbh {

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
    public UserDbh() {
    }


    public UserDbh(Context c) {
        this.context = c;
    }

    public UserDbh open() throws SQLException {
        dh = new DatabaseHelper(context);
        database = dh.getWritableDatabase();
        return this;
    }

    public void close() {
        dh.close();
    }
//user
//user_id, user_name, user_email, acct_type

    public void addUser(User user) {
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUserName());
        values.put("user_email", user.getUserEmail());
        values.put("password", user.getPassword());
        values.put("acct_type", user.getAcctType());

        db.insert("user", null, values);
        db.close();
    }


//user
//user_id, user_name, user_email, password, acct_type
    public User checkUser(User usr) {
        String[] columns = {"user_id", "user_email", "acct_type"};
        SQLiteDatabase db = dh.getReadableDatabase();
        String selection = "user_name = ? and password = ?";
        String[] selectionArgs = {usr.getUserName(), usr.getPassword()};
        Cursor cursor = db.query("user", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.moveToFirst();
        if (cursorCount > 0) {
            usr.setUserID(cursor.getInt(0));
            usr.setUserName(usr.getUserName());
            usr.setUserEmail(cursor.getString(1));
            usr.setPassword(usr.getPassword());
            usr.setAcctType(cursor.getString(2));
        }
        else{
            usr.setUserID(0);
        }
        cursor.close();
        return usr;
    }

    //user
    //user_id, user_name, user_email, password, acct_type
    public Boolean isValidAdmin(User usr) {
        open();
        String[] columns = {"user_id", "user_email", "acct_type"};
        String selection = "user_name = ? and password = ?";
        String[] selectionArgs = {usr.getUserName(), usr.getPassword()};
        Cursor cur = database.query("user", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cnt = cur.getCount();
        if (cnt == 0){
            setMessage("Invalid User/Password");
            cur.close();
            close();
            return false;
        }
        cur.moveToFirst();
        if (cnt > 0) {
            if ((cur.getString(2).equalsIgnoreCase("ADMIN"))
                ||(cur.getString(2).equalsIgnoreCase("LINELEADER"))) {
                cur.close();
                close();
                return true;
            }
        }
        setMessage("Invalid Admin/Line Leader!");
        close();
        return false;
    }

    public boolean checkUserEmail(User usr) {
        open();
        String[] columns = {"user_id"};
        String selection = "user_email = ?";
        String[] selectionArgs = {usr.getUserEmail()};
        Cursor cursor = database.query("user", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        cursor.close();
        close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public List<User> getAllUser() {
        open();

        // array of columns to fetch
        String[] columns = {
                "user_id",
                "user_email",
                "user_name",
                "password"
        };
        // sorting orders
        String sortOrder =
                "user_name" + " ASC";
        List<User> userList = new ArrayList<User>();
        Cursor cursor = database.query("user", //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserID(Integer.parseInt(cursor.getString(0)));
                user.setUserName(cursor.getString(1));
                user.setUserEmail(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        // return user list
        return userList;
    }

    public void deleteUserID(Integer userid) {
        database.delete("user", "user_id" + " = ?",
                new String[]{String.valueOf(userid)});
        database.close();
    }

    public boolean updateUser(User user) {
        try{
            open();
            ContentValues values = new ContentValues();
            values.put("user_name", user.getUserName());
            values.put("user_email", user.getUserEmail());
            values.put("password", user.getPassword());
            // updating row
            database.update("user", values, "user_id" + " = ?",
                    new String[]{String.valueOf(user.getUserID())});
            close();
            return true;
        }catch (Exception ex){
            this.setMessage(ex.getMessage());
            return false;
        }
    }
}
