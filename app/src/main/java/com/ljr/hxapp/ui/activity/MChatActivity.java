package com.ljr.hxapp.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.ljr.hxapp.HXApplication;
import com.ljr.hxapp.R;
import com.ljr.hxapp.base.EaseBaseActivity;
import com.ljr.hxapp.utils.PermissionsManager;
import com.ljr.hxapp.utils.PermissionsResultAction;

public class MChatActivity extends EaseBaseActivity {


    public static MChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);

        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new EaseChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
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
                    chatFragment.onResume();
//                startActivity(new Intent(ChatActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, userAccount.getGroupId()));
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
