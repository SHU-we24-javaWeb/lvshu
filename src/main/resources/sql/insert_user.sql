-- 插入一个用户数据，带个性签名和头像
INSERT INTO user (username, password, gender, date_of_birth, native_place, mobile_phone, email, major, status, signature, avatar)
VALUES (
           '小猪',
           '123',
           'M',
           '2004-11-02',
           '上海',
           '15288225565',
           '1350575461@qq.com',
           '计算机科学技术',
           'active',
           '热爱睡觉，梦想是早点退休',
           'images/pig.png'  -- 头像的 URL
       );
