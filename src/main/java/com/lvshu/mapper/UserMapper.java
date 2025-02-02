package com.lvshu.mapper;

import com.lvshu.pojo.User;
import org.apache.ibatis.annotations.*;

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
    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void insertUser(User user);

    User selectById(int userId);


    @Update("UPDATE user SET gender = #{gender}, date_of_birth = #{dateOfBirth}, " +
            "native_place = #{nativePlace}, mobile_phone = #{mobilePhone}, " +
            "email = #{email}, major = #{major}, signature = #{signature}, " +
            "avatar = #{avatar} WHERE user_id = #{userId}")
    int update(User user);

    @Update("UPDATE user SET password = #{newPassword} WHERE user_id = #{userId}")
    int updatePassword(@Param("userId") Integer userId, @Param("newPassword") String newPassword);

    @Update("UPDATE user SET status = #{status} WHERE user_id = #{userId}")
    int updateStatus(@Param("userId") Integer userId, @Param("status") String status);
}
