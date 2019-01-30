package com.ljr.hxapp.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.base.Constant;
import com.ljr.hxapp.bean.UserAccount;
import com.ljr.hxapp.databinding.WebViewActBinding;
import com.ljr.hxapp.rsa.encryption.utils.SPUtil;
import com.ljr.hxapp.ui.JsToAndroid;
import com.ljr.hxapp.utils.GsonUtil;
import com.ljr.hxapp.utils.PermissionsManager;
import com.ljr.hxapp.utils.PermissionsResultAction;


/**
 * @author:liujinrui
 * @Date:2019/1/14
 * @Description:
 */
public class WebViewActivity extends BaseActivity implements JsToAndroid.JsData {


    private WebViewActBinding binding;
    private JsToAndroid jsToAndroid = new JsToAndroid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.web_view_act);
        jsToAndroid.setJsData(this);
        initView();
        requestPermissions();
    }

    private void initView() {


        WebView webView = binding.webView;
        WebSettings mWebSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.loadUrl(Constant.URL);
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

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void Into(String message) {
        final UserAccount userAccount = GsonUtil.GsonToBean(message, UserAccount.class);
        userAccount.setLogin(true);
        HXApplication.getInstance().setUserAccount(userAccount,true);
        SPUtil.saveValue(SPUtil.getSp(getBaseContext(),Constant.SP_NAME),Constant.USER_INFO,GsonUtil.BeanToJson(userAccount));
        startActivity(new Intent(WebViewActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, userAccount.getGroupId()));
        finish();
    }
}
