package com.lvshu.pojo;

import java.util.Date;

public class User {

    private Integer userId;                // 自增主键
    private String username;           // 用户名
    private String password;           // 哈希后的密码
    private String gender;             // 性别 (M, F, O)
    private Date dateOfBirth;          // 出生日期
    private String nativePlace;        // 籍贯
    private String mobilePhone;        // 手机号
    private String email;              // 邮箱
    private String major;              // 专业
    private String status;             // 用户状态 (active, inactive, banned)
    private Date createdAt;            // 创建时间
    private String signature;           // 个性签名
    private String avatar;              // 头像

    // 默认构造方法
    public User() {}

    // 全参数构造方法
    public User(Integer userId, String username, String password, String gender, Date dateOfBirth,
                String nativePlace, String mobilePhone, String email, String major,
                String status, Date createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nativePlace = nativePlace;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.major = major;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getter and Setter Methods

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nativePlace='" + nativePlace + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", major='" + major + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
