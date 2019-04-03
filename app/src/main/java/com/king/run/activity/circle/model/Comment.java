package com.king.run.activity.circle.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2017/11/24 17:15
 * 邮箱：674618016@qq.com
 */
public class Comment implements Serializable {
    /**
     * id ：评论标识id
     * momentId: 评论所属momentId
     * reviewer: 评论人id
     * reviewerName: 评论人name
     * toUser: 被回复人id
     * toUserName: 别回复人姓名
     * content: 评论内容
     * date: 时间
     * <p>
     * <p>
     * id: 7,
     * momentId: 196,
     * reviewer: "20171125fqjg0m7n",
     * toUser: null,
     * content: "突突突",
     * toUserName: null,
     * reviewerName: "阿萨德123",
     * canDeleted: true,
     * date: 1511968462000
     */
    private int id;
    private int momentId;
    private String reviewer;
    private String reviewerName;
    private String toUser;
    private String toUserName;
    private String content;
    private long date;
    private String avator;
    private boolean canDeleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMomentId() {
        return momentId;
    }

    public void setMomentId(int momentId) {
        this.momentId = momentId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public boolean isCanDeleted() {
        return canDeleted;
    }

    public void setCanDeleted(boolean canDeleted) {
        this.canDeleted = canDeleted;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
