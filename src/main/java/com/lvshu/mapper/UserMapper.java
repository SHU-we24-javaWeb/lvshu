package com.lvshu.mapper;

import com.lvshu.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    /**
     * 用于用户登录验证，根据密码和用户名查询用户对象
     * @param username
     * @param password
     * @return
     */
    @Select("select * from user where username = #{username} and password = #{password}")
    User selectUserByUsernameAndPassword(@Param("username")String username, @Param("password") String password);

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User selectUserByUsername(String username);

    /**
     * 注册时添加用户，只填充用户名和密码，id自增长
     * @param user
     */
//    @Insert("INSERT INTO user values (null, #{username}, #{password}, null, null, null, null, null, null, null)")
//    void insertUser(User user);
    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void insertUser(User user);

}
