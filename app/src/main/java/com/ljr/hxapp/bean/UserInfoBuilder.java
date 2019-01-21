package com.ljr.hxapp.bean;

/**
 * @author:liujinrui
 * @Date:2019/1/18
 * @Description:
 */
public class UserInfoBuilder {
    private String groupName;
    private String userAccout;
    private String password;

    public UserInfoBuilder() {
    }

    public static UserInfoBuilder anUserInfo() {
        return new UserInfoBuilder();
    }

    public UserInfoBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public UserInfoBuilder withUserAccout(String userAccout) {
        this.userAccout = userAccout;
        return this;
    }

    public UserInfoBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserInfo build() {
        return new UserInfo(groupName, userAccout, password);
    }
}
