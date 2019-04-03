package com.king.run.activity.circle.model;

import com.king.run.model.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/11/24 17:18
 * 邮箱：674618016@qq.com
 */
public class HotCircleResult extends BaseResult {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<String> bannerPic = new ArrayList<>();
        private List<CircleInfo> hotCircle = new ArrayList<>();
        private List<Moment> moment = new ArrayList<>();

        public List<String> getBannerPic() {
            return bannerPic;
        }

        public void setBannerPic(List<String> bannerPic) {
            this.bannerPic = bannerPic;
        }

        public List<CircleInfo> getHotCircle() {
            return hotCircle;
        }

        public void setHotCircle(List<CircleInfo> hotCircle) {
            this.hotCircle = hotCircle;
        }

        public List<Moment> getMoment() {
            return moment;
        }

        public void setMoment(List<Moment> moment) {
            this.moment = moment;
        }
    }
}
