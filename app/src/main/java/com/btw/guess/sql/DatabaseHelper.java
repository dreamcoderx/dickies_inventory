package com.btw.guess.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "guess.db3";
    static final String TABLE_USER = "user";
    static final String TABLE_TRANSACTION = "scan_transaction";

    //user
    //user_id, user_name, user_email, password, acct_type
    private String CREATE_USER = "CREATE TABLE user (" +
            " user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT UNIQUE," +
            " user_email TEXT UNIQUE, password TEXT, acct_type TEXT)";

    private String CREATE_TRANSACTION = "CREATE TABLE  scan_transaction" +
            " (scan_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " rack TEXT NOT NULL," +
            " row_scan TEXT NOT NULL," +
            " barcode TEXT NOT NULL," +
            " line_no INTEGER NOT NULL," +
            " qty INTEGER NOT NULL," +
            " scan_by TEXT NOT NULL," +
            " date_time TEXT DEFAULT CURRENT_TIMESTAMP)" ;


    private String DROP_TRANSACTION = "DROP TABLE IF EXISTS " + TABLE_TRANSACTION ;
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        String sql = "INSERT INTO user (user_name, user_email, password, acct_type) VALUES (\"admin\", \"a@a.com\", \"admin\", \"admin\")";
        db.execSQL(sql);
        db.execSQL(CREATE_TRANSACTION );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TRANSACTION);
        onCreate(db);
    }


}
