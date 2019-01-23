package com.ljr.hxapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.ljr.hxapp.bean.UserImage;
import com.ljr.hxapp.bean.UserInfo;
import com.ljr.hxapp.bean.UserInfoBuilder;
import com.ljr.hxapp.databinding.WebViewActBinding;
import com.ljr.hxapp.network.entity.Params;
import com.ljr.hxapp.ui.JsToAndroid;
import com.ljr.hxapp.utils.AppLogger;
import com.ljr.hxapp.utils.FileUtils;
import com.ljr.hxapp.utils.GsonUtil;
import com.ljr.hxapp.utils.ToastUtil;
import com.ljr.hxapp.widget.BaseProgressDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.xml.transform.Result;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author:liujinrui
 * @Date:2019/1/18
 * @Description:
 */
public class MineWebActivity extends BaseActivity implements JsToAndroid.LogOut , JsToAndroid.GetImg {

    private static final int REQUEST_CODE_LOCAL = 3;
    private BaseProgressDialog baseProgressDialog;
    private Uri selectedImage;

    public static void MineWebActivityStart(Activity activity, String groupName, String userAccout, String password) {
        Intent intent = new Intent(activity, MineWebActivity.class);
        intent.putExtra("groupName", groupName);
        intent.putExtra("userAccout", userAccout);
        intent.putExtra("password", password);
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
        baseProgressDialog = new BaseProgressDialog(MineWebActivity.this);
        jsToAndroid.setLogOut(this);
        jsToAndroid.setGetImg(this);
        WebSettings mWebSettings = binding.webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.webView.setWebContentsDebuggingEnabled(true);
        }
        binding.webView.loadUrl(Constant.UserPage);
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
                UserInfo userInfo = new UserInfoBuilder()
                        .withGroupName(HXApplication.getInstance().getUserAccount().getGroupName())
                        .withPassword(HXApplication.getInstance().getUserAccount().getPassword())
                        .withUserAccout(HXApplication.getInstance().getUserAccount().getUserAccount()).build();
                binding.webView.loadUrl(String.format("javascript:window.getInfo('%s')", GsonUtil.BeanToJson(userInfo)));

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
        if (requestCode == REQUEST_CODE_LOCAL) { // send local image
            if (data != null) {
                selectedImage = data.getData();
                if (selectedImage != null) {
                    upData();
                }
            }
        }
    }


    private void upData() {

        HttpParams httpParams = new HttpParams();
        String string=FileUtils.UriToString(selectedImage,MineWebActivity.this);

        httpParams.put("imgFile", new File(string));
        httpParams.put("userId", HXApplication.getInstance().getUserAccount().getUserId());
        OkHttpUtils.post("http://imtx.lmuze.xyz/uploadHeaderImage")
                .params(httpParams)
                .readTimeOut(30000)
                .writeTimeOut(30000)
                .connTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        UserImage userImage=GsonUtil.GsonToBean(s,UserImage.class);
                        binding.webView.loadUrl(String.format("javascript:window.getHref('%s')",userImage.getImgUrl()));
                        baseProgressDialog.dismiss();
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        baseProgressDialog.show();
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        baseProgressDialog.dismiss();
                      ToastUtil.showShortToast(e.toString());
                    }
                });
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

    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    @Override
    public void getImg() {
        selectPicFromLocal();
    }
}
