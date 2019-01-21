package com.ljr.hxapp.ui.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ljr.hxapp.R;
import com.ljr.hxapp.base.BaseActivity;

public class MainActivity extends BaseActivity {

    ImageView ic_im;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ic_im = findViewById(R.id.ic_im);

        final AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(300);
        mHideAnimation.setFillAfter(false);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
//                ic_im.setVisibility(View.GONE);
            }


        });

        ic_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic_im.startAnimation(mHideAnimation);
            }
        });
    }
}
