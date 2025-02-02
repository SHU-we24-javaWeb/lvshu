package com.lvshu.web;

import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.Guide;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/createServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
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

        // TODO 我没有解决图片里带中文名字的问题 虽然可以存 但是没有试过可以用嘛

        // 处理封面图
        Part coverImagePart = req.getPart("coverImage");
        String coverImage = "";
        if (coverImagePart != null && coverImagePart.getSize() > 0) {
            // 获取文件名
            coverImage = getSubmittedFileName(coverImagePart);
            // 保存文件，路径为 src/main/webapp/images 目录下
            String savePath = getServletContext().getRealPath("/images");
            File uploadDir = new File(savePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // 如果目录不存在，则创建
            }
            // 文件写入
            coverImagePart.write(savePath + File.separator + coverImage);
        }
        // 将封面图的相对路径保存到 coverPaths
        String coverPaths = "/images/" + coverImage; // 使用相对路径


        // 处理多图上传
        List<Part> imageParts = req.getParts().stream()
                .filter(part -> "images".equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());
        List<String> imageNames = new ArrayList<>();
        for (Part imagePart : imageParts) {
            String fileName = getSubmittedFileName(imagePart);
            imageNames.add(fileName);  // 添加文件名到列表

            // 保存文件，路径为 src/main/webapp/images 目录下
            String savePath = getServletContext().getRealPath("/images");
            File uploadDir = new File(savePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // 如果目录不存在，则创建
            }
            imagePart.write(savePath + File.separator + fileName);

            // 将相对路径添加到 imageNames 中
            imageNames.set(imageNames.size() - 1, "/images/" + fileName);  // 更新最后一个路径为相对路径
        }

        // 将多张图片的路径以逗号分隔开
        String imagesPaths = String.join(",", imageNames);

        // 封装对象
        Guide guide = new Guide();
        guide.setTitle(title);
        guide.setLocation(location);
        guide.setTags(tags);
        guide.setCategory(category);
        guide.setSeason(season);
        guide.setPriceRange(price_range);
        guide.setContent(content);
        guide.setImagePaths(imagesPaths);
        guide.setCoverImage(coverPaths);

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

    // 添加文件名解析方法
    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                // 处理文件名的编码，确保中文字符正确显示
                fileName = this.encodeString(fileName);
                return fileName;
            }
        }
        return null;
    }



    public String encodeString(String str) {
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}
