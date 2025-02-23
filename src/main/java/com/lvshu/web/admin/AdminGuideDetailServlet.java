package com.lvshu.web.admin;

import com.lvshu.mapper.GuideMapper;
import com.lvshu.mapper.GuideCommentMapper;
import com.lvshu.pojo.Guide;
import com.lvshu.pojo.GuideComment;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/adminGuideDetailServlet")
public class AdminGuideDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        try {
            // 获取攻略ID
            int guideId = Integer.parseInt(req.getParameter("guideId"));

            // 构建基础URL
            String baseUrl = "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/";

            // 查询数据
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);
            GuideCommentMapper commentMapper = sqlSession.getMapper(GuideCommentMapper.class);

            // 查询攻略详情和评论列表
            Guide guide = guideMapper.selectById(guideId);
            List<GuideComment> comments = commentMapper.selectByGuideId(guideId);

            // 处理图片路径
            if (guide.getCoverImage() != null) {
                guide.setCoverImage(baseUrl + guide.getCoverImage());
            }
            if (guide.getImagePaths() != null) {
                String[] paths = guide.getImagePaths().split(",");
                StringBuilder newPaths = new StringBuilder();
                for (int i = 0; i < paths.length; i++) {
                    if (i > 0) newPaths.append(",");
                    newPaths.append(baseUrl).append(paths[i].trim());
                }
                guide.setImagePaths(newPaths.toString());
            }

            // 构建返回的JSON
            StringBuilder json = new StringBuilder();
            json.append("{\"success\":true,\"guide\":{")
                .append("\"guideId\":").append(guide.getGuideId()).append(",")
                .append("\"title\":\"").append(guide.getTitle()).append("\",")
                .append("\"content\":\"").append(guide.getContent()).append("\",")
                .append("\"location\":\"").append(guide.getLocation()).append("\",")
                .append("\"season\":\"").append(guide.getSeason()).append("\",")
                .append("\"category\":\"").append(guide.getCategory()).append("\",")
                .append("\"priceRange\":\"").append(guide.getPriceRange()).append("\",")
                .append("\"imagePaths\":\"").append(guide.getImagePaths()).append("\",")
                .append("\"coverImage\":\"").append(guide.getCoverImage()).append("\",")
                .append("\"authorId\":").append(guide.getAuthorId()).append(",")
                .append("\"authorName\":\"").append(guide.getAuthor().getUsername()).append("\",")
                .append("\"status\":\"").append(guide.getStatus()).append("\",")
                .append("\"createdAt\":\"").append(guide.getCreatedAt()).append("\",")
                .append("\"updatedAt\":\"").append(guide.getUpdatedAt()).append("\",")
                .append("\"viewCount\":").append(guide.getViewCount()).append(",")
                .append("\"commentCount\":").append(guide.getCommentCount()).append(",")
                .append("\"favoriteCount\":").append(guide.getFavoriteCount()).append(",")
                .append("\"likeCount\":").append(guide.getLikeCount()).append(",")
                .append("\"isFeatured\":").append(guide.getIsFeatured())
                .append("},\"comments\":[");

            // 添加评论数据
            for (int i = 0; i < comments.size(); i++) {
                GuideComment comment = comments.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{")
                    .append("\"commentId\":").append(comment.getCommentId()).append(",")
                    .append("\"userId\":").append(comment.getUserId()).append(",")
                    .append("\"comment\":\"").append(comment.getComment()).append("\",")
                    .append("\"status\":\"").append(comment.getStatus()).append("\",")
                    .append("\"createdAt\":\"").append(comment.getCreatedAt()).append("\"")
                    .append("}");
            }

            json.append("]}");
            writer.write(json.toString());

            sqlSession.close();
        } catch (Exception e) {
            writer.write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
} 