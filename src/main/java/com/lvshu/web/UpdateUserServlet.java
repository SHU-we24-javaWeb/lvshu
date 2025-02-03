package com.lvshu.web;

import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/updateUserServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class UpdateUserServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 1.接收信息、

        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");  // 获取 user
        if (user == null) {
            // 如果用户没有登录（session 中没有 userId），可以重定向到登录页面
            resp.sendRedirect("login.html");
            return;
        }

        // 获取普通信息
        String username = req.getParameter("username");
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String phone = req.getParameter("mobilePhone");
        String nativePlace = req.getParameter("nativePlace");
        String major = req.getParameter("major");
        String signature = req.getParameter("signature");
        String dateOfBirthStr = req.getParameter("dateOfBirth");
        Date dateOfBirth = null;
        // String转date
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // 假设日期格式为 yyyy-MM-dd
                dateOfBirth = dateFormat.parse(dateOfBirthStr);  // 将字符串解析为日期
            } catch (ParseException e) {
                e.printStackTrace();  // 捕获并打印解析错误
            }
        }

        // 封装对象
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setGender(gender);
        newUser.setEmail(email);
        newUser.setMobilePhone(phone);
        newUser.setNativePlace(nativePlace);
        newUser.setMajor(major);
        newUser.setSignature(signature);
        newUser.setDateOfBirth(dateOfBirth);

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 查一下现在的user
        user = userMapper.selectUserByUsername(user.getUsername());

        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 先查一下username重复吗 重复就不用继续了
        if(!newUser.getUsername().equals(user.getUsername())){
            // 如果和自己是同一个就不用查了 如果不是的话才查
            User flagUser = userMapper.selectUserByUsername(newUser.getUsername());
            if(flagUser != null){
                // 用户名和别人重复了，弹出提示框
                writer.write("<html><body>");
                writer.write("<h1>用户名被别人占用了!</h1>");
                writer.write("<script>alert('用户名被别人占用了!'); window.history.back();</script>"); // 弹窗并返回上一步
                writer.write("</body></html>");
                sqlSession.close();
                return;
            }
        }

        newUser.setUserId(user.getUserId());

        // 处理头像图
        Part avatarImagePart = req.getPart("avatar");
        if (avatarImagePart != null) {
            // 图片没传才不更新
            // 文件名
            String avatarImage = "";
            // 文件路径
            String avatarPaths = "";
            if (avatarImagePart != null && avatarImagePart.getSize() > 0) {
                // 获取文件名
                avatarImage = getSubmittedFileName(avatarImagePart);
                // 保存文件，路径为 src/main/webapp/images 目录下
                String savePath = getServletContext().getRealPath("/images");
                File uploadDir = new File(savePath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();  // 如果目录不存在，则创建
                }
                // 文件写入
                avatarImagePart.write(savePath + File.separator + avatarImage);
                // 将封面图的相对路径保存到 coverPaths
                avatarPaths = "images/" + avatarImage; // 使用相对路径
            }
            newUser.setAvatar(avatarPaths);
        }


        System.out.print("newUser: " + newUser);

        // 更新信息
        int count = userMapper.update(newUser);
        System.out.println(count);
        // 3.判断是否成功
        if (count > 0) {
            // 更新成功，跳转到主页或其他页面
            writer.write("<html><body>");
            writer.write("<h1>更新成功！</h1>");
            writer.write("<script>window.alert(\"更新成功\");</script>");
            writer.write("<script>window.location.href='my.html';</script>"); // 发布成功后跳转
            writer.write("</body></html>");
            // 提交事务
            sqlSession.commit();
        } else {
            // 登陆失败，弹出提示框
            writer.write("<html><body>");
            writer.write("<h1>更新失败！</h1>");
            writer.write("<script>alert('更新失败！'); window.history.back();</script>"); // 弹窗并返回上一步
            writer.write("</body></html>");
        }

        // 释放资源
        sqlSession.close();

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
