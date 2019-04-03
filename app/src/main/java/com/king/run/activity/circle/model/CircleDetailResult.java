package com.king.run.activity.circle.model;

import com.king.run.model.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/12/6 11:43
 * 邮箱：674618016@qq.com
 */
public class CircleDetailResult extends BaseResult {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private CircleDetail circlDetail;
        private List<Moment> moment = new ArrayList<>();

        public CircleDetail getCirclDetail() {
            return circlDetail;
        }

        public void setCirclDetail(CircleDetail circlDetail) {
            this.circlDetail = circlDetail;
        }

        public List<Moment> getMoment() {
            return moment;
        }

        public void setMoment(List<Moment> moment) {
            this.moment = moment;
        }
    }
}
