package com.lvshu.pojo;

import java.util.Date;

public class UserFavorite {
    private Integer userId; // 用户ID
    private Integer guideId; // 攻略ID
    private Date createdAt; // 收藏时间

    // 关联对象
    private User user; // 收藏的用户
    private Guide guide; // 被收藏的攻略

    // 默认构造函数
    public UserFavorite() {
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGuideId() {
        return guideId;
    }

    public void setGuideId(Integer guideId) {
        this.guideId = guideId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    @Override
    public String toString() {
        return "UserFavorite{" +
                "userId=" + userId +
                ", guideId=" + guideId +
                ", createdAt=" + createdAt +
                '}';
    }
}