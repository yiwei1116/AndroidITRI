package com.uscc.ncku.androiditri;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ConnectActivity extends Activity {
    private String serverURL = "http://140.116.82.48/interface/jsondecode.php";
    private String downloadURL = "http://140.116.82.48/interface/download.php";
    private final String filePathURLPrefix = "http://140.116.82.48/web/";
    public SQLiteDbManager sqLiteDbManager;


    public ConnectActivity(SQLiteDbManager manager) {
        sqLiteDbManager = manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    // for downloading
    // 1.SVG files   2.device   3.hipster template   4.shipster text
    // for uploading
    // 1.survey   2. feedback   3. counter

    /*
        need:
            function to download from server
                --> get SVG files
                --> get device data
                --> get hipster data
    */


    /*
        **** Upload function
        upload JSONArray to server
     */

    public void uploadJsonData(JSONObject uploadObject) {
        // call async method to execute upload task
        new SendData(uploadObject).execute();
    }

    // inner class to upload data to server
    public class SendData extends AsyncTask<String, Void, Void> {

        private int project_id;
        // data to send
        private JSONArray jsonArray;
        private JSONObject json_string;

        public SendData(int project_id) {
            this.project_id = project_id;
        }

        public SendData(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public SendData(JSONObject json_string) { this.json_string = json_string; }

        @Override
        protected void onPreExecute() {
            Log.i("upload", "pre execute success.");
        }

        @Override
        protected Void doInBackground(String... strings) {
            String response = "";
            // do all things here
            try {
                response = performUploadPost(serverURL, new HashMap<String, String>() {
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

    /*
        **** Download function
        ** REAL ** download function: send a project_id to server and get back all info and path about download contents
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
        private void saveToSQLite(JSONArray jsonArray) throws JSONException{
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

//    public class DownloadDeviceData extends AsyncTask<String, Void, Void> {
//
//        private String myProjectId;
//
//        @Override
//        protected void onPreExecute() {
//            Log.i("download", "Pre Execute success.");
//        }
//
//        public DownloadDeviceData(String projectId) {
//            myProjectId = projectId;
//        }
//
//        public DownloadDeviceData() {
//
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            String downloadResponse = "";
//            // do all things here
//            try {
//                downloadResponse = performDownloadPost(new HashMap<String, String>() {
//                    {
//                        put("Accept", "application/json");
//                        put("Content-Type", "application/json");
//                    }
//                });
//                // log http response code
//                Log.i("HTTP result", downloadResponse);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Log.i("download", "PostExecute success.");
//        }
//
//        private String performDownloadPost(HashMap<String, String> hashMap) {
//            String response = "";
//            URL url;
//            try {
//                // send URL request
//                url = new URL(downloadURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
//
//                // write to server
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(httpURLConnection.getOutputStream()));
//                // server requires only a project id POSTed
//                outputStreamWriter.write(myProjectId);
//                outputStreamWriter.flush();
//                outputStreamWriter.close();
//
//                int responseCode = httpURLConnection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.e("download", "HTTP Response: HTTP - OK");
//                    String line;
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream())));
//                    while ((line = bufferedReader.readLine()) != null) {
//                        response += line;
//                    }
//                    Log.i("response code", response);
//                    bufferedReader.close();
//                } else {
//                    Log.e("HTTP response", String.valueOf(responseCode));
//                    response = "";
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//    }

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

                        // 解析檔名
                        String[] splits = eachFile.split("/");
                        filename = splits[splits.length - 1];
                        // 解析路徑
                        String pathSuffix = eachFile.substring(3);
                        filepath = filePathURLPrefix + pathSuffix;
                        Log.i("file data", eachFile);
                        Log.i("filename", filename);
                        Log.i("filepath", filepath);


//                    URL url = new URL(filepath);
//                    Log.d("path", String.valueOf(path));
//                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setDoOutput(true);
//                    FileOutputStream outputStream = new FileOutputStream(new File(path, filename));
//                    InputStream inputStream = urlConnection.getInputStream();
//                    byte[] buffer = new byte[4096];
//                    int len = 0;
//                    while ( (len = inputStream.read(buffer)) > 0) {
//                        // write in file
//                        outputStream.write(buffer, 0, len);
//                    }
//                    // close fileoutputstream
//                    Log.i("download", "done");
//                    outputStream.close();

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



}
