# CentOS 安装 Maven
## tar.gz 安装

官网

    # 下载
    http://maven.apache.org/download.cgi
    
    # 镜像仓库列表
    http://www.apache.org/dyn/mirrors/mirrors.cgi
    
    # 如：
    http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/
    http://mirror.bit.edu.cn/apache/maven/maven-3/3.6.3/binaries/
    
下载

    # wget
    yum install -y wget
    wget http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
    # 或 wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
    
    # 手动
    浏览器下载并使用Xftp或Putty上传至服务器

安装

    mkdir /usr/local/maven #（在/usr/local目录下创建maven目录）
    mv apache-maven-3.6.3-bin.tar.gz /usr/local/maven/
    cd /usr/local/maven/
    tar -zxvf apache-maven-3.6.3-bin.tar.gz
    # rm -f apache-maven-3.6.3-bin.tar.gz #（删除mavem压缩包命令，我没删，建议还是留下吧）
    
配置

    sudo vim /etc/profile
    
    # set maven environment
    export MAVEN_HOME=/usr/local/maven
    export PATH=$PATH:$MAVEN_HOME/bin
    
    # 使生效
    source /etc/profile
    
验证

    mvn -version
    
输出

    Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
    Maven home: /usr/local/maven/apache-maven-3.6.3
    Java version: 1.8.0_241, vendor: Oracle Corporation, runtime: /usr/local/java/jdk1.8.0_241/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "3.10.0-1062.9.1.el7.x86_64", arch: "amd64", family: "unix
    
## yum 安装


设置 阿里云yum源，**阿里云ECS已默认配置**

    rm -rf /etc/yum.repos.d/*
    curl -o /etc/yum.repos.d/Centos-7.repo http://mirrors.aliyun.com/repo/Centos-7.repo
    curl -o /etc/yum.repos.d/epel-7.repo http://mirrors.aliyun.com/repo/epel-7.repo

安装maven

    yum -y install maven
    
验证

    mvn -version
    
输出

    Apache Maven 3.0.5 (Red Hat 3.0.5-17)
    Maven home: /usr/share/maven
    Java version: 1.8.0_242, vendor: Oracle Corporation
    Java home: /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "3.10.0-514.26.2.el7.x86_64", arch: "amd64", family: "unix"

## 设置maven仓库镜像

    # tar.gz 安装
    sudo vim /usr/local/maven/apache-maven-3.6.3/conf/settings.xml
    # yum 安装
    sudo vim /etc/maven/settings.xml

    # 添加仓库镜像
    <mirror>
      <id>aliyun</id>
      <name>aliyun</name>
      <mirrorOf>*</mirrorOf>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
