#!/bin/bash

echo "Starting KeyCraft Spring Boot Application..."

# Copy frontend static files to Spring Boot resources
mkdir -p src/main/resources/static
cp -r client/* src/main/resources/static/

# Start Spring Boot application
mvn spring-boot:run