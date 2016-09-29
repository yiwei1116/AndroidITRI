package com.uscc.ncku.androiditri;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ConnectActivity extends Activity {
    private String serverURL = "http://140.116.82.48/interface/deviceadd.php";

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
            1. function to parse json object
            2. function to upload to server
            3. function to download from server
     */


    /*
        ****   Get SVG files   ****
        --> field_map -> map_svg
        --> generally use
     */


    /*
        ****   Get device data   ****
        -->  basically by mode
        --> use device_id to query and
     */


    /*
        ****   Get 文青資料   ****

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
    public void uploadData(JSONArray jsonArray) {
        // call async method to execute upload task
        new SendData(jsonArray).execute();
    }

    // inner class to upload data to server
    public class SendData extends AsyncTask<String, Void, Void> {

        private int project_id;
        // data to send
        private JSONArray jsonArray;

        public SendData(int project_id) {
            this.project_id = project_id;
        }

        public SendData(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

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
        }

        private String performUploadPost(String serverUrl, HashMap<String, String> hashMap) {
            String response = "";
            URL url;
            try {
                url = new URL(serverUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");

                String jsonString = jsonArray.toString();
                byte[] outputBytes = jsonString.getBytes("UTF-8");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                // write to server
                outputStream.write(outputBytes);

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("http response", "HTTP - OK");
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                } else {
                    Log.e("http response", "Failed.");
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
    public void downloadAll(int project_id) {
        new DownloadProjectData(project_id).execute();
    }

    // inner class to download THE project data
    public class DownloadProjectData extends AsyncTask<String, Void, Void> {
        private int device_id;

        public DownloadProjectData(int device_id) {
            this.device_id = device_id;
        }

        @Override
        protected void onPreExecute() {
            Log.i("HTTP - ", "POST pre-execute download.");
        }

        @Override
        protected Void doInBackground(String... strings) {
            // do all things here

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
