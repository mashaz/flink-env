# Flink

## 测试环境部署

版本信息

```
flink: 1.11.2
scala: 2.12.12
jdk: 8
```

create docker network

```
docker network create -d bridge flink
```

start flink

```
docker-compose flink-docker/docker-compose.yml up -d
```



