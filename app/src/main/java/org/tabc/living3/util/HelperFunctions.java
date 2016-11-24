package org.tabc.living3.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.tabc.living3.CommunicationWithServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Oslo on 10/14/16.
 */
public class HelperFunctions extends Application{

    private static CommunicationWithServer commServer;
    private static Context context;
    private static SQLiteDbManager manager;

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

//        fileDirPath = "/data/data/org.tabc.living3/files/itri";
//        File directory = new File(fileDirPath);
//        File[] files = directory.listFiles();
//        for (int i = 0; i < files.length; i++)
//        {
//            if (files[i].exists())
//                Log.d("Files", "FileName:" + files[i].getName());
//        }
//        Log.d("Files", finalFile);
//
//        File imgFile = new  File(fileDirPath + "/itri", paths[paths.length-1]);
//        Bitmap b = null;
//        if(imgFile.isFile()) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            b = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
//            if (b == null)
//                Log.d("files", "GGGGGGGGGGGGGG");
//        }
//
//        return b;
        return HelperFunctions.readImageBitmap(finalFile);
    }

}
