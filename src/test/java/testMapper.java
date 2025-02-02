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
}
