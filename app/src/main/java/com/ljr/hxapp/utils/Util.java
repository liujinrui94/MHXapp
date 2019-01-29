package com.ljr.hxapp.utils;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.ljr.hxapp.rsa.encryption.utils.ContextHolder;

import java.util.regex.Pattern;
public class Util {
    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 通过view暴力获取getContext()(Android不支持view.getContext()了)
     *
     * @param view
     *         要获取context的view
     *
     * @return 返回一个activity
     */
    /**
     * 通过 View 获取Activity
     */
    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) view.getRootView().getContext();
    }

    /**
     * 获取通讯录的内容
     *
     * @param uri
     * @return
     */
    public static String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        try {
            //得到ContentResolver对象
            ContentResolver cr = ContextHolder.getContext().getContentResolver();
            //取得电话本中开始一项的光标
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                //取得联系人姓名
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                contact[0] = cursor.getString(nameFieldColumnIndex);
                //取得电话号码
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                /*Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                if (phone != null) {
                    phone.moveToFirst();
                    contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }*/

                // 查看联系人有多少个号码，如果没有号码，返回0
                int phoneCount = cursor
                        .getInt(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                Cursor phoneCursor;
                if (phoneCount > 0) {
                    // 获得联系人的电话号码列表
                    phoneCursor = ContextHolder.getContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + ContactId, null, null);
                    if (phoneCursor.moveToFirst()) {
                        StringBuffer str = new StringBuffer();
                        do {
                            //遍历所有的联系人下面所有的电话号码
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            str.append(phoneNumber + ",");
                            //使用Toast技术显示获得的号码
                            //Toast.makeText(context, "联系人电话：" + phoneNumber, Toast.LENGTH_LONG).show();
                        }
                        while (phoneCursor.moveToNext());
                        if (str.toString().length() > 0) {
                            contact[1] = str.toString().substring(0, str.toString().length() - 1);
                        }
                    }
                    phoneCursor.close();
                }
                cursor.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Error e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return contact;
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    //判断是否是6.0
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    //判断是否是手机号
    public static boolean isPhoneNumber(String input) {
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        Pattern p = Pattern.compile(regex);
        return p.matches(regex, input);
    }

    //获取程序的名称
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
