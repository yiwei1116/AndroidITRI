package com.uscc.ncku.androiditri.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uscc.ncku.androiditri.usage.DatabaseUtilizer;

/**
 * Created by Oslo on 10/6/16.
 */
public class SQLiteDbManager extends SQLiteOpenHelper{

    // database name
    public static final String DATABASE_NAME = "android_itri.db";

    public static final int VERSION = 1;

    public SQLiteDbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    public SQLiteDbManager(Context context, String name) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public SQLiteDbManager(Context context, String name, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    public SQLiteDbManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_DEVICE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PROJECT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_BEACON);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_COMPANY);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_FIELD_MAP);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_CONTENT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEMPLATE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEXT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_LEASE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_MODE);
        // problem with path
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PATH);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_SURVEY);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_SURVEY_RESULT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_USERS);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_DEVICE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_PI);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_VOICE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_ZONE);
    }

    public void onOpen(SQLiteDatabase db) {
        Log.i("database", "database opened");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    // device table query and insert
    public boolean insertDevice(int device_id,
                                String name,
                                String name_en,
                                String introduction,
                                String guide_voice,
                                String photo,
                                String photo_vertical,
                                String hint,
                                int mode_id,
                                int company_id,
                                int read_count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("device_id", device_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("introduction", introduction);
        values.put("guide_voice", guide_voice);
        values.put("photo", photo);
        values.put("photo_vertical", photo_vertical);
        values.put("hint", hint);
        values.put("mode_id", mode_id);
        values.put("company_id", company_id);
        values.put("read_count", read_count);
        db.insert("device", null, values);
        return true;
    }

    public Cursor getDevice(int device_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from device where device_id=" + device_id + "", null);
        return cursor;
    }

    // project table query and insert
    public boolean insertProject(int project_id,
                                String version,
                                String name,
                                String introduction,
                                int active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("project_id", project_id);
        values.put("version", version);
        values.put("name", name);
        values.put("introduction", introduction);
        values.put("active", active);
        db.insert("project", null, values);
        return true;
    }

    public Cursor getProject(int project_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from project where project_id=" + project_id + "", null);
        return cursor;
    }


    // beacon table query and insert
    public boolean insertBeacon(int beacon_id,
                                String mac_addr,
                                String name,
                                int power,
                                int status,
                                int zone,
                                int x,
                                int y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("beacon_id", beacon_id);
        values.put("mac_addr", mac_addr);
        values.put("name", name);
        values.put("power", power);
        values.put("status", status);
        values.put("zone", zone);
        values.put("x", x);
        values.put("y", y);
        db.insert("beacon", null, values);
        return true;
    }

    public Cursor getBeacon(int beacon_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from beacon where beacon_id=" + beacon_id + "", null);
        return cursor;
    }

    // company table query and insert
    public boolean insertCompany(int company_id,
                                 String name,
                                 String tel,
                                 String fax,
                                 String addr,
                                 String web,
                                 String qrcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("company_id", company_id);
        values.put("name", name);
        values.put("tel", tel);
        values.put("fax", fax);
        values.put("addr", addr);
        values.put("web", web);
        values.put("qrcode", qrcode);
        db.insert("company", null, values);
        return true;
    }

    public Cursor getCompany(int company_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from company where company_id=" + company_id + "", null);
        return cursor;
    }


    // field map table query and insert
    public boolean insertFieldMap(int field_map_id,
                                  String name,
                                  String name_en,
                                  int project_id,
                                  String introduction,
                                  String guide_voice,
                                  String photo,
                                  String photo_vertical,
                                  String map_svg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("field_map_id", field_map_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("project_id", project_id);
        values.put("introduction", introduction);
        values.put("guide_voice", guide_voice);
        values.put("photo", photo);
        values.put("photo_vertical", photo_vertical);
        values.put("map_svg", map_svg);
        db.insert("field_map", null, values);
        return true;
    }

    public Cursor getFieldMap(int field_map_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from field_map where field_map_id=" + field_map_id + "", null);
        return cursor;
    }

    // hipster content table query and insert
    public boolean insertHipsterContent(int hipster_content_id,
                                        String content,
                                        String picture,
                                        String combine_picture,
                                        int hipster_template_id,
                                        int hipster_text_id,
                                        int zone_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hipster_content_id", hipster_content_id);
        values.put("content", content);
        values.put("picture", picture);
        values.put("combine_picture", combine_picture);
        values.put("hipster_template_id", hipster_template_id);
        values.put("hipster_text_id", hipster_text_id);
        values.put("zone_id", zone_id);
        db.insert("hipster_content", null, values);
        return true;
    }

    public Cursor getHipsterContent(int hipster_content_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hipster_content where hipster_content_id=" + hipster_content_id + "", null);
        return cursor;
    }

    // hipster template table query and insert
    public boolean insertHipsterTemplate(int hipster_template_id,
                                         String name,
                                         String template) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hipster_template_id", hipster_template_id);
        values.put("name", name);
        values.put("template", template);
        db.insert("hipster_template", null, values);
        return true;
    }

    public Cursor getHipsterTemplate(int hipster_template_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hipster_template where hipster_template_id=" + hipster_template_id + "", null);
        return cursor;
    }


    // hipster text table query and insert
    public boolean insertHipsterText(int hipster_text_id,
                                     String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hipster_text_id", hipster_text_id);
        values.put("content", content);
        db.insert("hipster_text", null, values);
        return true;
    }

    public Cursor getHipsterText(int hipster_text_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hipster_text where hipster_text_id=" + hipster_text_id + "", null);
        return cursor;
    }


}
