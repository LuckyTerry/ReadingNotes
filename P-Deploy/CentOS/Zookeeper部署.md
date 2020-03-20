# CentOS安装Zookeeper

[Canal 出品的 Zookeeper QuickStart](https://github.com/alibaba/canal/wiki/Zookeeper-QuickStart)

## 一、安装JDK

    此处省略

## 二、安装Zookeeper

### 2.1 下载二进制包，并解压

官网下载地址：http://www.apache.org/dyn/closer.cgi/zookeeper

需要下载带bin的二进制包

    mkdir /usr/local/zookeeper
    cd /usr/local/zookeeper
    wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/stable/apache-zookeeper-3.5.7-bin.tar.gz
    tar zxvf apache-zookeeper-3.5.7-bin.tar.gz

### 2.2 修改环境变量

    编辑 /etc/profile 文件, 在文件末尾添加以下环境变量配置:

    # ZooKeeper Env
    export ZOOKEEPER_HOME=/usr/local/zookeeper/apache-zookeeper-3.5.7-bin
    export PATH=$PATH:$ZOOKEEPER_HOME/bin
    
    # 使环境变量生效
    source /etc/profile

### 2.3 重命名配置文件

    # 初次使用 ZooKeeper 时,需要将$ZOOKEEPER_HOME/conf 目录下的 zoo_sample.cfg 重命名为 zoo.cfg, 
    
    mv $ZOOKEEPER_HOME/conf/zoo_sample.cfg $ZOOKEEPER_HOME/conf/zoo.cfg
    
### 2.4 单机模式--修改配置文件

创建目录/usr/local/zookeeper/data 和/usr/local/zookeeper/logs 

    mkdir /usr/local/zookeeper/data
    mkdir /usr/local/zookeeper/logs
    
修改配置文件zoo.cfg
    
    vim $ZOOKEEPER_HOME/conf/zoo.cfg

    tickTime=2000
    initLimit=10
    syncLimit=5
    dataDir=/usr/local/zookeeper/data
    dataLogDir=/usr/local/zookeeper/logs
    clientPort=2181

如果是多节点,配置文件中尾部增加

    server.1=192.168.1.110:2888:3888
    server.2=192.168.1.111:2888:3888
    server.3=192.168.1.112:2888:3888

同时,增加

    #master
    echo "1">/usr/local/zookeeper/data/myid
    
    #slave1
    echo "2">/usr/local/zookeeper/data/myid
    
    #slave2
    echo "3">/usr/local/zookeeper/data/myid

### 2.4.1 修改内嵌 Jetty 端口

zookeeper有个内嵌的管理控制台是通过 jetty 启动，会占用 8080 端口。而 RocketMQ-Console 也会占用 8080 端口(netstat -ntulp|grep 8080)，故zk无法启动成功。
    
通过查看zookeeper的官方文档，发现有3种解决途径：
- （1）删除jetty。
- （2）修改端口。
修改方法的方法有两种，一种是在启动脚本中增加-Dzookeeper.admin.serverPort=你的端口号，一种是在zoo.cfg中增加admin.serverPort=没有被占用的端口号。
- （3）停用这个服务，在启动脚本中增加"-Dzookeeper.admin.enableServer=false"

我使用的第二种，问题解决。

### 2.5 启动 ZooKeeper 服务

    zkServer.sh  start
    
    # 输出
    ZooKeeper JMX enabled by default
    Using config: /usr/local/zookeeper/apache-zookeeper-3.5.7-bin/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED
    
    zkServer.sh  status
    
    # 输出
    ZooKeeper JMX enabled by default
    Using config: /usr/local/zookeeper/apache-zookeeper-3.5.7-bin/bin/../conf/zoo.cfg
    Client port found: 2181. Client address: localhost.
    Mode: standalone
    
### 2.6 验证zooKeeper服务

服务启动完成后，可以使用 telnet 和 stat 命令验证服务器启动是否正常:

    telnet 127.0.0.1 2181
    
    # 输出
    Trying 127.0.0.1...
    Connected to 127.0.0.1.
    Escape character is '^]'.
    
### 2.7 停止 ZooKeeper 服务

想要停止 ZooKeeper 服务, 可以使用如下命令:

    zkServer.sh  stop
    
    # 输出
    ZooKeeper JMX enabled by default
    Using config: /usr/local/zookeeper/apache-zookeeper-3.5.7-bin/bin/../conf/zoo.cfg
    Stopping zookeeper ... STOPPED

## 三、zk ui安装 (选装，页面查看zk的数据)

### 拉取代码

    git clone https://github.com/DeemOpen/zkui.git

### 源码编译需要安装 maven
    
    cd zkui/
    mvn clean install

### 修改配置文件默认值

    vim config.cfg
    
    # 修改如下配置
    serverPort=8123
    zkServer=127.0.0.1:2181
    sessionTimeout=300000

### 修改登录密码

    vim config.cfg
    
    # 修改如下配置
    userSet = {"users": [{ "username":"admin" , "password":"LuckyTerry0108","role": "ADMIN" },{ "username":"reader" , "password":"reader","role": "USER" }]}

### 启动程序至后台

2.0-SNAPSHOT 会随软件的更新版本不同而不同，执行时请查看target 目录中真正生成的版本

     nohup java -jar target/zkui-2.0-SNAPSHOT-jar-with-dependencies.jar & 
     
### 配置nginx

    vim /etc/nginx/conf.d/zkui.conf
    
    server {
        listen       443 ssl http2;
        listen       [::]:443 ssl http2;
        server_name  zkui.terry.wiki;
        root         /usr/share/nginx/html;
    
        ssl_certificate /etc/nginx/cert/3506525_zkui.terry.wiki.pem;
        ssl_certificate_key /etc/nginx/cert/3506525_zkui.terry.wiki.key;
        ssl_session_timeout 5m;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;
    
        location / {
            proxy_buffer_size  128k;
            proxy_buffers   32 32k;
            proxy_busy_buffers_size 128k;
            proxy_pass http://127.0.0.1:8123;
        }
    
        error_page 404 /404.html;
            location = /40x.html {
        }
    
        error_page 500 502 503 504 /50x.html;
            location = /50x.html {
        }
    }
    
    server {
        listen 80;
        server_name zkui.terry.wiki;
        rewrite ^(.*)$ https://$host$1 permanent;
    }

     
### 用浏览器访问：

    http://zkui.terry.wiki/