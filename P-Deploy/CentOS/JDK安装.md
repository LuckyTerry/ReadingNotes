# CentOS安装JDK

CentOS通常可安装 闭源OracleJDK 和 开源OpenJDK，推荐安装 OracleJDK，大部分Lib都是用的OracleJDK。

## OracleJDK（推荐）

卸载OpenJDK

    # 查看
    rpm -qa |grep java
    # 卸载
    rpm -qa | grep java | xargs rpm -e --nodeps
    # 查看是否已卸载
    java -version
    
官网下载最新JDK

    https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
    
    下载 Linux x64 Compressed Archive 版本，如 jdk-8u241-linux-x64.tar.gz
    
    注：需要登陆方可下载
    
    使用Xftp或Putty上传文件至服务器
    
安装JDK
    
    mkdir /usr/local/java #（在/usr/local目录下创建java目录）
    mv jdk-8u241-linux-x64.tar.gz /usr/local/java/ #（将上传到服务器上的jdk移动到上一步创建的java目录下）
    cd /usr/local/java/ #（进入到有jdk文件的目录下）
    tar -zxvf jdk-8u241-linux-x64.tar.gz #（解压jdk）
    rm -f jdk-8u241-linux-x64.tar.gz #（删除jdk压缩包命令，我没删，建议还是留下吧）

配置JDK

    # /etc/profile文件是每个用户登录时都会运行的环境变量设置，将jdk配置进去，在图中所示的位置加入这三行命令
    vi /etc/profile
    
    # 配置路径
    export JAVA_HOME=/usr/local/java/jdk1.8.0_241
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:$CLASSPATH:${JAVA_HOME}/lib:${JRE_HOME}/lib
    export PATH=$PATH:$JAVA_HOME/bin
    
    # 使生效
    source /etc/profile
  
验证是否安装成功  

    java -version

输出

    java version "1.8.0_241"
    Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
    Java HotSpot(TM) 64-Bit Server VM (build 25.241-b07, mixed mode)

## OpenJDK

先查看系统是否已有自带的jdk

    rpm -qa |grep java
    或 rpm -qa |grep jdk
    或 rpm -qa |grep gcj

如果没有输出信息，则说明系统没有安装。如果有输出信息，则执行下面的命令卸载

    rpm -qa | grep java | xargs rpm -e --nodeps

查看yum包含的jdk版本

    yum search java-1.8.0 或者 yum list java-1.8.0*

安装

    yum install java-1.8.0-openjdk* -y

验证是否安装成功

    java -version

输出

    openjdk version "1.8.0_242"
    OpenJDK Runtime Environment (build 1.8.0_242-b08)
    OpenJDK 64-Bit Server VM (build 25.242-b08, mixed mode)

默认jre jdk 安装路径是/usr/lib/jvm 下面

    [root@iZm5e4v2sdd3v6vi8twyj8Z ~]# ll /usr/lib/jvm
    total 8
    lrwxrwxrwx 1 root root   26 Feb 25 13:06 java -> /etc/alternatives/java_sdk
    lrwxrwxrwx 1 root root   32 Feb 25 13:06 java-1.8.0 -> /etc/alternatives/java_sdk_1.8.0
    lrwxrwxrwx 1 root root   40 Feb 25 13:06 java-1.8.0-openjdk -> /etc/alternatives/java_sdk_1.8.0_openjdk
    drwxr-xr-x 9 root root 4096 Feb 25 13:06 java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64
    drwxr-xr-x 9 root root 4096 Feb 25 13:06 java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64-debug
    lrwxrwxrwx 1 root root   34 Feb 25 13:06 java-openjdk -> /etc/alternatives/java_sdk_openjdk
    lrwxrwxrwx 1 root root   21 Feb 25 13:06 jre -> /etc/alternatives/jre
    lrwxrwxrwx 1 root root   27 Feb 25 13:06 jre-1.8.0 -> /etc/alternatives/jre_1.8.0
    lrwxrwxrwx 1 root root   35 Feb 25 13:06 jre-1.8.0-openjdk -> /etc/alternatives/jre_1.8.0_openjdk
    lrwxrwxrwx 1 root root   51 Feb 25 13:05 jre-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64 -> java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64/jre
    lrwxrwxrwx 1 root root   57 Feb 25 13:05 jre-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64-debug -> java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64-debug/jre
    lrwxrwxrwx 1 root root   29 Feb 25 13:06 jre-openjdk -> /etc/alternatives/jre_openjdk
    
特别的，环境变量已默认配置。如需自行配置，参考如下：

    sudo vim /etc/profile
    
    # 配置路径
    export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64
    export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
    export PATH=$PATH:$JAVA_HOME/bin

    # 使生效
    source /etc/profile