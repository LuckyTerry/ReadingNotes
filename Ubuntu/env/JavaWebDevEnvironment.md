# JavaWeb开发环境搭建

## Java是什么

## Java安装

1. tar.gz安装

    验证Java是否安装

    `java -version`

    官网下载最新JDK

    `http://www.oracle.com/technetwork/java/javase/downloads/`

    创建Java文件夹

    ```bash
    sudo mkdir /opt/java

    intellij对路径的识别只支持三个路径，所以，要把JDK安装在这三个之一：
    /usr/java    /opt/java    /usr/lib/jvm
    ```

    解压

    ```bash
    sudo tar -zxvf jdk-8u152-linux-x64.tar.gz -C /opt/java
    或
    sudo tar -zxvf jdk-8u152-linux-x64.tar.gz
    sudo mv jdk1.8.0_152 /opt/java
    ```

    配置系统环境变量

    ```bash
    sudo vim /etc/profile

    在末尾添加以下几行文字（添加错了可能导致无限循环登录）
    #set java environment
    export JAVA_HOME=/opt/java/jdk1.8.0_152
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:$CLASSPATH:${JAVA_HOME}/lib:${JRE_HOME}/lib
    export PATH=$PATH:${JAVA_HOME}/bin:${JRE_HOME}/bin
    ```

    配置默认JDK

    ```bash
    由于部分Linux已经自带了JDK,所以我们需要设置刚刚安装好的JDK来作为默认JDK

    sudo update-alternatives --install /usr/bin/java java /opt/java/jdk1.8.0_152/bin/java 300
    sudo update-alternatives --install /usr/bin/javac javac /opt/java/jdk1.8.0_152/bin/javac 300
    ```

    使生效

    ```bash
    source /etc/profile //在当前terminal下生效
    或
    logout->login //在当前用户下生效
    ```

    打开 命令提示行 验证一下

    ```bash
    java -version
    java
    javac
    ```
2. apt安装

    略

## JD-GUI

    官网下载最新版本

    `http://jd.benow.ca/`

    安装

    `sudo dpkg -i jd-gui*.deb`

    若出现依赖问题，解决依赖后再执行上面的命令

    `sudo apt install -f`

## Maven    

    官网下载最新版本

    `http://maven.apache.org/download.cgi`

    解压

    `sudo tar -zxvf apache-maven-3.6.1-bin.tar.gz -C /opt/`
    `sudo mv /opt/apache-maven-3.6.1 /opt/maven`

    配置 /opt/maven/confsettings.xml

    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
     
      <servers>
        <server>
          <id>snapshots</id>
          <username>admin</username>
          <password>admin123</password>
        </server>
        <server>
          <id>nexus-snapshots</id>
          <username>admin</username>
          <password>admin123</password>
        </server>
      </servers>
    
      <mirrors>
        <mirror> 
          <id>nexus-releases</id> 
          <mirrorOf>*</mirrorOf> 
          <url>http://192.168.100.135:8070/nexus/content/groups/public</url> 
        </mirror>
        <mirror> 
          <id>nexus-snapshots</id> 
          <mirrorOf>*</mirrorOf> 
          <url>http://192.168.100.135:8070/nexus/content/repositories/snapshots</url> 
        </mirror> 
      </mirrors>
    
      <profiles>
        <profile>
          <id>nexus</id>
          <repositories>
            <repository>
              <id>nexus-releases</id>
              <url>http://nexus-releases</url>
              <releases><enabled>true</enabled></releases>
              <snapshots><enabled>true</enabled></snapshots>
            </repository>
            <repository>
              <id>nexus-snapshots</id>
              <url>http://nexus-snapshots</url>
              <releases><enabled>true</enabled></releases>
              <snapshots><enabled>true</enabled></snapshots>
            </repository>
          </repositories>
          <pluginRepositories>
            <pluginRepository>
              <id>nexus-releases</id>
              <url>http://nexus-releases</url>
              <releases><enabled>true</enabled></releases>
              <snapshots><enabled>true</enabled></snapshots>
            </pluginRepository>
            <pluginRepository>
              <id>nexus-snapshots</id>
              <url>http://nexus-snapshots</url>
              <releases><enabled>true</enabled></releases>
              <snapshots><enabled>true</enabled></snapshots>
            </pluginRepository>
          </pluginRepositories>
        </profile>
      </profiles>
    
      <activeProfiles>
        <activeProfile>nexus</activeProfile>
      </activeProfiles>
    
    </settings>
    ```

    项目中使用

    ```
    <distributionManagement>
        <!-- 两个ID必须与 setting.xml中的<server><id>nexus-releases</id></server>保持一致-->
        <repository>
            <id>nexus-releases</id>
            <name>Nexus Release Repository</name>
            <url>http://192.168.100.135:8070/nexus/content/repositories/releases</url>
        </repository>
        <snapshotRepository>
            <id>nexus-snapshots</id>
            <name>Nexus Snapshot Repository</name>
            <url>http://192.168.100.135:8070/nexus/content/repositories/snapshots</url>
        </snapshotRepository>
    </distributionManagement>
    ```

## IntelliJ IDEA 安装 （强力推荐）

1. tar.gz安装

    官网下载最新Ultimate版本

    `https://www.jetbrains.com/idea/download/#section=linux`

    解压

    `sudo tar -zxvf *.tar.gz -C /opt`

    修改文件夹名

    `sudo mv idea-IU-* idea-IU`

    启动

    `/opt/idea-IU/bin/idea.sh`

    Activation code 激活

    ```text
    # 修改hosts文件
    sudo vim /etc/hosts
    添加host映射 0.0.0.0 account.jetbrains.com

    # 获取注册码
    http://idea.lanyus.com/ 点击获取注册码，复制

    # 添加激活
    选择Activation code项，粘贴获取到的注册码

    ```

    License server 激活

    ```text
    # 可用服务器
    http://intellij.mandroid.cn/ (已不可用)
    http://idea.imsxm.com/ (已不可用)
    http://idea.iteblog.com/key.php (已不可用)

    # 激活
    选择License server项，填入上面任一地址，点击Activate即可
    ```

    添加快捷方式

    ```bash
    Welcome面板 - Configure - Create Desktop Entry

    或（若上面不生效，则按照下面方法手动添加）

    sudo vim /usr/share/applications/intellij-idea.desktop

    将下面的内容粘贴到 intellij-idea.desktop 文件中：
    [Desktop Entry]
    Name=IntelliJ IDEA
    Exec=/opt/idea-IU/bin/idea.sh
    Comment=IntelliJ IDEA
    Icon=/opt/idea-IU/bin/idea.png
    Type=Application
    Terminal=false
    Encoding=UTF-8

    添加执行权限
    sudo chmod +x /usr/share/applications/intellij-idea.desktop

    添加Launcher快捷方式，桌面快捷方式
    sudo nautilus /usr/share/applications 拖动快捷方式到Launcher或桌面
    或 轻触Super键盘打开Dash，拖动快捷方式到Launcher或桌面
    ```

2. 软件商店安装

    略

3. IntelliJ-IDEA-Tutorial

    `https://github.com/judasn/IntelliJ-IDEA-Tutorial`