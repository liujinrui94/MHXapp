package com.ljr.hxapp.viewControl;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.ljr.hxapp.bean.MResponse;
import com.ljr.hxapp.databinding.ActivityRemarkBinding;
import com.ljr.hxapp.network.NetworkUtil;
import com.ljr.hxapp.network.RDClient;
import com.ljr.hxapp.network.api.UserService;
import com.ljr.hxapp.network.interceptor.HttpLoggingInterceptor;
import com.ljr.hxapp.utils.AppLogger;
import com.ljr.hxapp.utils.GsonUtil;
import com.ljr.hxapp.utils.ToastUtil;
import com.ljr.hxapp.utils.Util;
import com.ljr.hxapp.viewModel.RemarkVM;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author:liujinrui
 * @Date:2019/1/28
 * @Description:
 */
public class RemarkCtrl {
    private ActivityRemarkBinding binding;
    private RemarkVM remarkVM;

    public RemarkCtrl(ActivityRemarkBinding binding, String userId, String groupId) {
        this.binding = binding;
        remarkVM = new RemarkVM();
        remarkVM.setUserId(userId);
        remarkVM.setGroupId(groupId);
    }


    public void goBack(View view) {
        Util.getActivity(view).finish();
    }


    public void submit(final View view) {
        if (TextUtils.isEmpty(remarkVM.getRemark())) {
            ToastUtil.showLongToast("不可为空");
            return;
        }
        if (TextUtils.isEmpty(remarkVM.getUserId())){
            ToastUtil.showLongToast("当前不允许修改");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType mediaType = MediaType.parse("application/json");
                String requestBody = GsonUtil.BeanToJson(remarkVM);
                Request request = new Request.Builder()
                        .url("http://imtx.lmuze.xyz/changeUserGroupRemark")
                        .post(RequestBody.create(mediaType, requestBody))
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        final MResponse mResponse = GsonUtil.GsonToBean(response.body().string(), MResponse.class);
                        Util.getActivity(view).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showLongToast(mResponse.getMessage());
                                if (mResponse.getCode() == 200) {
                                    Util.getActivity(view).finish();
                                }
                            }
                        });

                    }
                });
            }
        }).start();


    }

    public static <T> HttpParams BeanToParams(T bean) {
        HttpParams httpParams = new HttpParams();
        //判断是否为空bean，如果是直接返回
        if (bean == null) {
            return httpParams;
        }
        //获取类的class
        Class cls = bean.getClass();

        Field[] fields = cls.getDeclaredFields();//获取所有字段
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();//获取所有字段名称
            Object filedValue = null;
            try {
                int typeInt = fields[i].getModifiers();//获取字段的类型

                //获取字段的类型申明表，8静态，2私有，16final  =26，类型26为静态常量，不做处理如最终serialVersionUID
                if (typeInt != 26) {
                    fields[i].setAccessible(true);//设置访问权限
                    filedValue = fields[i].get(bean);//获取所有字段的值

                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (filedValue != null && !TextUtils.equals(fieldName, "serialVersionUID")) {
                httpParams.put(fieldName, filedValue.toString());
            }

        }
        AppLogger.e("请求_内容", httpParams.toString());
        return httpParams;

    }

    public RemarkVM getRemarkVM() {
        return remarkVM;
    }
}
