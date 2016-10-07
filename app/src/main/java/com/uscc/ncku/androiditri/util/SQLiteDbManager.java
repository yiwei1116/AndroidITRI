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
        // device
        String DB_CREATE_TABLE_DEVICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.DEVICE_TABLE + " ("
                + DatabaseUtilizer.DEVICE_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.NAME + " TEXT NOT NULL,"
                + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.GUIDE_VOICE + " TEXT,"
                + DatabaseUtilizer.DEVICE_PHOTO + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT,"
                + DatabaseUtilizer.DEVICE_HINT + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.DEVICE_MODE_ID + " INT NOT NULL,"
                + DatabaseUtilizer.DEVICE_COMPANY_ID + " INT DEFAULT NULL,"
                + DatabaseUtilizer.READ_COUNT + " INT DEFAULT '0'"
                + " PRIMARY KEY (" + DatabaseUtilizer.DEVICE_ID + "),"
                + " KEY mode (" + DatabaseUtilizer.MODE_ID + "," + DatabaseUtilizer.COMPANY_ID + "),"
                + " KEY " + DatabaseUtilizer.COMPANY_ID + " (" + DatabaseUtilizer.COMPANY_ID + ")"
                + ")";
        // project
        String DB_CREATE_TABLE_PROJECT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.PROJECT_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.PROJECT_ID + " INT,"
                + DatabaseUtilizer.VERSION + " TEXT,"
                + DatabaseUtilizer.NAME + " TEXT,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.ACTIVE + " INT"
                + ")";
        // beacon
        String DB_CREATE_TABLE_BEACON = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.BEACON_TABLE + " ("
                + DatabaseUtilizer.BEACON_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.MAC_ADDR + " TEXT NOT NULL,"
                + DatabaseUtilizer.NAME + " TEXT NOT NULL,"
                + DatabaseUtilizer.BEACON_POWER + " INT NOT NULL,"
                + DatabaseUtilizer.BEACON_STATUS + " INT NOT NULL,"
                + DatabaseUtilizer.BEACON_ZONE + " INT NOT NULL,"
                + DatabaseUtilizer.X + " INT NOT NULL,"
                + DatabaseUtilizer.Y + " INT NOT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.BEACON_ID + "," + DatabaseUtilizer.MAC_ADDR + "),"
                + " KEY " + DatabaseUtilizer.BEACON_ZONE + " (" + DatabaseUtilizer.BEACON_ZONE + ")"
                + ")";
        // company
        String DB_CREATE_TABLE_COMPANY = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.COMPANY_TABLE + " ("
                + DatabaseUtilizer.COMPANY_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.NAME + " TEXT NOT NULL,"
                + DatabaseUtilizer.COMPANY_TEL + " TEXT NOT NULL,"
                + DatabaseUtilizer.COMPANY_FAX + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.COMPANY_ADDR + " TEXT,"
                + DatabaseUtilizer.COMPANY_WEB + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.QRCODE+ " TEXT DEFAULT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.COMPANY_ID + ")"
                + ")";
        // field map
        String DB_CREATE_TABLE_FIELD_MAP = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.FIELD_MAP_TABLE + " ("
                + DatabaseUtilizer.FIELD_MAP_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.NAME + " TEXT NOT NULL,"
                + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.PROJECT_ID + " INT NOT NULL,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.GUIDE_VOICE + " TEXT,"
                + DatabaseUtilizer.DEVICE_PHOTO + " TEXT NOT NULL,"
                + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.MAP_SVG + " TEXT NOT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.FIELD_MAP_ID + "),"
                + " UNIQUE KEY " + DatabaseUtilizer.FIELD_MAP_ID + " (" + DatabaseUtilizer.FIELD_MAP_ID + "),"
                + " KEY field_map_id_2 (" + DatabaseUtilizer.FIELD_MAP_ID + "),"
                + " KEY project_id (" + DatabaseUtilizer.PROJECT_ID + ")"
                + ")";
        // hipster content
        String DB_CREATE_TABLE_HIPSTER_CONTENT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_CONTENT_TABLE + " ("
                + DatabaseUtilizer.HIPSTER_CONTENT_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.CONTENT + " TEXT,"
                + DatabaseUtilizer.PICTURE + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.COMBINE_PICTURE + " TEXT NOT NULL,"
                + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + " INT DEFAULT NULL,"
                + DatabaseUtilizer.HIPSTER_TEXT_ID + " INT DEFAULT NULL,"
                + DatabaseUtilizer.ZONE_ID + " INT NOT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.HIPSTER_CONTENT_ID + "),"
                + " KEY hipster_template_id (" + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + "),"
                + " KEY hipster_text_id (" + DatabaseUtilizer.HIPSTER_TEXT_ID + "),"
                + " KEY zone_id (" + DatabaseUtilizer.ZONE_ID + "),"
                + " KEY zone_id_2 (" + DatabaseUtilizer.ZONE_ID + "),"
                + " KEY zone_id_3 (" + DatabaseUtilizer.ZONE_ID + ")"
                + ")";
        // hipster template
        String DB_CREATE_TABLE_HIPSTER_TEMPLATE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_TEMPLATE_TABLE + " ("
                 + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.NAME + " TEXT NOT NULL,"
                + DatabaseUtilizer.TEMPLATE + " TEXT NOT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + ")"
                + ")";
        // hipster text
        String DB_CREATE_TABLE_HIPSTER_TEXT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_TEXT_TABLE + " ("
                + DatabaseUtilizer.HIPSTER_TEXT_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.CONTENT + " TEXT NOT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.HIPSTER_TEXT_ID + ")"
                + ")";
        // lease
        String DB_CREATE_TABLE_LEASE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.LEASE_TABLE + " ("
                + DatabaseUtilizer.LEASE_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.PAD_ID + " TEXT NOT NULL,"
                + DatabaseUtilizer.BORROWER + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.BORROWER_TEL + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.LEASE_DATE + " datetime DEFAULT NULL,"
                + DatabaseUtilizer.RETURN_DATE + " datetime DEFAULT NULL,"
                + " PRIMARY KEY (" + DatabaseUtilizer.LEASE_ID + ")"
                + ")";
        // mode
        String DB_CREATE_TABLE_MODE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.MODE_TABLE + " ("
                + DatabaseUtilizer.MODE_ID + " INT NOT NULL AUTOINCREMENT,"
                + DatabaseUtilizer.NAME + " TEXT NOT NULL,"
                + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.GUIDE_VOICE + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.VIDEO + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.MODE_SPLASH_BG + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.MODE_SPLASH_FG + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.MODE_SPLASH_BLUR + " TEXT DEFAULT NULL,"
                + DatabaseUtilizer.LIKE_COUNT + " INT,"
                + DatabaseUtilizer.READ_COUNT + " INT,"
                + DatabaseUtilizer.TIME_TOTAL + " INT,"
                + DatabaseUtilizer.ZONE_ID + " INT"
                + ")";
        // path
        String DB_CREATE_TABLE_PATH = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.PATH_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.PATH_ID + " INT,"
                + DatabaseUtilizer.ZONE_ID + " INT,"
                + DatabaseUtilizer.PATH_ORDER + " INT"
                + ")";
        String DB_CREATE_TABLE_SURVEY = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.SURVEY_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.SURVEY_ID + " INT,"
                + DatabaseUtilizer.NAME + " TEXT,"
                + DatabaseUtilizer.EMAIL + " TEXT,"
                + DatabaseUtilizer.GENDER + " INT,"
                + DatabaseUtilizer.AGE + " INT,"
                + DatabaseUtilizer.EDUCATION + " INT,"
                + DatabaseUtilizer.CAREER + " INT,"
                + DatabaseUtilizer.LOCATION + " INT,"
                + DatabaseUtilizer.HOUSE_TYPE + " INT,"
                + DatabaseUtilizer.FAMILY_TYPE + " INT"
                + ")";
        String DB_CREATE_TABLE_SURVEY_RESULT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.SURVEY_RESULT_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.SURVEY_RESULT_ID + " INT,"
                + DatabaseUtilizer.SURVEY_QUESTION + " INT,"
                + DatabaseUtilizer.SURVEY_ANSWER + " INT,"
                + DatabaseUtilizer.TOTAL + " INT"
                + ")";
        String DB_CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.USERS_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.USER_ID + " INT,"
                + DatabaseUtilizer.EMAIL + " TEXT,"
                + DatabaseUtilizer.PASSWORD + " TEXT,"
                + DatabaseUtilizer.COMPETENCE + " INT,"
                + DatabaseUtilizer.LAST_LOGIN + " TEXT"
                + ")";
        String DB_CREATE_TABLE_VIP_DEVICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.VIP_DEVICE_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.VIP_DEVICE_ID + " INT,"
                + DatabaseUtilizer.MAC_ADDR + " TEXT,"
                + DatabaseUtilizer.NAME + " TEXT,"
                + DatabaseUtilizer.POWER + " INT"
                + ")";
        String DB_CREATE_TABLE_VIP_PI = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.VIP_PI_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.VIP_PI_ID + " INT,"
                + DatabaseUtilizer.IPADDRESS + " TEXT,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.VIP_VISIBLE + " INT"
                + ")";
        String DB_CREATE_TABLE_VIP_VOICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.VIP_VOICE_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.VOICE_ID + " INT,"
                + DatabaseUtilizer.VOICE + " TEXT,"
                + DatabaseUtilizer.VIP_DEVICE + " INT"
                + ")";
        String DB_CREATE_TABLE_ZONE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.ZONE_TABLE + " ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtilizer.ZONE_ID + " INT,"
                + DatabaseUtilizer.NAME + " TEXT,"
                + DatabaseUtilizer.NAME_EN + " TEXT,"
                + DatabaseUtilizer.INTRODUCTION + " TEXT,"
                + DatabaseUtilizer.GUIDE_VOICE + " TEXT,"
                + DatabaseUtilizer.DEVICE_HINT + " TEXT,"
                + DatabaseUtilizer.DEVICE_PHOTO + " TEXT,"
                + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT,"
                + DatabaseUtilizer.FIELD_ID + " INT"
                + ")";

        // create tables
        db.execSQL(DB_CREATE_TABLE_DEVICE);
        db.execSQL(DB_CREATE_TABLE_PROJECT);
        db.execSQL(DB_CREATE_TABLE_BEACON);
        db.execSQL(DB_CREATE_TABLE_COMPANY);
        db.execSQL(DB_CREATE_TABLE_FIELD_MAP);
        db.execSQL(DB_CREATE_TABLE_HIPSTER_CONTENT);
        db.execSQL(DB_CREATE_TABLE_HIPSTER_TEMPLATE);
        db.execSQL(DB_CREATE_TABLE_HIPSTER_TEXT);
        db.execSQL(DB_CREATE_TABLE_LEASE);
        db.execSQL(DB_CREATE_TABLE_MODE);
        db.execSQL(DB_CREATE_TABLE_PATH);
        db.execSQL(DB_CREATE_TABLE_SURVEY);
        db.execSQL(DB_CREATE_TABLE_SURVEY_RESULT);
        db.execSQL(DB_CREATE_TABLE_USERS);
        db.execSQL(DB_CREATE_TABLE_VIP_DEVICE);
        db.execSQL(DB_CREATE_TABLE_VIP_PI);
        db.execSQL(DB_CREATE_TABLE_VIP_VOICE);
        db.execSQL(DB_CREATE_TABLE_ZONE);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onUpgrade(sqLiteDatabase, i, i1);
    }
}
