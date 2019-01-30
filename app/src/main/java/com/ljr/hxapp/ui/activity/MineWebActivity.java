package com.ljr.hxapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.base.Constant;
import com.ljr.hxapp.bean.ToUser;
import com.ljr.hxapp.bean.UserAccount;
import com.ljr.hxapp.bean.UserImage;
import com.ljr.hxapp.bean.UserInfo;
import com.ljr.hxapp.bean.UserInfoBuilder;
import com.ljr.hxapp.databinding.WebViewActBinding;
import com.ljr.hxapp.rsa.encryption.utils.SPUtil;
import com.ljr.hxapp.ui.JsToAndroid;
import com.ljr.hxapp.utils.FileUtils;
import com.ljr.hxapp.utils.GsonUtil;
import com.ljr.hxapp.utils.ImageUtils;
import com.ljr.hxapp.utils.ToastUtil;
import com.ljr.hxapp.widget.BaseProgressDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.BaseRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author:liujinrui
 * @Date:2019/1/18
 * @Description:
 */
public class MineWebActivity extends BaseActivity implements JsToAndroid.LogOut, JsToAndroid.MineJs {

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
        jsToAndroid.setMineJs(this);
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
                if (url.contains("userPage")) {
                    UserInfo userInfo = new UserInfoBuilder()
                            .withGroupName(HXApplication.getInstance().getUserAccount().getGroupName())
                            .withPassword(HXApplication.getInstance().getUserAccount().getPassword())
                            .withUserAccout(HXApplication.getInstance().getUserAccount().getUserAccount()).build();
                    binding.webView.loadUrl(String.format("javascript:window.getInfo('%s')", GsonUtil.BeanToJson(userInfo)));
                }

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
                selectedImage = ImageUtils.getOutput(MineWebActivity.this);
                if (data.getData() != null) {
                    ImageUtils.cropImageUri(MineWebActivity.this, data.getData(), selectedImage, 101);
                }
            }
        } else if (requestCode == 101) {
            upData();
        }
    }

    @Override
    protected void onDestroy() {
        if (binding.webView != null) {
            jsToAndroid.setLogOut(null);
            jsToAndroid.setMineJs(null);
            binding.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            binding.webView.clearHistory();
            ((ViewGroup) binding.webView.getParent()).removeView(binding.webView);
            binding.webView.destroy();
        }
        super.onDestroy();
    }

    private void upData() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("imgFile", new File(selectedImage.getPath()));
        httpParams.put("userId", HXApplication.getInstance().getUserAccount().getUserId());
        OkHttpUtils.post("http://imtx.lmuze.xyz/uploadHeaderImage")
                .params(httpParams)
                .readTimeOut(30000)
                .writeTimeOut(30000)
                .connTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        UserImage userImage = GsonUtil.GsonToBean(s, UserImage.class);
                        binding.webView.loadUrl(String.format("javascript:window.getHref('%s')", userImage.getImgUrl()));
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
        SPUtil.clear(SPUtil.getSp(getBaseContext(), Constant.SP_NAME));
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

    @Override
    public void newsList() {

        startActivity(new Intent(MineWebActivity.this, ConversationListActivity.class));
    }

    @Override
    public void privateChat(String message) {
        ToUser toUser = GsonUtil.GsonToBean(message, ToUser.class);
        startActivity(new Intent(MineWebActivity.this, MChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, toUser.getToId()));

    }

    @Override
    public void changeGroup(String message) {
        final UserAccount userAccount = GsonUtil.GsonToBean(message, UserAccount.class);
        userAccount.setLogin(true);
        userAccount.setPassword(HXApplication.getInstance().getUserAccount().getPassword());
        userAccount.setUserAccount(HXApplication.getInstance().getUserAccount().getUserAccount());
        userAccount.setUserId(HXApplication.getInstance().getUserAccount().getUserId());
        HXApplication.getInstance().setUserAccount(userAccount, true);
        startActivity(new Intent(MineWebActivity.this, ChatActivity.class));
        finish();
    }
}
