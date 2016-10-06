package com.uscc.ncku.androiditri.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uscc.ncku.androiditri.usage.DatabaseUtilizer;

/**
 * Created by Oslo on 10/6/16.
 */
public class SQLiteDbManager extends SQLiteOpenHelper{

    // database name
    public static final String DATABASE_NAME = "ITRI.db";

    public static final int VERSION = 1;

    public SQLiteDbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDbManager(Context context, String name) {
        super(context, name, null, VERSION);
    }

    public SQLiteDbManager(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DB_CREATE_TABLE_DEVICE = "CREATE TABLE" + DatabaseUtilizer.DEVICE_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.DEVICE_ID + " INT,"
                + DatabaseUtilizer.NAME + " TEXT,"
                + DatabaseUtilizer.NAME_EN + " TEXT,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.GUIDE_VOICE + " TEXT,"
                + DatabaseUtilizer.DEVICE_PHOTO + " TEXT,"
                + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT,"
                + DatabaseUtilizer.DEVICE_HINT + " TEXT,"
                + DatabaseUtilizer.DEVICE_MODE_ID + " INT,"
                + DatabaseUtilizer.DEVICE_COMPANY_ID + " INT"
                + DatabaseUtilizer.READ_COUNT + " INT"
                + ")";





        // create tables
        db.execSQL(DB_CREATE_TABLE_DEVICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
