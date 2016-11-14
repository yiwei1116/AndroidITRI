package com.uscc.ncku.androiditri.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.uscc.ncku.androiditri.CommunicationWithServer;

import java.io.File;

/**
 * Created by Oslo on 10/14/16.
 */
public class HelperFunctions extends Application{

    private static CommunicationWithServer commServer;
    private static Context context;
    private static SQLiteDbManager manager;

    public static Bitmap readImageBitmap(String internalImagePath) {
        File fileObj = new File(internalImagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(fileObj.getAbsolutePath());
        return bitmap;
    }

    // ********************** get bitmap from file name ****************
    public static Bitmap getBitmapFromFile(Context context, String name) {
        // get file directory
        File fileDir = context.getFilesDir();
        String fileDirPath = String.valueOf(fileDir);

        // parse file name
        String[] paths = name.split("/");

        String finalFile = fileDirPath + "/itri/" + paths[paths.length-1];
        return HelperFunctions.readImageBitmap(finalFile);
    }
}
