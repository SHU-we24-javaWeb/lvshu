package com.lvshu.mapper;

import com.lvshu.pojo.GuideComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface GuideCommentMapper {

    /**
     * 添加评论
     */
    @Insert("INSERT INTO guide_comments (guide_id, user_id, comment) " +
            "VALUES (#{guideId}, #{userId}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    int insert(GuideComment comment);

    /**
     * 查询攻略的所有评论
     */
    @Select("SELECT * FROM guide_comments WHERE guide_id = #{guideId} AND status = 'active' " +
            "ORDER BY created_at DESC")
    @Results({
            @Result(property = "commentId", column = "comment_id"),
            @Result(property = "guideId", column = "guide_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user", column = "user_id", one = @One(select = "com.lvshu.mapper.UserMapper.selectById")),
            @Result(property = "guide", column = "guide_id", one = @One(select = "com.lvshu.mapper.GuideMapper.selectById"))
    })
    List<GuideComment> selectByGuideId(Integer guideId);

    /**
     * 删除评论（软删除）
     */
    @Update("UPDATE guide_comments SET status = 'deleted' WHERE comment_id = #{commentId}")
    int deleteComment(Integer commentId);

    /**
     * 查询用户的所有评论
     */
    @Select("SELECT * FROM guide_comments WHERE user_id = #{userId} AND status = 'active' " +
            "ORDER BY created_at DESC")
    List<GuideComment> selectByUserId(Integer userId);
}