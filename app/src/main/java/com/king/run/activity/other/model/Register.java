package com.king.run.activity.other.model;

import com.king.run.model.BaseResult;

/**
 * 作者：shuizi_wade on 2017/10/17 17:18
 * 邮箱：674618016@qq.com
 */
public class Register extends BaseResult {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String token;
        private String userId;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
