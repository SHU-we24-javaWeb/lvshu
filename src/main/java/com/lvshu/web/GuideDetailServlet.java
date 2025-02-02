package com.lvshu.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.lvshu.mapper.GuideMapper;
import com.lvshu.pojo.Guide;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;

@WebServlet("/guide_details")
public class GuideDetailServlet extends HttpServlet {
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void init() throws ServletException {
        sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        try {
            String guideIdStr = request.getParameter("id");
            System.out.println("Received request for guide ID: " + guideIdStr);

            if (guideIdStr == null || guideIdStr.trim().isEmpty()) {
                out.write("{\"success\":false,\"message\":\"缺少攻略ID\"}");
                return;
            }

            Integer guideId = Integer.parseInt(guideIdStr);

            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);
                Guide guide = guideMapper.selectById(guideId);

                if (guide != null) {
                    // 增加浏览次数
                    guideMapper.incrementViewCount(guideId);
                    sqlSession.commit();

                    // 返回攻略数据
                    StringBuilder json = new StringBuilder();
                    json.append("{\"success\":true,\"guide\":");
                    appendGuideJson(json, guide);
                    json.append("}");
                    out.write(json.toString());
                } else {
                    out.write("{\"success\":false,\"message\":\"攻略不存在\"}");
                }
            }
        } catch (Exception e) {
            out.write("{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            e.printStackTrace();
        }
    }

    private void appendGuideJson(StringBuilder json, Guide guide) {
        json.append("{")
                .append("\"guideId\":").append(guide.getGuideId()).append(",")
                .append("\"title\":\"").append(escapeJson(guide.getTitle())).append("\",")
                .append("\"content\":\"").append(escapeJson(guide.getContent())).append("\",")
                .append("\"imagePaths\":\"").append(escapeJson(guide.getImagePaths())).append("\",")
                .append("\"coverImage\":\"").append(escapeJson(guide.getCoverImage())).append("\",")
                .append("\"location\":\"").append(escapeJson(guide.getLocation())).append("\",")
                .append("\"season\":\"").append(escapeJson(guide.getSeason())).append("\",")
                .append("\"category\":\"").append(escapeJson(guide.getCategory())).append("\",")
                .append("\"priceRange\":\"").append(escapeJson(guide.getPriceRange())).append("\",")
                .append("\"viewCount\":").append(guide.getViewCount() != null ? guide.getViewCount() : 0).append(",")
                .append("\"favoriteCount\":").append(guide.getFavoriteCount() != null ? guide.getFavoriteCount() : 0)
                .append(",")
                .append("\"createdAt\":\"").append(guide.getCreatedAt() != null ? guide.getCreatedAt().getTime() : "")
                .append("\",")
                .append("\"author\":");

        User author = guide.getAuthor();
        if (author != null) {
            json.append("{")
                    .append("\"userId\":").append(author.getUserId()).append(",")
                    .append("\"username\":\"").append(escapeJson(author.getUsername())).append("\",")
                    .append("\"avatar\":\"").append(escapeJson(author.getAvatar())).append("\",")
                    .append("\"signature\":\"").append(escapeJson(author.getSignature())).append("\"")
                    .append("}");
        } else {
            json.append("null");
        }

        json.append("}");
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}