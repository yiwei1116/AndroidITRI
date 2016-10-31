package com.uscc.ncku.androiditri.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Oslo on 10/14/16.
 */
public class HelperFunctions {

    // save image to file in external storage
    public void saveImageToFile() {

    }

    // save mp3 to file in external storage
    public void saveMediaToFile() {

    }

    // save text to external storage
    public void saveTextToFile() {

    }

    public static Bitmap readImageBitmap(String internalImagePath) {
        File fileObj = new File(internalImagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(fileObj.getAbsolutePath());
        return bitmap;
    }

}
