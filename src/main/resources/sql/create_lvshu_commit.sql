-- 创建一个名为lvshu的数据库以后跑这个sql
CREATE DATABASE lvshu;

USE lvshu;

DROP TABLE IF EXISTS user;  -- 删除表 user，如果表存在的话
-- 创建表单 user
CREATE TABLE user (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,   -- 自增主键
    username VARCHAR(20) NOT NULL UNIQUE,               -- 用户名 变长字符，非空
    password VARCHAR(255) NOT NULL,                     -- 存储哈希后的密码，变长字符，非空 如果我们能做哈希的话 3-20位密码
    gender ENUM('M', 'F', 'O') DEFAULT 'O',             -- 性别，枚举类型，O表示其他，默认为'O'
    date_of_birth DATE,                                 -- 出生日期
    native_place VARCHAR(10),                           -- 籍贯，变长字符
    mobile_phone CHAR(11) UNIQUE,                       -- 手机号，定长且唯一
    email VARCHAR(100) UNIQUE,                          -- 邮箱，唯一
    major VARCHAR(20),                                  -- 专业，变长字符
    status ENUM('active', 'inactive', 'banned') DEFAULT 'active',  -- 用户状态，默认为活跃
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP     -- 用户创建时间
);


