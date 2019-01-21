package com.ljr.hxapp.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ljr.hxapp.BuildConfig;
import com.ljr.hxapp.utils.AppLogger;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        AppLogger.e("当前方法: onCreate");
        ARouter.getInstance().inject(this);
        if (BuildConfig.DEBUG) {
            initDeBug();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLogger.e("当前方法: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLogger.e("当前方法: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLogger.e("当前方法: onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.e("当前方法: onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppLogger.e("当前方法: onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogger.e("当前方法: onDestroy");
    }


    private void initDeBug() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName();    //类名
        String className = info.topActivity.getClassName();              //完整类名
        String packageName = info.topActivity.getPackageName();
        AppLogger.e("当前类名" + shortClassName);
    }

    @Override
    public void onBackPressed() {
        AppLogger.e("当前方法: onBackPressed");
        super.onBackPressed();
    }

    @Override
    public void finish() {
        AppLogger.e("当前方法: finish");
        ARouter.getInstance().inject(this);
        super.finish();
    }

}
