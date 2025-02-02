package com.lvshu.web;

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
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/createServlet")
public class createServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 1.接收信息
        String title = req.getParameter("title");
        String location = req.getParameter("location");
        String tags = req.getParameter("tags");
        String category = req.getParameter("category");
        String season = req.getParameter("season");
        String price_range = req.getParameter("priceRange");
        String content = req.getParameter("content");
//        String image_paths = req.getParameter("images");
//        String cover_image = req.getParameter("coverImage");

        // 处理文件上传
        Part coverImagePart = req.getPart("coverImage");
        // 获取封面图片的路径或内容（例如文件名）
        String coverImage = coverImagePart != null ? coverImagePart.getSubmittedFileName() : "";

        // 处理上传的多张图片
        Part imagesPart = req.getPart("images");
        String images = imagesPart != null ? imagesPart.getSubmittedFileName() : "";

        System.out.println(title);
        System.out.println(location);
        System.out.println(tags);
        System.out.println(category);
        System.out.println(season);
        System.out.println(price_range);
        System.out.println(content);
        System.out.println(images);
        System.out.println(coverImage);

        // 封装对象
        Guide guide = new Guide();
        guide.setTitle(title);
        guide.setLocation(location);
        guide.setTags(tags);
        guide.setCategory(category);
        guide.setSeason(season);
        guide.setPriceRange(price_range);
        guide.setContent(content);
        guide.setImagePaths(images);
        guide.setCoverImage(coverImage);

        //打印信息
        System.out.println(guide);

//        // 2.调用MyBatis完成查询
//        // 这里直接去官网复制粘贴过来
//        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
//        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
//        // 2.2 获取SqlSession对象
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        // 2.3 获取Mapper
//        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//        // 2.4 调用方法
//        // 传入信息
//
//
//        // 2.5 释放资源
//        sqlSession.close();
//
//        // 获取字符输出流,并设置content type
//        resp.setContentType("text/html;charset=utf-8");
//        PrintWriter writer = resp.getWriter();
        // 3.判断user是否为null
//        if (user != null) {
//            // 登陆成功，跳转到主页或其他页面
//            writer.write("<html><body>");
//            writer.write("<h1>登录成功！</h1>");
//            writer.write("<script>window.alert(\"登陆成功\");</script>");
//            writer.write("<script>window.location.href='home.html';</script>"); // 登录成功后跳转
//            writer.write("</body></html>");
//        } else {
//            // 登陆失败，弹出提示框
//            writer.write("<html><body>");
//            writer.write("<h1>登录失败！</h1>");
//            writer.write("<script>alert('用户名或密码错误！'); window.history.back();</script>"); // 弹窗并返回上一步
//            writer.write("</body></html>");
//        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    public String encodeString(String str) {
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}
