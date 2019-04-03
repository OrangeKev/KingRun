package com.king.run.activity.mine.model;

import com.king.run.model.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/10/20 16:25
 * 邮箱：674618016@qq.com
 */
public class UserInfo extends BaseResult {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        /**
         * "follow": 0,  关注
         * "follower": 0, 粉丝
         * "collect": 0,  收藏
         */
        private List<Albums> albums = new ArrayList<>();
        private int follow;
        private int follower;
        private int collect;
        private String nickName;
        private String avator;

        public List<Albums> getAlbums() {
            return albums;
        }

        public void setAlbums(List<Albums> albums) {
            this.albums = albums;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }

        public int getFollower() {
            return follower;
        }

        public void setFollower(int follower) {
            this.follower = follower;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

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
    }

}
