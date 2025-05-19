# -------- builder --------
FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration=production

# -------- runtime --------
FROM nginx:stable-alpine
COPY --from=builder /app/dist/jang-front/browser/ /usr/share/nginx/html
EXPOSE 80
