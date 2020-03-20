# 阿里云Jenkins部署

## 添加二级域名

**云解析DNS/域名解析/解析设置** 中 添加如下解析：
- 记录类型：A类
- 主机记录：jenkins

添加二级域名的SSL支持

**SSL证书** 中
- 证书购买：免费版（个人）DV
- 证书申请，输入绑定域名 jenkins.terry.wiki
- 证书验证

**ECS** 中添加nginx解析

```conf
server {
    listen 443 ssl;
    server_name jenkins.terry.wiki;
    ssl on;
    ssl_certificate /etc/nginx/cert/3498774_jenkins.terry.wiki.pem;
    ssl_certificate_key /etc/nginx/cert/3498774_jenkins.terry.wiki.key;
    ssl_session_timeout 5m;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers on;
    location / {
        proxy_buffer_size  128k;
        proxy_buffers   32 32k;
        proxy_busy_buffers_size 128k;
        proxy_pass http://127.0.0.1:8081;
   }
}

server {
    listen 80;
    server_name jenkins.terry.wiki;
    rewrite ^(.*)$ https://$host$1 permanent;
}
```

## 安装

> yum 和 rpm 安装，推荐 yum，但速度过慢的话就采用 rpm

### yum安装

指定仓库

    # wget -O 下载文件并以指定的文件名保存
    sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo

查看仓库，仅了解原理

    cat /etc/yum.repos.d/jenkins.repo
    
    # 输出，gpgcheck=1表示在安装时需要校验，故需要安装密钥
    [jenkins]
    name=Jenkins-stable
    baseurl=http://pkg.jenkins.io/redhat-stable
    gpgcheck=1

安装key

    sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

安装jenkins

    yum install -y jenkins
    
注：jenkins分redhat和redhat-stable，如果要切换，需要清除yum缓存
    
    yum clean all
    
### rpm安装

下载稳定版本jenkins
    
    # 官网稳定版
    # https://pkg.jenkins.io/redhat-stable/
    
    # wget下载，可根据最新版本修改
    wget https://pkg.jenkins.io/redhat-stable/jenkins-2.204.2-1.1.noarch.rpm

    # 或者，浏览器下载并上传到服务器
    
卸载旧jenkins
    
    # 查询以前是否安装jenkins
    rpm -qa |grep jenkins
    
    # 卸载 jenkins
    rpm -e jenkins
    
    # 彻底删除jenkins残留文件
    find / -iname jenkins | xargs -n 1000 rm -rf
    
安装

    rpm -ivh jenkins-2.164.1-1.1.noarch.rpm
    
    # 查看安装后的目录
    # find / -iname jenkins
    
## 配置jenkins
 
配置jdk环境变量
 
    sudo vim /etc/init.d/jenkins
     
    # 在 /etc/alternatives/java 前添加
    /usr/local/java/jdk1.8.0_241/bin/java

配置jenkis的端口

    sudo vim /etc/sysconfig/jenkins
    
    # 修改端口为8081
    JENKINS_PORT="8081"
    
修改jenkins默认的操作用户（可选）

linux下jenkins默认使用jenkins用户进行脚本和文件的操作，如果不修改，在部署项目时需要调整涉及到的文件和目录的操作权限，可以调整jenkins配置文件，将用户修改为root用户。将JENKINS_USER="jenkins"调整为JENKINS_USER=“root”：

    sudo vim /etc/sysconfig/jenkins
    
    # 修改用户为root
    JENKINS_USER="root"
    
启用端口（云主机：如阿里云ECS）

    配置8081的安全组规则即可
    
启用端口（非云主机，如公司的机器）

    sudo vim /etc/sysconfig/iptables
    
    # 添加如下规则
    -A INPUT -m state --state NEW -m tcp -p tcp --dport 8081 -j ACCEPT
    
## 启动jenkins

    service jenkins start/stop/restart
    # 或 systemctl start/stop/restart/status jenkins
    
## 配置jenkins

### 全局工具配置

Maven 配置
- 默认 settings 提供：/usr/local/maven/apache-maven-3.6.3/conf/settings.xml
- 默认全局 settings 提供：/usr/local/maven/apache-maven-3.6.3/conf/settings.xml

JDK
- JDK别名：JDK8
- JAVA_HOME：/usr/local/java/jdk1.8.0_241
    
Git
- Name：Git
- Path to Git executable：/usr/bin/git

Maven
- Name：Maven
- MAVEN_HOME：/usr/local/maven/apache-maven-3.6.3   

后续可参考：

先看这个 [centos安装jenkins以及初始化配置](https://blog.csdn.net/xuonline4196/article/details/88918355)

[jenkins之搭建部署](https://blog.csdn.net/weixin_43279032/article/details/90675968)

[Jenkins插件安装失败解决方法](https://blog.csdn.net/sinat_29217765/article/details/100889697)

[Jenkins配置:添加用户和管理权限](https://blog.csdn.net/pansaky/article/details/80746736)