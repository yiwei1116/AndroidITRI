package org.tabc.living3.util;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Oslo on 9/30/16.
 */
public class DownloadProject extends AsyncTask<Void, Void, Boolean> {
    private int project_id;
    private String downloadPath;
    private String mStoragePath;
    public boolean mIsDownloadCompleted;

    public DownloadProject(int project_id) {
        this.project_id = project_id;
        downloadPath = getDownloadPath();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            DownloadProjectWithId(project_id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }

    public String getDownloadPath() {
        String path = null;


        return path;
    }

    // do all downloading
    private void DownloadProjectWithId(int project_id) throws Exception{

    }

    private void DownloadAllField() {

    }


    /*
        -----> project hierarchy
     */
    static public class Device{
        public int id;
        public String name;
        public String introduction;
        public String photo;
        public String hint;
        public String company;
        public String tel;
        public String fax;
        public String addr;
        public String web;
        public String price;
        public String qrcode;
        public Device(int id,
                      String name,
                      String introduction,
                      String photo,
                      String hint,
                      String company,
                      String tel,
                      String fax,
                      String addr,
                      String web,
                      String price,
                      String qrcode){
            this.id = id;
            this.name = name;
            this.introduction = introduction;
            this.photo = photo;
            this.hint = hint;
            this.company = company;
            this.tel = tel;
            this.fax = fax;
            this.addr = addr;
            this.web = web;
            this.price = price;
            this.qrcode = qrcode;
        }
    }
    static public class Mode{
        public int id;
        public String name;
        public String name_en;
        public String introduction;
        public String splash_bg;
        public String splash_fg;
        public String splash_blur;
        public String video;
        public ArrayList<Device> mDeviceList;
        public Mode(int id, String name, String name_en, String introduction, String splash_fg, String splash_bg, String splash_blur, String video){
            this.id = id;
            this.name = name;
            this.name_en = name_en;
            this.introduction = introduction;
            this.splash_fg = splash_fg;
            this.splash_bg = splash_bg;
            this.splash_blur = splash_blur;
            this.video = video;
            mDeviceList = new ArrayList<>();
        }
    }

    static public class Zone{
        public int id;
        public String name;
        public String name_en;
        public String introduction;
        public String hint;
        public String photo;
        public int zone_num;
        public ArrayList<Mode> mModeList;

        public Zone(int id, String name, String name_en, String introduction,  String hint, String photo, int zone_num){
            this.id = id;
            this.name_en = name_en;
            this.name = name;
            this.introduction = introduction;
            this.zone_num = zone_num;
            this.hint = hint;
            this.photo = photo;
            mModeList = new ArrayList<>();
        }
    }

    static public class Beacon{
        public int id;
        public String name;
        public String mac_addr;
        public int field;
        public int zone_num;
        public int low_power;
        public Beacon(int id, String name, String mac_addr, int field, int zone_num){
            this.id = id;
            this.name = name;
            this.mac_addr = mac_addr;
            this.zone_num = zone_num;
            this.field = field;
        }
    }

    static public class Field{
        public int id;
        public String name;
        public String introduction;
        public String map_name;
        public ArrayList<Zone> mZoneList;
        public ArrayList<Beacon> mBeaconList;

        public Field(int id, String name, String introduction, String map_name){
            this.id = id;
            this.name = name;
            this.introduction = introduction;
            this.map_name = map_name;
            mZoneList = new ArrayList<>();
            mBeaconList = new ArrayList<>();
        }
    }

    public ArrayList<Field> mFieldList;

    public DownloadProject(){
        mStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/liveTour/";
        File f = new File(mStoragePath);
        if(!f.isDirectory()){
            f.mkdir();
        }
        //cleanStoragePath();
        mIsDownloadCompleted = false;
        mFieldList = new ArrayList<>();
    }

    private void cleanStoragePath(){
        File f = new File(mStoragePath);
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isFile()) {
                try{
                    inFile.delete();
                }catch (Exception e){
                    // Do nothing
                }
            }
        }
    }

    public String getStoragePath()
    {
        return mStoragePath;
    }


}
