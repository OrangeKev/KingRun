package com.king.run.activity.circle.model;

import com.king.run.model.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/12/5 13:04
 * 邮箱：674618016@qq.com
 */
public class UserInfoDetailResult extends BaseResult {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private UserInfoOther userInfo;
        private List<Moment> moment = new ArrayList<>();

        public UserInfoOther getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoOther userInfo) {
            this.userInfo = userInfo;
        }

        public List<Moment> getMoment() {
            return moment;
        }

        public void setMoment(List<Moment> moment) {
            this.moment = moment;
        }
    }
}
