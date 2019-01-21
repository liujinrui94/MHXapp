package com.ljr.hxapp.common;

/**
 *
 * @author:liujinrui
 * @Date:2019/1/14
 * @Description: 路由表
 *
 */
public class RouterUrl {
    private static final String URI_SCHEME = "wolverine:/";

    /**
     * 获得请求URI
     */
    public static String getRouterUrl(String host) {
        return URI_SCHEME + host;
    }

    public static final String Home_MainActivity  = "home/mainActivity";

}
