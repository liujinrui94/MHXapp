package com.ljr.hxapp.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.base.Constant;
import com.ljr.hxapp.bean.UserAccount;
import com.ljr.hxapp.bean.UserInfo;
import com.ljr.hxapp.bean.UserInfoBuilder;
import com.ljr.hxapp.databinding.WebViewActBinding;
import com.ljr.hxapp.ui.JsToAndroid;
import com.ljr.hxapp.utils.GsonUtil;

/**
 * @author:liujinrui
 * @Date:2019/1/18
 * @Description:
 */
public class MineWebActivity extends BaseActivity implements JsToAndroid.LogOut {


    public static void MineWebActivityStart(Activity activity, String groupName, String userAccout, String password) {
        Intent intent=new Intent(activity,MineWebActivity.class);
        intent.putExtra("groupName",groupName);
        intent.putExtra("userAccout",userAccout);
        intent.putExtra("password",password);
        activity.startActivity(intent);

    }

    private WebViewActBinding binding;
    private JsToAndroid jsToAndroid = new JsToAndroid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.web_view_act);
        initView();
    }

    private void initView() {
        jsToAndroid.setLogOut(this);
        final WebView webView = binding.webView;
        WebSettings mWebSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.loadUrl(Constant.UserPage);
        webView.addJavascriptInterface(jsToAndroid, "lmuze");
        mWebSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setSupportZoom(false);//是否可以缩放，默认true
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(false);//是否使用缓存
        mWebSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
              UserInfo userInfo=new UserInfoBuilder()
                      .withGroupName(HXApplication.getInstance().getUserAccount().getGroupName())
                      .withPassword(HXApplication.getInstance().getUserAccount().getHuanxinPassword())
                      .withUserAccout(HXApplication.getInstance().getUserAccount().getUserAccount()).build();
                webView.loadUrl(String.format("javascript:window.getInfo('%s')", GsonUtil.BeanToJson(userInfo)));

            }
        });

        //设置WebChromeClient类
        webView.setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @Override
            public void onReceivedTitle(final WebView view, final String title) {

            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();

    }


    @Override
    public void logOut() {
        EMClient.getInstance().logout(true);
        startActivity(new Intent(this,WebViewActivity.class));
        finish();
    }
}
