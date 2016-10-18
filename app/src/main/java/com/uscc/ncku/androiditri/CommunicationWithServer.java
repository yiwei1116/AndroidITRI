package com.uscc.ncku.androiditri;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.uscc.ncku.androiditri.util.DatabaseUtilizer;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

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

    private String serverURL = "http://140.116.82.48/interface/jsondecode.php";
    private String downloadURL = "http://140.116.82.48/interface/download.php";
    private String hipsterContentURL = "http://140.116.82.48/interface/catchhipster.php";
    private String surveyOneURL = "http://140.116.82.48/interface/surveyone.php";
    private String surveyTwoURL = "http://140.116.82.48/interface/surveytwo.php";
    private String counterURL = "http://140.116.82.48/interface/deviceadd.php";
    private final String filePathURLPrefix = "http://140.116.82.48/web/";
    public SQLiteDbManager sqLiteDbManager;


    // for downloading
    // 1.SVG files   2.device   3.hipster template   4.shipster text
    // for uploading
    // 1.survey   2. feedback   3. counter

//    /*
//        need:
//            function to download from server
//                --> get SVG files
//                --> get device data
//                --> get hipster data
//    */
//
//
//    /*
//        **** Upload function
//        upload JSONArray to server
//     */

    /*
        **** UPLOAD CLASS ****
     */
    public void uploadJsonData(JSONObject uploadObject, String uploadURL) {
        // call async method to execute upload task
        new SendData(uploadObject, uploadURL).execute();
    }

    // UPLOAD DATA TO SERVER
    public class SendData extends AsyncTask<String, Void, Void> {

        private int project_id;
        // data to send
        private JSONArray jsonArray;
        private JSONObject json_string;
        private String uploadURL;

        public SendData(int project_id) {
            this.project_id = project_id;
        }

        public SendData(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public SendData(JSONObject json_string, String uploadURL) {
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
                // configure json object & json array  --> example code
//                counter1 = new JSONObject();
//                counter1.put(mode_id, 1);
//                counter1.put(device_id, 1);
//                counter1.put(add_count, 25);
//
//                counter2 = new JSONObject();
//                counter2.put(mode_id, 1);
//                counter2.put(device_id, 2);
//                counter2.put(add_count, 100);
//
//                counter3 = new JSONObject();
//                counter3.put(mode_id, 1);
//                counter3.put(device_id, 23);
//                counter3.put(add_count, 50);
//
//                jsonArray = new JSONArray();
//                jsonArray.put(counter1);
//                jsonArray.put(counter2);
//                jsonArray.put(counter3);
//
//                JSONObject json_string = new JSONObject();
//                json_string.put(request_type, 1);
//                json_string.put(request_data, jsonArray);

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

                String jsonString = "json_string=" + json_string.toString();
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
    public JSONObject packHipsterContentData(String textContent, String picturePath, String combinePicturePath, int hipsterTemplateId, int hipsterTextId, int zoneId) throws JSONException {
        JSONObject uploadObject = new JSONObject();
        uploadObject.put("content", textContent);
        uploadObject.put("picture", picturePath);
        uploadObject.put("combine_picture", combinePicturePath);
        uploadObject.put("hipster_template_id", hipsterTemplateId);
        uploadObject.put("hipster_text_id", hipsterTextId);
        uploadObject.put("zone_id", zoneId);
        return uploadObject;
    }

    // WARNING: YIWEI should call this function
    public void uploadHipsterContent(String textContent, String picturePath, String combinePicturePath, int hipsterTemplateId, int hipsterTextId, int zoneId) throws JSONException {
        JSONObject jsonObject = packHipsterContentData(textContent, picturePath, combinePicturePath, hipsterTemplateId, hipsterTextId, zoneId);
        uploadJsonData(jsonObject, this.hipsterContentURL);
    }

    // TODO: yiwei has to upload file to server


    // UPLOAD to "survey" table
    public JSONObject packSurveyData (int gender, int age, int education, int career, int exp, int salary, int location, int house_type, int family_type, int fimily_member, int know_way) throws JSONException {
        JSONObject uploadObject = new JSONObject();
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

    public void uploadSurveyData(int gender, int age, int education, int career, int exp, int salary, int location, int house_type, int family_type, int fimily_member, int know_way) throws JSONException {
        JSONObject jsonObject = packSurveyData(gender, age, education, career, exp, salary, location, house_type, family_type, fimily_member, know_way);
        uploadJsonData(jsonObject, this.surveyOneURL);
    }

    // UPLOAD to "survey2" table
    public JSONObject packSurveyTwoData (int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, int first_consider, int second_consider, int third_consider, int fourth_consider, int fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) throws JSONException {
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

    public void uploadSecondSurveyData(int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, int first_consider, int second_consider, int third_consider, int fourth_consider, int fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) throws JSONException {
        JSONObject jsonObject = packSurveyTwoData( attitude,  functionality,  visual,  operability,  user_friendly,  price,  maintenance,  safety,  energy,  first_choise,  second_choise,  third_choise,  fourth_choise,  fifth_choise,  first_consider,  second_consider,  third_consider,  fourth_consider,  fifth_consider,  subscription1,  subscription2,  subscription3,  install1,  install2,  install3,  install4,  install5,  impression1,  impression2,  impression3,  impression4,  impression5,  buy,  reasonable_price);
        uploadJsonData(jsonObject, this.surveyTwoURL);
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
                Log.i("HTTP result", downloadResponse);
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
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO_VER),
                                json.optString(DatabaseUtilizer.DEVICE_HINT),
                                json.optInt(DatabaseUtilizer.DEVICE_MODE_ID),
                                json.optInt(DatabaseUtilizer.DEVICE_COMPANY_ID),
                                json.optInt(DatabaseUtilizer.READ_COUNT));
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
                                json.optString(DatabaseUtilizer.GUIDE_VOICE),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO_VER),
                                json.optString(DatabaseUtilizer.MAP_SVG));
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
                                json.optString(DatabaseUtilizer.CONTENT));
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
                                json.optString(DatabaseUtilizer.VIDEO),
                                json.optString(DatabaseUtilizer.MODE_SPLASH_BG),
                                json.optString(DatabaseUtilizer.MODE_SPLASH_FG),
                                json.optString(DatabaseUtilizer.MODE_SPLASH_BLUR),
                                json.optInt(DatabaseUtilizer.LIKE_COUNT),
                                json.optInt(DatabaseUtilizer.READ_COUNT),
                                json.optInt(DatabaseUtilizer.TIME_TOTAL),
                                json.optInt(DatabaseUtilizer.ZONE_ID));
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
                                json.optString(DatabaseUtilizer.DEVICE_HINT),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO),
                                json.optString(DatabaseUtilizer.DEVICE_PHOTO_VER),
                                json.optInt(DatabaseUtilizer.FIELD_ID));
                    }
                    break;
                case "path":
                    Log.i("path", String.valueOf(jsonArray));
                    for( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = (JSONObject)jsonArray.get(i);
                        // store each entry into database
                        sqLiteDbManager.insertPath(json.optInt(DatabaseUtilizer.CHOOSE_PATH_ID),
                                json.optInt(DatabaseUtilizer.PATH_ORDER),
                                json.optInt(DatabaseUtilizer.PATH_SVG_ID),
                                json.optInt(DatabaseUtilizer.START),
                                json.optInt(DatabaseUtilizer.PATH_SN),
                                json.optInt(DatabaseUtilizer.END),
                                json.optInt(DatabaseUtilizer.PATH_EN));
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

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("download", "HTTP Response: HTTP - OK");
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream())));
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    Log.i("response code", response);
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
    public class DownloadFilesTask extends AsyncTask<String, Void, Void> {

        private List<String> files;

        public DownloadFilesTask() {
            files = null;
        }

        public DownloadFilesTask(List<String> files) {
            this.files = files;
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("end", "Download tasks end");
        }

        private void downloadFiles() throws MalformedURLException {
            String filename = null;
            String filepath = null;
            File rootDir = Environment.getExternalStorageDirectory();
            final File path = new File(rootDir.getAbsolutePath() + "/itri");
            if ( !path.exists() ) {
                path.mkdirs();
            }

            try {
                // TODO : should put all urls with strings
                for (String eachFile : files) {
                    if (eachFile.length() != 0 && eachFile != null && !eachFile.equals("null")) {
                        String[] splits = eachFile.split("/");
                        filename = splits[splits.length - 1];
                        // 解析路徑
                        String pathSuffix = eachFile.substring(3);
                        filepath = filePathURLPrefix + pathSuffix;
                        Log.i("file data", eachFile);
                        Log.i("filename", filename);
                        Log.i("filepath", filepath);

                        URL url = new URL(filepath);
                        Log.d("path", String.valueOf(path));
                        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoOutput(true);
                        FileOutputStream outputStream = new FileOutputStream(new File(path, filename));
                        InputStream inputStream = urlConnection.getInputStream();
                        byte[] buffer = new byte[4096];
                        int len = 0;
                        while ( (len = inputStream.read(buffer)) > 0) {
                            // write in file
                            outputStream.write(buffer, 0, len);
                        }
                        // close fileoutputstream
                        Log.i("download", "done");
                        outputStream.close();
                    }
                }
            } catch (Exception e) {
                Log.e("error", String.valueOf(e));
            }

            // storage part

//                File root = Environment.getExternalStorageDirectory();
//                final File path = new File(root.getAbsolutePath() + "/download");
//                if (!path.exists()) {
//                    path.mkdirs();
//                }
//                // write file
//                final File file = new File(path, "jsonArray.txt");
//                try {
//                    file.createNewFile();
//                    FileOutputStream fout = new FileOutputStream(file);
//                    PrintWriter outputStreamWriter = new PrintWriter(fout);
//                    outputStreamWriter.write(String.valueOf(jsonArray));
//                    outputStreamWriter.flush();
//                    outputStreamWriter.close();
//                    fout.flush();
//                    fout.close();
//                } catch (IOException e) {
//                    Log.e("error", "write to file error.");
//                }
        }

    }

    // ********************** download files end **********************

}