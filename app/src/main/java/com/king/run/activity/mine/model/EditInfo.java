package com.king.run.activity.mine.model;

import com.king.run.model.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/10/20 17:21
 * 邮箱：674618016@qq.com
 */
public class EditInfo extends BaseResult {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    /**
     * "nickName": "",
     * "avator": "img\\avator\\bwbvhlojbtaxmy5wr47ud4qu.jpg",
     * "avatorPre": "img\\avator\\wcayf7ihlxchmyonm5sekf5xpre.jpg",
     * "age": "0",
     * "sex": 0,
     * "height": 0,
     * "weight": 0,
     * "year": "",
     * "location": "",
     */
    public class Data {
        private String nickName;
        private String avator;
        private String avatorPre;
        private String age;
        private String year;
        private String location;
        private int sex;
        private int height;
        private int weight;
        private List<Albums> albums = new ArrayList<>();
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

        public String getAvatorPre() {
            return avatorPre;
        }

        public void setAvatorPre(String avatorPre) {
            this.avatorPre = avatorPre;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<Albums> getAlbums() {
            return albums;
        }

        public void setAlbums(List<Albums> albums) {
            this.albums = albums;
        }
    }
}
