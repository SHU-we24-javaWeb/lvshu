package com.lvshu.mapper;

import com.lvshu.pojo.Guide;
import com.lvshu.pojo.UserFavorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserFavoriteMapper {

    /**
     * 添加收藏
     */
    @Insert("INSERT INTO user_favorites (user_id, guide_id) VALUES (#{userId}, #{guideId})")
    int insert(UserFavorite favorite);

    /**
     * 取消收藏
     */
    @Delete("DELETE FROM user_favorites WHERE user_id = #{userId} AND guide_id = #{guideId}")
    int delete(@Param("userId") Integer userId, @Param("guideId") Integer guideId);

    /**
     * 查询用户是否已收藏某攻略
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId} AND guide_id = #{guideId}")
    boolean isFavorited(@Param("userId") Integer userId, @Param("guideId") Integer guideId);

    /**
     * 查询用户的所有收藏攻略
     */
    @Select("SELECT g.* FROM guide g " +
            "INNER JOIN user_favorites uf ON g.guide_id = uf.guide_id " +
            "WHERE uf.user_id = #{userId} " +
            "ORDER BY uf.created_at DESC")
    @Results({
            @Result(property = "guideId", column = "guide_id"),
            @Result(property = "imagePaths", column = "image_paths"),
            @Result(property = "coverImage", column = "cover_image"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "favoriteCount", column = "favorite_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "isFeatured", column = "is_featured"),
            @Result(property = "priceRange", column = "price_range"),
            @Result(property = "author", column = "author_id", one = @One(select = "com.lvshu.mapper.UserMapper.selectById"))
    })
    List<Guide> selectFavoriteGuides(Integer userId);

    /**
     * 统计用户收藏的攻略数量
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId}")
    int countFavorites(Integer userId);

    /**
     * 统计攻略被收藏数量
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE guide_id = #{guideId}")
    int countGUideFavorites(Integer guideId);
}