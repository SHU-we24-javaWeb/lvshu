package com.lvshu.web;

import com.lvshu.mapper.UserFavoriteMapper;
import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.User;
import com.lvshu.pojo.UserFavorite;
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

@WebServlet("/favoriteServlet")
public class FavoriteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 1.接收信息

        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user"); // 获取 user
        if (user == null) {
            // 如果用户没有登录（session 中没有 userId），可以重定向到登录页面
            resp.sendRedirect("login.html");
            return;
        }

        // 获取攻略ID（先尝试从URL参数获取，如果没有再从表单参数获取）
        String guideIdStr = req.getParameter("id");
        if (guideIdStr == null || guideIdStr.trim().isEmpty()) {
            guideIdStr = req.getParameter("guideId");
        }

        System.out.println("(FavoriteServlet)Received request for guide ID: " + guideIdStr);
        int guideId;
        try {
            guideId = Integer.parseInt(guideIdStr);
        } catch (NumberFormatException e) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"success\":false,\"message\":\"无效的攻略ID\"}");
            return;
        }

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        UserFavoriteMapper userFavoriteMapper = sqlSession.getMapper(UserFavoriteMapper.class);
        // 2.4 调用方法
        // 先去查user 因为登陆以后他只保存了简单的信息 全部是没有的 我们要获取这个userid就得弄好一整个user
        user = userMapper.selectUserByUsername(user.getUsername());
        int userId = user.getUserId();
        System.out.println(user);

        // 封装数据
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUserId(userId);
        userFavorite.setGuideId(guideId);

        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        if (!userFavoriteMapper.isFavorited(userId, guideId)) {
            // 没有收藏过
            // 插入收藏信息
            int count = userFavoriteMapper.insert(userFavorite);

            if (count > 0) {
                // 成功
                writer.write("<html><body>");
                writer.write("<h1>收藏成功！</h1>");
                writer.write("<script>window.alert('收藏成功');</script>");
                writer.write("<script>window.history.back();</script>");
                writer.write("</body></html>");
                // 提交
                sqlSession.commit();
            } else {
                // 失败
                writer.write("<html><body>");
                writer.write("<h1>收藏失败！</h1>");
                writer.write("<script>window.alert('收藏失败');</script>");
                writer.write("<script>window.history.back();</script>");
                writer.write("</body></html>");
            }
        } else {
            // 收藏过了
            writer.write("<html><body>");
            writer.write("<h1>已经收藏过！</h1>");
            writer.write("<script>window.alert('已经收藏过此攻略！');</script>");
            writer.write("<script>window.history.back();</script>");
            writer.write("</body></html>");
        }


        // 释放资源
        sqlSession.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
