#!/bin/bash

# Build the frontend first
echo "Building frontend..."
npm run build

# Copy frontend build to Spring Boot static resources
echo "Copying frontend to Spring Boot static resources..."
mkdir -p src/main/resources/static/client
cp -r client/dist/* src/main/resources/static/client/

# Run Spring Boot application
echo "Starting Spring Boot application..."
./mvnw spring-boot:run