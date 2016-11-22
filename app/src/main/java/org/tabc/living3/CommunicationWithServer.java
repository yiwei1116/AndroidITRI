package org.tabc.living3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.tabc.living3.util.DatabaseUtilizer;
import org.tabc.living3.util.SQLiteDbManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Oslo on 10/18/16.
 */
public class CommunicationWithServer {

//    private String serverURL = "http://140.116.82.48/interface/jsondecode.php";
//    private String downloadURL = "http://140.116.82.48/interface/getfile.php";
//    private String hipsterContentURL = "http://140.116.82.48/interface/hipster.php"; // hipster content
//    private String surveyOneURL = "http://140.116.82.48/interface/survey.php"; // first survey
//    private String surveyTwoURL = "http://140.116.82.48/interface/surveytwo.php";
//    private String counterURL = "http://140.116.82.48/interface/deviceadd.php"; // counter
//    private final String filePathURLPrefix = "http://140.116.82.48/web/";
    private String serverURL = "http://60.251.33.54:98/interface/jsondecode.php";
    private String downloadURL = "http://60.251.33.54:98/interface/getfile.php";
    private String hipsterContentURL = "http://60.251.33.54:98/interface/hipster.php"; // hipster content
    private String surveyOneURL = "http://60.251.33.54:98/interface/survey.php"; // first survey
    private String surveyTwoURL = "http://60.251.33.54:98/interface/surveytwo.php";
    private String counterURL = "http://60.251.33.54:98/interface/deviceadd.php"; // counter
    private final String filePathURLPrefix = "http://60.251.33.54:98/web/";

    public int totalCount;
    public int partialCount;
    public SQLiteDbManager sqLiteDbManager;

    private LoadingActivity loadingActivity;

    public CommunicationWithServer() {
        this.totalCount = 0;
        this.partialCount = 0;
    }

    public CommunicationWithServer(LoadingActivity loadingActivity) {
        this.loadingActivity = loadingActivity;
        this.sqLiteDbManager = new SQLiteDbManager(this.loadingActivity);
    }

    public void setTotalCount(int count) {
        this.totalCount = count;
    }

    public void setPartialCount(int partial) {
        this.partialCount = partial;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPartialCount() {
        return partialCount;
    }

    /*
        **** UPLOAD CLASS ****
     */
    public void uploadJsonData(String uploadType, JSONObject uploadObject, String uploadURL) {
        // call async method to execute upload task
        new SendData(uploadType, uploadObject, uploadURL).execute();
    }

    // UPLOAD DATA TO SERVER
    public class SendData extends AsyncTask<String, Void, Void> {

        private int project_id;
        // data to send
        private JSONArray jsonArray;
        private JSONObject json_string;
        private String uploadURL;
        private String uploadType;

        public SendData(int project_id) {
            this.project_id = project_id;
        }

        public SendData(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public SendData(String type, JSONObject json_string, String uploadURL) {
            this.uploadType = type;
            this.json_string = json_string;
            this.uploadURL = uploadURL;
        }

        @Override
        protected void onPreExecute() {
            Log.i("upload", "pre execute success.");
        }

        @Override
        protected Void doInBackground(String... strings) {
            String response = "";
            // do all things here
            try {
                response = performUploadPost(this.uploadURL, new HashMap<String, String>() {
                    {
                        put("Accept", "application/json");
                        put("Content-Type", "application/json");
                    }
                });
                // log http response code
                Log.i("HTTP result", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("upload", "post execute success.");
        }

        private String performUploadPost(String serverUrl, HashMap<String, String> hashMap) {
            String response = "";
            URL url;
            try {
                if (json_string == null) {
                    return "JSON data is null";
                }
                // send URL request
                url = new URL(serverUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                String jsonString = this.uploadType + "=" + json_string.toString();
                Log.i("json", jsonString);

                // write to server
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(httpURLConnection.getOutputStream()));
                outputStreamWriter.write(jsonString);
                outputStreamWriter.flush();
                outputStreamWriter.close();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("http response", "HTTP - OK");
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream())));
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    Log.i("resp. code", response);
                    bufferedReader.close();
                } else {
                    Log.e("http response", String.valueOf(responseCode));
                    response = "";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

    }

    // upload should include : hipster_content, survey_result
    public JSONObject packHipsterContentData(String textContent, String picture, int hipsterTemplateId, int hipsterTextId, int zoneId) throws JSONException {
        JSONObject uploadObject = new JSONObject();
        uploadObject.put("content", textContent);
        uploadObject.put("combine_picture", picture);
        uploadObject.put("hipster_template_id", hipsterTemplateId);
        uploadObject.put("hipster_text_id", hipsterTextId);
        uploadObject.put("zone_id", zoneId);
        return uploadObject;
    }

    // UPLOAD to "survey" table
    public JSONObject packSurveyData (String name, String email, int gender, int age, int education, int career, int exp, int salary, int location, int house_type, int family_type, int fimily_member, int know_way) throws JSONException {
        JSONObject uploadObject = new JSONObject();
        uploadObject.put("name", name);
        uploadObject.put("email", email);
        uploadObject.put("gender", gender);
        uploadObject.put("age", age);
        uploadObject.put("education", education);
        uploadObject.put("career", career);
        uploadObject.put("experience", exp);
        uploadObject.put("salary", salary);
        uploadObject.put("location", location);
        uploadObject.put("house_type", house_type);
        uploadObject.put("family_type", family_type);
        uploadObject.put("fimily_member", fimily_member);
        uploadObject.put("know_way", know_way);
        return uploadObject;
    }

    // UPLOAD to "survey2" table
    public JSONObject packSurveyTwoData (int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, String first_consider, String second_consider, String third_consider, String fourth_consider, String fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) throws JSONException {
        JSONObject uploadObject = new JSONObject();
        uploadObject.put("attitude", attitude);
        uploadObject.put("functionality", functionality);
        uploadObject.put("visual", visual);
        uploadObject.put("operability", operability);
        uploadObject.put("user_friendly", user_friendly);
        uploadObject.put("price", price);
        uploadObject.put("maintenance", maintenance);
        uploadObject.put("safety", safety);
        uploadObject.put("energy", energy);
        uploadObject.put("first_choise", first_choise);
        uploadObject.put("second_choise", second_choise);
        uploadObject.put("third_choise", third_choise);
        uploadObject.put("fourth_choise", fourth_choise);
        uploadObject.put("fifth_choise", fifth_choise);
        uploadObject.put("first_consider", first_consider);
        uploadObject.put("second_consider", second_consider);
        uploadObject.put("third_consider", third_consider);
        uploadObject.put("fourth_consider", fourth_consider);
        uploadObject.put("fifth_consider", fifth_consider);
        uploadObject.put("subscription1", subscription1);
        uploadObject.put("subscription2", subscription2);
        uploadObject.put("subscription3", subscription3);
        uploadObject.put("install1", install1);
        uploadObject.put("install2", install2);
        uploadObject.put("install3", install3);
        uploadObject.put("install4", install4);
        uploadObject.put("install5", install5);
        uploadObject.put("impression1", impression1);
        uploadObject.put("impression2", impression2);
        uploadObject.put("impression3", impression3);
        uploadObject.put("impression4", impression4);
        uploadObject.put("impression5", impression5);
        uploadObject.put("buy", buy);
        uploadObject.put("reasonable_price", reasonable_price);
        return uploadObject;
    }

    // type 1, 2, 3 ; id = type id
    public JSONObject packLikeReadCount(int type, int typeId, int types[]) throws JSONException{
        SQLiteDatabase db = sqLiteDbManager.getReadableDatabase();
        JSONObject object = new JSONObject();
        switch (type) {
            case 1:
                JSONArray array = new JSONArray();
                // device packing
                for (int i = 0 ; i < types.length; i++) {
                    int tempId = types[i];
                    JSONObject tempObj = new JSONObject();
                    Cursor bcursor = db.rawQuery("select like_count, read_count from device where device_id=" + tempId, null);
                    bcursor.moveToFirst();
                    int like_count = bcursor.getInt(bcursor.getColumnIndex("like_count"));
                    int read_count = bcursor.getInt(bcursor.getColumnIndex("read_count"));
                    tempObj.put("id", tempId);
                    tempObj.put("like_count", like_count);
                    tempObj.put("read_count", read_count);
                    array.put(tempObj);
                }
                object.put("devices", array);
                break;
            case 2:
                // mode packing
                Cursor cursor = db.rawQuery("select like_count, read_count from mode where mode_id=" + typeId, null);
                cursor.moveToFirst();
                int like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
                int read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
                object.put("like_count", like_count);
                object.put("read_count", read_count);
                cursor.close();
                break;
            case 3:
                // zone packing
                Cursor acursor = db.rawQuery("select like_count from zone where zone_id=" + typeId, null);
                acursor.moveToFirst();
                int alike_count = acursor.getInt(acursor.getColumnIndex("like_count"));
                object.put("like_count", alike_count);
                acursor.close();
                break;
            default:
                break;
        }
        return object;
    }

    // 奕崴呼叫此函數 should call this function
    // 文字內容，照片檔名，版型id，罐頭文字id，zoneid
    public void uploadHipsterContent(String textContent, String picture, int hipsterTemplateId, int hipsterTextId, int zoneId) throws JSONException {
        JSONObject jsonObject = packHipsterContentData(textContent, picture, hipsterTemplateId, hipsterTextId, zoneId);
        uploadJsonData("hipster_content", jsonObject, this.hipsterContentURL);
    }

    public void uploadSurveyData(String name, String email, int gender, int age, int education, int career, int exp, int salary, int location, int house_type, int family_type, int fimily_member, int know_way) throws JSONException {
        JSONObject jsonObject = packSurveyData(name, email, gender, age, education, career, exp, salary, location, house_type, family_type, fimily_member, know_way);
        uploadJsonData("survey", jsonObject, this.surveyOneURL);
    }

    public void uploadSecondSurveyData(int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, String first_consider, String second_consider, String third_consider, String fourth_consider, String fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) throws JSONException {
        JSONObject jsonObject = packSurveyTwoData( attitude,  functionality,  visual,  operability,  user_friendly,  price,  maintenance,  safety,  energy,  first_choise,  second_choise,  third_choise,  fourth_choise,  fifth_choise,  first_consider,  second_consider,  third_consider,  fourth_consider,  fifth_consider,  subscription1,  subscription2,  subscription3,  install1,  install2,  install3,  install4,  install5,  impression1,  impression2,  impression3,  impression4,  impression5,  buy,  reasonable_price);
        uploadJsonData("survey_two", jsonObject, this.surveyTwoURL);
    }

    // type 1
    public void uploadDeviceLikeAndReadCount(int typeId, int types[]) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = packLikeReadCount(1, typeId, types);

            // TODO: not sure about the server url
            uploadJsonData("device_counts", jsonObject, this.counterURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // type 2
    public void uploadModeLikeAndReadCount(int typeId, int types[]) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = packLikeReadCount(2, typeId, types);

            // TODO: not sure about the server url
            uploadJsonData("mode_counts", jsonObject, this.counterURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // type 3
    public void uploadZoneLikeAndReadCount(int typeId, int types[]) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = packLikeReadCount(3, typeId, types);

            // TODO: not sure about the server url
            uploadJsonData("zone_counts", jsonObject, this.counterURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ********************** download all tables start **********************
    // Starting point: call this method to download all tables
    public void downloadAllTables() {
        downloadProjectData("device");
        downloadProjectData("beacon");
        downloadProjectData("company");
        downloadProjectData("field_map");
        downloadProjectData("hipster_template");
        downloadProjectData("hipster_text");
        downloadProjectData("mode");
        downloadProjectData("zone");
        downloadProjectData("path");
    }
    /*
        **** DOWNLOAD CLASS : download all table data
     */
    // give string to check whether table should be fetched data from.
    public void downloadProjectData(String intendedTable) {
        new ReceiveData(intendedTable).execute();
    }

    public class ReceiveData extends AsyncTask<String, Void, Void> {

        private String intendedTable;

        @Override
        protected void onPreExecute() {
            Log.i("download", "Pre Execute success.");
        }

        public ReceiveData(String table) {
            intendedTable = table;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String downloadResponse = "";
            // do all things here
            try {
                downloadResponse = performDownloadPost(new HashMap<String, String>() {
                    {
                        put("Accept", "application/json");
                        put("Content-Type", "application/json");
                    }
                });
                // log http response code
                // fetch as json array
                JSONArray responseJSON = new JSONArray(downloadResponse);
                saveToSQLite(responseJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("download", "PostExecute success.");
        }

        // parse and save to SQLite DB
        private void saveToSQLite(JSONArray jsonArray) throws JSONException {
            SQLiteDatabase db = sqLiteDbManager.getWritableDatabase();
            // parse json string
            // check which table it is
            switch (intendedTable) {
                case "device":
                    Log.i("device", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertDevice(json.optInt(DatabaseUtilizer.DEVICE_ID),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optString(DatabaseUtilizer.NAME_EN),
                                json.optString(DatabaseUtilizer.INTRODUCTION),
                                json.optString(DatabaseUtilizer.GUIDE_VOICE),
                                json.optString(DatabaseUtilizer.GUIDE_VOICE_EN),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO_VER),
                                json.optString(DatabaseUtilizer.DEVICE_HINT),
                                json.optInt(DatabaseUtilizer.DEVICE_MODE_ID),
                                json.optInt(DatabaseUtilizer.DEVICE_COMPANY_ID),
                                json.optInt(DatabaseUtilizer.READ_COUNT),
                                json.optInt(DatabaseUtilizer.LIKE_COUNT));
                    }
                    break;
                case "company":
                    Log.i("company", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertCompany(json.optInt(DatabaseUtilizer.COMPANY_ID),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optString(DatabaseUtilizer.COMPANY_TEL),
                                json.optString(DatabaseUtilizer.COMPANY_FAX),
                                json.optString(DatabaseUtilizer.COMPANY_ADDR),
                                json.optString(DatabaseUtilizer.COMPANY_WEB),
                                json.optString(DatabaseUtilizer.QRCODE));
                    }
                    break;
                case "beacon":
                    Log.i("beacon", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertBeacon(json.optInt(DatabaseUtilizer.BEACON_ID),
                                json.optString(DatabaseUtilizer.MAC_ADDR),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optInt(DatabaseUtilizer.BEACON_POWER),
                                json.optInt(DatabaseUtilizer.BEACON_STATUS),
                                json.optInt(DatabaseUtilizer.BEACON_ZONE),
                                json.optInt(DatabaseUtilizer.X),
                                json.optInt(DatabaseUtilizer.Y),
                                json.optInt(DatabaseUtilizer.BEACON_FIELD_ID),
                                json.optString(DatabaseUtilizer.BEACON_FIELD_NAME));
                    }
                    break;
                case "field_map":
                    Log.i("field map", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertFieldMap(json.optInt(DatabaseUtilizer.FIELD_MAP_ID),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optString(DatabaseUtilizer.NAME_EN),
                                json.optInt(DatabaseUtilizer.PROJECT_ID),
                                json.optString(DatabaseUtilizer.INTRODUCTION),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO_VER),
                                json.optString(DatabaseUtilizer.MAP_SVG),
                                json.optString(DatabaseUtilizer.MAP_SVG_EN),
                                json.optString(DatabaseUtilizer.MAP_BG));
                    }
                    break;
                case "hipster_template":
                    Log.i("template", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertHipsterTemplate(json.optInt(DatabaseUtilizer.HIPSTER_TEMPLATE_ID),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optString(DatabaseUtilizer.TEMPLATE));
                    }
                    break;
                case "hipster_text":
                    Log.i("text", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertHipsterText(json.optInt(DatabaseUtilizer.HIPSTER_TEXT_ID),
                                json.optString(DatabaseUtilizer.CONTENT),
                                json.optString(DatabaseUtilizer.CONTENT_EN));
                    }
                    break;
                case "mode":
                    Log.i("mode", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertMode(json.optInt(DatabaseUtilizer.MODE_ID),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optString(DatabaseUtilizer.NAME_EN),
                                json.optString(DatabaseUtilizer.INTRODUCTION),
                                json.optString(DatabaseUtilizer.GUIDE_VOICE),
                                json.optString(DatabaseUtilizer.GUIDE_VOICE_EN),
                                json.optString(DatabaseUtilizer.VIDEO),
                                json.optString(DatabaseUtilizer.MODE_SPLASH_BG),
                                json.optString(DatabaseUtilizer.MODE_SPLASH_FG),
                                json.optString(DatabaseUtilizer.MODE_SPLASH_BLUR),
                                json.optInt(DatabaseUtilizer.LIKE_COUNT),
                                json.optInt(DatabaseUtilizer.READ_COUNT),
                                json.optInt(DatabaseUtilizer.TIME_TOTAL),
                                json.optInt(DatabaseUtilizer.ZONE_ID),
                                0);
                    }
                    break;
                case "zone":
                    Log.i("zone", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertZone(json.optInt(DatabaseUtilizer.ZONE_ID),
                                json.optString(DatabaseUtilizer.NAME),
                                json.optString(DatabaseUtilizer.NAME_EN),
                                json.optString(DatabaseUtilizer.INTRODUCTION),
                                json.optString(DatabaseUtilizer.GUIDE_VOICE),
                                json.optString(DatabaseUtilizer.GUIDE_VOICE_EN),
                                json.optString(DatabaseUtilizer.DEVICE_HINT),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO_VER),
                                json.optInt(DatabaseUtilizer.FIELD_ID),
                                json.optInt(DatabaseUtilizer.LIKE_COUNT));
                    }
                    break;
                case "path":
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        Log.i("pathhhh", String.valueOf(json));
                        // store each entry into database
                        sqLiteDbManager.insertPath(json.optInt(DatabaseUtilizer.CHOOSE_PATH_ID),
                                json.optInt("order"),
                                json.optString(DatabaseUtilizer.PATH_SVG_ID),
                                json.optInt(DatabaseUtilizer.START),
                                json.optString(DatabaseUtilizer.PATH_SN),
                                json.optInt(DatabaseUtilizer.END),
                                json.optString(DatabaseUtilizer.PATH_EN));
                    }
                    break;
                default:
                    break;
            }

        }

        private String performDownloadPost(HashMap<String, String> hashMap) {
            String response = "";
            URL url;
            try {
                // send URL request
                url = new URL(downloadURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                String queryTable = "data=" + intendedTable;

                // write to server
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(httpURLConnection.getOutputStream()));
                // server requires only a project id POSTed
                outputStreamWriter.write(queryTable);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                Log.i("request", queryTable);
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream())));
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                } else {
                    Log.e("HTTP response", String.valueOf(responseCode));
                    response = "";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

    }

    // ********************** download all tables end **********************


    // ********************** download files start **********************
    /*
        **** DOWNLOAD CLASS : download all files
     */
    public void DownloadFiles(List<String> pathList, LoadingActivity loadingActivity, Handler handler) {
        this.loadingActivity = loadingActivity;
        new DownloadFilesTask(pathList, handler).execute();
    }

    public class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

        private List<String> files;
        private Handler handler;
        private int progressLevel;
        private int progress;

        public DownloadFilesTask(List<String> files, Handler handler) {
            this.files = files;
            this.handler = handler;

            // if files is not empty, set level to 10000 / files.size
            if (!files.isEmpty()) {
                this.progressLevel = 10000 / files.size();
                this.progress = 0;
            } else {
                this.progressLevel = 0;
                this.progress = 10000;
            }
        }

        @Override
        protected void onPreExecute() {
            Log.i("start", "Download tasks start");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                downloadFiles();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // update progress bar in main UI thread
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.arg1 = values[0];
            handler.sendMessage(msg);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("end", "Download tasks end");

            loadingActivity.startNextActivity();
        }

        private void downloadFiles() throws MalformedURLException {
            String filename = null;
            String filepath = null;
            // using external storage space
//            File rootDir = Environment.getExternalStorageDirectory();
            File rootDir = loadingActivity.getFilesDir();
//            rootDir = loadingActivity.getApplicationInfo().da
            final File path = new File(rootDir.getAbsolutePath() + "/itri");
            if ( !path.exists() ) {
                path.mkdirs();
            }
            Log.i("files", String.valueOf(files));

            // if files is empty set progress to 100%
            if (files.isEmpty())
                onProgressUpdate(progress);

            try {
                for (String eachFile : files) {
                    if (eachFile.length() != 0 && eachFile != null && !eachFile.equals("null")) {
                        String[] splits = eachFile.split("/");
                        filename = splits[splits.length - 1];
                        // 解析路徑
                        String pathSuffix = eachFile.substring(3);
                        filepath = filePathURLPrefix + pathSuffix;
                        Log.i("each file", eachFile);
                        URL url = new URL(filepath);
                        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                        urlConnection.setDoOutput(true);
                        File outputFile = new File(path, filename);
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                            FileOutputStream outputStream = new FileOutputStream(outputFile);
                            InputStream inputStream = urlConnection.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024 * 50);
                            byte[] buffer = new byte[4096];
                            int len = 0;
                            while ( (len = bufferedInputStream.read(buffer)) != -1) {
                                // write in file
                                outputStream.write(buffer, 0, len);
                            }
                            // close fileoutputstream
                            Log.i("f-outputstream", "download " + filename + " done.");
                            bufferedInputStream.close();
                            inputStream.close();
                            outputStream.close();
                        } else {
                            // delete and create new one
//                            outputFile.delete();
//                            outputFile.createNewFile();
                            Log.i("exists", filename + " skip download - already exists");
                        }
                        progress += progressLevel;
                        onProgressUpdate(progress);
                    }
                }
            } catch (Exception e) {
                Log.e("error", String.valueOf(e));
            }

        }

    }

    // ********************** download files end **********************
}
