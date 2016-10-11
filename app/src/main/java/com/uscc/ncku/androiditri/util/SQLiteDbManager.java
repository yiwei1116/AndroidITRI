package com.uscc.ncku.androiditri.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uscc.ncku.androiditri.usage.DatabaseUtilizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oslo on 10/6/16.
 */
public class SQLiteDbManager extends SQLiteOpenHelper{

    // database name
    public static final String DATABASE_NAME = "ITRI.db";


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
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PROJECT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_BEACON);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_COMPANY);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_FIELD_MAP);
        // need to upload hipster content
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_CONTENT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEMPLATE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEXT);
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_LEASE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_MODE);
        // problem with path
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PATH);
        // upload survey
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_SURVEY);
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_SURVEY_RESULT);
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_USERS);
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_DEVICE);
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_PI);
//        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_VIP_VOICE);
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

    // get device files
    public JSONArray queryDeviceFiles() throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select device_id, guide_voice, photo, photo_vertical from device", null);
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
        db.insert("field_map", null, values);
        return true;
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
        db.insert("hipster_text", null, values);
        return true;
    }

    public Cursor getHipsterText(int hipster_text_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hipster_text where hipster_text_id=" + hipster_text_id + "", null);
        return cursor;
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
        db.insert("lease", null, values);
        return true;
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
                              int zone_id) {
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
        values.put("zone_id", zone_id);
        db.insert("mode", null, values);
        return true;
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
        Cursor cursor = db.rawQuery("select mode_id, guide_voice, video, splash_bg_vertical, splash_fg_vertical, splash_blur_vertical from mode", null);
        cursor.moveToFirst();
        String device_id;
        String guide_voice;
        String video;
        String splash_bg_vertical;
        String splash_fg_vertical;
        String splash_blur_vertical;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            video = cursor.getString(cursor.getColumnIndex("video"));
            splash_bg_vertical = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
            splash_fg_vertical = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
            splash_blur_vertical = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
            // add to JSONObject
            file.put("device_id", device_id);
            file.put("guide_voice", guide_voice);
            file.put("video", video);
            file.put("splash_bg_vertical", splash_bg_vertical);
            file.put("splash_fg_vertical", splash_fg_vertical);
            file.put("splash_blur_vertical", splash_blur_vertical);
            filePaths.put(file);
            file = null;
            cursor.moveToNext();
        }
        return filePaths;
    }

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
        db.insert("survey", null, values);
        return true;
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
        db.insert("survey_result", null, values);
        return true;
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
        db.insert("zone", null, values);
        return true;
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
    /*
        --> get those entries that would need to fetch data from server
     */


}
