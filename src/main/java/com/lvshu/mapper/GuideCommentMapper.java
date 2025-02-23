package com.lvshu.mapper;

import com.lvshu.pojo.GuideComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface GuideCommentMapper {

    /**
     * 添加评论
     */
    @Insert("INSERT INTO guide_comments (guide_id, user_id, comment, status) " +
            "VALUES (#{guideId}, #{userId}, #{comment}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    int insert(GuideComment comment);

    /**
     * 查询攻略的所有评论
     */
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

    /**
     * 更新评论状态
     */
    @Update("UPDATE guide_comments SET status = #{status} WHERE comment_id = #{commentId}")
    int updateStatus(@Param("commentId") Integer commentId, @Param("status") String status);

    /**
     * 统计评论总数
     */
    @Select("SELECT COUNT(*) FROM guide_comments")
    int countTotal();
}