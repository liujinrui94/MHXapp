package com.ljr.hxapp.base;

import java.util.Stack;

public class AppManager {
    private static Stack<BaseActivity> activityStack;   // Activity栈  ， 先进后出
    private static AppManager instance;

    /**
     * 单例模式实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void addActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
        activityStack.add(activity);
    }

    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }
}