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
         * 更新攻略信息
         */
        int updateFavorite(Guide guide);

        /**
         * 删除攻略
         * @param guideId
         * @return
         */
        @Delete("delete from guide where guide_id = #{guideId}")
        int deleteByPrimaryKey(Integer guideId);

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
        Guide selectById(int guideId);

        /**
         * 查询用户的所有攻略
         */
        List<Guide> selectByAuthorId(Integer authorId);


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
         * 根据关键词、季节、分类、价格区间搜索攻略
         */
        List<Guide> searchByKeywords(@Param("keyword") String keyword,
                        @Param("season") String season,
                        @Param("category") String category,
                        @Param("priceRange") String priceRange);

        /**
         * 增加浏览次数
         */
        @Update("UPDATE guide SET view_count = view_count + 1 WHERE guide_id = #{guideId}")
        int incrementViewCount(int guideId);

        /**
         * 更新攻略状态
         */
        @Update("UPDATE guide SET status = #{status} WHERE guide_id = #{guideId}")
        int updateStatus(@Param("guideId") Integer guideId, @Param("status") String status);

        /**
         * 根据状态统计攻略数量
         */
        @Select("SELECT COUNT(*) FROM guide WHERE status = #{status}")
        int countByStatus(@Param("status") String status);

        /**
         * 根据状态分页查询攻略
         */
        @Select("SELECT g.guide_id, g.title, g.author_id, g.status, g.created_at, g.cover_image FROM guide g " +
                "WHERE g.status = #{status} ORDER BY g.created_at DESC LIMIT #{offset}, #{pageSize}")
        @Results({
                @Result(property = "guideId", column = "guide_id"),
                @Result(property = "authorId", column = "author_id"),
                @Result(property = "coverImage", column = "cover_image"),
                @Result(property = "createdAt", column = "created_at")
        })
        List<Guide> selectByStatus(@Param("status") String status, @Param("offset") int offset, @Param("pageSize") int pageSize);

        /**
         * 统计攻略总数
         */
        @Select("SELECT COUNT(*) FROM guide")
        int countTotal();

        /**
         * 统计总浏览量
         */
        @Select("SELECT SUM(view_count) FROM guide")
        int countAllViewCount();

        /**
         * 搜索热门攻略
         * @return
         */
        List<Guide> selectHotGuide();
}