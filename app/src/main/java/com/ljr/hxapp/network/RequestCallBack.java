package com.ljr.hxapp.network;

import android.databinding.ObservableInt;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Author: hebihe
 * E-mail: hbh@erongdu.com
 * Date: 2016/4/22 19:31
 * <p/>
 * Description: 网络请求回调封装类
 */
public abstract class RequestCallBack<T> implements Callback<T> {
    public abstract void onSuccess(Call<T> call, Response<T> response);

//    private SwipeToLoadLayout swipeLayout;
    private ObservableInt placeholderState;

    public RequestCallBack() {
    }

//    public RequestCallBack(SwipeToLoadLayout swipeLayout) {
//        this.swipeLayout = swipeLayout;
//    }
//
//    public RequestCallBack(SwipeToLoadLayout swipeLayout, ObservableInt placeholderState) {
//        this.swipeLayout = swipeLayout;
//        this.placeholderState = placeholderState;
//    }
    public RequestCallBack(ObservableInt placeholderState) {
        this.placeholderState = placeholderState;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        NetworkUtil.dismissCutscenes();

        if (response.isSuccessful() && response.body() != null) {
            onSuccess(call, response);
        } else {
            onFailed(call, response);
        }
    }

    public void onFailed(Call<T> call, Response<T> response) {

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        NetworkUtil.dismissCutscenes();
        t.printStackTrace();
    }
}
