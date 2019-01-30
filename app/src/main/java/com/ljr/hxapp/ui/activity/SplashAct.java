package com.ljr.hxapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.base.Constant;
import com.ljr.hxapp.bean.UserAccount;
import com.ljr.hxapp.rsa.encryption.utils.ContextHolder;
import com.ljr.hxapp.rsa.encryption.utils.SPUtil;
import com.ljr.hxapp.utils.GsonUtil;
import com.ljr.hxapp.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author:liujinrui
 * @Date:2019/1/30
 * @Description:
 */
public class SplashAct  extends BaseActivity {



    //    // 跳转引导页
    private static final int GO_GUIDE = 0x01;
    // 跳转首页
    private static final int GO_MAIN = 0x02;
    // 页面跳转逻辑
    private static final int DO_HANDLER = 0x99;
    // 最小显示时间
    private static final int SHOW_TIME_MIN = 700;
    // 开始时间
    private static long mStartTime;
    // 下载更新
    private static final int NET_DOWN = 0x03;
    private SplashHandler mHandler;

    private static final int REQUEST_CODE_STORAGE = 1001;

    private boolean first = true;


    private String[] STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        mHandler = new SplashHandler(this);
        // 记录开始时间
        mStartTime = System.currentTimeMillis();
        // 初始化一些数据

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Long l1 = new Long(201902140000l);
        if (Long.parseLong(sdf.format(Calendar.getInstance().getTime())) < l1) {
            somethingToDo();
        }else {
            ToastUtil.showLongToast("请联系管理员升级最新版本");
        }
    }

    private void somethingToDo() {
        mHandler.removeMessages(DO_HANDLER);
        String string= (String) SPUtil.getValue(SPUtil.getSp(ContextHolder.getContext(), Constant.SP_NAME), Constant.USER_INFO,"");
        UserAccount userAccount=GsonUtil.GsonToBean(string,UserAccount.class);
        HXApplication.getInstance().setUserAccount(userAccount,false);
        mHandler.sendEmptyMessage(DO_HANDLER);


    }





    /**
     * Handler:跳转到不同界面
     */
    private static class SplashHandler extends Handler {
        WeakReference<SplashAct> act;

        SplashHandler(SplashAct act) {
            this.act = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 计算一下总共花费的时间g
            long loadingTime = System.currentTimeMillis() - mStartTime;
            switch (msg.what) {
                case DO_HANDLER:
                    if (null!=HXApplication.getInstance().getUserAccount()&&HXApplication.getInstance().getUserAccount().isLogin()) {
                        sendEmptyMessage(GO_MAIN);
                    } else {
                        sendEmptyMessage(GO_GUIDE);
                    }
                    break;

                case GO_GUIDE:
                    // 如果比最小显示时间还短，就延时进入GuideActivity，否则直接进入
                    if (loadingTime < SHOW_TIME_MIN) {
                        postDelayed(goToGuideActivity, SHOW_TIME_MIN - loadingTime);
                    } else {
                        post(goToGuideActivity);
                    }
                    break;

                case GO_MAIN:
                    // 如果比最小显示时间还短，就延时进入HomeActivity，否则直接进入
                    if (loadingTime < SHOW_TIME_MIN) {
                        postDelayed(goToMainActivity, SHOW_TIME_MIN - loadingTime);
                    } else {
                        post(goToMainActivity);
                    }
                    break;

            }
        }

        // 进入 GuideAct
        Runnable goToGuideActivity = new Runnable() {
            @Override
            public void run() {
                act.get().startActivity(new Intent(act.get(),WebViewActivity.class));
                act.get().finish();
            }
        };
        // 进入 MainAct
        Runnable goToMainActivity = new Runnable() {
            @Override
            public void run() {
                act.get().startActivity(new Intent(act.get(),ChatActivity.class));
                act.get().finish();
            }
        };
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
