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
//    @Insert("INSERT INTO user values (null, #{username}, #{password}, null, null, null, null, null, null, null)")
//    void insertUser(User user);
    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User selectById(Integer userId);

//    @Insert("INSERT INTO user (username, password, gender, date_of_birth, native_place, mobile_phone, email, major, signature, avatar) " +
//            "VALUES (#{username}, #{password}, #{gender}, #{dateOfBirth}, #{nativePlace}, #{mobilePhone}, #{email}, #{major}, #{signature}, #{avatar})")
//    @Options(useGeneratedKeys = true, keyProperty = "userId")
//    int insert(User user);

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
