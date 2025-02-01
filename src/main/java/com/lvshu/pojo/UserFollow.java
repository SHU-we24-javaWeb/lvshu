package com.lvshu.pojo;

import java.util.Date;

public class UserFollow {
    private Integer followerId; // 关注者的用户ID
    private Integer followedId; // 被关注者的用户ID
    private Date createdAt; // 关注时间

    // 关联对象
    private User follower; // 关注者
    private User followed; // 被关注者

    // 默认构造函数
    public UserFollow() {
    }

    // Getters and Setters
    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Integer followedId) {
        this.followedId = followedId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "UserFollow{" +
                "followerId=" + followerId +
                ", followedId=" + followedId +
                ", createdAt=" + createdAt +
                '}';
    }
}