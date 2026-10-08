# 星屿空间（QZone Space）

一个适合学习前后端分离的轻量动态相册网站源码。它实现注册、登录、发布图文动态、删除本人动态、点赞、评论和本地图片上传。

## 技术栈

- 前端：Vue 3、Vite、Element Plus、Axios
- 后端：Java 17、Spring Boot 3、Spring Data JPA、Spring Session Data Redis
- 数据：MySQL 8、Redis 7

Redis 保存登录 Session，并缓存未登录访问的动态列表 60 秒；用户执行发布、删除、点赞或评论时会自动清除缓存。

## 启动前准备

准备本地运行的 MySQL 与 Redis：

```sql
CREATE DATABASE qzone DEFAULT CHARACTER SET utf8mb4;
```

执行 `database/init.sql`。默认后端连接：MySQL `localhost:3306/qzone`（用户名 `qzone`、密码 `qzone123`）和 Redis `localhost:6379`；可用环境变量覆盖：

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/qzone
SPRING_DATASOURCE_USERNAME=你的用户名
SPRING_DATASOURCE_PASSWORD=你的密码
SPRING_DATA_REDIS_HOST=localhost
```

## 运行

先启动后端：

```bash
cd backend
mvn spring-boot:run
```

另开终端启动前端：

```bash
cd frontend
npm install
npm run dev
```

访问终端显示的地址（通常是 `http://localhost:5173`）。Vite 已将 `/api` 和 `/uploads` 代理到后端 `http://localhost:8080`。

## 目录

```text
backend/      Spring Boot REST API
frontend/     Vue 单页应用
database/     MySQL 初始化 SQL
```

上传的图片默认保存在后端工作目录下的 `uploads/`，后续部署时可将该目录挂载为持久化卷。

## 无 MySQL/Redis 的本地接口验证

仅用于验证源码时，可使用内存 H2 数据库与内存 Session/缓存：

```bash
cd backend
mvn -Plocal-test spring-boot:run -Dspring-boot.run.profiles=test
```

该模式的数据在进程关闭后会清空，不能替代正式的 MySQL 与 Redis 配置。
