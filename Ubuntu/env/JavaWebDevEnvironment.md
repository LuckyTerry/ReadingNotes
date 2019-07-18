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
    # set java environment
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

配置 /opt/maven/conf/settings.xml

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

maven 项目的 pom.xml 配置

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

    Terry自购 Active Code 激活，active until July 1, 2020

    ```
    ZKVVPH4MIO-eyJsaWNlbnNlSWQiOiJaS1ZWUEg0TUlPIiwibGljZW5zZWVOYW1lIjoi5o6I5p2D5Luj55CG5ZWGIGh0dHA6Ly9pZGVhLmhrLmNuIiwiYXNzaWduZWVOYW1lIjoiIiwiYXNzaWduZWVFbWFpbCI6IiIsImxpY2Vuc2VSZXN0cmljdGlvbiI6IiIsImNoZWNrQ29uY3VycmVudFVzZSI6ZmFsc2UsInByb2R1Y3RzIjpbeyJjb2RlIjoiSUkiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTAxIiwicGFpZFVwVG8iOiIyMDIwLTA2LTMwIn0seyJjb2RlIjoiQUMiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTAxIiwicGFpZFVwVG8iOiIyMDIwLTA2LTMwIn0seyJjb2RlIjoiRFBOIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IlBTIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IkdPIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IkRNIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IkNMIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IlJTMCIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSRCIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJQQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSTSIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJXUyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJEQiIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJEQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSU1UiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTAxIiwicGFpZFVwVG8iOiIyMDIwLTA2LTMwIn1dLCJoYXNoIjoiMTM1NTgzMjIvMCIsImdyYWNlUGVyaW9kRGF5cyI6NywiYXV0b1Byb2xvbmdhdGVkIjpmYWxzZSwiaXNBdXRvUHJvbG9uZ2F0ZWQiOmZhbHNlfQ==-i/ZK8vfXLX80OFpkhwEo9QxMhsWaOu3SfBmNPup63N0kjM2XBIoR67s8fk0Li45CreS2zQcPZdypLPeyRrdrUYGTw77tkK/kUygxEwRKauqgdJhUs+881TGitcmZvk8obLXjjpv+tZEbV31ee6Fb2/iuK36Q1NCuhKGlo8mA68kGXLOk5ppRYCqQUnHY2zk8spzxC/yJtG+JAQGlPDyvQmkQ5taRxM77b1/v2/62t5Xa2HqnPkuJBrS+XXuGz++RBuYEv6cVe5hmsUaQJZe9/Z4BrhMy48fVEG6bsKTmJ4yILs9sSyUM6uA05AOm8lXWmCG3m9AdVyawsWqBJIn7Rw==-MIIElTCCAn2gAwIBAgIBCTANBgkqhkiG9w0BAQsFADAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBMB4XDTE4MTEwMTEyMjk0NloXDTIwMTEwMjEyMjk0NlowaDELMAkGA1UEBhMCQ1oxDjAMBgNVBAgMBU51c2xlMQ8wDQYDVQQHDAZQcmFndWUxGTAXBgNVBAoMEEpldEJyYWlucyBzLnIuby4xHTAbBgNVBAMMFHByb2QzeS1mcm9tLTIwMTgxMTAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcQkq+zdxlR2mmRYBPzGbUNdMN6OaXiXzxIWtMEkrJMO/5oUfQJbLLuMSMK0QHFmaI37WShyxZcfRCidwXjot4zmNBKnlyHodDij/78TmVqFl8nOeD5+07B8VEaIu7c3E1N+e1doC6wht4I4+IEmtsPAdoaj5WCQVQbrI8KeT8M9VcBIWX7fD0fhexfg3ZRt0xqwMcXGNp3DdJHiO0rCdU+Itv7EmtnSVq9jBG1usMSFvMowR25mju2JcPFp1+I4ZI+FqgR8gyG8oiNDyNEoAbsR3lOpI7grUYSvkB/xVy/VoklPCK2h0f0GJxFjnye8NT1PAywoyl7RmiAVRE/EKwIDAQABo4GZMIGWMAkGA1UdEwQCMAAwHQYDVR0OBBYEFGEpG9oZGcfLMGNBkY7SgHiMGgTcMEgGA1UdIwRBMD+AFKOetkhnQhI2Qb1t4Lm0oFKLl/GzoRykGjAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBggkA0myxg7KDeeEwEwYDVR0lBAwwCgYIKwYBBQUHAwEwCwYDVR0PBAQDAgWgMA0GCSqGSIb3DQEBCwUAA4ICAQAF8uc+YJOHHwOFcPzmbjcxNDuGoOUIP+2h1R75Lecswb7ru2LWWSUMtXVKQzChLNPn/72W0k+oI056tgiwuG7M49LXp4zQVlQnFmWU1wwGvVhq5R63Rpjx1zjGUhcXgayu7+9zMUW596Lbomsg8qVve6euqsrFicYkIIuUu4zYPndJwfe0YkS5nY72SHnNdbPhEnN8wcB2Kz+OIG0lih3yz5EqFhld03bGp222ZQCIghCTVL6QBNadGsiN/lWLl4JdR3lJkZzlpFdiHijoVRdWeSWqM4y0t23c92HXKrgppoSV18XMxrWVdoSM3nuMHwxGhFyde05OdDtLpCv+jlWf5REAHHA201pAU6bJSZINyHDUTB+Beo28rRXSwSh3OUIvYwKNVeoBY+KwOJ7WnuTCUq1meE6GkKc4D/cXmgpOyW/1SmBz3XjVIi/zprZ0zf3qH5mkphtg6ksjKgKjmx1cXfZAAX6wcDBNaCL+Ortep1Dh8xDUbqbBVNBL4jbiL3i3xsfNiyJgaZ5sX7i8tmStEpLbPwvHcByuf59qJhV/bZOl8KqJBETCDJcY6O2aqhTUy+9x93ThKs1GKrRPePrWPluud7ttlgtRveit/pcBrnQcXOl1rHq7ByB8CFAxNotRUYL9IF5n3wJOgkPojMy6jetQA5Ogc8Sm7RG6vg1yow== 
    ```

    开源 Activation code 激活

    ```text

    # 激活前清除hosts中屏蔽的域名

    # 获取注册码
    http://idea.lanyus.com/ 点击获取注册码，复制（注意不要选中注册码文字起始的非Code部分）

    # 执行激活
    选择 Activation Code 项，粘贴获取到的注册码

    # 修改hosts文件，屏蔽域名
    sudo vim /etc/hosts
    0.0.0.0 account.jetbrains.com
    0.0.0.0 www.jetbrains.com

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