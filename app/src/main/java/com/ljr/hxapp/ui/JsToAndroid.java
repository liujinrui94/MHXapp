package com.ljr.hxapp.ui;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * @author:liujinrui
 * @Date:2019/1/16
 * @Description:
 */
public class JsToAndroid extends Object {

    /*
    * huanxinId:环信的账号；
huanxinPassword:环信的密码；
nickname：用户昵称；
userImg：用户头像；
groupId:群组Id；
userType:用户类型（1会员，0管理员，-1超级管理员）； 
    * */

    private JsData jsData;

    private LogOut logOut;

    private GetImg getImg;


    public void setGetImg(GetImg getImg) {
        this.getImg = getImg;
    }

    public void setLogOut(LogOut logOut) {
        this.logOut = logOut;
    }

    public void setJsData(JsData jsData) {
        this.jsData = jsData;
    }


    public interface JsData {
        void Into(String message);
    }

    public interface LogOut {
        void logOut();
        void backRoom();
    }

    public interface GetImg {
        void getImg();
    }

    @JavascriptInterface
    public void getImg() {
        getImg.getImg();
    }

    @JavascriptInterface
    public void backRoom() {
        logOut.backRoom();
    }

    @JavascriptInterface
    public void getInto(String message) {
        jsData.Into(message);
    }


    @JavascriptInterface
    public void logOut() {
        logOut.logOut();
    }

    @JavascriptInterface
    public void logOut(String a) {
        logOut.logOut();
    }
}
