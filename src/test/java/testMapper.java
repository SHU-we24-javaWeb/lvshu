import com.lvshu.mapper.GuideMapper;
import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.Guide;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class testMapper {
    public static void main(String[] args) {
            //test1();
            test3();

    }

    public static void test1 (){
                // 获取搜索参数
                int guide_id = 1;

                // 使用MyBatis查询
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
                SqlSession sqlSession = sqlSessionFactory.openSession();
                GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);
                Guide guide = guideMapper.selectById(guide_id);
                System.out.println(guide);

                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
                User user = userMapper.selectById(1);
                System.out.println(user);
    }

    public static void test2(){
            String savePath = System.getProperty("user.dir");
            System.out.println(savePath);
    }

    public static void test3(){
        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 2.4 调用方法
        // 先去查user 因为登陆以后他只保存了简单的信息 全部是没有的 我们要获取这个userid就得弄好一整个user
        User user = userMapper.selectUserByUsername("小猪");
        System.out.println(user);
    }
}

