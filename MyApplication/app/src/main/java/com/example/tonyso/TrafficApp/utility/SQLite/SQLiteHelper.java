package com.example.tonyso.TrafficApp.utility.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TonySoMan on 25/6/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mydata.db";
    public static final int OLD_VERSION = 1;
    public static final int NEW_VERSION = 8;
    private static SQLiteDatabase database;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new SQLiteHelper(context, DATABASE_NAME,
                    null, NEW_VERSION).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL(TrafficApp_SQLiteDatabase.CREATE_TABLE);
        //  db.execSQL(FAQItems_SQLiteDatabase.CREATE_FAQ_TABLE_ZH);
        // db.execSQL(StaffClinic_SQLiteDatabase.CREATE_STATEMENT_ZH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TrafficApp_SQLiteDatabase.TABLE_NAME);
        //   db.execSQL("DROP TABLE IF EXISTS " + FAQItems_SQLiteDatabase.TABLE_NAME_ZH);
        //   db.execSQL("DROP TABLE IF EXISTS " + StaffClinic_SQLiteDatabase.TABLE_NAME_ZH);
        onCreate(db);
    }
}
