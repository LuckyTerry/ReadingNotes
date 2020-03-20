# Redis单机部署

> 现有 yum、tar.gz 和 docker 安装，推荐使用 tar.gz 安装

## yum安装

不推荐，版本太旧

## 压缩包安装（推荐）

[按照官方链接中的Installation部分操作即可](https://redis.io/download)

下载压缩包

    wget http://download.redis.io/releases/redis-5.0.7.tar.gz
    # 或 浏览器下载并使用Xftp上传至服务器

编译安装

    tar xzf redis-5.0.7.tar.gz
    cd redis-5.0.7
    # 如果没有事先安装gcc等依赖，则需要安装如下
    yum -y install gcc automake autoconf libtool make 
    make install

注释 绑定本地

    sudo vim redis.conf

    bind 127.0.0.1
    改为
    #bind 127.0.0.1

关闭 保护模式

    sudo vim redis.conf

    protected-mode yes
    改为
    protected-mode no

开启守护线程

    sudo vim redis.conf

    daemonize no
    改为
    daemonize yes

开启 密码认证

    sudo vim redis.conf
    
    #requirepass abc
    改为
    requirepass LuckyTerry0108

添加脚本

    #!/bin/sh
         
    # chkconfig: 2345 80 90
    # description: Start and Stop redis
    # PATH=/usr/local/bin:/sbin:/usr/bin:/bin
    REDISPORT=6379　                      #端口
    EXEC=/home/app/redis-5.0.7/src/redis-server    #redis-server路径 
    REDIS_CLI=/home/app/redis-5.0.7/src/redis-cli  #redis_cli路径
    PIDFILE=/var/run/redis_$REDISPORT.pid
    CONF="/home/app/redis-5.0.7/redis.conf"    #redis.conf路径
    AUTH="LuckyTerry0108"                  #密码信息
     
    case "$1" in
        start)
            if [ -f $PIDFILE ]
            then
                    echo "$PIDFILE exists, process is already running or crashed"
            else
                    echo "Starting Redis server..."
                    $EXEC $CONF
            fi
            if [ "$?"="0" ]
            then
                  echo "Redis is running..."
            fi
            ;;
        stop)
            if [ ! -f $PIDFILE ]
            then
                    echo "$PIDFILE does not exist, process is not running"
            else
                    PID=$(cat $PIDFILE)
                    echo "Stopping ..."
                     
                    if [ -z $AUTH ]
                    then
                            $REDIS_CLI -p $REDISPORT SHUTDOWN
                    else
                            $REDIS_CLI -a $AUTH -p $REDISPORT SHUTDOWN
                    fi
                     
                    while [ -x ${PIDFILE} ]
                   do
                        echo "Waiting for Redis to shutdown ..."
                        sleep 1
                    done
                    echo "Redis stopped"
            fi
            ;;
       restart|force-reload)
            ${0} stop
            ${0} start
            ;;
      *)
        echo "Usage: /etc/init.d/redis {start|stop|restart|force-reload}" >&2
            exit 1
    esac

赋予权限

    chmod 755 /etc/init.d/redis
    # 或 chmod +x  /etc/init.d/redis 给予执行权限

启动

    service redis start/stop/restart
    # 或  /etc/init.d/redis start/stop/restart
    # 不支持 systemctl 的方式

连接

    redis-cli 
    redis-cli ../redis.conf
    redis-cii -a LuckyTerry0108
    redis-cii -h localhost -p 6379

开机运行

    chkconfig redis on # 设置开机运行该服务，默认是设置2345等级开机运行服务 
    # chkconfig --add redis # 添加服务，以便让chkconfig指令管理它，chkconfig redis on 会默认添加服务
    # chkconfig --del redis
    # chkconfig redis off
    # chkconfig --list # 列出所有被chkconfig管理的服务

备注，非原生服务，不能使用systemctl，如：

    systemctl enable redis.service
    
    # 将会输出
    redis.service is not a native service, redirecting to /sbin/chkconfig.
    Executing /sbin/chkconfig redis on

## Docker安装

TODO

## 阿里云Docker安装

下载镜像

```
docker search redis
docker pull redis

```

创建目录

```
/home/app/redis
/home/app/redis/data
```

创建redis.conf

```
/home/app/redis/redis.conf

redis.conf模板文件从Github仓库下载，如下：

[redis](https://github.com/antirez/redis)

切换最新Tag，拷贝redis.conf

如 https://github.com/antirez/redis/blob/5.0.6/redis.conf
```

注释 绑定本地

```
bind 127.0.0.1
改为
#bind 127.0.0.1
```

关闭 保护模式

```
protected-mode yes
改为
protected-mode no
```

启动容器

```
docker run \
-p 6379:6379 \
-v /home/app/redis/redis.conf:/etc/redis/redis.conf \
-v /home/app/redis/data:/data \
--name redis-singleton \
-d redis redis-server /etc/redis/redis.conf --appendonly yes
```

## 添加安全组规则

![阿里云安全组Redis规则](https://note.youdao.com/yws/res/3599/80C43DAC46C34ED1BAB8F62C489A643A)

## Reference Link

[云服务器上使用Docker快速部署安装Redis](http://www.imooc.com/article/277939)

[基于阿里云的docker中安装运行redis并远程访问](https://blog.csdn.net/You_are_my_Mr_Right/article/details/100515089)

[阿里云服务器使用Docker安装redis并挂载配置文件和数据](https://www.jianshu.com/p/d9d70c3559c4)

[Docker安装Redis以及遇到的坑](https://www.jianshu.com/p/ede209c259a9)