# Beyond Admin API Server

## Introduction

Beyond Admin API Server is an common admin app framework, you can build admin tools with this framework quickly!

The frontend project is: https://github.com/beyondops/beyond-admin-web

## Development

### Start MariaDB and Redis with docker

```shell
./start_mariadb_redis.sh

# Connect to mysql
docker exec -it beyond_mariadb mysql -ubeyondops -pbeyondops-dev beyondops

# Connect to redis
docker exec -it beyond_redis redis-cli

```

### Gradle Run

```shell
gradle bootRun
```

### Build

```
gradle build -x test
```

### Run

```shell
java -jar -Dspring.profiles.active=production build/libs/beyond-admin-server-0.0.1-SNAPSHOT.war
```

### API

- swagger: [http://localhost:8080/swagger-ui.html]


