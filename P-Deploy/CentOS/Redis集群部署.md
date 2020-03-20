# Redis集群部署

## 手动部署

略

## 本地Docker部署

> docker镜像 [grokzen/redis-cluster](https://hub.docker.com/r/grokzen/redis-cluster)

> github地址 [docker-redis-cluster](https://github.com/Grokzen/docker-redis-cluster)

### 步骤

1. 下载 github 仓库

    ```bash
    git clone git@github.com:Grokzen/docker-redis-cluster.git
    ```

2. 修改 docker-compose.yml

    设置版本为 4.0.14

3. 启动容器

    ```bash
    make up
    ```

4. 停止容器

    ```bash
    make down
    ``` 

5. 使用 redis-cli 连接

    ```bash
    docker-compose exec redis-cluster /redis/src/redis-cli -c -p 7000
    ```

    PS：官方提供了如下命令

    ```bash
    make cli
    ```

    实际上执行的是

    ```bashs
    docker-compose exec redis-cluster /redis/src/redis-cli -p 7000
    ```

    由于没有开启集群模式（-c），所以无法使用

6. 检查集群状态

    ```bash
    redis-cli -h 118.24.149.18 -p 7000 cluster nodes
    ```

### ISSUE

1. redis操作key时出现以下错误

    ```bash
    (error) MOVED 16265 172.19.0.2:7002
    ```

    **原因**

    这种情况一般是因为启动redis-cli时没有设置集群模式所导致。

    **解决方案**

    启动时使用-c参数来启动集群模式，命令如下：

    ```bash
    redis-cli -c -p 7000
    ```

2. redis操作key时出现以下错误

    ```bash
    -> Redirected to slot [1786] located at 172.19.0.2:7000
    Could not connect to Redis at 172.19.0.2:7000: No route to host
    ```

    **原因**

    该方案仅适用于本地部署

    **解决方案**

   部署在自己电脑上
   
3. 错误：ERR CROSSSLOT Keys in request don't hash to the same slot

    原因：

    在集群中，key会被划分到不同的槽中。不同的节点会拥有散列槽的一个子集。
    
    多个key的操作、事务或者lua脚本调用多个key是允许的，只要所有被调用的key都在一个节点的hash槽中就可以。
    
    redis集群实现了所有非分布式版本的单key命令。复杂命令的执行像set类型联合或者插入也是可以实现的，只要保证所有的key都属于单个节点。
    
    你可以使用Hash Tags强制所有的key属于一个节点。
    
    解决：
    
    要解决此错误，请使用哈希标签强制将密钥放入相同的哈希槽。当密钥包含“{...}”这种样式时，只有大括号“{”和“}”之间的子字符串得到哈希以获得哈希槽。
    
    例如，密钥 {user1}:myset 和 {user1}:myset2 被哈希到相同的哈希槽，因为只有大括号“{”和“}”内的字符串，即“user1”，用于计算哈希槽
    
    **特别地，Lua script 的入参KEYS也要求Hash到同一个Slot**
    
4. 错误： Lua script attempted to access a non local key in a cluster node

    原因：lua脚本的**入参KEYS**和**脚本使用到的KEY**都必须Hash到同一个Slot，所以这是个非常严格的限制。
    
    解决：使用HashTag

## 远程Docker部署

[Docker Redis 5.0 集群（cluster）搭建](https://juejin.im/post/5c9ca08f5188252d5a14a31b)

### 一、准备工具

安装docker（来自官网）

(1) 安装所需的软件包

```bash
sudo yum install -y yum-utils \ device-mapper-persistent-data \ lvm2
```

(2) 使用以下命令来设置稳定的存储库

```bash
sudo yum-config-manager \ --add-repo \ https://download.docker.com/linux/centos/docker-ce.repo
```

(3) 安装docker ce(docker社区版)

```bash
sudo yum install docker-ce
```

(4) 启动docker

```bash
sudo systemctl start docker
```

在docker库获取redis镜像（截至2019-03-27，最新版为5.0.4）

```bash
docker pull redis
```

至此，docker上redis cluster所有工具准备完毕，我们在命令行上输入docker images，就可以查看到已经安装的镜像

### 二、 集群搭建

#### 创建Redis 容器

（1）创建redis配置文件（redis-cluster.tmpl）

我在路径/root下创建一个文件夹redis-cluster,在路径/root/redis-cluster下创建一个文件redis-cluster.tmpl，并把以下内容复制过去。（注：路径可自定义，我用的是/root/redis-cluster）

```bash
port ${PORT}                                       ##节点端口
protected-mode no                                  ##开启集群模式
cluster-enabled yes                                ##cluster集群模式
cluster-config-file nodes.conf                     ##集群配置名
cluster-node-timeout 5000                          ##超时时间
cluster-announce-ip 118.24.149.18                  ##这里用腾讯云外网IP
cluster-announce-port ${PORT}                      ##节点映射端口
cluster-announce-bus-port 1${PORT}                 ##节点总线端口
appendonly yes                                     ##持久化模式
```

通过命令，可查看

```
[root@node-01 redis-cluster]# cat /root/redis-cluster/redis-cluster.tmpl
port ${PORT}
protected-mode no
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip 118.24.149.18
cluster-announce-port ${PORT}
cluster-announce-bus-port 1${PORT}
appendonly yes
```

备注：此模版文件为集群节点通用文件  其中${PORT} 将读取命令行变量  ip则根据网卡分配ip进行替换  以保证节点配置文件除端口以及ip 全部一致。

(2) 创建自定义network

```bash
docker network create redis-net
```

docker network ls ##查看网卡信息

```bash
[root@VM_0_6_centos ~]# docker network ls 

NETWORK ID NAME DRIVER SCOPE 7f804fa5fbd2 redis-net bridge local
```

备注：创建redis-net虚拟网卡 目的是让docker容器能与宿主（centos7）桥接网络 并间接与外界连接

(3) 查看redis-net虚拟网卡网关ip

```
[root@node-01 conf]# docker network inspect redis-net | grep "Gateway" | grep --color=auto -P '(\d{1,3}.){3}\d{1,3}'

                                        "Gateway": "172.20.0.1"
```

备注：docker network inspect network-name 显示 network-name对应配置信息（gerp 过滤网关配置行 并筛选ip ） 可人工

(4) 在/root/redis-cluster下生成conf和data目标，并生成配置信息

```bash
 $ for port in `seq 7000 7005`; do \
  mkdir -p ./${port}/conf \
  && PORT=${port} envsubst < ./redis-cluster.tmpl > ./${port}/conf/redis.conf \
  && mkdir -p ./${port}/data; \
  done
```

共生成6个文件夹，从7000到7005，每个文件夹下包含data和conf文件夹，同时conf里面有redis.conf配置文件

(5) 创建6个redis容器

```bash
$ for port in `seq 7000 7005`; do \
docker run -d -ti -p ${port}:${port} -p 1${port}:1${port} \
-v /root/redis-cluster/${port}/conf/redis.conf:/usr/local/etc/redis/redis.conf \
-v /root/redis-cluster/${port}/data:/data \
--restart always --name redis-${port} --net redis-net \
--sysctl net.core.somaxconn=1024 redis redis-server /usr/local/etc/redis/redis.conf; \
done
```

备注：命令译为  循环7010 - 7015  运行redis 容器
docker  run            运行
-d                          守护进程模式
--restart always     保持容器启动
--name redis-710* 容器起名
--net redis-net    容器使用虚拟网卡
-p                        指定宿主机器与容器端口映射 701*:701*
-P                        指定宿主机与容器redis总线端口映射 1701*:1701*
--privileged=true -v /root/redis-cluster/701*/conf/redis.conf:/usr/local/etc/redis/redis.conf
付权将宿主701*节点文件挂载到容器/usr/local/etc/redis/redis.conf 文件中
--privileged=true -v /root/redis-cluster/${port}/data:/data \
付权将宿主701*/data目录挂载到容器/data目录中
--sysctl net.core.somaxconn=1024 redis redis-server /usr/local/etc/redis/redis.conf;
容器根据挂载的配置文件启动 redis服务端


（6）、通过命令docker ps可查看刚刚生成的6个容器信息

```bash
[root@node-01 redis-cluster]# docker ps
CONTAINER ID        IMAGE                                         COMMAND                  CREATED             STATUS                PORTS                                                              NAMES
15c479074b87        redis                                         "docker-entrypoint..."   10 seconds ago      Up 9 seconds          0.0.0.0:7005->7005/tcp, 6379/tcp, 0.0.0.0:17005->17005/tcp         redis-7005
45bec33c2c35        redis                                         "docker-entrypoint..."   11 seconds ago      Up 10 seconds         0.0.0.0:7004->7004/tcp, 6379/tcp, 0.0.0.0:17004->17004/tcp         redis-7004
45482d9e5bb8        redis                                         "docker-entrypoint..."   11 seconds ago      Up 10 seconds         0.0.0.0:7003->7003/tcp, 6379/tcp, 0.0.0.0:17003->17003/tcp         redis-7003
f633d5c767c9        redis                                         "docker-entrypoint..."   11 seconds ago      Up 10 seconds         0.0.0.0:7002->7002/tcp, 6379/tcp, 0.0.0.0:17002->17002/tcp         redis-7002
eefc0d49fedf        redis                                         "docker-entrypoint..."   11 seconds ago      Up 10 seconds         0.0.0.0:7001->7001/tcp, 6379/tcp, 0.0.0.0:17001->17001/tcp         redis-7001
58b311e5dbcb        redis                                         "docker-entrypoint..."   12 seconds ago      Up 11 seconds         0.0.0.0:7000->7000/tcp, 6379/tcp, 0.0.0.0:17000->17000/tcp         redis-7000  
```

### 三、启动集群

进入一个节点

```bash
docker exec -it redis-7000 bash
```

使用Redis 5创建集群，redis-cli只需键入：

```bash
redis-cli --cluster create 172.20.0.2:7000 172.20.0.3:7001 172.20.0.4:7002 172.20.0.5:7003 172.20.0.6:7004 172.20.0.7:7005 --cluster-replicas 1
```

至此，集群已经创建好了。

```bash
[root@node-01 data]# docker exec -it redis-7000 bash
root@600aa2f84dfb:/data# redis-cli --cluster create 172.20.0.2:7000  172.20.0.3:7001  172.20.0.4:7002  172.20.0.5:7003  172.20.0.6:7004  172.20.0.7:7005 --cluster-replicas 1
```

### 四、其它注意事项

宿主主机对外开放ip

```bash
for port in `seq 7000 7005`; do \  
firewall-cmd --zone=public --add-port=${port}/tcp --permanent  
done
#重新载入
firewall-cmd --reload
```
需要暂停容器并删除容器 以便重复6步骤

```bash
for port in `seq 7000 7005`; do \
  docker stop redis-${port};
  docker rm redis-${port};
done
```

### 五、配置腾讯云安全组

（1）添加安全组

（2）关联实例

（3）添加如下入站规则

- 0.0.0.0/0 TCP:7000-7005 允许 Redis Cluster Port

- 0.0.0.0/0 TCP:17000-17005 允许 Redis Cluster Node Ping Pong

（4）添加如下出站规则

- 0.0.0.0/0 ALL 允许 一键放通出站规则