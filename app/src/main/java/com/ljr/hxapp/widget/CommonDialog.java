package com.ljr.hxapp.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ljr.hxapp.R;

public class CommonDialog extends Dialog {
    private String detail;
    private boolean isCenter;
    private View.OnClickListener OnPositiveClickListener;
    private String positive;
    private String negate;

    //
    public CommonDialog(Context context) {
        super(context, R.style.MCommonDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_common);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth() - 120; //设置dialog的宽度为当前手机屏幕的宽度-100
        getWindow().setAttributes(p);
        TextView positiveTv = (TextView) findViewById(R.id.common_dialog_positive_tv);
        positiveTv.setVisibility(View.VISIBLE);
        positiveTv.setOnClickListener(OnPositiveClickListener);
        setCanceledOnTouchOutside(false);
        setCancelable(true);

    }



    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDetailCenter(boolean isCenter) {
        this.isCenter = isCenter;
    }


    /**
     * 确定按钮
     */
    public void setOnPositiveListener(View.OnClickListener onPositiveClickListener) {
        this.OnPositiveClickListener = onPositiveClickListener;
    }


}