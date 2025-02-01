package com.lvshu.mapper;

import com.lvshu.pojo.Guide;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface GuideMapper {

        /**
         * 插入新的攻略
         */
        @Insert("INSERT INTO guide (title, content, image_paths, cover_image, author_id, category, " +
                        "location, tags, season, status, price_range) VALUES (#{title}, #{content}, " +
                        "#{imagePaths}, #{coverImage}, #{authorId}, #{category}, #{location}, #{tags}, " +
                        "#{season}, #{status}, #{priceRange})")
        @Options(useGeneratedKeys = true, keyProperty = "guideId")
        int insert(Guide guide);

        /**
         * 更新攻略信息
         */
        @Update("UPDATE guide SET title=#{title}, content=#{content}, image_paths=#{imagePaths}, " +
                        "cover_image=#{coverImage}, category=#{category}, location=#{location}, " +
                        "tags=#{tags}, season=#{season}, status=#{status}, price_range=#{priceRange} " +
                        "WHERE guide_id=#{guideId}")
        int update(Guide guide);

        /**
         * 根据ID查询攻略
         */
        @Select("SELECT * FROM guide WHERE guide_id = #{guideId}")
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
        Guide selectById(Integer guideId);

        /**
         * 查询用户的所有攻略
         */
        @Select("SELECT * FROM guide WHERE author_id = #{authorId}")
        List<Guide> selectByAuthorId(Integer authorId);

        // /**
        // * 搜索攻略
        // */
        // @Select("SELECT * FROM guide WHERE status = 'published' AND " +
        // "(title LIKE CONCAT('%',#{keyword},'%') OR " +
        // "content LIKE CONCAT('%',#{keyword},'%') OR " +
        // "location LIKE CONCAT('%',#{keyword},'%') OR " +
        // "tags LIKE CONCAT('%',#{keyword},'%'))")
        // List<Guide> search(String keyword);

        /**
         * 综合搜索攻略
         */
        @Select("SELECT * FROM guide WHERE status = 'published' AND " +
                        "(location LIKE CONCAT('%',#{keyword},'%') OR " +
                        "season = #{keyword} OR " +
                        "content LIKE CONCAT('%',#{keyword},'%') OR " +
                        "title LIKE CONCAT('%',#{keyword},'%')) " +
                        "ORDER BY created_at DESC")
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
        List<Guide> search(String keyword);

        /**
         * 增加浏览次数
         */
        @Update("UPDATE guide SET view_count = view_count + 1 WHERE guide_id = #{guideId}")
        int incrementViewCount(Integer guideId);

        /**
         * 更新攻略状态
         */
        @Update("UPDATE guide SET status = #{status} WHERE guide_id = #{guideId}")
        int updateStatus(@Param("guideId") Integer guideId, @Param("status") String status);
}