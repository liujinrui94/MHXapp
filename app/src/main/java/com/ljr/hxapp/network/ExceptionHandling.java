package com.ljr.hxapp.network;

import android.util.Log;

import com.ljr.hxapp.network.entity.HttpResult;


/**
 * Author: liujinrui
 * Date: 2016/5/30 11:53
 * <p/>
 * Description: 异常处理
 */
@SuppressWarnings("unchecked")
final class ExceptionHandling {


    static void operate(final HttpResult result) {
        Log.e("请求返回_",result.getMsg()+"\n"+result.toString());

    }
}
