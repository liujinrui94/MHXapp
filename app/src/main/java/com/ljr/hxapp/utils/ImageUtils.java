package com.ljr.hxapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * @author:liujinrui
 * @Date:2019/1/25
 * @Description:
 */
public class ImageUtils {

    public static void startPhotoCrop(Activity activity, Uri imageUri, Uri cropImgUri, int CROP_PHOTO) {
        //创建file文件，用于存储剪裁后的照片
//        File cropImage = new File(Environment.getExternalStorageDirectory(), "crop_image.jpg");
        File cropImage = new File(activity.getExternalCacheDir(), "crop_image.jpg");
        try {
            if (cropImage.exists()) {
                cropImage.delete();
            }
            cropImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImgUri = Uri.fromFile(cropImage);
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置源地址uri
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //设置目的地址uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
        //设置图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);//data不需要返回,避免图片太大异常
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, CROP_PHOTO);
    }

    public static void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requestCode);
    }

    public static Uri getOutput(Activity activity) {
        File outputImage = new File(activity.getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
                outputImage.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(outputImage);
    }


}
