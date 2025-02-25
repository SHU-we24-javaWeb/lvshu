package com.lvshu.web;

import com.lvshu.mapper.GuideMapper;
import com.lvshu.pojo.Guide;
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
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/hotGuideServlet")
public class hotGuideServlet extends HttpServlet {
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void init() throws ServletException {
        sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        try {

            // 使用MyBatis查询
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);
                List<Guide> guides = guideMapper.selectHotGuide();

                // 构建JSON响应
                StringBuilder json = new StringBuilder();
                json.append("{\"success\":true,\"guides\":[");

                for (int i = 0; i < guides.size(); i++) {
                    if (i > 0)
                        json.append(",");
                    Guide guide = guides.get(i);
                    json.append("{")
                            .append("\"guideId\":").append(guide.getGuideId()).append(",")
                            .append("\"title\":\"").append(escapeJson(guide.getTitle())).append("\",")
                            .append("\"content\":\"").append(escapeJson(guide.getContent())).append("\",")
                            .append("\"coverImage\":\"").append(escapeJson(guide.getCoverImage())).append("\",")
                            .append("\"location\":\"").append(escapeJson(guide.getLocation())).append("\",")
                            .append("\"season\":\"").append(escapeJson(guide.getSeason())).append("\",")
                            .append("\"category\":\"").append(escapeJson(guide.getCategory())).append("\",")
                            .append("\"priceRange\":\"").append(escapeJson(guide.getPriceRange())).append("\",")
                            .append("\"viewCount\":").append(guide.getViewCount() != null ? guide.getViewCount() : 0)
                            .append(",")
                            .append("\"favoriteCount\":")
                            .append(guide.getFavoriteCount() != null ? guide.getFavoriteCount() : 0)
                            .append("}");
                }

                json.append("]}");
                System.out.println(json.toString());
                out.write(json.toString());
            }
        } catch (Exception e) {
            out.write("{\"success\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
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