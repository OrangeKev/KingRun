package com.king.run.model;

import java.io.Serializable;

/**
 * 作者：水子wade on 2017/12/4 23:20
 * 邮箱：674618016@qq.com
 */
public class WxLoginInfo implements Serializable{
    /**
     * openid: "oxoyIwxNPlqKgQkSrsCLk16nSLnI",
     * nickname: "",
     * sex: 1,
     * language: "zh_CN",
     * city: "",
     * province: "",
     * country: "CN",
     * headimgurl: "http://wx.qlogo.cn/mmopen/PiajxSqBRaELl6YESpCaLQoeCwS5AQwdiazfGuic8PSAO1dow3vNerviczCXhTrY8dc9H36RT9wOHUVvibCxCkot6CA/0",
     * privilege: [ ],
     * unionid: "ojA6nw6WE5OmW30KWqMlOZ0awuaM"
     */
    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
