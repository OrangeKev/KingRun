package com.king.run.activity.circle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/11/24 16:55
 * 邮箱：674618016@qq.com
 */
public class Moment implements Serializable {
    /**
     * "id": 70,
     * "name": "严浩",
     * "avatar": "img\/avator\/b23bvk7pt8etid8j5c0jitgo.jpg",
     * "content": "你好啊哈哈",
     * "distance": 9983573,
     * "likeCount": 0,
     * "isLiked": false,
     * "circleId": null,
     * "sex": 0,
     * "isFollowed": true,
     * "commentCount": 0,
     * "comments": []
     */
    private int id;
    private String name;
    private String avatar;
    private String content;
    private String circleName;
    private String age;
    private String userId;
    private String kilometer;
    private String pace;
    private long exerciseTime;
    private long distance;
    private long date;
    private int likeCount;
    private boolean isLiked;
    private boolean isFollowed;
    private boolean canDeleted;
    private int circleId;
    private int sex;
    private int eType;
    private int commentCount;
    private List<Comment> comments = new ArrayList<>();
    private Video video;
    private List<String> album = new ArrayList<>();
    private List<LikeUser> likeUser = new ArrayList<>();

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isCanDeleted() {
        return canDeleted;
    }

    public void setCanDeleted(boolean canDeleted) {
        this.canDeleted = canDeleted;
    }

    public int geteType() {
        return eType;
    }

    public void seteType(int eType) {
        this.eType = eType;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public long getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(long exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public List<LikeUser> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<LikeUser> likeUser) {
        this.likeUser = likeUser;
    }

    public class Video implements Serializable {
        private String framePic;
        private String videoUrl;

        public String getFramePic() {
            return framePic;
        }

        public void setFramePic(String framePic) {
            this.framePic = framePic;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }
}
