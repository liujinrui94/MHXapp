package com.ljr.hxapp.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.EasyUtils;
import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.EaseBaseActivity;
import com.ljr.hxapp.ui.fragment.MEaseChatFragment;
import com.ljr.hxapp.utils.GsonUtil;
import com.ljr.hxapp.utils.PermissionsManager;
import com.ljr.hxapp.utils.PermissionsResultAction;
import com.ljr.hxapp.viewModel.RemarkVM;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author:liujinrui
 * @Date:2019/1/15
 * @Description:群聊页面
 */
public class ChatActivity extends EaseBaseActivity {


    public static ChatActivity activityInstance;
    private MEaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        //.putExtra(EaseConstant.EXTRA_USER_ID, userAccount.getGroupId()))
        toChatUsername = HXApplication.getInstance().getUserAccount().getGroupId();
        chatFragment = new MEaseChatFragment();
        //set arguments
        Bundle bundle=new Bundle();
        bundle.putString(EaseConstant.EXTRA_USER_ID,toChatUsername);
        chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        activityInstance = this;
        requestPermissions();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (HXApplication.getInstance().getUserAccount()!=null&&HXApplication.getInstance().getUserAccount().isLogin()) {
            EMClient.getInstance().login(HXApplication.getInstance().getUserAccount().getHuanxinId(), HXApplication.getInstance().getUserAccount().getHuanxinPassword(), new EMCallBack() {
                @Override
                public void onSuccess() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RemarkVM remarkVM = new RemarkVM();
                            remarkVM.setUserId(HXApplication.getInstance().getUserAccount().getUserId());
                            remarkVM.setGroupId(HXApplication.getInstance().getUserAccount().getGroupId());
                            MediaType mediaType = MediaType.parse("application/json");
                            String requestBody = GsonUtil.BeanToJson(remarkVM);
                            Request request = new Request.Builder()
                                    .url("http://imtx.lmuze.xyz/updateUserLastActiveTime")
                                    .post(RequestBody.create(mediaType, requestBody))
                                    .build();
                            OkHttpClient okHttpClient = new OkHttpClient();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(okhttp3.Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(okhttp3.Call call, Response response) throws IOException {

                                }
                            });
                        }
                    }).start();
                    chatFragment.onResume();
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String error) {
                    Log.e("WebViewActivity", error);
                }
            });
        }else {
            finish();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


}
