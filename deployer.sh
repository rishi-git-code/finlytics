#!/bin/bash

cd /home/ec2-user/finlytics || exit

echo "🔄 Pulling latest code..."
git pull origin main

echo "🔨 Building with Maven..."
mvn clean package -DskipTests

echo "🛑 Stopping old app..."
pkill -f 'finlytics-0.0.1-SNAPSHOT.jar'

echo "🚀 Starting new app..."
nohup java -jar target/finlytics-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "✅ Deployment complete!"
