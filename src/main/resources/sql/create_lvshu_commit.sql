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
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,     -- 用户创建时间
                      signature VARCHAR(255),                             -- 个性签名，变长字符
                      avatar VARCHAR(255)                                 -- 头像，存储头像的文件路径或 URL
);


-- 攻略表设计
CREATE TABLE guide (
                       guide_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 攻略的唯一标识，自增
                       title VARCHAR(100) NOT NULL,                        -- 攻略的标题，最大100字符
                       content TEXT NOT NULL,                              -- 攻略的内容，可以存储较长的文本
                       image_paths TEXT,                                   -- 攻略涉及的图片路径，可以存储多个图片路径，使用逗号分隔
                       cover_image VARCHAR(255),                           -- 封面图片路径
                       author_id INT NOT NULL,                             -- 攻略作者的用户 ID，外键关联到 user 表
                       category ENUM('旅游', '餐饮', '住宿', '交通', '其他') DEFAULT '旅游',-- 攻略的分类，例如“旅游”、“餐饮”、“住宿”,"交通"“其他”等
                       location VARCHAR(100),                              -- 攻略涉及的地点（城市、景点等）
                       tags VARCHAR(255),                                  -- 攻略的标签，用逗号分隔（例如“背包旅游、上海”）
                       season ENUM('春', '夏', '秋', '冬', '四季') DEFAULT '四季',  -- 攻略适合的季节，默认四季都适合
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,     -- 攻略创建时间，自动记录当前时间
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 攻略最后更新时间
                       status ENUM('draft', 'published', 'archived') DEFAULT 'draft',  -- 攻略的状态（草稿、已发布、已归档）
                       view_count INT DEFAULT 0,                           -- 查看次数，用于记录攻略被查看的次数
                       comment_count INT DEFAULT 0,                        -- 评论数
                       favorite_count INT DEFAULT 0,                       -- 收藏次数
                       like_count INT DEFAULT 0,                           -- 点赞数，默认是0
                       is_featured BOOLEAN DEFAULT FALSE,                  -- 是否为推荐攻略
                       price_range ENUM('0-500', '500-1000', '1000-2000', '2000-5000', '5000+') DEFAULT '0-500',  -- 价格区间
                       FOREIGN KEY (author_id) REFERENCES user(user_id)    -- 外键约束，关联到用户表
);


-- 攻略评论表设计
CREATE TABLE guide_comments (
                                comment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 评论ID，自增
                                guide_id INT NOT NULL,                               -- 关联的攻略ID，外键关联到 guide 表
                                user_id INT NOT NULL,                                -- 评论者用户ID，外键关联到 user 表
                                comment TEXT NOT NULL,                               -- 评论内容
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,      -- 评论时间，自动记录当前时间
                                status ENUM('active', 'deleted') DEFAULT 'active',   -- 评论状态，'active' 表示正常，'deleted' 表示已删除
                                FOREIGN KEY (guide_id) REFERENCES guide(guide_id),   -- 外键约束，关联到 guide 表
                                FOREIGN KEY (user_id) REFERENCES user(user_id)      -- 外键约束，关联到 user 表
);


-- 用户收藏攻略的表格
CREATE TABLE user_favorites (
                                user_id INT NOT NULL,                             -- 用户ID
                                guide_id INT NOT NULL,                            -- 攻略ID
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- 收藏时间
                                PRIMARY KEY (user_id, guide_id),                  -- 联合主键，确保一个用户对同一攻略只能收藏一次
                                FOREIGN KEY (user_id) REFERENCES user(user_id),   -- 外键，关联用户
                                FOREIGN KEY (guide_id) REFERENCES guide(guide_id) -- 外键，关联攻略
);

-- 用户之间关注的关系表
CREATE TABLE user_follows (
                              follower_id INT NOT NULL,                        -- 关注者的用户ID
                              followed_id INT NOT NULL,                        -- 被关注者的用户ID
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- 关注时间
                              PRIMARY KEY (follower_id, followed_id),          -- 联合主键，确保一个用户只能对另一个用户关注一次
                              FOREIGN KEY (follower_id) REFERENCES user(user_id),   -- 外键，关注者
                              FOREIGN KEY (followed_id) REFERENCES user(user_id)    -- 外键，被关注者
);




