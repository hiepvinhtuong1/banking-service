# Stage 1: Build React Vite app
FROM node:20-alpine AS build
WORKDIR /app

# Copy package.json + lock file
COPY package.json yarn.lock* package-lock.json* ./

# Cài dependencies (ưu tiên yarn nếu có)
RUN if [ -f yarn.lock ]; then yarn install --frozen-lockfile; \
    elif [ -f package-lock.json ]; then npm ci --legacy-peer-deps; \
    else npm install --legacy-peer-deps; fi

COPY . .

RUN npm run build

# Stage 2: Serve bằng Nginx
FROM nginx:1.25-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
