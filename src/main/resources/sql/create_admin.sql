-- 创建管理员表
CREATE TABLE admin (
    admin_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 管理员ID，自增主键
    username VARCHAR(20) NOT NULL UNIQUE,              -- 管理员用户名，唯一
    password VARCHAR(255) NOT NULL,                    -- 管理员密码
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP     -- 创建时间
);

-- 插入一个默认管理员账号
INSERT INTO admin (username, password) VALUES ('admin', 'admin123'); 