package com.uscc.ncku.androiditri.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oslo on 10/6/16.
 */
public class SQLiteDbManager extends SQLiteOpenHelper{

    // database name
    public static final String DATABASE_NAME = "android_itri_1.db";


    // TODO: 2. 下載音樂檔

    /*
           ----> beacon, company, device, field_map, hipster_template, hipster_text, mode, zone
     */
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
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_BEACON);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_COMPANY);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_FIELD_MAP);
        // need to upload hipster content
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_CONTENT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEMPLATE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEXT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_MODE);
        // upload survey
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_SURVEY);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_ZONE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PATH);


        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PROJECT);
        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_LEASE);
        // problem with path
        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_SURVEY_RESULT);
        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_USERS);
        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_DEVICE);
        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_PI);
        //        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_VOICE);
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

        long rowId = db.insertWithOnConflict("device", null, values, 4);
        if (rowId != -1) {
            Log.i("device", "insert device_id=" + device_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getDevice(int device_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from device where device_id=" + device_id + "", null);
        return cursor;
    }

    // get device files
    public JSONArray queryDeviceFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select device_id, name, name_en, introduction, photo, photo_vertical, hint, mode_id, company_id, read_count from device", null);
        cursor.moveToFirst();
        String device_id;
        String name;
        String name_en;
        String introduction;
        String photo;
        String photo_vertical;
        String hint;
        int mode_id;
        int company_id;
        int read_count;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            name_en = cursor.getString(cursor.getColumnIndex("name_en"));
            introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            hint = cursor.getString(cursor.getColumnIndex("hint"));
            mode_id = cursor.getInt(cursor.getColumnIndex("zone"));
            company_id = cursor.getInt(cursor.getColumnIndex("company_id"));
            read_count = cursor.getInt(cursor.getColumnIndex("read_count"));

            // add to JSONObject
            file.put("device_id", device_id);
            file.put("name", name);
            file.put("name_en", name_en);
            file.put("introduction", introduction);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            file.put("hint", hint);
            file.put("mode_id", mode_id);
            file.put("company_id", company_id);
            file.put("read_count", read_count);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
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
//        db.insert("project", null, values);
        long rowId = db.insertWithOnConflict("project", null, values, 4);
        if (rowId != -1) {
            Log.i("project", "insert project_id=" + project_id + " success.");
            return true;
        } else {
            return false;
        }
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
                                int y,
                                int field_id,
                                String field_name) {
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
        values.put("field_id", field_id);
        values.put("field_name", field_name);
        long rowId = db.insertWithOnConflict("beacon", null, values, 4);
        if (rowId != -1) {
            Log.i("beacon", "insert beacon_id=" + beacon_id + " success.");
            return true;
        } else {
            return false;
        }
//        db.insert("beacon", null, values);
//        return true;
    }

    public Cursor getBeacon(int beacon_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from beacon where beacon_id=" + beacon_id + "", null);
        return cursor;
    }

    public JSONArray queryBeaconFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from beacon", null);
        cursor.moveToFirst();
        String beacon_id;
        String mac_addr;
        String name;
        int power;
        int status;
        int zone;
        int x;
        int y;
        int field_id;
        String field_name;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            beacon_id = cursor.getString(cursor.getColumnIndex("beacon_id"));
            mac_addr = cursor.getString(cursor.getColumnIndex("mac_addr"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            power = cursor.getInt(cursor.getColumnIndex("power"));
            status = cursor.getInt(cursor.getColumnIndex("status"));
            zone = cursor.getInt(cursor.getColumnIndex("zone"));
            x = cursor.getInt(cursor.getColumnIndex("x"));
            y = cursor.getInt(cursor.getColumnIndex("y"));
            field_id = cursor.getInt(cursor.getColumnIndex("field_id"));
            field_name = cursor.getString(cursor.getColumnIndex("field_name"));

            // add to JSONObject
            file.put("device_id", beacon_id);
            file.put("mac_addr", mac_addr);
            file.put("name", name);
            file.put("power", power);
            file.put("status", status);
            file.put("zone", zone);
            file.put("x", x);
            file.put("y", y);
            file.put("field_id", field_id);
            file.put("field_name", field_name);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    // return JSONObject for 佳穎
    public JSONObject queryBeaconFileWithMacAddr(String mac_addr) throws JSONException {
        JSONObject file = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from beacon where mac_addr=" + mac_addr, null);
        cursor.moveToFirst();
        String beacon_id;
        String name;
        int power;
        int status;
        int zone;
        int x;
        int y;
        int field_id;
        String field_name;
        // fetch all company_id & qrcode
        beacon_id = cursor.getString(cursor.getColumnIndex("beacon_id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        power = cursor.getInt(cursor.getColumnIndex("power"));
        status = cursor.getInt(cursor.getColumnIndex("status"));
        zone = cursor.getInt(cursor.getColumnIndex("zone"));
        x = cursor.getInt(cursor.getColumnIndex("x"));
        y = cursor.getInt(cursor.getColumnIndex("y"));
        field_id = cursor.getInt(cursor.getColumnIndex("field_id"));
        field_name = cursor.getString(cursor.getColumnIndex("field_name"));

        // add to JSONObject
        file.put("device_id", beacon_id);
        file.put("name", name);
        file.put("power", power);
        file.put("status", status);
        file.put("zone", zone);
        file.put("x", x);
        file.put("y", y);
        file.put("field_id", field_id);
        file.put("field_name", field_name);
        return file;

        // P.S.
        // how to parse JSONObject:
        // JSONObject obj = new JSONObject();
        // int number = obj.optInt("column");
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
        long rowId = db.insertWithOnConflict("company", null, values, 4);
        if (rowId != -1) {
            Log.i("company", "insert company_id=" + company_id + " success.");
            return true;
        } else {
            return false;
        }
//        db.insert("company", null, values);
//        return true;
    }

    public Cursor getCompany(int company_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from company where company_id=" + company_id + "", null);
        return cursor;
    }

    // query company files
    public JSONArray queryCompanyFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select company_id, qrcode from company", null);
        cursor.moveToFirst();
        String company_id;
        String qrcode;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            company_id = cursor.getString(cursor.getColumnIndex("company_id"));
            qrcode = cursor.getString(cursor.getColumnIndex("qrcode"));
            // add to JSONObject
            file.put("company_id", company_id);
            file.put("qrcode", qrcode);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
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
        long rowId = db.insertWithOnConflict("field_map", null, values, 4);
        if (rowId != -1) {
            Log.i("field_map", "insert field_map_id=" + field_map_id + " success.");
            return true;
        } else {
            return false;
        }
//        db.insert("field_map", null, values);
//        return true;
    }

    public Cursor getFieldMap(int field_map_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from field_map where field_map_id=" + field_map_id + "", null);
        return cursor;
    }

    // get field_map columns
    public JSONArray queryFieldMapFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select field_map_id, guide_voice, photo, photo_vertical, map_svg from field_map", null);
        cursor.moveToFirst();
        String field_map_id;
        String guide_voice;
        String photo;
        String photo_vertical;
        String map_svg;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            field_map_id = cursor.getString(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            map_svg = cursor.getString(cursor.getColumnIndex("map_svg"));
            // add to JSONObject
            file.put("device_id", field_map_id);
            file.put("guide_voice", guide_voice);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            file.put("map_svg", map_svg);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
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
        long rowId = db.insertWithOnConflict("hipster_content", null, values, 4);
        if (rowId != -1) {
            Log.i("hipster_content", "insert hipster_content_id=" + hipster_content_id + " success.");
            return true;
        } else {
            return false;
        }
//        db.insert("hipster_content", null, values);
//        return true;
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
//        db.insert("hipster_template", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("hipster_template", null, values, 4);
        if (rowId != -1) {
            Log.i("hipster_template", "insert hipster_template_id=" + hipster_template_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getHipsterTemplate(int hipster_template_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hipster_template where hipster_template_id=" + hipster_template_id + "", null);
        return cursor;
    }

    // get hipster template columns
    public JSONArray queryHipsterTemplateFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select hipster_template_id, template from hipster_template", null);
        cursor.moveToFirst();
        String hipster_template_id;
        String template;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            hipster_template_id = cursor.getString(cursor.getColumnIndex("hipster_template_id"));
            template = cursor.getString(cursor.getColumnIndex("template"));
            // add to JSONObject
            file.put("hipster_template_id", hipster_template_id);
            file.put("template", template);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    // hipster text table query and insert
    public boolean insertHipsterText(int hipster_text_id,
                                     String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hipster_text_id", hipster_text_id);
        values.put("content", content);
//        db.insert("hipster_text", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("hipster_text", null, values, 4);
        if (rowId != -1) {
            Log.i("hipster_text", "insert hipster_text_id=" + hipster_text_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getHipsterText(int hipster_text_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hipster_text where hipster_text_id=" + hipster_text_id + "", null);
        return cursor;
    }

    // get hipster text files in an JSONArray
    public JSONArray queryHipsterTextFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select hipster_text_id, content from hipster_text", null);
        cursor.moveToFirst();
        String hipster_text_id;
        String content;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            hipster_text_id = cursor.getString(cursor.getColumnIndex("hipster_text_id"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            // add to JSONObject
            file.put("hipster_text_id", hipster_text_id);
            file.put("content", content);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    // lease table query and insert
    public boolean insertLease(int id,
                               int pad_id,
                               String borrower,
                               String borrower_tel,
                               String lease_date,
                               String return_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("pad_id", pad_id);
        values.put("borrower", borrower);
        values.put("borrower_tel", borrower_tel);
        // lease_date & return_date have to be in correct datetime format
        values.put("lease_date", lease_date);
        values.put("return_date", return_date);
//        db.insert("lease", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("lease", null, values, 4);
        if (rowId != -1) {
            Log.i("lease", "insert id=" + id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getLease(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from lease where id=" + id + "", null);
        return cursor;
    }


    // mode table query and insert
    public boolean insertMode(int mode_id,
                              String name,
                              String name_en,
                              String introduction,
                              String guide_voice,
                              String video,
                              String splash_bg_vertical,
                              String splash_fg_vertical,
                              String splash_blur_vertical,
                              int like_count,
                              int read_count,
                              int time_total,
                              int zone_id,
                              int did_read) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode_id", mode_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("introduction", introduction);
        values.put("guide_voice", guide_voice);
        values.put("video", video);
        values.put("splash_bg_vertical", splash_bg_vertical);
        values.put("splash_fg_vertical", splash_fg_vertical);
        values.put("splash_blur_vertical", splash_blur_vertical);
        values.put("like_count", like_count);
        values.put("read_count", read_count);
        values.put("time_total", time_total);
        values.put("zone_id", zone_id);
        // 0 or 1
        values.put("did_read", did_read);
//        db.insert("mode", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("mode", null, values, 4);
        if (rowId != -1) {
            Log.i("mode", "insert mode_id=" + mode_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getMode(int mode_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from mode where mode_id=" + mode_id + "", null);
        return cursor;
    }

    // get mode files
    public JSONArray queryModeFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode_id, guide_voice, video, splash_bg_vertical, splash_fg_vertical, splash_blur_vertical, like_count, read_count, time_total, zone_id, did_read from mode", null);
        cursor.moveToFirst();
        String device_id;
        String guide_voice;
        String video;
        String splash_bg_vertical;
        String splash_fg_vertical;
        String splash_blur_vertical;
        int like_count = 0;
        int read_count = 0;
        int time_total = 0;
        int zone_id = 0;
        int did_read = 0;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            video = cursor.getString(cursor.getColumnIndex("video"));
            splash_bg_vertical = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
            splash_fg_vertical = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
            splash_blur_vertical = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
            like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
            read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
            time_total = cursor.getInt(cursor.getColumnIndex("time_total"));
            zone_id = cursor.getInt(cursor.getColumnIndex("zone_id"));
            did_read = cursor.getInt(cursor.getColumnIndex("did_read"));
            // add to JSONObject
            file.put("device_id", device_id);
            file.put("guide_voice", guide_voice);
            file.put("video", video);
            file.put("splash_bg_vertical", splash_bg_vertical);
            file.put("splash_fg_vertical", splash_fg_vertical);
            file.put("splash_blur_vertical", splash_blur_vertical);
            file.put("like_count", like_count);
            file.put("read_count", read_count);
            file.put("time_total", time_total);
            file.put("zone_id", zone_id);
            file.put("did_read", did_read);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    //////// *************** 振哥 ****************

    // for 振哥 return the "已讀" given with mode_id
    public int getModeDidRead(int mode_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select did_read from mode where mode_id=" + mode_id, null);
        cursor.moveToFirst();
        int mode_did_read = 0;
        // fetch all company_id & qrcode
        mode_did_read = cursor.getInt(cursor.getColumnIndex("did_read"));
        cursor.moveToNext();
        return mode_did_read;
    }

    // for 振哥 --> return mode data with zone_id
    public JSONArray queryModeDataWithZoneId(int zone_id) throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode_id, guide_voice, video, splash_bg_vertical, splash_fg_vertical, splash_blur_vertical, like_count, read_count, time_total, did_read from mode where zone_id=" + zone_id, null);
        cursor.moveToFirst();
        String device_id;
        String guide_voice;
        String video;
        String splash_bg_vertical;
        String splash_fg_vertical;
        String splash_blur_vertical;
        int like_count = 0;
        int read_count = 0;
        int time_total = 0;
        int did_read = 0;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            video = cursor.getString(cursor.getColumnIndex("video"));
            splash_bg_vertical = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
            splash_fg_vertical = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
            splash_blur_vertical = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
            like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
            read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
            time_total = cursor.getInt(cursor.getColumnIndex("time_total"));
            did_read = cursor.getInt(cursor.getColumnIndex("did_read"));
            // add to JSONObject
            file.put("device_id", device_id);
            file.put("guide_voice", guide_voice);
            file.put("video", video);
            file.put("splash_bg_vertical", splash_bg_vertical);
            file.put("splash_fg_vertical", splash_fg_vertical);
            file.put("splash_blur_vertical", splash_blur_vertical);
            file.put("like_count", like_count);
            file.put("read_count", read_count);
            file.put("time_total", time_total);
            file.put("did_read", did_read);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    public JSONArray customizedDeviceDataWithCounts(int mode_id) throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select device_id, name, name_en, introduction, photo, photo_vertical, hint, company_id, read_count from device where mode_id=" + mode_id, null);
        cursor.moveToFirst();
        String device_id;
        String name;
        String name_en;
        String introduction;
        String photo;
        String photo_vertical;
        String hint;
        int company_id;
        int read_count;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            name_en = cursor.getString(cursor.getColumnIndex("name_en"));
            introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            hint = cursor.getString(cursor.getColumnIndex("hint"));
            company_id = cursor.getInt(cursor.getColumnIndex("company_id"));
            read_count = cursor.getInt(cursor.getColumnIndex("read_count"));

            // add to JSONObject
            file.put("device_id", device_id);
            file.put("name", name);
            file.put("name_en", name_en);
            file.put("introduction", introduction);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            file.put("hint", hint);
            file.put("company_id", company_id);
            file.put("read_count", read_count);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    public int getNumbersOfDevicesFromMode(int mode_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from device where mode_id=" + mode_id, null);
        cursor.moveToFirst();

        int count_devices = cursor.getCount();
        cursor.close();
        // get counts and return
        return count_devices;
    }

    // **************  survey ************
    // survey table query and insert
    public boolean insertSurvey(int survey_id,
                                String name,
                                String email,
                                String gender,
                                String age,
                                String education,
                                String career,
                                String experience,
                                String salary,
                                String location,
                                int house_type,
                                int family_type,
                                int family_member,
                                int know_way) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("survey_id", survey_id);
        values.put("name", name);
        values.put("email", email);
        values.put("gender", gender);
        values.put("age", age);
        values.put("education", education);
        values.put("career", career);
        values.put("experience", experience);
        values.put("salary", salary);
        values.put("location", location);
        values.put("house_type", house_type);
        values.put("family_type", family_type);
        values.put("family_member", family_member);
        values.put("know_way", know_way);
//        db.insert("survey", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("survey", null, values, 4);
        if (rowId != -1) {
            Log.i("survey", "insert survey_id=" + survey_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getSurvey(int survey_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from survey where survey_id=" + survey_id + "", null);
        return cursor;
    }


    // survey result table query and insert
    public boolean insertSurveyResult(int id,
                                      int question,
                                      int answer,
                                      int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("question", question);
        values.put("answer", answer);
        values.put("total", total);
//        db.insert("survey_result", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("survey_result", null, values, 4);
        if (rowId != -1) {
            Log.i("survey_result", "insert id=" + id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getSurveyResult(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from survey_result where id=" + id + "", null);
        return cursor;
    }


    // zone table query and insert
    public boolean insertZone(int zone_id,
                                String name,
                                String name_en,
                                String introduction,
                                String guide_voice,
                                String hint,
                                String photo,
                                String photo_vertical,
                                int field_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("zone_id", zone_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("introduction", introduction);
        values.put("guide_voice", guide_voice);
        values.put("hint", hint);
        values.put("photo", photo);
        values.put("photo_vertical", photo_vertical);
        values.put("field_id", field_id);
//        db.insert("zone", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("zone", null, values, 4);
        if (rowId != -1) {
            Log.i("zone", "insert zone_id=" + zone_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getZone(int zone_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from zone where zone_id=" + zone_id + "", null);
        return cursor;
    }


    // get zone files
    public JSONArray queryZoneFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select zone_id, guide_voice, photo, photo_vertical from zone", null);
        cursor.moveToFirst();
        String device_id;
        String guide_voice;
        String photo;
        String photo_vertical;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            // add to JSONObject
            file.put("device_id", device_id);
            file.put("guide_voice", guide_voice);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }


    // path table query and insert
    public boolean insertPath(int choose_path_id,
                              int path_order,
                              int svg_id,
                              int start,
                              int sn,
                              int end,
                              int en) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("choose_path_id", choose_path_id);
        values.put("path_order", path_order);
        values.put("svg_id", svg_id);
        values.put("start", start);
        values.put("Sn", sn);
        values.put("End", end);
        values.put("En", en);
//        db.insert("path", null, values);
//        return true;
        long rowId = db.insertWithOnConflict("path", null, values, 4);
        if (rowId != -1) {
            Log.i("path", "insert choose_path_id=" + choose_path_id + " success.");
            return true;
        } else {
            return false;
        }
    }


    public Cursor getPath(int path_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from path where choose_path_id=" + path_id + "", null);
        return cursor;
    }


    // get zone files
    public JSONArray queryPaths() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from path", null);
        cursor.moveToFirst();
        int choose_path_id;
        int order;
        int svg_id;
        int start;
        int Sn;
        int End;
        int En;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            choose_path_id = cursor.getInt(cursor.getColumnIndex("choose_path_id"));
            order = cursor.getInt(cursor.getColumnIndex("order"));
            svg_id = cursor.getInt(cursor.getColumnIndex("svg_id"));
            start = cursor.getInt(cursor.getColumnIndex("start"));
            Sn = cursor.getInt(cursor.getColumnIndex("Sn"));
            End = cursor.getInt(cursor.getColumnIndex("End"));
            En = cursor.getInt(cursor.getColumnIndex("En"));
            // add to JSONObject
            file.put("choose_path_id", choose_path_id);
            file.put("path_order", order);
            file.put("svg_id", svg_id);
            file.put("start", start);
            file.put("Sn", Sn);
            file.put("End", End);
            file.put("En", En);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }


    ///////// TODO ( DONE ) : seperate getting img paths from video paths
    /*
        --> get those entries that would need to fetch data from server
     */
    public List<String> getAllDownloadPaths() {
        List<String> paths = new ArrayList<String>();
        // get files needed to be download in company, device, field_map
        // hipster_template, mode, zone
        paths.addAll(getCompanyDownloadFiles());
        paths.addAll(getDeviceDownloadFiles());
        paths.addAll(getFieldMapDownloadFiles());
        paths.addAll(getHipsterTemplateDownloadFiles());
        paths.addAll(getModeDownloadFiles());
        paths.addAll(getZoneDownloadFiles());

        return paths;
    }


    public List<String> getCompanyDownloadFiles() {
        List<String> companyFiles = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select qrcode from company", null);

        cursor.moveToFirst();
        String qrcode = null;
        // fetch all qrcode
        while (cursor.isAfterLast() == false) {
            qrcode = cursor.getString(cursor.getColumnIndex("qrcode"));
            Log.i("company", qrcode);
            if (qrcode.length() != 0 && qrcode != null && qrcode != "null") {
                // add to List if only the qrcode of that company is not empty
                companyFiles.add(qrcode);
            }
            cursor.moveToNext();
        }
        return companyFiles;
    }

    public List<String> getDeviceDownloadFiles() {
        List<String> deviceFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, photo, photo_vertical from device", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String photo = null;
        String photo_vertical = null;
        // fetch all guide_voice, photo, photo_vertical
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
//            Log.i("device guide", guide_voice);
//            Log.i("device photo", photo);
//            Log.i("device ph_ve", photo_vertical);

            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                deviceFiles.add(guide_voice);
            }
            if (photo_vertical.length() != 0 && photo_vertical != null && photo_vertical != "null") {
                deviceFiles.add(photo_vertical);
            }
            // add to List
            deviceFiles.add(photo);
            cursor.moveToNext();
        }
        return deviceFiles;
    }

    public List<String> getFieldMapDownloadFiles() {
        List<String> fieldMapFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, photo, photo_vertical, map_svg from field_map", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String photo = null;
        String photo_vertical = null;
        String map_svg = null;
        // fetch all guide_voice, photo, photo_vertical & svg
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            map_svg = cursor.getString(cursor.getColumnIndex("map_svg"));
            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                fieldMapFiles.add(guide_voice);
            }
            if (photo_vertical.length() != 0 && photo_vertical != null && photo_vertical != "null") {
                fieldMapFiles.add(photo_vertical);
            }
            fieldMapFiles.add(map_svg);
            // add to List
            fieldMapFiles.add(photo);
            cursor.moveToNext();
        }
        return fieldMapFiles;
    }

    public List<String> getHipsterTemplateDownloadFiles() {
        List<String> templateFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select template from hipster_template", null);

        cursor.moveToFirst();
        String template = null;
        // fetch all template
        while (cursor.isAfterLast() == false) {
            template = cursor.getString(cursor.getColumnIndex("template"));
            // add to List
            templateFiles.add(template);
            cursor.moveToNext();
        }
        return templateFiles;
    }


    public List<String> getModeDownloadFiles() {
        List<String> modeFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, splash_bg_vertical, splash_fg_vertical, splash_blur_vertical from mode", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String bg = null;
        String fg = null;
        String blur = null;
        // fetch all guide_voice, video, bg, fg, blur img
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            bg = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
            fg = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
            blur = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
            // check empty or not
            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                modeFiles.add(guide_voice);
            }
            if (bg.length() != 0 && bg != null && bg != "null") {
                modeFiles.add(bg);
            }
            if (fg.length() != 0 && fg != null && fg != "null") {
                modeFiles.add(fg);
            }
            if (blur.length() != 0 && blur != null && blur != "null") {
                modeFiles.add(blur);
            }
            // add to List
            cursor.moveToNext();
        }
        return modeFiles;
    }

    public List<String> getZoneDownloadFiles() {
        List<String> zoneFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, photo, photo_vertical from zone", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String photo = null;
        String photo_vertical = null;
        // fetch all guide_voice, photo, photo_vertical
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                zoneFiles.add(guide_voice);
            }
            if (photo_vertical.length() != 0 && photo_vertical != null && photo_vertical != "null") {
                zoneFiles.add(photo_vertical);
            }
            // add to List
            zoneFiles.add(photo);

            cursor.moveToNext();
        }
        return zoneFiles;
    }

    /*
        ----> get videos from Mode table
     */
    public List<String> getVideoFiles() {
        List<String> videoFiles = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select video from mode", null);
        cursor.moveToFirst();
        String video = null;
        // fetch all guide_voice, video, bg, fg, blur img
        while (cursor.isAfterLast() == false) {
            video = cursor.getString(cursor.getColumnIndex("video"));
            if (video.length() != 0 && video != null && video != "null") {
                // add to List
                videoFiles.add(video);
            }
            cursor.moveToNext();
        }
        return videoFiles;
    }


    // ********************** 文青樣板、罐頭文字、區域顯示  給定資料部分 **********************
    // 文青樣板：用 queryHipsterTemplateFiles()
    // 罐頭文字：用 queryHipsterTextFiles()
    // 區域清單顯示：
    public JSONArray queryListOfZones() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select zone_id, name, name_en from zone", null);
        cursor.moveToFirst();
        String zone_id;
        String name;
        String name_en;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            zone_id = cursor.getString(cursor.getColumnIndex("zone_id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            name_en = cursor.getString(cursor.getColumnIndex("name_en"));

            // add to JSONObject
            file.put("zone_id", zone_id);
            file.put("name", name);
            file.put("name_en", name_en);

            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    // ********************** 裝置  給定資料部分 **********************

    // 用給定的mode_id取出所有裝置
    public JSONArray queryDeviceFilesByMode(int mode_id) throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select device_id, guide_voice, photo, photo_vertical from device where mode_id=" + mode_id, null);
        cursor.moveToFirst();
        String device_id;
        String guide_voice;
        String photo;
        String photo_vertical;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            // add to JSONObject
            file.put("device_id", device_id);
            file.put("guide_voice", guide_voice);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

    // ******************** counter part ********************
    // 每次經過一個device，揪呼叫此函數進行拜訪次數的+1
    public void addReadCount(int device_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select read_count from device where device_id=" + device_id, null);
        int read_count = 0;
        cursor.moveToFirst();
        read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
        read_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("read_count",read_count);
        // update to the same field
        writeDB.update(DatabaseUtilizer.DEVICE_TABLE, cv, "device_id=" + device_id, null);
    }

    // 按讚就加1
    public void addLikeCount(int device_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select like_count from device where device_id=" + device_id, null);
        int like_count = 0;
        cursor.moveToFirst();
        like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
        like_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("like_count",like_count);
        // update to the same field
        writeDB.update(DatabaseUtilizer.DEVICE_TABLE, cv, "device_id=" + device_id, null);
    }



}
