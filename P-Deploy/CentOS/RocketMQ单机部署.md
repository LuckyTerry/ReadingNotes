# RocketMQ单机部署

[quick-start](https://rocketmq.apache.org/docs/quick-start/) 供参考

## 单机部署

Download Binary Package

[4.6.1 版本](http://rocketmq.apache.org/release_notes/release-notes-4.6.1/)

    wget http://mirrors.tuna.tsinghua.edu.cn/apache/rocketmq/4.6.1/rocketmq-all-4.6.1-bin-release.zip
    
Start Name Server

    > nohup sh bin/mqnamesrv &
    > tail -f ~/logs/rocketmqlogs/namesrv.log
    The Name Server boot success...

Resize Broker Memory
  
    sudo vim /bin/runbrocker.sh
    
    # 根据本机的内存，修改为合适大小即可。
    # 推荐 -Xms256m -Xmx256m -Xmn125m
    # 由于ECS内存只有8G，因此我设为 -Xms128m -Xmx128m -Xmn64m
    JAVA_OPT="${JAVA_OPT} -server -Xms128m -Xmx128m -Xmn64m"
  
Start Broker
  
    > nohup sh bin/mqbroker -n localhost:9876 &
    > tail -f ~/logs/rocketmqlogs/broker.log 
    The broker[%s, 172.30.30.233:10911] boot success...
    
Send & Receive Messages

    > export NAMESRV_ADDR=localhost:9876
    > sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
    SendResult [sendStatus=SEND_OK, msgId= ...
    
    > sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer
    ConsumeMessageThread_%d Receive New Messages: [MessageExt...
    
Shutdown Servers

    > sh bin/mqshutdown broker
    The mqbroker(36695) is running...
    Send shutdown request to mqbroker(36695) OK
    
    > sh bin/mqshutdown namesrv
    The mqnamesrv(36664) is running...
    Send shutdown request to mqnamesrv(36664) OK
    
额外的，可以用 docker 来安装部署，参见 [RocketMQ-Docker](https://github.com/apache/rocketmq-docker)


## 安装 RocketMQ Console 

[RocketMQ-Console-Ng](https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console) 安装向导，推荐 Docker Pull 的方式，建议端口分配如下

- rocketmq-console：9880
- rocketmq-nameServer：？
- rocketmq-broker：9876

### Maven 打包

    # 下载
    cd /usr/local/rocketmq/
    git clone git@github.com:apache/rocketmq-externals.git
    cd rocketmq-externals/rocketmq-console/
    # 修改如下属性
    sudo vim src/main/resources/application.properties
    server.port=9880 # 天坑，修改了端口后登录有问题！保持8080才行！
    rocketmq.config.namesrvAddr=172.31.227.55:9876
    rocketmq.config.isVIPChannel=false
    rocketmq.config.loginRequired=true
    # 修改登录密码
    cp src/main/resources/users.properties /tmp/rocketmq-console/data/
    sudo vim /tmp/rocketmq-console/data/users.properties
    修改 “admin=LuckyTerry0108,1”，并注释其他配置
    # 运行测试
    mvn spring-boot:run
    # 测试无误，打包，后台运行
    mvn clean package -Dmaven.test.skip=true
    java -jar target/rocketmq-console-ng-1.0.1.jar &
    
当修改端口后（非8080），登录出现问题！！！
    
关于 https 和 登录 部分，可参考 [HTTPS 方式访问Console 、登录访问Console](https://github.com/apache/rocketmq-externals/blob/master/rocketmq-console/doc/1_0_0/UserGuide_CN.md)
    
    
### Docker 安装

简要安装启动如下：

    docker pull styletang/rocketmq-console-ng
    docker run -d \
    -e "JAVA_OPTS=-Drocketmq.namesrv.addr=172.31.227.55:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false -Drocketmq.config.loginRequired=true" \
    --name rocketmq-console \
    -v /tmp/rocketmq-console/data:/tmp/rocketmq-console/data \
    -p 9880:8080 \
    -t styletang/rocketmq-console-ng
    
如果要启用密码认证：（一顿操作猛如虎，然而还是没开启auth）

- -Drocketmq.config.loginRequired=true 开启
- -v /tmp/rocketmq-console/data:/tmp/rocketmq-console/data 挂载 users.properties 所在目录
- 容器没有yum/vim/vi，蛋疼，可以下载源码自行构建，mvn clean package -Dmaven.test.skip=true docker:build
    
如果要进入容器，操作如下：

    docker exec -it <id> /bin/bash
    