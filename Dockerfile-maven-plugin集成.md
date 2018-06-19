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

## 更改仓库

默认是localhost:2375，可通过如下配置设为远程

编辑 /etc/profile

```text
sudo vim /etc/profile
```

添加如下环境变量

```text
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

ADD target/dockerfile-sample-0.0.1.jar ~/dockerfile-sample.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "~/dockerfile-sample.jar"]
```

### 运行构建

```text
mvn clean package -Ddockerfile.push.skip
```

### 查看镜像

```text
docker images
```

### 运行镜像

```text
docker run -d -p 8080:8080 --name hello dockerfile-sample/dockerfile-sample
```