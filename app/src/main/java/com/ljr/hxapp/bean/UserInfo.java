package com.ljr.hxapp.bean;

import java.io.Serializable;

/**
 * @author:liujinrui
 * @Date:2019/1/18
 * @Description:
 */
public class UserInfo implements Serializable {


    private String groupName;
    private String userAccout;
    private String password;

    public UserInfo(String groupName, String userAccout, String password) {
        this.groupName = groupName;
        this.userAccout = userAccout;
        this.password = password;
    }



    @Override
    public String toString() {
        return "UserInfo{" +
                "groupName='" + groupName + '\'' +
                ", userAccout='" + userAccout + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
