package com.uscc.ncku.androiditri;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ConnectActivity extends Activity {
    private String serverURL = "http://140.116.82.48/interface/jsondecode.php";
    private final String mode_id = "mode_id";
    private final String device_id = "device_id";
    private final String add_count = "add_count";
    private final String request_type = "type";
    private final String request_data = "data";

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
        **** Download function
        * ----> incorrect one
     */
    public JSONArray downloadData(int id) throws JSONException {

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonData = null;

        try {
            URL url = new URL(serverURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect(); // conenct to url
            inputStream = urlConnection.getInputStream();

            // use buffer to store data
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 16);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            jsonData = builder.toString();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

        JSONArray resultArray = new JSONArray(jsonData);
        return resultArray;
    }

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
//        private JSONObject counter1;
//        private JSONObject counter2;
//        private JSONObject counter3;

        public SendData(int project_id) {
            this.project_id = project_id;
        }

        public SendData(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public SendData(JSONObject json_string) { this.json_string = json_string; }

        @Override
        protected void onPreExecute() {
            Log.i("HTTP - ", "POST pre-execute upload.");
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
            Log.i("postExecute", "info");
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
        download data from the server that returns a JSONArray
     */
//    public void downloadAll(int project_id) {
//        new DownloadProjectData(project_id).execute();
//    }
//
//    // inner class to download THE project data
//    public class DownloadProjectData extends AsyncTask<String, Void, Void> {
//        private int device_id;
//
//        public DownloadProjectData(int device_id) {
//            this.device_id = device_id;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Log.i("HTTP - ", "POST pre-execute download.");
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            // do all things here
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//    }

}
