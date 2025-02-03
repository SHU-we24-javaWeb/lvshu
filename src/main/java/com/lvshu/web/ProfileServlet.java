package com.lvshu.web;

import com.lvshu.mapper.GuideMapper;
import com.lvshu.mapper.UserFavoriteMapper;
import com.lvshu.mapper.UserFollowMapper;
import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.Guide;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/profileServlet")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user"); // 获取 user
        if (user == null) {
            // 如果用户没有登录（session 中没有 userId），可以重定向到登录页面
            resp.sendRedirect("login.html");
            return;
        }

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        // UserMapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 收藏mapper
        UserFavoriteMapper userFavoriteMapper = sqlSession.getMapper(UserFavoriteMapper.class);
        // 关注和粉丝mapper
        UserFollowMapper userFollowMapper = sqlSession.getMapper(UserFollowMapper.class);
        // GuideMapper
        GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);

        // 更新user的信息
        user = userMapper.selectUserByUsername(user.getUsername());
        System.out.println(user);

        // 获取用户的各种数量
        // 收藏数量
        int countFavorite = userFavoriteMapper.countFavorites(user.getUserId());
        // 粉丝数
        int countFollowers = userFollowMapper.countFollowers(user.getUserId());
        // 关注数
        int countFollowing = userFollowMapper.countFollowings(user.getUserId());

        // 查询我的攻略
        List<Guide> myGuides = guideMapper.selectByAuthorId(user.getUserId());
        // 查询我的收藏
        List<Guide> myFavorites = userFavoriteMapper.selectFavoriteGuides(user.getUserId());

        // 构建JSON响应
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"success\":true,")
                .append("\"data\":{")
                .append("\"user\":");
        appendUserJson(json, user);
        json.append(",")
                .append("\"stats\":{")
                .append("\"favoriteCount\":").append(countFavorite).append(",")
                .append("\"followersCount\":").append(countFollowers).append(",")
                .append("\"followingCount\":").append(countFollowing)
                .append("},")
                .append("\"guides\":[");

        // 添加我的攻略
        for (int i = 0; i < myGuides.size(); i++) {
            if (i > 0)
                json.append(",");
            appendGuideJson(json, myGuides.get(i));
        }

        json.append("],")
                .append("\"favorites\":[");

        // 添加我的收藏
        for (int i = 0; i < myFavorites.size(); i++) {
            if (i > 0)
                json.append(",");
            appendGuideJson(json, myFavorites.get(i));
        }

        json.append("]")
                .append("}}");

        // 设置响应类型和编码
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
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

    private void appendUserJson(StringBuilder json, User user) {
        json.append("{")
                .append("\"userId\":").append(user.getUserId()).append(",")
                .append("\"username\":\"").append(escapeJson(user.getUsername())).append("\",")
                .append("\"avatar\":\"").append(escapeJson(user.getAvatar())).append("\",")
                .append("\"signature\":\"").append(escapeJson(user.getSignature())).append("\",")
                .append("\"gender\":\"").append(escapeJson(user.getGender())).append("\",")
                .append("\"email\":\"").append(escapeJson(user.getEmail())).append("\",")
                .append("\"mobilePhone\":\"").append(escapeJson(user.getMobilePhone())).append("\",")
                .append("\"major\":\"").append(escapeJson(user.getMajor())).append("\",")
                .append("\"nativePlace\":\"").append(escapeJson(user.getNativePlace())).append("\"")
                .append("}");
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
                .append("\"")
                .append("}");
    }
}