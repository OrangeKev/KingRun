package com.king.run.activity.other.model;

import com.king.run.model.BaseResult;

/**
 * 作者：shuizi_wade on 2017/10/19 10:57
 * 邮箱：674618016@qq.com
 */
public class Login extends BaseResult {
    /**
     * {"data":{"nickName":"20171019ag6t1maf",
     * "avator":null,
     * "token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMDE3MTAxOWFnNnQxbWFmIiwiaWF0IjoxNTA4MzgxNjc4LCJleHAiOjE1MDg5ODE2Nzh9.t4alW_nnzWuz6YvskSg4HWKGgbskcR1ZrYmcA6cxGa9dEW_jSXND_1JFHE7V6lf9g_lHraV0xGWpBJ86OwrTVQ"
     * },
     * "msg":"登录成功","code":0}
     */
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String nickName;
        private String avator;
        private String token;
        private String userId;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvator() {
            return avator;
        }

        public void setAvator(String avator) {
            this.avator = avator;
        }

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
