package org.tabc.living3;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tabc.living3.util.DatabaseUtilizer;
import org.tabc.living3.util.SQLiteDbManager;

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
                Log.e("server_resp", String.valueOf(downloadResponse));
                JSONArray responseJSON = new JSONArray(downloadResponse);
                saveToSQLite(responseJSON);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("json_error", String.valueOf(e));
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
                                json.optString(DatabaseUtilizer.INTRODUCTION_EN),
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
                                json.optString(DatabaseUtilizer.NAME_EN),
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
                                json.optString(DatabaseUtilizer.INTRODUCTION_EN),
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
                                json.optString(DatabaseUtilizer.INTRODUCTION_EN),
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
                url = new URL(DatabaseUtilizer.downloadURL);
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

        private static final int PROGRESS_FULL_LEVEL = 10000;
        private static final int LOADING_SPEEDUP_SCALE = 4;
        private List<String> files;
        private Handler handler;
        private int progressLevel;
        private int progress;

        public DownloadFilesTask(List<String> files, Handler handler) {
            this.files = files;
            this.handler = handler;

            // if files is not empty, set level to 10000 / files.size
            if (!files.isEmpty()) {
                this.progressLevel = PROGRESS_FULL_LEVEL * LOADING_SPEEDUP_SCALE / files.size();
                this.progress = 0;
            } else {
                this.progressLevel = 0;
                this.progress = PROGRESS_FULL_LEVEL;
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

            if (progress >= PROGRESS_FULL_LEVEL)
                loadingActivity.startNextActivity();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("end", "Download tasks end");
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
                        filepath = DatabaseUtilizer.filePathURLPrefix + pathSuffix;
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

                        if (progress <= PROGRESS_FULL_LEVEL) {
                            progress += progressLevel;
                            onProgressUpdate(progress);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("error", String.valueOf(e));
            }

        }

    }

    // ********************** download files end **********************
}
