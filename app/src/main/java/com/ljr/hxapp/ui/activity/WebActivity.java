package com.ljr.hxapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hyphenate.chat.EMClient;
import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.base.Constant;
import com.ljr.hxapp.bean.UserInfo;
import com.ljr.hxapp.bean.UserInfoBuilder;
import com.ljr.hxapp.databinding.WebViewActBinding;
import com.ljr.hxapp.ui.JsToAndroid;
import com.ljr.hxapp.utils.GsonUtil;

/**
 * @author:liujinrui
 * @Date:2019/1/22
 * @Description:
 */
public class WebActivity extends BaseActivity implements JsToAndroid.LogOut {


    public static void WebActivity(Activity activity, String url, String groupName, String groupId) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
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
        WebSettings mWebSettings = binding.webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.webView.setWebContentsDebuggingEnabled(true);
        }
        binding.webView.loadUrl(getIntent().getStringExtra("url"));
        binding.webView.addJavascriptInterface(jsToAndroid, "lmuze");
        mWebSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setSupportZoom(false);//是否可以缩放，默认true
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(false);//是否使用缓存
        mWebSettings.setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.webView.loadUrl(String.format("javascript:window.getMemberList('%s','%s')", getIntent().getStringExtra("groupName"), getIntent().getStringExtra("groupId")));

            }
        });

        //设置WebChromeClient类
        binding.webView.setWebChromeClient(new WebChromeClient() {

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void logOut() {
        EMClient.getInstance().logout(true);
        HXApplication.getInstance().getUserAccount().setLogin(false);
        startActivity(new Intent(this, WebViewActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void backRoom() {
        finish();
    }
}
