package org.tabc.living3.util;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tabc.living3.CommunicationWithServer;
import org.tabc.living3.SurveyActivity;
import org.tabc.living3.fragment.FeedbackFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Oslo on 10/14/16.
 */
public class HelperFunctions extends Application{

    private static CommunicationWithServer commServer;
    private Context context;
    private SQLiteDbManager manager;

    public HelperFunctions(FeedbackFragment feedbackFragment) {
        this.context = feedbackFragment.getActivity().getApplicationContext();
        this.manager = new SQLiteDbManager(this.context);
    }

    public HelperFunctions(SurveyActivity surveyActivity) {
        this.context = surveyActivity.getApplicationContext();
        this.manager = new SQLiteDbManager(this.context);
    }

    public HelperFunctions() {

    }

    public static Bitmap readImageBitmap(String internalImagePath) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        File fileObj = new File(internalImagePath);
        /*Bitmap bitmap = BitmapFactory.decodeFile(fileObj.getAbsolutePath());
        return bitmap;*/
        InputStream inputStream = new FileInputStream(fileObj.getAbsolutePath());
        return BitmapFactory.decodeStream(inputStream, null, opt);
    }

    // ********************** get bitmap from file name ****************
    public static Bitmap getBitmapFromFile(Context context, String name) throws FileNotFoundException {
        // get file directory
        File fileDir = context.getFilesDir();
        String fileDirPath = String.valueOf(fileDir);

        // parse file name
        String[] paths = name.split("/");

        String finalFile = fileDirPath + "/itri/" + paths[paths.length-1];
        return HelperFunctions.readImageBitmap(finalFile);
    }

    //public static int
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
        SQLiteDatabase db = manager.getReadableDatabase();
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
        uploadJsonData("hipster_content", jsonObject, CommunicationWithServer.hipsterContentURL);
    }

    public void uploadSurveyData(String name, String email, int gender, int age, int education, int career, int exp, int salary, int location, int house_type, int family_type, int fimily_member, int know_way) throws JSONException {
        JSONObject jsonObject = packSurveyData(name, email, gender, age, education, career, exp, salary, location, house_type, family_type, fimily_member, know_way);
        uploadJsonData("survey", jsonObject, CommunicationWithServer.surveyOneURL);
    }

    public void uploadSecondSurveyData(int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, String first_consider, String second_consider, String third_consider, String fourth_consider, String fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) throws JSONException {
        JSONObject jsonObject = packSurveyTwoData( attitude,  functionality,  visual,  operability,  user_friendly,  price,  maintenance,  safety,  energy,  first_choise,  second_choise,  third_choise,  fourth_choise,  fifth_choise,  first_consider,  second_consider,  third_consider,  fourth_consider,  fifth_consider,  subscription1,  subscription2,  subscription3,  install1,  install2,  install3,  install4,  install5,  impression1,  impression2,  impression3,  impression4,  impression5,  buy,  reasonable_price);
        uploadJsonData("survey_two", jsonObject, CommunicationWithServer.surveyTwoURL);
    }

    // type 1
    public void uploadDeviceLikeAndReadCount(int typeId, int types[]) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = packLikeReadCount(1, typeId, types);

            // TODO: not sure about the server url
            uploadJsonData("device_counts", jsonObject, CommunicationWithServer.counterURL);
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
            uploadJsonData("mode_counts", jsonObject, CommunicationWithServer.counterURL);
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
            uploadJsonData("zone_counts", jsonObject, CommunicationWithServer.counterURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // second method
    /////// upload using android Volley
    public void uploadImage() throws FileNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File rootDir = Environment.getExternalStorageDirectory();
        File f = new File(rootDir.getAbsolutePath() + "/itri", "photofds.png");
        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        final String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CommunicationWithServer.hipsterContentURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("response", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //Showing toast
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = encodeImage;

                //Getting Image Name
                String name = "abc.jpeg";

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("picture_data", image);
                params.put("picture_name", name);

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
