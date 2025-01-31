# 项目部署与使用说明

## 环境准备

1. **安装 JDK**  
   确保你的机器上安装了 Java 开发工具包（JDK）。推荐版本：JDK 11 或更高。

2. **安装 Maven**  
   如果你还没有安装 Maven，请参考 [Maven 官方文档](https://maven.apache.org/install.html) 进行安装。

3. **安装 Tomcat**  
   如果使用本地 Tomcat 进行部署，请下载并安装 [Apache Tomcat](https://tomcat.apache.org/download-90.cgi)，解压后记住安装路径。

4. **安装 MySQL**  
   请确保已在本地或远程服务器安装 MySQL 数据库，并配置好数据库用户和权限。

## 项目部署步骤

### 1. 拉取项目代码
```bash
git clone <项目仓库地址>
```

### 2. 配置本地数据库
1. 在 MySQL 中创建数据库：
   ```sql
   CREATE DATABASE lvshu;
   ```
2. 导入项目中的 `sql` 文件到数据库：
   ```bash
   mysql -u <username> -p lvshu < path_to_sql_file
   ```
3. 修改 `src/main/resources/mybatis-config.xml` 中的数据库连接配置为你本地的 MySQL 配置。
   ```xml
   <configuration>
       <properties>
           <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
           <property name="url" value="jdbc:mysql://localhost:3306/lvshu?useSSL=false&amp;serverTimezone=UTC"/>
           <property name="username" value="root"/>
           <property name="password" value="password"/>
       </properties>
   </configuration>
   ```

### 3. 使用 Maven 构建项目
在终端中进入项目根目录，执行以下命令构建项目：
```bash
mvn clean install
```

### 4. 启动 Tomcat
#### (1)使用 Maven 插件启动 Tomcat

1. 运行 Maven 插件中的 Tomcat 7 插件：
   ```bash
   mvn tomcat7:run
   ```
2. 配置 Tomcat 启动时使用的端口号（默认 8080），你可以在 `pom.xml` 中修改：
   ```xml
   <configuration>
       <httpPort>8080</httpPort>
       <path>/</path>
   </configuration>
   ```
   这个你可以不配，只是说修改成80 和 / 以后访问路径变短

#### (2)使用本地 Tomcat 服务器

1. 将项目打包为 `.war` 文件：
   ```bash
   mvn clean package
   ```
2. 将 `.war` 文件部署到本地 Tomcat 的 `webapps` 目录下。
3. 启动 Tomcat，访问 `http://localhost:8080`。

### 5. 访问项目
1. 打开浏览器，访问 [http://localhost:8080](http://localhost:8080)。
2. 若修改端口为 80，则访问 [http://localhost](http://localhost)。

## 常见问题解决

- **数据库连接失败**  
  确保数据库已启动，且 `mybatis-config.xml` 中的数据库配置正确。

- **Tomcat 启动失败**  
  确保端口未被占用，检查 Tomcat 的 `logs` 目录中的错误日志。
