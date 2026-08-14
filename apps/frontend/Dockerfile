# =========================================================================
# Stage 1: Build React Vite SPA Production Bundle
# =========================================================================
FROM node:20-alpine AS builder
WORKDIR /app

# Copy package dependencies and install
COPY package.json package-lock.json* ./
RUN npm ci

# Copy source files and build
COPY . .
RUN npm run build

# =========================================================================
# Stage 2: Serve Bundle with Nginx Alpine
# =========================================================================
FROM nginx:alpine AS runner

# Copy custom Nginx configuration
COPY deployment/docker/nginx.conf /etc/nginx/conf.d/default.conf

# Copy compiled static assets from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
