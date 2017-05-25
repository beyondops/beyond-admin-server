#!/bin/bash

echo "Start MariaDB with docker"
MARIADB_NAME="beyond_mariadb"
mkdir -p .dev/$MARIADB_NAME
set +e
docker stop $MARIADB_NAME
docker rm -f $MARIADB_NAME
set -e

docker run \
--name $MARIADB_NAME \
-p 3306:3306 \
-v `pwd`/.dev/$MARIADB_NAME:/var/lib/mysql \
-e MYSQL_DATABASE=beyondops \
-e MYSQL_USER=beyondops \
-e MYSQL_PASSWORD=beyondops-dev \
-e MYSQL_ROOT_PASSWORD=beyondops-dev \
-d mariadb:10 \
--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

echo "Connect: docker exec -it $MARIADB_NAME mysql -ubeyondops -pbeyondops-dev beyondops"

echo "Start Redis with docker"
REDIS_NAME=beyond_redis
set +e
docker stop $REDIS_NAME
docker rm -f $REDIS_NAME
set -e

docker run -d --name $REDIS_NAME -p 6379:6379 redis

echo "Connect: docker exec -it $REDIS_NAME redis-cli"
