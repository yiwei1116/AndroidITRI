package org.tabc.living3.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tabc.living3.CommunicationWithServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oslo on 10/6/16.
 */
public class SQLiteDbManager extends SQLiteOpenHelper{

    // database name
    public static final String DATABASE_NAME = "living_3_0_ITRI.db";
    public Context context;
    public CommunicationWithServer communicationWithServer;

    // TODO: 需要根據資料庫欄位的“最後更新時間”，去決定需要下載哪些欄位
    /// TODO: 若檔案大小介於可以容忍的範圍內則不下載, 若否則不下載

    /*
           ----> beacon, company, device, field_map, hipster_template, hipster_text, mode, zone
     */
    public static final int VERSION = 1;

    public SQLiteDbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        this.context = context;
        this.communicationWithServer = new CommunicationWithServer();
    }

    public SQLiteDbManager(Context context, String name) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    public SQLiteDbManager(Context context, String name, int version) {
        super(context, DATABASE_NAME, null, version);
        this.context = context;
    }

    public SQLiteDbManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_DEVICE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_BEACON);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_COMPANY);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_FIELD_MAP);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEMPLATE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_HIPSTER_TEXT);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_MODE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_ZONE);
        db.execSQL(DatabaseUtilizer.DB_CREATE_TABLE_PATH);
    }

    public void onOpen(SQLiteDatabase db) {
        Log.i("SQLiteDB", "database opened");
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
                                String introduction_en,
                                String guide_voice,
                                String guide_voice_en,
                                String photo,
                                String photo_size,
                                String photo_vertical,
                                String photo_vertical_size,
                                String hint,
                                int mode_id,
                                int company_id,
                                int read_count,
                                int like_count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("device_id", device_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("introduction", introduction);
        values.put("introduction_en", introduction_en);
        values.put("guide_voice", guide_voice);
        values.put("guide_voice_en", guide_voice_en);
        values.put("photo", photo);
        values.put("photo_vertical", photo_vertical);
        values.put("hint", hint);
        values.put("mode_id", mode_id);
        values.put("company_id", company_id);
        values.put("read_count", read_count);
        values.put("like_count", like_count);
        int photo_size_int = Integer.parseInt(photo_size);
        int photo_vertical_size_int = Integer.parseInt(photo_vertical_size);
        values.put("photo_size", photo_size_int);
        values.put("photo_vertical_size", photo_vertical_size_int);

        // error
        long rowId = db.insertWithOnConflict("device", null, values, 4);
        if (rowId != -1) {
            Log.i("device", "insert device_id=" + device_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    // get device files without guide voice
    public JSONArray queryDeviceFilesWithModeId(int mode_id) throws JSONException {
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select device_id, name, name_en, introduction, introduction_en, guide_voice, guide_voice_en, photo, photo_size, photo_vertical, photo_vertical_size, hint, company_id, read_count, like_count from device where mode_id=" + mode_id, null);
        cursor.moveToFirst();
        int device_id;
        String name;
        String name_en;
        String introduction;
        String introduction_en;
        String guide_voice;
        String guide_voice_en;
        String photo;
        int photo_size;
        String photo_vertical;
        int photo_vertical_size;
        String hint;
        int company_id;
        int read_count;
        int like_count;

        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            JSONObject file = new JSONObject();
            device_id = cursor.getInt(cursor.getColumnIndex("device_id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            name_en = cursor.getString(cursor.getColumnIndex("name_en"));
            introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            introduction_en = cursor.getString(cursor.getColumnIndex("introduction_en"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_size = cursor.getInt(cursor.getColumnIndex("photo_size"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            photo_vertical_size = cursor.getInt(cursor.getColumnIndex("photo_vertical_size"));
            hint = cursor.getString(cursor.getColumnIndex("hint"));
            company_id = cursor.getInt(cursor.getColumnIndex("company_id"));
            read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
            like_count = cursor.getInt(cursor.getColumnIndex("like_count"));

            // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
            File rootDir = this.context.getFilesDir();
            File path = new File(rootDir.getAbsolutePath() + "/itri");
            if ( !path.exists() ) {
                path.mkdirs();
            }

            String[] photo_path = photo.split("/");
            String[] photo_vertical_path = photo_vertical.split("/");
            File photo_file = new File(path, photo_path[photo_path.length -1]);
            File photo_vertical_file = new File(path, photo_vertical_path[photo_vertical_path.length -1]);

            if (!photo_file.exists()) {
                Log.d("GGG", "mode photo");
                //DownloadSingleFile(photo);
                new DownloadSingleFileTask(photo).execute();
            } else {
                int photo_f_size = Integer.parseInt(String.valueOf(photo_file.length()/1024));
                if ((photo_size - photo_f_size) > 10 || (photo_size - photo_f_size) < -10) {
                    // re-download
                    Log.d("GGG", "mode photo");
                    new DownloadSingleFileTask(photo).execute();
                }
            }

            if (!photo_vertical_file.exists()) {
                Log.d("GGG", "mode photo_v");
                // DownloadSingleFile(photo_vertical);
                new DownloadSingleFileTask(photo_vertical).execute();
            } else {
                int photo_vertical_file_size = Integer.parseInt(String.valueOf(photo_vertical_file.length()/1024));
                if ( (photo_vertical_size - photo_vertical_file_size) > 10 || (photo_vertical_size - photo_vertical_file_size) < -10) {
                    // re-download
                    Log.d("GGG", "mode photo_v");
                    new DownloadSingleFileTask(photo_vertical).execute();
                }
            }

            // add to JSONObject
            file.put("device_id", device_id);
            file.put("name", name);
            file.put("name_en", name_en);
            file.put("introduction", introduction);
            file.put("introduction_en", introduction_en);
            file.put("guide_voice", guide_voice);
            file.put("guide_voice_en", guide_voice_en);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            file.put("hint", hint);
            file.put("mode_id", mode_id);
            file.put("company_id", company_id);
            file.put("read_count", read_count);
            file.put("like_count", like_count);
            filePaths.put(file);
            cursor.moveToNext();
        }
        cursor.close();

        return filePaths;
    }

    // 振哥
    public JSONObject queryDeviceAndCompanyData(int device_id) throws JSONException {
        JSONObject file = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name, name_en, introduction, introduction_en, guide_voice, guide_voice_en, photo, photo_size, photo_vertical, photo_vertical_size, hint, mode_id, company_id, read_count, like_count from device where device_id=" + device_id, null);
        cursor.moveToFirst();
        String name;
        String name_en;
        String introduction;
        String introduction_en;
        String guide_voice;
        String guide_voice_en;
        String photo;
        int photo_size;
        String photo_vertical;
        int photo_vertical_size;
        String hint;
        int mode_id;
        int company_id;
        int read_count;
        int like_count;

        // fetch all company_id & qrcode
        name = cursor.getString(cursor.getColumnIndex("name"));
        name_en = cursor.getString(cursor.getColumnIndex("name_en"));
        introduction = cursor.getString(cursor.getColumnIndex("introduction"));
        introduction_en = cursor.getString(cursor.getColumnIndex("introduction_en"));
        guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
        guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
        photo = cursor.getString(cursor.getColumnIndex("photo"));
        photo_size = cursor.getInt(cursor.getColumnIndex("photo_size"));
        photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
        photo_vertical_size = cursor.getInt(cursor.getColumnIndex("photo_vertical_size"));
        hint = cursor.getString(cursor.getColumnIndex("hint"));
        mode_id = cursor.getInt(cursor.getColumnIndex("mode_id"));
        company_id = cursor.getInt(cursor.getColumnIndex("company_id"));
        read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
        like_count = cursor.getInt(cursor.getColumnIndex("like_count"));

        // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
        File rootDir = this.context.getFilesDir();
        File path = new File(rootDir.getAbsolutePath() + "/itri");
        if ( !path.exists() ) {
            path.mkdirs();
        }

        String[] photo_path = photo.split("/");
        String[] photo_vertical_path = photo_vertical.split("/");
        File photo_file = new File(path, photo_path[photo_path.length -1]);
        File photo_vertical_file = new File(path, photo_vertical_path[photo_vertical_path.length -1]);

        if (!photo_file.exists()) {
            Log.d("GGG", "comp photo");
            new DownloadSingleFileTask(photo).execute();
        } else {
            int photo_f_size = Integer.parseInt(String.valueOf(photo_file.length()/1024));
            if ((photo_size - photo_f_size) > 10 || (photo_size - photo_f_size) < -10) {
                // re-download
                Log.d("GGG", "s comp photo");
                new DownloadSingleFileTask(photo).execute();
            }
        }

        if (!photo_vertical_file.exists()) {
            Log.d("GGG", "comp photo_v");
            new DownloadSingleFileTask(photo_vertical).execute();
        } else {
            int photo_vertical_file_size = Integer.parseInt(String.valueOf(photo_vertical_file.length()/1024));
            if ( (photo_vertical_size - photo_vertical_file_size) > 10 || (photo_vertical_size - photo_vertical_file_size) < -10) {
                // re-download
                Log.d("GGG", "s comp photo");
                new DownloadSingleFileTask(photo_vertical).execute();
            }
        }

        // add to JSONObject
        file.put("device_id", device_id);
        file.put("name", name);
        file.put("name_en", name_en);
        file.put("introduction", introduction);
        file.put("introduction_en", introduction_en);
        file.put("guide_voice", guide_voice);
        file.put("guide_voice_en", guide_voice_en);
        file.put("photo", photo);
        file.put("photo_vertical", photo_vertical);
        file.put("hint", hint);
        file.put("mode_id", mode_id);
        file.put("company_id", company_id);
        file.put("read_count", read_count);
        file.put("like_count", like_count);
        file.put("company_data", getCompanyJSONObject(company_id));
        // close cursor
        cursor.close();
        return file;
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
            file = new JSONObject();
            cursor.moveToNext();
        }
        cursor.close();
        return filePaths;
    }

    // return JSONObject for beacon
    public JSONObject queryBeaconFileWithMacAddr(String mac) throws JSONException {
        JSONObject file = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from beacon where mac_addr=" + "'" + mac + "'", null);
        cursor.moveToFirst();
        String beacon_id;
        String name;
        String mac_addr;
        int power;
        int status;
        int zone;
        int x;
        int y;
        int field_id;
        String field_name;
        String map_svg;
        String map_svg_en;
        String map_bg;
        int map_svg_size;
        int map_bg_size;
        int map_svg_en_size;
        int start;
        int end;
        String svg_id;
        // fetch all company_id & qrcode
        mac_addr = cursor.getString(cursor.getColumnIndex("mac_addr"));
        beacon_id = cursor.getString(cursor.getColumnIndex("beacon_id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        power = cursor.getInt(cursor.getColumnIndex("power"));
        status = cursor.getInt(cursor.getColumnIndex("status"));
        zone = cursor.getInt(cursor.getColumnIndex("zone"));
        x = cursor.getInt(cursor.getColumnIndex("x"));
        y = cursor.getInt(cursor.getColumnIndex("y"));
        field_id = cursor.getInt(cursor.getColumnIndex("field_id"));
        field_name = cursor.getString(cursor.getColumnIndex("field_name"));

        Cursor fieldMapCursor = db.rawQuery("select map_svg, map_svg_size, map_svg_en, map_svg_en_size, map_bg, map_bg_size from field_map where field_map_id=" + field_id, null);
        fieldMapCursor.moveToFirst();
        map_svg = fieldMapCursor.getString(fieldMapCursor.getColumnIndex("map_svg"));
        map_svg_en = fieldMapCursor.getString(fieldMapCursor.getColumnIndex("map_svg_en"));
        map_bg = fieldMapCursor.getString(fieldMapCursor.getColumnIndex("map_bg"));

        map_svg_size = fieldMapCursor.getInt(fieldMapCursor.getColumnIndex(DatabaseUtilizer.MAP_SVG_SIZE));
        map_svg_en_size = fieldMapCursor.getInt(fieldMapCursor.getColumnIndex(DatabaseUtilizer.MAP_SVG_EN_SIZE));
        map_bg_size = fieldMapCursor.getInt(fieldMapCursor.getColumnIndex(DatabaseUtilizer.MAP_BG_SIZE));

        // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
        File rootDir = this.context.getFilesDir();
        File path = new File(rootDir.getAbsolutePath() + "/itri");
        if ( !path.exists() ) {
            path.mkdirs();
        }

        String[] svg_path = map_svg.split("/");
        String[] svg_en_path = map_svg_en.split("/");
        String[] bg_path = map_bg.split("/");
        File svg_file = new File(path, svg_path[svg_path.length - 1]);
        File svg_en_file = new File(path, svg_en_path[svg_en_path.length - 1]);
        File bg_file = new File(path, bg_path[bg_path.length - 1]);

        if (!svg_file.exists()) {
            Log.d("GGG", "mac map svg");
            new DownloadSingleFileTask(map_svg).execute();
        } else {
            int svg_file_size = Integer.parseInt(String.valueOf(svg_file.length()/1024));
            if ( (map_svg_size - svg_file_size) > 10 || (map_svg_size - svg_file_size) < -10) {
                // re-download
                Log.d("GGG", "mac s map svg");
                new DownloadSingleFileTask(map_svg).execute();
            }
        }

        if (!svg_en_file.exists()) {
            Log.d("GGG", "map svg en");
            new DownloadSingleFileTask(map_svg_en).execute();
        } else {
            int svg_en_file_size = Integer.parseInt(String.valueOf(svg_en_file.length()/1024));
            if ( (map_svg_en_size - svg_en_file_size) > 10 || (map_svg_en_size - svg_en_file_size) < -10) {
                // re-download
                Log.d("GGG", "mac s map svg en");
                new DownloadSingleFileTask(map_svg_en).execute();
            }
        }

        if (!bg_file.exists()) {
            Log.d("GGG", "mac map bg");
            new DownloadSingleFileTask(map_bg).execute();
        } else {
            int bg_file_size = Integer.parseInt(String.valueOf(bg_file.length()/1024));
            if ( (map_bg_size - bg_file_size) > 10 || (map_bg_size - bg_file_size) < -10) {
                // re-download
                Log.d("GGG", "mac s map bg");
                new DownloadSingleFileTask(map_bg).execute();
            }
        }

        Cursor pathCursor = db.rawQuery("select start, end, svg_id from path where start=" + beacon_id, null);
        pathCursor.moveToFirst();
        start = pathCursor.getInt(pathCursor.getColumnIndex("start"));
        end = pathCursor.getInt(pathCursor.getColumnIndex("end"));
        svg_id = pathCursor.getString(pathCursor.getColumnIndex("svg_id"));

        // add to JSONObject
        file.put("mac_addr", mac_addr);
        file.put("device_id", beacon_id);
        file.put("name", name);
        file.put("power", power);
        file.put("status", status);
        file.put("zone", zone);
        file.put("x", x);
        file.put("y", y);
        file.put("field_id", field_id);
        file.put("field_name", field_name);
        file.put("map_svg", map_svg);
        file.put("map_svg_en", map_svg_en);
        file.put("map_bg", map_bg);
        file.put("start", start);
        file.put("end", end);
        file.put("svg_id", svg_id);
        Log.e("everything", String.valueOf(file));
        // close cursor
        cursor.close();
        fieldMapCursor.close();
        pathCursor.close();

        return file;
    }

    // same function as above, ** query with ZoneId **
    public JSONObject queryBeaconFileWithZoneId(int zoneId) throws JSONException{
        JSONObject file = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from beacon where zone=" + zoneId, null);
        cursor.moveToFirst();
        String beacon_id;
        String name;
        String mac_addr;
        int power;
        int status;
        int zone;
        int x;
        int y;
        int field_id;
        String field_name;
        String map_svg;
        String map_svg_en;
        String map_bg;
        int map_svg_size;
        int map_bg_size;
        int map_svg_en_size;
        int start;
        int end;
        String svg_id;
        // fetch all company_id & qrcode
        mac_addr = cursor.getString(cursor.getColumnIndex("mac_addr"));
        beacon_id = cursor.getString(cursor.getColumnIndex("beacon_id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        power = cursor.getInt(cursor.getColumnIndex("power"));
        status = cursor.getInt(cursor.getColumnIndex("status"));
        zone = cursor.getInt(cursor.getColumnIndex("zone"));
        x = cursor.getInt(cursor.getColumnIndex("x"));
        y = cursor.getInt(cursor.getColumnIndex("y"));
        field_id = cursor.getInt(cursor.getColumnIndex("field_id"));
        field_name = cursor.getString(cursor.getColumnIndex("field_name"));

        Cursor fieldMapCursor = db.rawQuery("select map_svg, map_svg_size, map_svg_en, map_svg_en_size, map_bg, map_bg_size from field_map where field_map_id=" + field_id, null);
        fieldMapCursor.moveToFirst();
        map_svg = fieldMapCursor.getString(fieldMapCursor.getColumnIndex("map_svg"));
        map_svg_en = fieldMapCursor.getString(fieldMapCursor.getColumnIndex("map_svg_en"));
        map_bg = fieldMapCursor.getString(fieldMapCursor.getColumnIndex("map_bg"));

        map_svg_size = fieldMapCursor.getInt(fieldMapCursor.getColumnIndex(DatabaseUtilizer.MAP_SVG_SIZE));
        map_svg_en_size = fieldMapCursor.getInt(fieldMapCursor.getColumnIndex(DatabaseUtilizer.MAP_SVG_EN_SIZE));
        map_bg_size = fieldMapCursor.getInt(fieldMapCursor.getColumnIndex(DatabaseUtilizer.MAP_BG_SIZE));

        // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
        File rootDir = this.context.getFilesDir();
        File path = new File(rootDir.getAbsolutePath() + "/itri");
        if ( !path.exists() ) {
            path.mkdirs();
        }

        String[] svg_path = map_svg.split("/");
        String[] svg_en_path = map_svg_en.split("/");
        String[] bg_path = map_bg.split("/");
        File svg_file = new File(path, svg_path[svg_path.length - 1]);
        File svg_en_file = new File(path, svg_en_path[svg_en_path.length - 1]);
        File bg_file = new File(path, bg_path[bg_path.length - 1]);

        if (!svg_file.exists()) {
            Log.d("GGG", "zone map svg");
            new DownloadSingleFileTask(map_svg).execute();
        } else {
            int svg_file_size = Integer.parseInt(String.valueOf(svg_file.length()/1024));
            if ( (map_svg_size - svg_file_size) > 10 || (map_svg_size - svg_file_size) < -10) {
                // re-download
                Log.d("GGG", "zone s map svg");
                new DownloadSingleFileTask(map_svg).execute();
            }
        }

        if (!svg_en_file.exists()) {
            Log.d("GGG", "zone map svg en");
            new DownloadSingleFileTask(map_svg_en).execute();
        } else {
            int svg_en_file_size = Integer.parseInt(String.valueOf(svg_en_file.length()/1024));
            if ( (map_svg_en_size - svg_en_file_size) > 10 || (map_svg_en_size - svg_en_file_size) < -10) {
                // re-download
                Log.d("GGG", "s zone map svg en");
                new DownloadSingleFileTask(map_svg_en).execute();
            }
        }

        if (!bg_file.exists()) {
            Log.d("GGG", "zone map bg");
            new DownloadSingleFileTask(map_bg).execute();
        } else {
            int bg_file_size = Integer.parseInt(String.valueOf(bg_file.length()/1024));
            if ( (map_bg_size - bg_file_size) > 10 || (map_bg_size - bg_file_size) < -10) {
                // re-download
                Log.d("GGG", "s zone map bg");
                new DownloadSingleFileTask(map_bg).execute();
            }
        }
        Cursor pathCursor = db.rawQuery("select start, end, svg_id from path where start=" + beacon_id, null);
        pathCursor.moveToFirst();
        start = pathCursor.getInt(pathCursor.getColumnIndex("start"));
        end = pathCursor.getInt(pathCursor.getColumnIndex("end"));
        svg_id = pathCursor.getString(pathCursor.getColumnIndex("svg_id"));

        // add to JSONObject
        file.put("mac_addr", mac_addr);
        file.put("device_id", beacon_id);
        file.put("name", name);
        file.put("power", power);
        file.put("status", status);
        file.put("zone", zone);
        file.put("x", x);
        file.put("y", y);
        file.put("field_id", field_id);
        file.put("field_name", field_name);
        file.put("map_svg", map_svg);
        file.put("map_svg_en", map_svg_en);
        file.put("map_bg", map_bg);
        file.put("start", start);
        file.put("end", end);
        file.put("svg_id", svg_id);
        Log.e("everything", String.valueOf(file));
        // close cursor
        cursor.close();
        fieldMapCursor.close();
        pathCursor.close();

        return file;
    }

    public ArrayList<String> querySvgId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select svg_id from path", null);
        cursor.moveToFirst();
        ArrayList<String> pths = new ArrayList<String>();
        String svg_id = "";
        while(cursor.isAfterLast() == false) {
            svg_id = cursor.getString(cursor.getColumnIndex("svg_id"));
            pths.add(svg_id);
            cursor.moveToNext();
        }
        cursor.close();
        return pths;
    }

    // company table query and insert
    public boolean insertCompany(int company_id,
                                 String name,
                                 String name_en,
                                 String tel,
                                 String fax,
                                 String addr,
                                 String web,
                                 String qrcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("company_id", company_id);
        values.put("name", name);
        values.put("name_en", name_en);
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
    }

    public Cursor getCompany(int company_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from company where company_id=" + company_id + "", null);
        return cursor;
    }

    public JSONObject getCompanyJSONObject(int company_id) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name, name_en, tel, fax, addr, web, qrcode from company where company_id=" + company_id, null);
        String name ;
        String name_en ;
        String tel ;
        String fax ;
        String addr;
        String web;
        String qrcode;
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndex("name"));
        name_en = cursor.getString(cursor.getColumnIndex("name_en"));
        tel = cursor.getString(cursor.getColumnIndex("tel"));
        fax = cursor.getString(cursor.getColumnIndex("fax"));
        addr = cursor.getString(cursor.getColumnIndex("addr"));
        web = cursor.getString(cursor.getColumnIndex("web"));
        qrcode = cursor.getString(cursor.getColumnIndex("qrcode"));
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("name_en", name_en);
        obj.put("tel", tel);
        obj.put("fax", fax);
        obj.put("addr", addr);
        obj.put("web", web);
        obj.put("qrcode", qrcode);
        cursor.close();
        return obj;
    }

    // field map table query and insert
    public boolean insertFieldMap(int field_map_id,
                                  String name,
                                  String name_en,
                                  int project_id,
                                  String introduction,
                                  String photo,
                                  String photo_size,
                                  String photo_vertical,
                                  String photo_vertical_size,
                                  String map_svg,
                                  String map_svg_size,
                                  String map_svg_en,
                                  String map_svg_en_size,
                                  String map_bg,
                                  String map_bg_size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("field_map_id", field_map_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("project_id", project_id);
        values.put("introduction", introduction);
        values.put("photo", photo);
        // parse string to ints to store those file sizes in "kb"
        int photo_size_int = Integer.parseInt(photo_size);
        values.put("photo_size", photo_size_int);
        values.put("photo_vertical", photo_vertical);
        int photo_vertical_size_int = Integer.parseInt(photo_vertical_size);
        values.put("photo_vertical_size", photo_vertical_size_int);
        values.put("map_svg", map_svg);
        int map_svg_size_int = Integer.parseInt(map_svg_size);
        values.put("map_svg_size", map_svg_size_int);
        values.put("map_svg_en", map_svg_en);
        int map_svg_en_size_int = Integer.parseInt(map_svg_en_size);
        values.put("map_svg_en_size", map_svg_en_size_int);
        values.put("map_bg", map_bg);
        int map_bg_size_int = Integer.parseInt(map_bg_size);
        values.put("map_bg_size", map_bg_size_int);

        long rowId = db.insertWithOnConflict("field_map", null, values, 4);
        if (rowId != -1) {
            Log.i("field_map", "insert field_map_id=" + field_map_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public Cursor getFieldMap(int field_map_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from field_map where field_map_id=" + field_map_id + "", null);
        return cursor;
    }

    // give jiang
    public JSONObject queryFieldMapWithFieldMapId(int field_map_id) throws JSONException {
        JSONObject file = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select field_map_id, photo, photo_size, photo_vertical, photo_vertical_size, map_svg, map_svg_size, map_svg_en, map_svg_en_size, map_bg, map_bg_size from field_map where field_map_id=" + field_map_id, null);
        cursor.moveToFirst();
        String photo;
        int photo_size;
        int photo_vertical_size;

        String photo_vertical;
        String map_svg;
        String map_svg_en;
        String map_bg;
        int map_svg_size;
        int map_bg_size;
        int map_svg_en_size;
        // fetch all company_id & qrcode
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            map_svg = cursor.getString(cursor.getColumnIndex("map_svg"));
            map_svg_en = cursor.getString(cursor.getColumnIndex("map_svg_en"));
            map_bg = cursor.getString(cursor.getColumnIndex("map_bg"));
            photo_size = cursor.getInt(cursor.getColumnIndex("photo_size"));
            photo_vertical_size = cursor.getInt(cursor.getColumnIndex("photo_vertical_size"));
            map_svg_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.MAP_SVG_SIZE));
            map_svg_en_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.MAP_SVG_EN_SIZE));
            map_bg_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.MAP_BG_SIZE));

            // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
            File rootDir = this.context.getFilesDir();
            File path = new File(rootDir.getAbsolutePath() + "/itri");
            if ( !path.exists() ) {
                path.mkdirs();
            }

            String[] svg_path = map_svg.split("/");
            String[] svg_en_path = map_svg_en.split("/");
            String[] bg_path = map_bg.split("/");
            File svg_file = new File(path, svg_path[svg_path.length - 1]);
            File svg_en_file = new File(path, svg_en_path[svg_en_path.length - 1]);
            File bg_file = new File(path, bg_path[bg_path.length - 1]);

            int svg_file_size = Integer.parseInt(String.valueOf(svg_file.length()/1024));
            int svg_en_file_size = Integer.parseInt(String.valueOf(svg_en_file.length()/1024));
            int bg_file_size = Integer.parseInt(String.valueOf(bg_file.length()/1024));

            String[] photo_path = photo.split("/");
            String[] photo_vertical_path = photo_vertical.split("/");
            File photo_file = new File(path, photo_path[photo_path.length -1]);
            File photo_vertical_file = new File(path, photo_vertical_path[photo_vertical_path.length -1]);
            int photo_f_size = Integer.parseInt(String.valueOf(photo_file.length()/1024));
            int photo_vertical_file_size = Integer.parseInt(String.valueOf(photo_vertical_file.length()/1024));

            if (!photo_file.exists()) {
                Log.d("GGG", "field photo");
                new DownloadSingleFileTask(photo).execute();
            } else if ( (photo_size - photo_f_size) > 10 || (photo_size - photo_f_size) < -10) {
                // re-download
                Log.d("GGG", "s field photo");
                new DownloadSingleFileTask(photo).execute();
            }

            if (!photo_vertical_file.exists()) {
                Log.d("GGG", "field photo v");
                new DownloadSingleFileTask(photo_vertical).execute();
            } else if ( (photo_vertical_size - photo_vertical_file_size) > 10 || (photo_vertical_size - photo_vertical_file_size) < -10) {
                // re-download
                Log.d("GGG", "s field photo v");
                new DownloadSingleFileTask(photo_vertical).execute();
            }


            if (!svg_file.exists()) {
                Log.d("GGG", "field map svg");
                new DownloadSingleFileTask(map_svg).execute();
            } else if ( (map_svg_size - svg_file_size) > 10 || (map_svg_size - svg_file_size) < -10) {
                // re-download
                Log.d("GGG", "s field map svg");
                new DownloadSingleFileTask(map_svg).execute();
            }

            if (!svg_en_file.exists()) {
                Log.d("GGG", "field map svg en");
                new DownloadSingleFileTask(map_svg_en).execute();
            } else if ( (map_svg_en_size - svg_en_file_size) > 10 || (map_svg_en_size - svg_en_file_size) < -10) {
                // re-download
                Log.d("GGG", "s field map svg en");
                new DownloadSingleFileTask(map_svg_en).execute();
            }

            if (!bg_file.exists()) {
                Log.d("GGG", "field map bg");
                new DownloadSingleFileTask(map_bg).execute();
            } else if ( (map_bg_size - bg_file_size) > 10 || (map_bg_size - bg_file_size) < -10) {
                // re-download
                Log.d("GGG", "s field map bg");
                new DownloadSingleFileTask(map_bg).execute();
            }

            map_svg = svg_path[svg_path.length - 1];
            map_svg_en = svg_en_path[svg_en_path.length - 1];
            map_bg = bg_path[bg_path.length - 1];

            // add to JSONObject
            file.put("field_map_id", field_map_id);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            file.put("map_svg", map_svg);
            file.put("map_svg_en", map_svg_en);
            file.put("map_bg", map_bg);
        cursor.close();
        return file;
    }

    // hipster template table query and insert
    public boolean insertHipsterTemplate(int hipster_template_id,
                                         String name,
                                         String template,
                                         String template_size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hipster_template_id", hipster_template_id);
        values.put("name", name);
        values.put("template", template);
        int template_size_int = Integer.parseInt(template_size);
        values.put("template_size", template_size_int);

        long rowId = db.insertWithOnConflict("hipster_template", null, values, 4);
        if (rowId != -1) {
            Log.i("hipster_template", "insert hipster_template_id=" + hipster_template_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    // hipster text table query and insert
    public boolean insertHipsterText(int hipster_text_id,
                                     String content,
                                     String content_en) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hipster_text_id", hipster_text_id);
        values.put("content", content);
        values.put("content_en", content_en);
        long rowId = db.insertWithOnConflict("hipster_text", null, values, 4);
        if (rowId != -1) {
            Log.i("hipster_text", "insert hipster_text_id=" + hipster_text_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    // mode table query and insert
    public boolean insertMode(int mode_id,
                              String name,
                              String name_en,
                              String introduction,
                              String introduction_en,
                              String guide_voice,
                              String guide_voice_en,
                              String video,
                              String splash_bg_vertical,
                              String splash_bg_vertical_size,
                              String splash_fg_vertical,
                              String splash_fg_vertical_size,
                              String splash_blur_vertical,
                              String splash_blur_vertical_size,
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
        values.put("introduction_en", introduction_en);
        values.put("guide_voice", guide_voice);
        values.put("guide_voice_en", guide_voice_en);
        values.put("video", video);
        values.put("splash_bg_vertical", splash_bg_vertical);
        int splash_bg_vertical_size_int = Integer.parseInt(splash_bg_vertical_size);
        values.put("splash_bg_vertical_size", splash_bg_vertical_size_int);
        values.put("splash_fg_vertical", splash_fg_vertical);
        int splash_fg_vertical_size_int = Integer.parseInt(splash_fg_vertical_size);
        values.put("splash_fg_vertical_size", splash_fg_vertical_size_int);
        values.put("splash_blur_vertical", splash_blur_vertical);
        int splash_blur_vertical_size_int = Integer.parseInt(splash_blur_vertical_size);
        values.put("splash_blur_vertical_size", splash_blur_vertical_size_int);
        values.put("like_count", like_count);
        values.put("read_count", read_count);
        values.put("time_total", time_total);
        values.put("zone_id", zone_id);
        // 0 or 1
        values.put("did_read", did_read);
        long rowId = db.insertWithOnConflict("mode", null, values, 4);
        if (rowId != -1) {
            Log.i("mode", "insert mode_id=" + mode_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    // get mode files with mode_id  振哥
    public JSONObject queryModeFiles(int mode_id) throws JSONException {
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from mode where mode_id=" + mode_id, null);
        cursor.moveToFirst();
        String name;
        String name_en;
        String introduction;
        String introduction_en;
        String guide_voice;
        String guide_voice_en;
        String video;
        String splash_bg_vertical;
        int splash_bg_size;
        String splash_fg_vertical;
        int splash_fg_size;
        String splash_blur_vertical;
        int splash_blur_size;
        int like_count = 0;
        int read_count = 0;
        int time_total = 0;
        int zone_id = 0;
        int did_read = 0;
        // fetch all company_id & qrcode
        JSONObject file = new JSONObject();
        name = cursor.getString(cursor.getColumnIndex("name"));
        name_en = cursor.getString(cursor.getColumnIndex("name_en"));
        introduction = cursor.getString(cursor.getColumnIndex("introduction"));
        introduction_en = cursor.getString(cursor.getColumnIndex("introduction_en"));
        guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
        guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
        video = cursor.getString(cursor.getColumnIndex("video"));
        splash_bg_vertical = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
        splash_bg_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.SPLASH_BG_SIZE));
        splash_fg_vertical = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
        splash_fg_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.SPLASH_FG_SIZE));
        splash_blur_vertical = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
        splash_blur_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.SPLASH_BLUR_SIZE));
        like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
        read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
        time_total = cursor.getInt(cursor.getColumnIndex("time_total"));
        zone_id = cursor.getInt(cursor.getColumnIndex("zone_id"));
        did_read = cursor.getInt(cursor.getColumnIndex("did_read"));

        // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
        File rootDir = this.context.getFilesDir();
        File path = new File(rootDir.getAbsolutePath() + "/itri");
        if ( !path.exists() ) {
            path.mkdirs();
        }

        String[] bg_path = splash_bg_vertical.split("/");
        String[] fg_path = splash_fg_vertical.split("/");
        String[] blur_path = splash_blur_vertical.split("/");
        File bg_file = new File(path, bg_path[bg_path.length - 1]);
        File fg_file = new File(path, fg_path[fg_path.length - 1]);
        File blur_file = new File(path, blur_path[blur_path.length - 1]);

        if (!bg_file.exists()) {
            Log.d("GGG", "mode s bg v");
            new DownloadSingleFileTask(splash_bg_vertical).execute();
        } else {
            int bg_file_size = Integer.parseInt(String.valueOf(bg_file.length()/1024));
            if ( (splash_bg_size - bg_file_size) > 10 || (splash_bg_size - bg_file_size) < -10) {
                // re-download
                Log.d("GGG", "s mode s bg v");
                new DownloadSingleFileTask(splash_bg_vertical).execute();
            }
        }

        if (!fg_file.exists()) {
            Log.d("GGG", "mode s fg v");
            new DownloadSingleFileTask(splash_fg_vertical).execute();
        } else {
            int fg_file_size = Integer.parseInt(String.valueOf(fg_file.length()/1024));
            if ( (splash_fg_size - fg_file_size) > 10 || (splash_fg_size - fg_file_size) < -10) {
                // re-download
                Log.d("GGG", "s mode s fg v");
                new DownloadSingleFileTask(splash_fg_vertical).execute();
            }
        }

        if (!blur_file.exists()) {
            Log.d("GGG", "mode s bl v");
            new DownloadSingleFileTask(splash_blur_vertical).execute();
        } else {
            int blur_file_size = Integer.parseInt(String.valueOf(blur_file.length()/1024));
            if ( (splash_blur_size - blur_file_size) > 10 || (splash_blur_size - blur_file_size) < -10) {
                // re-download
                Log.d("GGG", "s mode s bl v");
                new DownloadSingleFileTask(splash_blur_vertical).execute();
            }
        }

        // add to JSONObject
        file.put("mode_id", mode_id);
        file.put("name", name);
        file.put("name_en", name_en);
        file.put("introduction", introduction);
        file.put("introduction_en", introduction_en);
        file.put("guide_voice", guide_voice);
        file.put("guide_voice_en", guide_voice_en);
        file.put("video", video);
        file.put("splash_bg_vertical", splash_bg_vertical);
        file.put("splash_fg_vertical", splash_fg_vertical);
        file.put("splash_blur_vertical", splash_blur_vertical);
        file.put("like_count", like_count);
        file.put("read_count", read_count);
        file.put("time_total", time_total);
        file.put("zone_id", zone_id);
        file.put("did_read", did_read);
        cursor.close();
        return file;
    }

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
        Cursor cursor = db.rawQuery("select * from mode where zone_id=" + zone_id, null);
        cursor.moveToFirst();
        int mode_id;
        String name;
        String name_en;
        String introduction;
        String introduction_en;
        String guide_voice;
        String guide_voice_en;
        String video;
        String splash_bg_vertical;
        int splash_bg_size;
        String splash_fg_vertical;
        int splash_fg_size;
        String splash_blur_vertical;
        int splash_blur_size;
        int like_count = 0;
        int read_count = 0;
        int time_total = 0;
        int did_read = 0;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            mode_id = cursor.getInt(cursor.getColumnIndex("mode_id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            name_en = cursor.getString(cursor.getColumnIndex("name_en"));
            introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            introduction_en = cursor.getString(cursor.getColumnIndex("introduction_en"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
            video = cursor.getString(cursor.getColumnIndex("video"));
            splash_bg_vertical = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
            splash_bg_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.SPLASH_BG_SIZE));
            splash_fg_vertical = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
            splash_fg_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.SPLASH_FG_SIZE));
            splash_blur_vertical = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
            splash_blur_size = cursor.getInt(cursor.getColumnIndex(DatabaseUtilizer.SPLASH_BLUR_SIZE));
            like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
            read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
            time_total = cursor.getInt(cursor.getColumnIndex("time_total"));
            did_read = cursor.getInt(cursor.getColumnIndex("did_read"));

            // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
            File rootDir = this.context.getFilesDir();
            File path = new File(rootDir.getAbsolutePath() + "/itri");
            if ( !path.exists() ) {
                path.mkdirs();
            }

            String[] bg_path = splash_bg_vertical.split("/");
            String[] fg_path = splash_fg_vertical.split("/");
            String[] blur_path = splash_blur_vertical.split("/");
            File bg_file = new File(path, bg_path[bg_path.length - 1]);
            File fg_file = new File(path, fg_path[fg_path.length - 1]);
            File blur_file = new File(path, blur_path[blur_path.length - 1]);

            if (!bg_file.exists()) {
                Log.d("GGG", "zone s bg v");
                new DownloadSingleFileTask(splash_bg_vertical).execute();
            } else {
                int bg_file_size = Integer.parseInt(String.valueOf(bg_file.length()/1024));
                if ( (splash_bg_size - bg_file_size) > 10 || (splash_bg_size - bg_file_size) < -10) {
                    // re-download
                    Log.d("GGG", "s zone s bg v");
                    new DownloadSingleFileTask(splash_bg_vertical).execute();
                }
            }

            if (!fg_file.exists()) {
                Log.d("GGG", "zone s fg v");
                new DownloadSingleFileTask(splash_fg_vertical).execute();
            } else {
                int fg_file_size = Integer.parseInt(String.valueOf(fg_file.length()/1024));
                if ( (splash_fg_size - fg_file_size) > 10 || (splash_fg_size - fg_file_size) < -10) {
                    // re-download
                    Log.d("GGG", "s zone s fg v");
                    new DownloadSingleFileTask(splash_fg_vertical).execute();
                }
            }

            if (!blur_file.exists()) {
                Log.d("GGG", "zone s bl v");
                new DownloadSingleFileTask(splash_blur_vertical).execute();
            } else {
                int blur_file_size = Integer.parseInt(String.valueOf(blur_file.length()/1024));
                if ( (splash_blur_size - blur_file_size) > 10 || (splash_blur_size - blur_file_size) < -10) {
                    // re-download
                    Log.d("GGG", "s zone s bl v");
                    new DownloadSingleFileTask(splash_blur_vertical).execute();
                }
            }

            // add to JSONObject
            file.put("mode_id", mode_id);
            file.put("name", name);
            file.put("name_en", name_en);
            file.put("introduction", introduction);
            file.put("introduction_en", introduction_en);
            file.put("guide_voice", guide_voice);
            file.put("guide_voice_en", guide_voice_en);
            file.put("video", video);
            file.put("splash_bg_vertical", splash_bg_vertical);
            file.put("splash_fg_vertical", splash_fg_vertical);
            file.put("splash_blur_vertical", splash_blur_vertical);
            file.put("like_count", like_count);
            file.put("read_count", read_count);
            file.put("time_total", time_total);
            file.put("did_read", did_read);
            filePaths.put(file);
            file = new JSONObject();
            cursor.moveToNext();
        }
        cursor.close();
        return filePaths;
    }

    public int getNumbersOfModeFromZone(int zone_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from mode where zone_id=" + zone_id, null);
        cursor.moveToFirst();

        int count_mode = cursor.getCount();
        cursor.close();
        return count_mode;
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

    // update mode table with mode_id
    public boolean updateModeDidRead(int modeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("did_read", 1);
        long correctUpdateId = db.update("mode", values, "mode_id=" +modeId, null);
        if (correctUpdateId != -1) {} else {
            Log.e("更新已讀失敗", "mode_id=" + modeId);
            return false;
        }
        return true;
    }

    // zone table query and insert
    public boolean insertZone(int zone_id,
                                String name,
                                String name_en,
                                String introduction,
                                String introduction_en,
                                String guide_voice,
                                String guide_voice_en,
                                String hint,
                                String photo,
                                String photo_size,
                                String photo_vertical,
                                String photo_vertical_size,
                                int field_id,
                                int like_count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("zone_id", zone_id);
        values.put("name", name);
        values.put("name_en", name_en);
        values.put("introduction", introduction);
        values.put("introduction_en", introduction_en);
        values.put("guide_voice", guide_voice);
        values.put("guide_voice_en", guide_voice_en);
        values.put("hint", hint);
        values.put("photo", photo);
        int photo_size_int = Integer.parseInt(photo_size);
        values.put("photo_size", photo_size_int);
        values.put("photo_vertical", photo_vertical);
        int photo_vertical_size_int = Integer.parseInt(photo_vertical_size);
        values.put("photo_vertical_size", photo_vertical_size_int);
        values.put("field_id", field_id);
        values.put("like_count", like_count);

        long rowId = db.insertWithOnConflict("zone", null, values, 4);
        if (rowId != -1) {
            Log.i("zone", "insert zone_id=" + zone_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    public JSONObject queryZone(int zone_id) throws JSONException {
        JSONObject file = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from zone where zone_id=" + zone_id + "", null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String name_en = cursor.getString(cursor.getColumnIndex("name_en"));
        String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
        String introduction_en = cursor.getString(cursor.getColumnIndex("introduction_en"));
        String guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
        String guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
        String hint = cursor.getString(cursor.getColumnIndex("hint"));
        String photo = cursor.getString(cursor.getColumnIndex("photo"));
        int photo_size = cursor.getInt(cursor.getColumnIndex("photo_size"));
        String photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
        int photo_vertical_size = cursor.getInt(cursor.getColumnIndex("photo_vertical_size"));
        int field_id = cursor.getInt(cursor.getColumnIndex("field_id"));
        int like_count = cursor.getInt(cursor.getColumnIndex("like_count"));

        // 掃過 FILE，看是不是沒有這個檔案，或者檔案沒有載完全
        File rootDir = this.context.getFilesDir();
        File path = new File(rootDir.getAbsolutePath() + "/itri");
        if ( !path.exists() ) {
            path.mkdirs();
        }

        String[] photo_path = photo.split("/");
        String[] photo_vertical_path = photo_vertical.split("/");
        File photo_file = new File(path, photo_path[photo_path.length -1]);
        File photo_vertical_file = new File(path, photo_vertical_path[photo_vertical_path.length -1]);

        if (!photo_file.exists()) {
            Log.d("GGG", "zone p");
            new DownloadSingleFileTask(photo).execute();
        } else {
            int photo_f_size = Integer.parseInt(String.valueOf(photo_file.length()/1024));
            if ((photo_size - photo_f_size) > 10 || (photo_size - photo_f_size) < -10) {
                // re-download
                Log.d("GGG", "s zone p");
                new DownloadSingleFileTask(photo).execute();
            }
        }

        if (!photo_vertical_file.exists()) {
            Log.d("GGG", "zone p v");
            new DownloadSingleFileTask(photo_vertical).execute();
        } else {
            int photo_vertical_file_size = Integer.parseInt(String.valueOf(photo_vertical_file.length()/1024));
            if ( (photo_vertical_size - photo_vertical_file_size) > 10 || (photo_vertical_size - photo_vertical_file_size) < -10) {
                // re-download
                Log.d("GGG", "s zone p v");
                new DownloadSingleFileTask(photo_vertical).execute();
            }
        }

        // add to json obj
        file.put("name", name);
        file.put("name_en", name_en);
        file.put("introduction", introduction);
        file.put("introduction_en", introduction_en);
        file.put("guide_voice", guide_voice);
        file.put("guide_voice_en", guide_voice_en);
        file.put("hint", hint);
        file.put("photo", photo);
        file.put("photo_vertical", photo_vertical);
        file.put("field_id", field_id);
        file.put("like_count", like_count);

        cursor.close();
        return file;
    }

    // path table query and insert
    public boolean insertPath(int choose_path_id,
                              int path_order,
                              String svg_id,
                              int start,
                              String Sn,
                              int end,
                              String En) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("choose_path_id", choose_path_id);
        values.put("path_order", path_order);
        values.put("svg_id", svg_id);
        values.put("start", start);
        values.put("Sn", Sn);
        values.put("End", end);
        values.put("En", En);
        //Log.e("vvvvvvvv", String.valueOf(path_order));
        long rowId = db.insertWithOnConflict("path", null, values, 4);
        if (rowId != -1) {
            Log.i("path", "insert choose_path_id=" + choose_path_id + " success.");
            return true;
        } else {
            return false;
        }
    }

    /*
        --> get those entries that would need to fetch data from server
     */
    public List<String> getAllDownloadPaths() {
        List<String> paths = new ArrayList<String>();
        // get files needed to be download in company, device, field_map
        // hipster_template, mode, zone
        paths.addAll(getFieldMapDownloadFiles());
        paths.addAll(getZoneDownloadFiles());
        paths.addAll(getModeDownloadFiles());
        paths.addAll(getDeviceDownloadFiles());
        paths.addAll(getHipsterTemplateDownloadFiles());
        paths.addAll(getCompanyDownloadFiles());
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
        cursor.close();
        return companyFiles;
    }

    public List<String> getDeviceDownloadFiles() {
        List<String> deviceFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, guide_voice_en, photo, photo_vertical from device", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String guide_voice_en = null;
        String photo = null;
        String photo_vertical = null;
        // fetch all guide_voice, photo, photo_vertical
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            // add to List
            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                deviceFiles.add(guide_voice);
            }
            if (guide_voice_en.length() != 0 && guide_voice_en != null && guide_voice_en != "null") {
                deviceFiles.add(guide_voice_en);
            }
            if (photo_vertical.length() != 0 && photo_vertical != null && photo_vertical != "null") {
                deviceFiles.add(photo_vertical);
            }
            deviceFiles.add(photo);
            cursor.moveToNext();
        }
        cursor.close();
        return deviceFiles;
    }

    // jiang
    public List<String> getFieldMapDownloadFiles() {
        List<String> fieldMapFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select photo, photo_vertical, map_svg, map_svg_en, map_bg from field_map", null);

        cursor.moveToFirst();
        String photo = null;
        String photo_vertical = null;
        String map_svg = null;
        String map_svg_en = null;
        String map_bg = null;
        // fetch all guide_voice, photo, photo_vertical & svg
        while (cursor.isAfterLast() == false) {
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            map_svg = cursor.getString(cursor.getColumnIndex("map_svg"));
            map_svg_en = cursor.getString(cursor.getColumnIndex("map_svg_en"));
            map_bg = cursor.getString(cursor.getColumnIndex("map_bg"));

            if (photo_vertical.length() != 0 && photo_vertical != null && photo_vertical != "null") {
                fieldMapFiles.add(photo_vertical);
            }
            if (map_svg.length() != 0 && map_svg != null && map_svg != "null") {
                fieldMapFiles.add(map_svg);
            }
            if (map_svg_en.length() != 0 && map_svg_en != null && map_svg_en != "null") {
                fieldMapFiles.add(map_svg_en);
            }
            if (map_bg.length() != 0 && map_bg != null && map_bg != "null") {
                fieldMapFiles.add(map_bg);
            }
            // add to List
            fieldMapFiles.add(photo);
            cursor.moveToNext();
        }
        cursor.close();
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
        cursor.close();
        return templateFiles;
    }

    public List<String> getModeDownloadFiles() {
        List<String> modeFiles = new ArrayList<String>();;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, guide_voice_en, splash_bg_vertical, splash_fg_vertical, splash_blur_vertical from mode", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String guide_voice_en = null;
        String bg = null;
        String fg = null;
        String blur = null;
        // fetch all guide_voice, video, bg, fg, blur img
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
            bg = cursor.getString(cursor.getColumnIndex("splash_bg_vertical"));
            fg = cursor.getString(cursor.getColumnIndex("splash_fg_vertical"));
            blur = cursor.getString(cursor.getColumnIndex("splash_blur_vertical"));
            // check empty or not
            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                modeFiles.add(guide_voice);
            }
            if (guide_voice_en.length() != 0 && guide_voice_en != null && guide_voice_en != "null") {
                modeFiles.add(guide_voice_en);
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
        cursor.close();
        return modeFiles;
    }

    public List<String> getZoneDownloadFiles() {
        List<String> zoneFiles = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select guide_voice, guide_voice_en, photo, photo_vertical from zone", null);

        cursor.moveToFirst();
        String guide_voice = null;
        String guide_voice_en = null;
        String photo = null;
        String photo_vertical = null;
        // fetch all guide_voice, photo, photo_vertical
        while (cursor.isAfterLast() == false) {
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            if (guide_voice.length() != 0 && guide_voice != null && guide_voice != "null") {
                zoneFiles.add(guide_voice);
            }
            if (guide_voice_en.length() != 0 && guide_voice_en != null && guide_voice_en != "null") {
                zoneFiles.add(guide_voice_en);
            }
            if (photo_vertical.length() != 0 && photo_vertical != null && photo_vertical != "null") {
                zoneFiles.add(photo_vertical);
            }
            // add to List
            zoneFiles.add(photo);

            cursor.moveToNext();
        }
        cursor.close();
        return zoneFiles;
    }

    // ********************** 裝置  給定資料部分 **********************

    // 用給定的mode_id取出所有裝置
    public JSONArray queryDeviceFilesByMode(int mode_id) throws JSONException {
        JSONObject file = new JSONObject();
        JSONArray filePaths = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select device_id, guide_voice, guide_voice_en, photo, photo_vertical from device where mode_id=" + mode_id, null);
        cursor.moveToFirst();
        int device_id;
        String guide_voice;
        String guide_voice_en;
        String photo;
        String photo_vertical;
        // fetch all company_id & qrcode
        while (cursor.isAfterLast() == false) {
            device_id = cursor.getInt(cursor.getColumnIndex("device_id"));
            guide_voice = cursor.getString(cursor.getColumnIndex("guide_voice"));
            guide_voice_en = cursor.getString(cursor.getColumnIndex("guide_voice_en"));
            photo = cursor.getString(cursor.getColumnIndex("photo"));
            photo_vertical = cursor.getString(cursor.getColumnIndex("photo_vertical"));
            // add to JSONObject
            file.put("device_id", device_id);
            file.put("guide_voice", guide_voice);
            file.put("guide_voice_en", guide_voice_en);
            file.put("photo", photo);
            file.put("photo_vertical", photo_vertical);
            filePaths.put(file);
            file = new JSONObject();
            cursor.moveToNext();
        }
        cursor.close();
        return filePaths;
    }

    // ******************** counter part ********************


    // 每次經過一個device，揪呼叫此函數進行拜訪次數的+1
    public void addDeviceReadCount(int device_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select read_count from device where device_id=" + device_id, null);
        int read_count = 0;
        cursor.moveToFirst();
        read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
        read_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("read_count",read_count);
        cursor.close();
        // update to the same field
        writeDB.update(DatabaseUtilizer.DEVICE_TABLE, cv, "device_id=" + device_id, null);
    }

    public void addModeReadCount(int mode_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select read_count from mode where mode_id=" + mode_id, null);
        int read_count = 0;
        cursor.moveToFirst();
        read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
        read_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("read_count",read_count);
        cursor.close();
        // update to the same field
        writeDB.update(DatabaseUtilizer.MODE_TABLE, cv, "mode_id=" + mode_id, null);
    }

    // 按讚就加1
    public void addDeviceLikeCount(int device_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select like_count from device where device_id=" + device_id, null);
        int like_count = 0;
        cursor.moveToFirst();
        like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
        like_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("like_count",like_count);
        cursor.close();
        // update to the same field
        writeDB.update(DatabaseUtilizer.DEVICE_TABLE, cv, "device_id=" + device_id, null);
    }

    public void addModeLikeCount(int mode_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select like_count from mode where mode_id=" + mode_id, null);
        int like_count = 0;
        cursor.moveToFirst();
        like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
        like_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("like_count",like_count);
        cursor.close();
        // update to the same field
        writeDB.update(DatabaseUtilizer.MODE_TABLE, cv, "mode_id=" + mode_id, null);
    }

    public void addZoneLikeCount(int zone_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select like_count from zone where zone_id=" + zone_id, null);
        int like_count = 0;
        cursor.moveToFirst();
        like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
        like_count++;
        SQLiteDatabase writeDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("like_count",like_count);
        cursor.close();
        // update to the same field
        writeDB.update(DatabaseUtilizer.ZONE_TABLE, cv, "zone_id=" + zone_id, null);
    }

    // set all mode did_read to 0 --> 振哥 to test
    public void setModeDidReadZero() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE mode SET did_read=0");
        db.close();
    }

    public void DownloadSingleFile(String dfile) {
        Log.d("GGG", dfile);
        new DownloadSingleFileTask(dfile).execute();
    }

    public class DownloadSingleFileTask extends AsyncTask<String, Integer, Void> {

        private String dfile;

        public DownloadSingleFileTask(String dfile) {
            this.dfile = dfile;
        }

        @Override
        protected void onPreExecute() {
            Log.i("start", "Download tasks start");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                downloadFile();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("end", "Download tasks end");
        }

        private void downloadFile() throws MalformedURLException {

            File rootDir = context.getFilesDir();
            final File path = new File(rootDir.getAbsolutePath() + "/itri");
            String filepath;
            try {
                String[] all = dfile.split("/");
                // 解析路徑
                Log.e("all", String.valueOf(all.length));
                String pathSuffix = dfile.substring(3);
                filepath = DatabaseUtilizer.filePathURLPrefix + pathSuffix;
                String filename = all[all.length - 1];
                URL url = new URL(filepath);
                Log.e("url", String.valueOf(url));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                File outputFile = new File(path, filename);
                // delete first
                if (outputFile.exists()) outputFile.delete();

                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024 * 50);
                    byte[] buffer = new byte[4096];
                    int len = 0;
                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                        // write in file
                        outputStream.write(buffer, 0, len);
                    }
                    // close fileoutputstream
                    Log.i("f-outputstream", "download " + filename + " done.");
                    bufferedInputStream.close();
                    inputStream.close();
                    outputStream.close();
                } else {
                    Log.i("exists", filename + " skip download - already exists");
                }

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
