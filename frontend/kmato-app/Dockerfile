# Stage 1: build
FROM node:18-alpine AS build
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm ci
COPY . .
RUN npm run build -- --output-path=dist/kmato-app

# Stage 2: serve with nginx
FROM nginx:alpine
COPY --from=build /app/dist/kmato-app /usr/share/nginx/html
EXPOSE 80
CMD ["/bin/sh","-c","nginx -g 'daemon off;' "]
