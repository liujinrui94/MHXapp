package com.ljr.hxapp.viewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * @author:liujinrui
 * @Date:2019/1/28
 * @Description:
 */
public class RemarkVM extends BaseObservable {

    private String remark;

    private String userId;

    private String groupId;

    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
