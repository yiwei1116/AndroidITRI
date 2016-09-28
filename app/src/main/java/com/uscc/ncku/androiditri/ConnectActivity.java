package com.uscc.ncku.androiditri;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonToken;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class ConnectActivity extends Activity {
    private String serverURL = "http://140.116.82.48/interface/deviceadd.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        // call to execute
        // new SendData().execute(serverURL);
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
        download data from the server then returns a JSONArray
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
            String line = null;
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
        download data from the server then returns a JSONArray
     */
    public void uploadData(String jsonArray) {
        // call async method to execute upload task
         new SendData().execute(jsonArray);
    }

    private class SendData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            BufferedReader reader = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(serverURL);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            String data = strings[0];
            pairs.add(new BasicNameValuePair("data", data));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs);
                post.setEntity(formEntity);
                // post data
                httpClient.execute(post);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    // try convert string to json object
    public JSONObject str2JSONObject(String id) {
        JSONObject output = null;
        try {
            output = new JSONObject(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }


}
