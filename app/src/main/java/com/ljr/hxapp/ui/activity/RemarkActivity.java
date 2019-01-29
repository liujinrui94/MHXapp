package com.ljr.hxapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.databinding.ActivityRemarkBinding;
import com.ljr.hxapp.viewControl.RemarkCtrl;

/**
 * @author:liujinrui
 * @Date:2019/1/28
 * @Description:
 */
public class RemarkActivity extends BaseActivity {

    private ActivityRemarkBinding binding;

    public static void RemarkActivity(Activity activity, String userId, String groupId) {
        Intent intent = new Intent(activity, RemarkActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", groupId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remark);
        binding.setViewCtrl(new RemarkCtrl(binding,getIntent().getStringExtra("userId"),getIntent().getStringExtra("groupId")));

    }
}
