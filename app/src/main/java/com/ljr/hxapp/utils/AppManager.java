package com.ljr.hxapp.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.ljr.hxapp.base.BaseActivity;

import java.util.Stack;

public class AppManager {
    
    private static Stack<BaseActivity> activityStack;
    
    private static AppManager instance;    
    
    private AppManager() {    
    }    
    
    /**  
     * 单一实例  
     */    
    public static AppManager getAppManager() {    
        if (instance == null) {    
            instance = new AppManager();    
        }    
        return instance;    
    }    
    
    /**  
     * 添加Activity到堆栈  
     */    
    public void addActivity(BaseActivity activity) {
        if (activityStack == null) {    
            activityStack = new Stack<BaseActivity>();
        }    
        activityStack.add(activity);    
    }    
    
    /**  
     * 获取当前Activity（堆栈中最后一个压入的）  
     */    
    public BaseActivity currentActivity() {
        BaseActivity activity = activityStack.lastElement();
        return activity;    
    }    
    
    /**  
     * 结束当前Activity（堆栈中最后一个压入的）  
     */    
    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        finishActivity(activity);    
    }    
    
    /**  
     * 结束指定的Activity  
     */    
    public void finishActivity(BaseActivity activity) {
        if (activity != null) {    
            activityStack.remove(activity);    
            activity.finish();    
            activity = null;    
        }    
    }    
    
    /**  
     * 结束指定类名的Activity  
     */    
    public void finishActivity(Class<?> cls) {    
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {    
                finishActivity(activity);    
            }    
        }    
    }    
    
    /**  
     * 结束所有Activity  
     */    
    public void finishAllActivity() {    
        for (int i = 0, size = activityStack.size(); i < size; i++) {    
            if (null != activityStack.get(i)) {    
                activityStack.get(i).finish();    
            }    
        }    
        activityStack.clear();    
    }    
    
    /**  
     * 退出应用程序  
     */    
    @SuppressWarnings("deprecation")    
    public void AppExit(Context context) {
        try {    
            finishAllActivity();    
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());    
            System.exit(0);    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
}
