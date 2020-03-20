# CentOS安装MongoDB

> 通常由 tar.gz、yum 和 docker 安装，推荐 yum 安装

## 压缩包安装

略

## yum 安装（推荐）

详细安装：[Centos安装社区版](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-red-hat/)

快速安装

Configure the package management system (yum)

    Create a /etc/yum.repos.d/mongodb-org-4.2.repo file so that you can install MongoDB directly using yum:
    
    ```bash
    sudo vim /etc/yum.repos.d/mongodb-org-4.2.repo
    
    [mongodb-org-4.2]
    name=MongoDB Repository
    baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.2/x86_64/
    gpgcheck=1
    enabled=1
    gpgkey=https://www.mongodb.org/static/pgp/server-4.2.asc
    ```

Install the MongoDB packages.

    ```bash
    sudo yum install -y mongodb-org
    ```
    
    如果安装速度慢，中断重新开始
    
Procedure

    ```bash
    sudo systemctl start mongod
    #sudo systemctl daemon-reload #如果start报错
    sudo systemctl status mongod
    sudo systemctl enable mongod
    
    sudo systemctl stop mongod
    sudo systemctl restart mongod
    ```
    
如果是阿里云：配置绑定IP

    ```bash
    sudo vim /etc/mongod.conf
    
    注释
    net:
      bindIp: 127.0.0.1
    改为
    net:
      bindIpAll: true
    ```

如果是阿里云：配置ecs安全组

    略

## centos docker 安装

下载镜像

```
docker search mongo
docker pull mongo
```

创建目录

```
mkdir /home/app/mongo
mkdir /home/app/mongo/db
```

运行镜像

```
docker run \
-p 27017:27017 \
-v /home/app/mongo/db:/data/db \
-v /etc/localtime:/etc/localtime:ro \
-v /etc/timezone:/etc/timezone:ro \
-d mongo
```

如果是阿里云：新增安全组Mongo规则

![阿里云安全组Mongo规则](https://note.youdao.com/yws/res/3633/202632AF99CB4F66A8D496936532A26C)

## 添加管理员

创建管理眼

```
mongo 
> use admin
> db.createUser({user:"admin",pwd:"LuckyTerry0108",roles:["root"]})
```

开启认证

```xml
sudo vim /etc/mongod.conf 

# 开启认证
security:
  authorization: enabled
```

## 开始使用

基础命令

```bash
mongo 
mongo --host localhost --port 27017
mongo -u "admin" -p "LuckyTerry0108" --authenticationDatabase "admin"

help
show dbs
use alibaba
show tables / show collections
db.collName.save({name: 'terry'})
```

## Reference Link

[使用 Docker 安装 MongoDB](https://blog.csdn.net/weixin_42902669/article/details/90178702)