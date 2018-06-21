# Dockerfile-maven-plugin集成

## Docker 开启远程访问

### centos6

修改/etc/default/docker文件，重启后生效（service docker restart）。

```text
DOCKER_OPTS="-H=unix:///var/run/docker.sock -H=0.0.0.0:2375"
```

### centos7

打开/usr/lib/systemd/system/docker.service文件，修改ExecStart这行。

```text
ExecStart=/usr/bin/dockerd  -H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock
```

重启后生效

```text
  systemctl daemon-reload
  systemctl restart docker.service
```

测试是否生效

```text
curl http://127.0.0.1:2375/info
```

详见[出处](https://www.cnblogs.com/java-my-life/p/7001998.html)

### ubuntu

创建/etc/systemd/system/docker.service.d目录。

```text
sudo mkdir /etc/systemd/system/docker.service.d
```

创建一个/etc/systemd/system/docker.service.d/http-proxy.conf文件

```text
sudo vim /etc/systemd/system/docker.service.d/http-proxy.conf

[Service]

ExecStart=

ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
```

刷新配置

```text
sudo systemctl daemon-reload
```

重新启动docker守护进程

```text
sudo systemctl restart docker
```

测试是否生效

```text
curl http://127.0.0.1:2375/info
```

详见[出处](https://blog.csdn.net/csde12/article/details/70240721)

## How can I use docker without sudo

* Add the docker group if it doesn't already exist:

```text
sudo groupadd docker
```

* Add the connected user "$USER" to the docker group. Change the user name to match your preferred user if you do not want to use your current user:

```text
sudo gpasswd -a $USER docker
```

* Either do a newgrp docker or log out/in to activate the changes to groups.

* Check if you can run docker without sudo.

```text
docker ps
```

详见[出处](https://askubuntu.com/questions/477551/how-can-i-use-docker-without-sudo)

## Dockerfile-maven-plugin

[Github地址](https://github.com/spotify/dockerfile-maven)

[Github示例](https://github.com/spotify/dockerfile-maven/tree/master/plugin/src/it)

## Docker Grammar

[Dockerfile reference地址](https://docs.docker.com/engine/reference/builder/)

[Compose file reference](https://docs.docker.com/compose/compose-file/)

## 更改仓库

默认是localhost:2375，可通过如下配置设为远程

编辑 /etc/profile

```text
sudo vim /etc/profile
```

添加如下环境变量

```text
#export DOCKER_HOST=tcp://127.0.0.1:2375
#export DOCKER_HOST=tcp://192.168.100.135:2375
export DOCKER_HOST=tcp://118.24.149.18:2375
```

使当前terminal生效，或者重启使所有生效，然后执行构建过程

```text
source /etc/profile
```

## Permission Deny -> 改变目录权限

```text
chmod 777 /var/run/docker.sock
或者(如果使用jenkins)
usermod -a -G docker jenkins
```

详见[出处](https://www.jianshu.com/p/db58d1cfd01b)，自测前者生效

## 项目配置

### 添加依赖

```text
<dockerfile-maven-version>1.3.7</dockerfile-maven-version>
<docker.image.prefix>somePrefix</docker.image.prefix>

<plugin>
  <groupId>com.spotify</groupId>
  <artifactId>dockerfile-maven-plugin</artifactId>
  <version>${dockerfile-maven-version}</version>
  <executions>
    <execution>
      <id>default</id>
      <goals>
        <goal>build</goal>
        <goal>tag</goal>
        <goal>push</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <repository>${docker.image.prefix}/${project.artifactId}</repository>
    <tag>${project.version}</tag>
    <buildArgs>
      <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
    </buildArgs>
  </configuration>
</plugin>
```

### 创建Dockerfile

```text
FROM openjdk:8-jre
MAINTAINER LuckyTerry <tcw1018498538@gmail.com>
ENTRYPOINT ["java", "-jar", "~/dockerfile-sample.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} ~/dockerfile-sample.jar

EXPOSE 8080
```

### 运行构建

```text
mvn clean package -Ddockerfile.push.skip

mvn clean package -Ddockerfile.push.skip -Dmaven.test.skip=true
```

### 查看镜像

```text
docker images
```

### 运行镜像

```text
docker run -d -p 8080:8080 --name hello dockerfile-sample/dockerfile-sample
```

## 聚合支付项目

### 相关镜像全部打好

```text
mvn clean compile
mvn package
```

### 创建小网(默认使用桥接方式)

```text
#创建命令
docker network create --driver bridge holder-network

#检视创建的网络信息
docker network inspect holder-network
```

### 确保 Nginx、Mysql、Redis、RabbitMq 等都在 holder-network网络下

### 创建Container并加入小网（通过容器名进行通信）

```text
docker run -d -p 9411:9411 --name fmbk-zipkin --network holder-network -it openzipkin/zipkin:latest
docker run -d -p 9011:9011 --name fmbk-zuul --network holder-network -it payment-fmbk/payment-fmbk-zuul:1.0.0
docker run -d -p 8761:8761 --name fmbk-registry --network holder-network -it payment-fmbk/payment-fmbk-registry:1.0.0
docker run -d -p 8762:8762 --name fmbk-aggregation --network holder-network -it payment-fmbk/payment-fmbk-aggregation:1.0.0
docker run -d -p 8763:8763 --name fmbk-mchnt --network holder-network -it payment-fmbk/payment-fmbk-mchnt:1.0.0
docker run -d -p 8764:8764 --name fmbk-trading --network holder-network -it payment-fmbk/payment-fmbk-trading:1.0.0
docker run -d -p 8765:8765 --name fmbk-refund --network holder-network -it payment-fmbk/payment-fmbk-refund:1.0.0
```

或者分两步，如下文所示。

### 运行容器

```text
docker run -d -p 9411:9411 --name fmbk-zipkin -it openzipkin/zipkin:latest
docker run -d -p 9011:9011 --name fmbk-zuul -it payment-fmbk/payment-fmbk-zuul:1.0.0
docker run -d -p 8761:8761 --name fmbk-registry -it payment-fmbk/payment-fmbk-registry:1.0.0
docker run -d -p 8762:8762 --name fmbk-aggregation -it payment-fmbk/payment-fmbk-aggregation:1.0.0
docker run -d -p 8763:8763 --name fmbk-mchnt -it payment-fmbk/payment-fmbk-mchnt:1.0.0
docker run -d -p 8764:8764 --name fmbk-trading -it payment-fmbk/payment-fmbk-trading:1.0.0
docker run -d -p 8765:8765 --name fmbk-refund -it payment-fmbk/payment-fmbk-refund:1.0.0
```

### 加入小网

```text
docker network connect holder-network fmbk-zipkin
docker network connect holder-network fmbk-zuul
docker network connect holder-network fmbk-registry
docker network connect holder-network fmbk-aggregation
docker network connect holder-network fmbk-mchnt
docker network connect holder-network fmbk-trading
docker network connect holder-network fmbk-refund
```

### 脱离网络

```text
docker network disconnect holder-network fmbk-zipkin
docker network disconnect holder-network fmbk-zuul
docker network disconnect holder-network fmbk-registry
docker network disconnect holder-network fmbk-aggregation
docker network disconnect holder-network fmbk-mchnt
docker network disconnect holder-network fmbk-trading
docker network disconnect holder-network fmbk-refund
```

### 查看日志

```text
docker logs -f fmbk-zipkin
docker logs -f fmbk-zuul
docker logs -f fmbk-registry
docker logs -f fmbk-aggregation
docker logs -f fmbk-mchnt
docker logs -f fmbk-trading
docker logs -f fmbk-refund
```

### 移除容器

```text
docker rm -f fmbk-zipkin
docker rm -f fmbk-zuul
docker rm -f fmbk-registry
docker rm -f fmbk-aggregation
docker rm -f fmbk-mchnt
docker rm -f fmbk-trading
docker rm -f fmbk-refund
```

### 移除镜像

```text
docker rmi payment-fmbk/payment-fmbk-zuul:1.0.0
docker rmi payment-fmbk/payment-fmbk-registry:1.0.0
docker rmi payment-fmbk/payment-fmbk-aggregation:1.0.0
docker rmi payment-fmbk/payment-fmbk-mchnt:1.0.0
docker rmi payment-fmbk/payment-fmbk-trading:1.0.0
docker rmi payment-fmbk/payment-fmbk-refund:1.0.0
```

## Docker-compose

* 前台启动容器

```text
docker-compose up
```

* 关闭容器，但不移除

```text
ctrl+c
```

* 后台启动容器

```text
docker-compose up -d
```

* 关闭并移除容器

```text
docker-compose down
```

## 相关博客

### 使用DockerFile maven插件自动化部署

[Spring Boot使用DockerFile maven插件自动化部署](https://blog.csdn.net/liubingyu12345/article/details/79015966)

[Spring Boot使用DockerFile maven插件自动化部署之容器通信](https://blog.csdn.net/liubingyu12345/article/details/79021287)

### 使用Jenkins自动化部署

[Jenkins+Docker自动化部署Spring boot项目 （一）概述](https://blog.csdn.net/liubingyu12345/article/details/80703491)

[Jenkins+Docker自动化部署Spring boot项目 （三）搭建jenkins](https://blog.csdn.net/liubingyu12345/article/details/80737412)

### Docker官网

[网络配置](https://docs.docker.com/network/)

[桥接模式](https://docs.docker.com/network/bridge/)