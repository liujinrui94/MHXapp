package com.ljr.hxapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;
import com.ljr.hxapp.databinding.ActShowPicBinding;
import com.ljr.hxapp.viewControl.RemarkCtrl;
import com.ljr.hxapp.viewModel.ShowImageViewModel;

/**
 * @author:liujinrui
 * @Date:2019/1/30
 * @Description:
 */
public class ShowPicAct extends BaseActivity {


    public static void ShowPicActStart(Activity activity,String url){

        Intent intent=new Intent(activity,ShowPicAct.class);
        intent.putExtra("image",url);
        activity.startActivity(intent);

    }

    ActShowPicBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_show_pic);
        binding.setViewCtrl(new ShowImageViewModel(binding,getIntent().getStringExtra("image")));

    }
}
