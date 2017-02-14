package org.tabc.living3.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tabc.living3.CommunicationWithServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oslo on 10/14/16.
 */
public class HelperFunctions extends Application{

    private static CommunicationWithServer commServer;
    private Context context;
    private SQLiteDbManager manager;
    public static final String uploadUrlForHipsterContentSuffix = "http://";
    private String uploadString;
    private String pictureByteImage,combineByteImage;

    public HelperFunctions(Activity activity) {
        this.context = activity.getApplicationContext();
        this.manager = new SQLiteDbManager(this.context);
        this.uploadString = new String();
        // 2/13/2017
        Foreground.init(this);
    }

    public HelperFunctions(SQLiteDbManager manager) {
        this.manager = manager;
        this.uploadString = new String();
        // 2/13/2017
        Foreground.init(this);
    }

    public HelperFunctions(Context context) {
        this.context = context;
        this.uploadString = new String();
        this.manager = new SQLiteDbManager(this.context);
        // 2/13/2017
        Foreground.init(this);
    }

    public HelperFunctions() {
        // this.uploadString = new String();
        // 2/13/2017
        Foreground.init(this);
    }

    public static Bitmap readImageBitmap(String internalImagePath) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        File fileObj = new File(internalImagePath);
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
    public JSONObject packHipsterContentData(String content, String picture, String combine, String filepath, int hipsterTemplateId, int hipsterTextId, int zoneId) throws JSONException, FileNotFoundException {
        JSONObject uploadObject = new JSONObject();
        uploadObject.put("content", content);
        uploadObject.put("picture_name", picture);
        uploadObject.put("combine_name", combine);
        uploadObject.put("hipster_template_id", hipsterTemplateId);
        uploadObject.put("hipster_text_id", hipsterTextId);
        uploadObject.put("zone_id", zoneId);

        File pictureFile = new File(filepath, picture);
        File combineFile = new File(filepath, combine);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        Bitmap pictureBmp = BitmapFactory.decodeStream(new FileInputStream(pictureFile));
        Bitmap combineBmp = BitmapFactory.decodeStream(new FileInputStream(combineFile));

        pictureBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        combineBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream1);

        byte[] pictureBytes = outputStream.toByteArray();
        byte[] combineBytes = outputStream1.toByteArray();
        final String pictureByteImage = Base64.encodeToString(pictureBytes, Base64.DEFAULT);
        final String combineByteImage = Base64.encodeToString(combineBytes, Base64.DEFAULT);

        uploadObject.put("picture_data", pictureByteImage);
        uploadObject.put("combine_data", combineByteImage);

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
        uploadObject.put("family_member", fimily_member);
        uploadObject.put("know_way", know_way);
        return uploadObject;
    }

    // UPLOAD to "survey2" table
    public JSONObject packFeedbackData (int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, String first_consider, String second_consider, String third_consider, String fourth_consider, String fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) throws JSONException {
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

    // type 1
    public void uploadDeviceLikeAndReadCount(ArrayList<Integer> types) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONArray array = new JSONArray();
        SQLiteDatabase db = manager.getReadableDatabase();
        // device packing
        for (Integer i:
             types) {
            Log.e("life_count", String.valueOf(i));
            JSONObject tempObj = new JSONObject();
            Cursor bcursor = db.rawQuery("select like_count, read_count from device where device_id=" + i, null);
            bcursor.moveToFirst();
            Log.e("fhksjls", String.valueOf(bcursor));
            int like_count = bcursor.getInt(bcursor.getColumnIndex("like_count"));
            int read_count = bcursor.getInt(bcursor.getColumnIndex("read_count"));
            tempObj.put("device_id", i);
            tempObj.put("like_count", like_count);
            tempObj.put("read_count", read_count);
            array.put(tempObj);
            bcursor.close();
        }
        //jsonObject.put("devices", array);
        // / convert to string format
        final String uploadString = array.toString();

        StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.deviceaddURL,
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
                        NetworkResponse networkResponse = volleyError.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }

                        if (volleyError instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(volleyError instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (volleyError instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (volleyError instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (volleyError instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (volleyError instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //Creating parameters
                HashMap<String,String> params = new HashMap<String, String>();
                //Adding parameters
                params.put("device_counts", uploadString);
                Log.e("device_counts", uploadString);
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(hipsterUploadRequest);
    }

    // type 2
    public void uploadModeLikeAndReadCount(int typeId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        SQLiteDatabase db = manager.getReadableDatabase();

        // mode packing
        Cursor cursor = db.rawQuery("select like_count, read_count from mode where mode_id=" + typeId, null);
        cursor.moveToFirst();
        int like_count = cursor.getInt(cursor.getColumnIndex("like_count"));
        int read_count = cursor.getInt(cursor.getColumnIndex("read_count"));
        jsonObject.put("mode_id", typeId);
        jsonObject.put("like_count", like_count);
        jsonObject.put("read_count", read_count);
        cursor.close();

        // convert to string format
        final String uploadString = jsonObject.toString();

        StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.modeaddURL,
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
                        NetworkResponse networkResponse = volleyError.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }

                        if (volleyError instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(volleyError instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (volleyError instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (volleyError instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (volleyError instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (volleyError instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //Creating parameters
                HashMap<String,String> params = new HashMap<String, String>();
                //Adding parameters
                params.put("mode_counts", uploadString);
                Log.e("mode_counts", uploadString);
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(hipsterUploadRequest);
    }

    // type 3
    public void uploadZoneLikeAndReadCount(int typeId) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        SQLiteDatabase db = manager.getReadableDatabase();
        Cursor acursor = db.rawQuery("select like_count from zone where zone_id=" + typeId, null);
        acursor.moveToFirst();
        int alike_count = acursor.getInt(acursor.getColumnIndex("like_count"));
        jsonObject.put("zone_id", typeId);
        jsonObject.put("like_count", alike_count);
        acursor.close();

        final String uploadString = jsonObject.toString();

        StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.zoneaddURL,
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
                        NetworkResponse networkResponse = volleyError.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }
                        if (volleyError instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(volleyError instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (volleyError instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (volleyError instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (volleyError instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (volleyError instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //Creating parameters
                HashMap<String,String> params = new HashMap<String, String>();
                //Adding parameters
                params.put("zone_counts", uploadString);
                Log.e("zone_counts", uploadString);
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(hipsterUploadRequest);

    }

    public void uploadSurvey(int gender, int age, int education, int career, int experience, int salary, int location, int house_type, int family_type, int family_member, int know_way, String name, String email) {
        try {
            JSONObject uploadObj = new JSONObject();
            uploadObj.put("gender", gender);
            uploadObj.put("age", age);
            uploadObj.put("education", education);
            uploadObj.put("career", career);
            uploadObj.put("experience", experience);
            uploadObj.put("salary", salary);
            uploadObj.put("location", location);
            uploadObj.put("house_type", house_type);
            uploadObj.put("family_type", family_type);
            uploadObj.put("family_member", family_member);
            uploadObj.put("know_way", know_way);
            uploadObj.put("name", name);
            uploadObj.put("email", email);

            uploadString = uploadObj.toString();

            StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.surveyOneURL,
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
                            NetworkResponse networkResponse = volleyError.networkResponse;
                            if (networkResponse != null) {
                                Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                            }

                            if (volleyError instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            }else if(volleyError instanceof NoConnectionError){
                                Log.e("Volley", "NoConnectionError");
                            } else if (volleyError instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                            } else if (volleyError instanceof ServerError) {
                                Log.e("Volley", "ServerError");
                            } else if (volleyError instanceof NetworkError) {
                                Log.e("Volley", "NetworkError");
                            } else if (volleyError instanceof ParseError) {
                                Log.e("Volley", "ParseError");
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //Getting Image Name
                    //Creating parameters
                    HashMap<String,String> params = new HashMap<String, String>();
                    //Adding parameters
                    params.put("survey", uploadString);
                    Log.e("volley", uploadString);
                    //returning parameters
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this.context);
            requestQueue.add(hipsterUploadRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFeedback(int attitude, int functionality, int visual, int operability, int user_friendly, int price, int maintenance, int safety, int energy, int first_choise, int second_choise, int third_choise, int fourth_choise, int fifth_choise, String first_consider, String second_consider, String third_consider, String fourth_consider, String fifth_consider, int subscription1, int subscription2, int subscription3, int install1, int install2, int install3, int install4, int install5, int impression1, int impression2, int impression3, int impression4, int impression5, int buy, int reasonable_price) {
        try {
            JSONObject uploadObj = new JSONObject();
            uploadObj.put("attitude", attitude);
            uploadObj.put("functionality", functionality);
            uploadObj.put("visual", visual);
            uploadObj.put("operability", operability);
            uploadObj.put("operability", operability);
            uploadObj.put("user_friendly", user_friendly);
            uploadObj.put("price", price);
            uploadObj.put("maintenance", maintenance);
            uploadObj.put("safety", safety);
            uploadObj.put("energy", energy);
            uploadObj.put("first_choise", first_choise);
            uploadObj.put("second_choise", second_choise);
            uploadObj.put("third_choise", third_choise);
            uploadObj.put("fourth_choise", fourth_choise);
            uploadObj.put("fifth_choise", fifth_choise);
            uploadObj.put("first_consider", first_consider);
            uploadObj.put("second_consider", second_consider);
            uploadObj.put("third_consider", third_consider);
            uploadObj.put("fourth_consider", fourth_consider);
            uploadObj.put("fifth_consider", fifth_consider);
            uploadObj.put("subscription1", subscription1);
            uploadObj.put("subscription2", subscription2);
            uploadObj.put("subscription3", subscription3);
            uploadObj.put("install1", install1);
            uploadObj.put("install2", install2);
            uploadObj.put("install3", install3);
            uploadObj.put("install4", install4);
            uploadObj.put("install5", install5);
            uploadObj.put("impression1", impression1);
            uploadObj.put("impression2", impression2);
            uploadObj.put("impression3", impression3);
            uploadObj.put("impression4", impression4);
            uploadObj.put("impression5", impression5);
            uploadObj.put("buy", buy);
            uploadObj.put("reasonable_price", reasonable_price);

            uploadString = uploadObj.toString();

            StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.feedbackURL,
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
                            NetworkResponse networkResponse = volleyError.networkResponse;
                            if (networkResponse != null) {
                                Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                            }

                            if (volleyError instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            }else if(volleyError instanceof NoConnectionError){
                                Log.e("Volley", "NoConnectionError");
                            } else if (volleyError instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                            } else if (volleyError instanceof ServerError) {
                                Log.e("Volley", "ServerError");
                            } else if (volleyError instanceof NetworkError) {
                                Log.e("Volley", "NetworkError");
                            } else if (volleyError instanceof ParseError) {
                                Log.e("Volley", "ParseError");
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //Getting Image Name
                    //Creating parameters
                    HashMap<String,String> params = new HashMap<String, String>();
                    //Adding parameters
                    params.put("feedback", uploadString);
                    Log.e("volley", uploadString);
                    //returning parameters
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this.context);
            requestQueue.add(hipsterUploadRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void uploadHipster(String content, String picture, String combine, String pictureFilepPath, String combineFilePath, int hipsterTemplateId, int hipsterTextId, int zoneId) {
        try {

            JSONObject uploadObj = new JSONObject(); //packHipsterContentData(content, picture, combine, filepath, hipsterTemplateId, hipsterTextId, zoneId);
            uploadObj.put("content", content);
            uploadObj.put("picture_name", picture);
            uploadObj.put("combine_name", combine);
            uploadObj.put("hipster_template_id", hipsterTemplateId);
            uploadObj.put("hipster_text_id", hipsterTextId);
            uploadObj.put("zone_id", zoneId);

            File pictureFile = new File(pictureFilepPath, picture);
            File combineFile = new File(combineFilePath, combine);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();

            Bitmap pictureBmp=null;
            Bitmap combineBmp=null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inJustDecodeBounds = false;
            options.inSampleSize = 3;   //width，hight設為原來的五分之一
            try {
                pictureBmp = BitmapFactory.decodeStream(new FileInputStream(pictureFile), null, options);
                combineBmp = BitmapFactory.decodeStream(new FileInputStream(combineFile), null, options);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
                Log.e("12","32");
            }
            /*Bitmap pictureBmp = BitmapFactory.decodeStream(new FileInputStream(pictureFile));
            Bitmap combineBmp = BitmapFactory.decodeStream(new FileInputStream(combineFile));*/
            Log.e("LLLLLLLLLL", String.valueOf(pictureFile));

            //pictureBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //combineBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream1);

            pictureBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            combineBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream1);
            byte[] pictureBytes = outputStream.toByteArray();
            byte[] combineBytes = outputStream1.toByteArray();
            pictureByteImage = Base64.encodeToString(pictureBytes, Base64.DEFAULT);
            Log.e("pictureByteImage",pictureByteImage);
            combineByteImage = Base64.encodeToString(combineBytes, Base64.DEFAULT);
            Log.e("combineByteImage",combineByteImage);
            uploadObj.put("picture_data", pictureByteImage);
            uploadObj.put("combine_data", combineByteImage);

            uploadString = uploadObj.toString();

            StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.hipsterContentURL,
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
                            NetworkResponse networkResponse = volleyError.networkResponse;
                            if (networkResponse != null) {
                                Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                            }

                            if (volleyError instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            }else if(volleyError instanceof NoConnectionError){
                                Log.e("Volley", "NoConnectionError");
                            } else if (volleyError instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                            } else if (volleyError instanceof ServerError) {
                                Log.e("Volley", "ServerError");
                            } else if (volleyError instanceof NetworkError) {
                                Log.e("Volley", "NetworkError");
                            } else if (volleyError instanceof ParseError) {
                                Log.e("Volley", "ParseError");
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //Creating parameters
                    HashMap<String,String> params = new HashMap<String, String>();
                    //Adding parameters
                    params.put("hipster_content", uploadString);
                    Log.e("hipster_content", uploadString);
                    //returning parameters
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this.context);
            requestQueue.add(hipsterUploadRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadSuccessSample(String content, String picture1, String combine1, String filepath, int hipsterTemplateId, int hipsterTextId, int zoneId) {
        try {
            File rootDir = Environment.getExternalStorageDirectory();
            File f = new File(rootDir.getAbsolutePath() + "/yudelinsteven", "a3m1_mark@3x.png");
//                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));

            // parse input name to get the file
//                File picture = new File(filePath, picture_name);
//                File combine = new File(filePath, combine_name);
            File picture = f;
            File combine = f;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
            Bitmap pictureBmp = BitmapFactory.decodeStream(new FileInputStream(picture));
            Bitmap combineBmp = BitmapFactory.decodeStream(new FileInputStream(combine));

            pictureBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            combineBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream1);

            byte[] pictureBytes = outputStream.toByteArray();
            byte[] combineBytes = outputStream1.toByteArray();
            final String pictureByteImage = Base64.encodeToString(pictureBytes, Base64.DEFAULT);
            final String combineByteImage = Base64.encodeToString(combineBytes, Base64.DEFAULT);

            JSONObject uploadObject = new JSONObject();
            uploadObject.put("content", content);
            uploadObject.put("picture_name", picture);
            uploadObject.put("combine_name", combine);
            uploadObject.put("hipster_template_id", hipsterTemplateId);
            uploadObject.put("hipster_text_id", hipsterTextId);
            uploadObject.put("zone_id", zoneId);
            uploadObject.put("picture_data", pictureByteImage);
            uploadObject.put("combine_data", combineByteImage);

            final String uploadString = uploadObject.toString();
            StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.surveyOneURL,
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
                            NetworkResponse networkResponse = volleyError.networkResponse;
                            if (networkResponse != null) {
                                Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                            }

                            if (volleyError instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            }else if(volleyError instanceof NoConnectionError){
                                Log.e("Volley", "NoConnectionError");
                            } else if (volleyError instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                            } else if (volleyError instanceof ServerError) {
                                Log.e("Volley", "ServerError");
                            } else if (volleyError instanceof NetworkError) {
                                Log.e("Volley", "NetworkError");
                            } else if (volleyError instanceof ParseError) {
                                Log.e("Volley", "ParseError");
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //Creating parameters
                    HashMap<String,String> params = new HashMap<String, String>();
                    //Adding parameters
                    params.put("hipster_content", uploadString);
                    //returning parameters
                    return params;
                }
            };

            hipsterUploadRequest.setRetryPolicy(new DefaultRetryPolicy(
                    100000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(hipsterUploadRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // successful uploading to server: SURVEY
    public void volleyWhat(int gender, int age, int education, int career, int experience, int salary, int location, int house_type, int family_type, int family_member, int know_way, String name, String email) {
        try {
            JSONObject uploadObj = new JSONObject();
            uploadObj.put("gender", gender);
            uploadObj.put("age", age);
            uploadObj.put("education", education);
            uploadObj.put("career", career);
            uploadObj.put("experience", experience);
            uploadObj.put("salary", salary);
            uploadObj.put("location", location);
            uploadObj.put("house_type", house_type);
            uploadObj.put("family_type", family_type);
            uploadObj.put("family_member", family_member);
            uploadObj.put("know_way", know_way);
            uploadObj.put("name", name);
            uploadObj.put("email", email);

            final String uploadString = uploadObj.toString();

            StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.surveyOneURL,
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
                            NetworkResponse networkResponse = volleyError.networkResponse;
                            if (networkResponse != null) {
                                Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                            }

                            if (volleyError instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            }else if(volleyError instanceof NoConnectionError){
                                Log.e("Volley", "NoConnectionError");
                            } else if (volleyError instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                            } else if (volleyError instanceof ServerError) {
                                Log.e("Volley", "ServerError");
                            } else if (volleyError instanceof NetworkError) {
                                Log.e("Volley", "NetworkError");
                            } else if (volleyError instanceof ParseError) {
                                Log.e("Volley", "ParseError");
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //Getting Image Name
                    //Creating parameters
                    HashMap<String,String> params = new HashMap<String, String>();
                    //Adding parameters
                    params.put("survey", uploadString);
                    Log.e("volley", "getParames");
                    //returning parameters
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this.context);
            requestQueue.add(hipsterUploadRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testNothing() {
        Log.e("simply", "testing");
    }

    /*
        2/13/2017
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void uploadCounts() throws JSONException {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // pack data from sqlite and upload all counts from device, mode, zone
        SQLiteDatabase db = manager.getReadableDatabase();
        // device
        JSONObject jsonObject = new JSONObject();
        JSONObject device_count = new JSONObject();
        JSONObject device_object = new JSONObject();
        Cursor device_cursor = db.rawQuery("select device_id, read_count, like_count from device", null);
        device_cursor.moveToFirst();
        int device_id;
        int device_read;
        int device_like;
        JSONObject read_obj = new JSONObject();
        JSONObject like_obj = new JSONObject();
        while (device_cursor.isAfterLast() == false) {
            device_id = device_cursor.getInt(device_cursor.getColumnIndex("device_id"));
            device_read = device_cursor.getInt(device_cursor.getColumnIndex("read_count"));
            device_like = device_cursor.getInt(device_cursor.getColumnIndex("like_count"));
            read_obj.put("count_number", device_read);
            read_obj.put("create_date", dateFormat.format(c.getTime()));
            like_obj.put("count_number", device_like);
            like_obj.put("create_date", dateFormat.format(c.getTime()));
            device_object.put("1", read_obj);
            device_object.put("2", like_obj);
            device_count.put(String.valueOf(device_id), device_object);
            // clear temporary json objs - reset
            read_obj = new JSONObject();
            like_obj = new JSONObject();
            device_object = new JSONObject();
            device_cursor.moveToNext();
        }
        Log.e("simply", "testing");
        jsonObject.put("device_count", device_count);
        Log.e("count1", device_count.toString());

        // mode
        JSONObject mode_count = new JSONObject();
        JSONObject mode_object = new JSONObject();
        Cursor mode_cursor = db.rawQuery("select mode_id, read_count, like_count from mode", null);
        mode_cursor.moveToFirst();
        int mode_id;
        int mode_read;
        int mode_like;
        read_obj = new JSONObject();
        like_obj = new JSONObject();
        while (mode_cursor.isAfterLast() == false) {
            mode_id = mode_cursor.getInt(mode_cursor.getColumnIndex("mode_id"));
            mode_read = mode_cursor.getInt(mode_cursor.getColumnIndex("read_count"));
            mode_like = mode_cursor.getInt(mode_cursor.getColumnIndex("like_count"));
            read_obj.put("count_number", mode_read);
            read_obj.put("create_date", dateFormat.format(c.getTime()));
            like_obj.put("count_number", mode_like);
            like_obj.put("create_date", dateFormat.format(c.getTime()));
            mode_object.put("1", read_obj);
            mode_object.put("2", like_obj);
            mode_count.put(String.valueOf(mode_id), mode_object);
            // clear temporary json objs - reset
            read_obj = new JSONObject();
            like_obj = new JSONObject();
            mode_object = new JSONObject();
            mode_cursor.moveToNext();
        }
        jsonObject.put("mode_count", mode_count);
        Log.e("count2", String.valueOf(mode_count));

        // zone
        JSONObject zone_count = new JSONObject();
        JSONObject zone_object = new JSONObject();
        Cursor zone_cursor = db.rawQuery("select zone_id, like_count from zone", null);
        zone_cursor.moveToFirst();
        int zone_id;
        int zone_like;
        like_obj = new JSONObject();
        while (zone_cursor.isAfterLast() == false) {
            zone_id = zone_cursor.getInt(zone_cursor.getColumnIndex("zone_id"));
            zone_like = zone_cursor.getInt(zone_cursor.getColumnIndex("like_count"));
            like_obj.put("count_number", zone_like);
            like_obj.put("create_date", dateFormat.format(c.getTime()));
            zone_object.put("1", like_obj);
            zone_count.put(String.valueOf(zone_id), zone_object);
            // clear temporary json objs - reset
            like_obj = new JSONObject();
            zone_object = new JSONObject();
            zone_cursor.moveToNext();
        }
        jsonObject.put("zone_count", zone_count);
        Log.e("count3", String.valueOf(zone_count));

        JSONObject count_type = new JSONObject();
        count_type.put("count_table", jsonObject);
        final String upload = count_type.toString();
        // try-with-resources statement based on post comment below :)
        try  {
            File rootDir = context.getFilesDir();
            FileWriter file = new FileWriter(rootDir + "/test.txt");
            file.write(upload);
            Log.e("fds", "Successfully Copied JSON Object to File...");
            Log.e("sgfd", "\nJSON Object: " + upload);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringRequest hipsterUploadRequest = new StringRequest(Request.Method.POST, DatabaseUtilizer.counttypeURL,
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
                        NetworkResponse networkResponse = volleyError.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }
                        if (volleyError instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(volleyError instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (volleyError instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (volleyError instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (volleyError instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (volleyError instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //Creating parameters
                HashMap<String,String> params = new HashMap<String, String>();
                //Adding parameters
                params.put("count_type", upload);
                Log.e("count_type", upload);
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(hipsterUploadRequest);


    }
}
