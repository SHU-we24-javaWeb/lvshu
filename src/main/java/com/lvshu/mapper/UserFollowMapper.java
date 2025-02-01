package com.lvshu.mapper;

import com.lvshu.pojo.User;
import com.lvshu.pojo.UserFollow;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserFollowMapper {

    /**
     * 添加关注关系
     */
    @Insert("INSERT INTO user_follows (follower_id, followed_id) VALUES (#{followerId}, #{followedId})")
    int insert(UserFollow follow);

    /**
     * 取消关注
     */
    @Delete("DELETE FROM user_follows WHERE follower_id = #{followerId} AND followed_id = #{followedId}")
    int delete(@Param("followerId") Integer followerId, @Param("followedId") Integer followedId);

    /**
     * 查询是否已关注
     */
    @Select("SELECT COUNT(*) FROM user_follows WHERE follower_id = #{followerId} AND followed_id = #{followedId}")
    boolean isFollowing(@Param("followerId") Integer followerId, @Param("followedId") Integer followedId);

    /**
     * 查询用户的关注列表
     */
    @Select("SELECT u.* FROM user u " +
            "INNER JOIN user_follows uf ON u.user_id = uf.followed_id " +
            "WHERE uf.follower_id = #{userId} " +
            "ORDER BY uf.created_at DESC")
    List<User> selectFollowings(Integer userId);

    /**
     * 查询用户的粉丝列表
     */
    @Select("SELECT u.* FROM user u " +
            "INNER JOIN user_follows uf ON u.user_id = uf.follower_id " +
            "WHERE uf.followed_id = #{userId} " +
            "ORDER BY uf.created_at DESC")
    List<User> selectFollowers(Integer userId);

    /**
     * 统计用户的关注数
     */
    @Select("SELECT COUNT(*) FROM user_follows WHERE follower_id = #{userId}")
    int countFollowings(Integer userId);

    /**
     * 统计用户的粉丝数
     */
    @Select("SELECT COUNT(*) FROM user_follows WHERE followed_id = #{userId}")
    int countFollowers(Integer userId);
}