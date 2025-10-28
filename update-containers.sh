#!/bin/bash
# EC2 Container Update Script
# Run this on the EC2 instance to pull latest images and restart containers

echo "Pulling latest frontend image..."
docker pull palanikalyan27/kmato-frontend:latest

echo "Pulling latest backend image..."
docker pull palanikalyan27/kmato-backend:latest

echo "Restarting frontend container..."
docker stop kmato-frontend || true
docker rm kmato-frontend || true
docker run -d \
  --name kmato-frontend \
  --restart unless-stopped \
  -p 80:80 \
  palanikalyan27/kmato-frontend:latest

echo "Restarting backend container..."
docker stop kmato-backend || true
docker rm kmato-backend || true
docker run -d \
  --name kmato-backend \
  --restart unless-stopped \
  -p 8081:8081 \
  -e JWT_SECRET="your-secret-key-min-256-bits-change-this-in-production" \
  -e SPRING_DATASOURCE_URL="jdbc:h2:file:./data/fooddelivery;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE" \
  -e SPRING_DATASOURCE_USERNAME="sa" \
  -e SPRING_DATASOURCE_PASSWORD="" \
  palanikalyan27/kmato-backend:latest

echo "Done! Containers restarted with latest images."
docker ps
