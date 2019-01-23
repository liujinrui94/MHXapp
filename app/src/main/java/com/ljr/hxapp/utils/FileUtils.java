package com.ljr.hxapp.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import com.ljr.hxapp.base.Constant;

import java.io.File;

/**
 * @author:liujinrui
 * @Date:2019/1/23
 * @Description:
 */
public class FileUtils {

    public static String UriToString(Uri selectedImage, Activity constant) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = constant.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(constant, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return "";
            }
            return picturePath;
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(constant, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return "";

            }
           return file.getAbsolutePath();
        }
    }
}
