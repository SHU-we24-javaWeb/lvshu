package com.lvshu.web;

import com.lvshu.mapper.UserFollowMapper;
import com.lvshu.mapper.UserMapper;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/loadUserList")
public class LoadUserListServlet extends HttpServlet {
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void init() throws ServletException {
        sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();

        // 1.接收信息
        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");  // 获取 user
        if (user == null) {
            // 如果用户没有登录（session 中没有 userId），可以重定向到登录页面
            resp.sendRedirect("login.html");
            return;
        }

        String type = req.getParameter("type"); // 'following' 或 'followers'
        if (type == null || type.trim().isEmpty()) {
            out.write("{\"success\":false,\"message\":\"缺少type\"}");
            return;
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        user = userMapper.selectUserByUsername(user.getUsername());

        UserFollowMapper userFollowMapper = sqlSession.getMapper(UserFollowMapper.class);


        List<User> userList = new ArrayList<>();
        if ("following".equals(type)) {
            userList = userFollowMapper.selectFollowings(user.getUserId()); // 获取关注的用户
        } else if ("followers".equals(type)) {
            userList = userFollowMapper.selectFollowers(user.getUserId()); // 获取粉丝
        }

        // 构建返回的 JSON 数据
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{\"success\":true, \"users\":[");

        // 遍历所有用户，构建用户信息
        for (int i = 0; i < userList.size(); i++) {
            User followUser = userList.get(i);
            if (i > 0) {
                jsonResponse.append(",");
            }

            jsonResponse.append("{")
                    .append("\"username\":\"").append(followUser.getUsername()).append("\",")
                    .append("\"gender\":\"").append(followUser.getGender() == null ? "未填写" : followUser.getGender()).append("\",")
                    .append("\"nativePlace\":\"").append(followUser.getNativePlace() == null ? "未填写" : followUser.getNativePlace()).append("\",")
                    .append("\"mobilePhone\":\"").append(followUser.getMobilePhone() == null ? "未填写" : followUser.getMobilePhone()).append("\",")
                    .append("\"email\":\"").append(followUser.getEmail() == null ? "未填写" : followUser.getEmail()).append("\",")
                    .append("\"major\":\"").append(followUser.getMajor() == null ? "未填写" : followUser.getMajor()).append("\",")
                    .append("\"avatar\":\"").append(followUser.getAvatar() == null ? "images/default-avatar.jpg" : followUser.getAvatar()).append("\"")
                    .append("}");
        }

        jsonResponse.append("]}");

        // 输出 JSON 响应
        out.write(jsonResponse.toString());
        out.flush();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
