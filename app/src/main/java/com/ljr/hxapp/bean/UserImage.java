package com.ljr.hxapp.bean;

/**
 * @author:liujinrui
 * @Date:2019/1/23
 * @Description:
 */
public class UserImage {
    /*
     * {"code":200,"message":"用户头像设置成功！","data":{"imgUrl":"/picture/IMG_20190119_131003.jpg"}}*/

    private int code;
    private String message;

    private UserImageInfo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserImageInfo getData() {
        return data;
    }

    public void setData(UserImageInfo data) {
        this.data = data;
    }


    public String getImgUrl() {
       return this.data.getImgUrl();
    }

    class UserImageInfo {
        private String imgUrl;

        public String getImgUrl() {
            return "http://imtx.lmuze.xyz"+imgUrl;
        }
    }

}
