# Flink

## 测试环境部署

版本信息

```
flink: 1.12.2
scala: 2.12.12
jdk: 8
```

create docker network

```
docker network create -d bridge flink
```

start flink

```
docker-compose -f flink-docker/docker-compose.yml up -d
```

启动flink-sql-client（如果需要）

```
docker-compose -f flink-sql-client-docker/docker-compose.yml up -d
```

进入sql-client shell

```
docker-compose exec sql-client bash -c "./sql-client.sh"
```

start mysql

```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 --network flink -d mysql
```
