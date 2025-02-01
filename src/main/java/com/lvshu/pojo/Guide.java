package com.lvshu.pojo;

import java.util.Date;

public class Guide {
    private Integer guideId; // 攻略的唯一标识，自增
    private String title; // 攻略的标题，最大100字符
    private String content; // 攻略的内容，可以存储较长的文本
    private String imagePaths; // 攻略涉及的图片路径，多个路径用逗号分隔
    private String coverImage; // 封面图片路径
    private Integer authorId; // 攻略作者的用户ID，关联user表
    private String category; // 攻略的分类:旅游', '餐饮', '住宿', '交通', '其他') DEFAULT '旅游'
    private String location; // 攻略涉及的地点（城市、景点等）
    private String tags; // 攻略的标签，用逗号分隔
    private String season; // 攻略适合的季节：春、夏、秋、冬、四季
    private Date createdAt; // 攻略创建时间
    private Date updatedAt; // 攻略最后更新时间
    private String status; // 攻略状态：draft草稿、published已发布、archived已归档
    private Integer viewCount; // 查看次数
    private Integer commentCount; // 评论数
    private Integer favoriteCount; // 收藏次数
    private Integer likeCount; // 点赞数
    private Boolean isFeatured; // 是否为推荐攻略
    private String priceRange; // 价格区间：0-500、500-1000、1000-2000、2000-5000、5000+

    // 关联对象：攻略作者信息
    private User author;

    // 默认构造函数
    public Guide() {
    }

    // Getters and Setters
    public Integer getGuideId() {
        return guideId;
    }

    public void setGuideId(Integer guideId) {
        this.guideId = guideId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "guideId=" + guideId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imagePaths='" + imagePaths + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", authorId=" + authorId +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", tags='" + tags + '\'' +
                ", season='" + season + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status='" + status + '\'' +
                ", viewCount=" + viewCount +
                ", commentCount=" + commentCount +
                ", favoriteCount=" + favoriteCount +
                ", likeCount=" + likeCount +
                ", isFeatured=" + isFeatured +
                ", priceRange='" + priceRange + '\'' +
                '}';
    }
}