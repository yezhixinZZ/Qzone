# =========================
# 第一阶段：构建 Vue
# =========================
FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build


# =========================
# 第二阶段：Nginx
# =========================
FROM nginx:1.31.2-alpine

# 明确使用 root 启动 Nginx
USER root

COPY --from=build /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]