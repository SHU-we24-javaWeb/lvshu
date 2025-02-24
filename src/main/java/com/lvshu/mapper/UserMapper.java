package com.lvshu.mapper;

import com.lvshu.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    /**
     * 用于用户登录验证，根据密码和用户名查询用户对象
     * @param username
     * @param password
     * @return
     */
    User selectUserByUsernameAndPassword(@Param("username")String username, @Param("password") String password);

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User selectUserByUsername(String username);

    /**
     * 注册时添加用户，只填充用户名和密码，id自增长
     * @param user
     */
    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void insertUser(User user);

    /**
     * 按用户id查询用户
     * @param userId
     * @return
     */
    User selectById(int userId);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int update(User user);

    @Update("UPDATE user SET password = #{newPassword} WHERE user_id = #{userId}")
    int updatePassword(@Param("userId") Integer userId, @Param("newPassword") String newPassword);

    @Update("UPDATE user SET status = #{status} WHERE user_id = #{userId}")
    int updateStatus(@Param("userId") Integer userId, @Param("status") String status);

    /**
     * 统计用户总数
     */
    @Select("SELECT COUNT(*) FROM user")
    int countTotal();

    /**
     * 分页查询用户列表
     */
    List<User> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
}
