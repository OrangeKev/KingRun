package com.king.run.activity.mine.model;

/**
 * 作者：shuizi_wade on 2017/10/20 17:18
 * 邮箱：674618016@qq.com
 */
public class Albums {
    /**
     * "id": 11,
     * "ord": "1",
     * "url": "img\\album\\knti1uktl3aoujljwhxqig3m.jpg",
     * "urlPre": "img\\album\\knti1uktl3aoujljwhxqig3mpre.jpg"
     */
    private int id;
    private String ord;
    private String url;
    private String urlPre;
    private boolean canDeleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlPre() {
        return urlPre;
    }

    public void setUrlPre(String urlPre) {
        this.urlPre = urlPre;
    }

    public boolean isCanDeleted() {
        return canDeleted;
    }

    public void setCanDeleted(boolean canDeleted) {
        this.canDeleted = canDeleted;
    }
}
