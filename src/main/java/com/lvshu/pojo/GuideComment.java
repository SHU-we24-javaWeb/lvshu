package com.lvshu.pojo;

import java.util.Date;

public class GuideComment {
    private Integer commentId; // 评论ID，自增
    private Integer guideId; // 关联的攻略ID
    private Integer userId; // 评论者用户ID
    private String comment; // 评论内容
    private Date createdAt; // 评论时间
    private String status; // 评论状态：active正常、deleted已删除

    // 关联对象：评论者信息
    private User user;
    // 关联对象：攻略信息
    private Guide guide;

    // 默认构造函数
    public GuideComment() {
    }

    // Getters and Setters
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getGuideId() {
        return guideId;
    }

    public void setGuideId(Integer guideId) {
        this.guideId = guideId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "GuideComment{" +
                "commentId=" + commentId +
                ", guideId=" + guideId +
                ", userId=" + userId +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}