package com.uscc.ncku.androiditri;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ConnectActivity extends Activity {
    private String serverURL = "http://140.116.82.48/interface/jsondecode.php";
    private String downloadURL = "http://140.116.82.48/interface/download.php";
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
        private void saveToSQLite(JSONArray jsonArray) {
            SQLiteDatabase db = sqLiteDbManager.getWritableDatabase();

            // parse json string
            switch (intendedTable) {
                case "device":
                    Log.i("device", String.valueOf(jsonArray));
                    
                    break;
                case "beacon":
                    Log.i("beacon", String.valueOf(jsonArray));

                    break;
                case "company":
                    Log.i("company", String.valueOf(jsonArray));

                    break;
                case "field_map":
                    Log.i("field_map", String.valueOf(jsonArray));

                    break;
                case "hipster_template":
                    Log.i("hipster_template", String.valueOf(jsonArray));

                    break;
                case "hipster_text":
                    Log.i("hipster_text", String.valueOf(jsonArray));

                    break;
                case "mode":
                    Log.i("mode", String.valueOf(jsonArray));

                    break;
                case "zone":
                    Log.i("zone", String.valueOf(jsonArray));

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

}
