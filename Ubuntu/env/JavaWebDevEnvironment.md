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

配置环境变量

```
sudo vim /etc/profile
在最后面追加以下代码

export MAVEN_HOME=/opt/maven
export PATH=$PATH:${MAVEN_HOME}/bin

生效环境变量
source /etc/profile 
```

配置 /opt/maven/conf/settings.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
 
  <servers>
    <server>
      <id>nexus-releases</id>
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
      <id>nexus-aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>Nexus Aliyun</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
  </mirrors>

  <profiles>
    <profile>
      <id>nexus-holder</id>
      <repositories>
	<repository>
	  <id>snapshots</id>
	  <name>Nexus Snapshots</name>
	  <url>http://192.168.100.135:8070/nexus/content/repositories/snapshots/</url>
	  <snapshots><enabled>true</enabled></snapshots>
	</repository>
	<repository>
	  <id>thirdparty</id>
	  <name>Nexus ThirdParty</name>
	  <url>http://192.168.100.135:8070/nexus/content/repositories/thirdparty/</url>
	</repository>
	<repository>
	  <id>releases</id>
	  <name>Nexus Releases</name>
	  <url>http://192.168.100.135:8070/nexus/content/repositories/releases/</url>
	</repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
	  <id>snapshots</id>
	  <name>Nexus Snapshots</name>
	  <url>http://192.168.100.135:8070/nexus/content/repositories/snapshots/</url>
	  <snapshots><enabled>true</enabled></snapshots>
	</pluginRepository>
	<pluginRepository>
	  <id>thirdparty</id>
	  <name>Nexus ThirdParty</name>
	  <url>http://192.168.100.135:8070/nexus/content/repositories/thirdparty/</url>
	</pluginRepository>
	<pluginRepository>
	  <id>releases</id>
	  <name>Nexus Releases</name>
	  <url>http://192.168.100.135:8070/nexus/content/repositories/releases/</url>
	</pluginRepository>
      </pluginRepositories>
    </profile>
    <profile>
      <id>nexus-aliyun</id>
      <repositories>
        <repository>
          <id>nexus-aliyun</id>
          <name>local private nexus</name>
          <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>false</enabled></snapshots>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>nexus-aliyun</id>
          <name>local private nexus</name>
          <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>false</enabled></snapshots>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>nexus-holder</activeProfile>
  </activeProfiles>

</settings>
```

配置 maven 项目的 pom.xml

```
<repositories>
	<repository>
		<id>nexus-snapshots</id>
		<name>Nexus Snapshots</name>
		<url>192.168.100.135:8070/nexus/content/repositories/snapshots/</url>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</repository>
	<repository>
		<id>nexus-thirdparty</id>
		<name>Nexus ThirdParty</name>
		<url>192.168.100.135:8070/nexus/content/repositories/thirdparty/</url>
	</repository>
	<repository>
		<id>nexus-releases</id>
		<name>Nexus Releases</name>
		<url>192.168.100.135:8070/nexus/content/repositories/releases/</url>
	</repository>
	<repository>
		<id>spring-snapshots</id>
		<name>Spring Snapshots</name>
		<url>https://repo.spring.io/snapshot</url>
		<snapshots><enabled>true</enabled></snapshots>
	</repository>
	<repository>
		<id>spring-milestones</id>
		<name>Spring Milestones</name>
		<url>https://repo.spring.io/milestone</url>
		<snapshots><enabled>false</enabled></snapshots>
	</repository>
</repositories>

<pluginRepositories>
	<pluginRepository>
		<id>nexus-snapshots</id>
		<name>Nexus Snapshots</name>
		<url>192.168.100.135:8070/nexus/content/repositories/snapshots/</url>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</pluginRepository>
	<pluginRepository>
		<id>nexus-thirdparty</id>
		<name>Nexus ThirdParty</name>
		<url>192.168.100.135:8070/nexus/content/repositories/thirdparty/</url>
	</pluginRepository>
	<pluginRepository>
		<id>nexus-releases</id>
		<name>Nexus Releases</name>
		<url>192.168.100.135:8070/nexus/content/repositories/releases/</url>
	</pluginRepository>
	<pluginRepository>
		<id>spring-snapshots</id>
		<name>Spring Snapshots</name>
		<url>https://repo.spring.io/snapshot</url>
		<snapshots><enabled>true</enabled></snapshots>
	</pluginRepository>
	<pluginRepository>
		<id>spring-milestones</id>
		<name>Spring Milestones</name>
		<url>https://repo.spring.io/milestone</url>
		<snapshots><enabled>false</enabled></snapshots>
	</pluginRepository>
</pluginRepositories>

<distributionManagement>
    <!-- 两个id必须与 setting.xml中的server.id保持一致-->
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

配置 idea 的 maven 选项

```
Maven home diretory: 选择 /opt/maven 
User settings file: override 选择 /opt/maven/conf/settings.xml
```

**mirrors中不存在 `<mirrorOf>*</mirrorOf>`**

查找优先级：本地仓库 > profile > pom > mirror

- 本地仓库已存在Jar，则不会从远程仓库下载；如果未找到，进入次优先级。
- conf/settings.xml 中 activeProfile 标签未配置，则跳过该优先级；若存在，则根据  profile.id 相应的配置，从指定的仓库下载。如果未找到，进入次优先级。
- 项目 pom.xml 中 repositories 和 pluginRepositories 未配置，则跳过该优先级；若已配置，则根据配置先后顺序，依次从指定的仓库下载。如果未找到，进入次优先级。
- conf/settings.xml 中 mirrors 标签未配置，则跳过该优先级；若已配置，则根据 mirror.id 的字母序，依次从指定的仓库下载。如果未找到，抛出错误。

**mirrors中有存在 `<mirrorOf>*</mirrorOf>`**

查找优先级：mirror > 无

**高级的镜像配置：**

1.`<mirrorOf>*</mirrorOf> `
匹配所有远程仓库。 这样所有pom中定义的仓库都不生效

2.`<mirrorOf>external:*</mirrorOf> `
匹配所有远程仓库，使用localhost的除外，使用file://协议的除外。也就是说，匹配所有不在本机上的远程仓库。 

3.`<mirrorOf>repo1,repo2</mirrorOf> `
匹配仓库repo1和repo2，使用逗号分隔多个远程仓库。 

4.`<mirrorOf>*,!repo1</miiroOf> `
匹配所有远程仓库，repo1除外，使用感叹号将仓库从匹配中排除。 

mirrors可以配置多个mirror，每个mirror有id,name,url,mirrorOf属性，id是唯一标识一个mirror就不多说了，name貌似没多大用，相当于描述，url是官方的库地址，mirrorOf代表了一个镜像的替代位置，例如central就表示代替官方的中央库。

我本以为镜像库是一个分库的概念，就是说当a.jar在第一个mirror中不存在的时候，maven会去第二个mirror中查询下载。但事实却不是这样，当第一个mirror中不存在a.jar的时候，并不会去第二个mirror中查找，甚至于，maven根本不会去其他的mirror地址查询。

后来终于知道，maven的mirror是镜像，而不是“分库”，只有当前一个mirror无法连接的时候，才会去找后一个，类似于备份和容灾。

还有，mirror也不是按settings.xml中写的那样的顺序来查询的。

所谓的第一个并不一定是最上面的那个。

当有id为B,A,C的顺序的mirror在mirrors节点中，maven会根据字母排序来指定第一个，所以不管怎么排列，一定会找到A这个mirror来进行查找，当A无法连接，出现意外的情况下，才会去B查询。

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

    已过期

    ```
    ZKVVPH4MIO-eyJsaWNlbnNlSWQiOiJaS1ZWUEg0TUlPIiwibGljZW5zZWVOYW1lIjoi5o6I5p2D5Luj55CG5ZWGIGh0dHA6Ly9pZGVhLmhrLmNuIiwiYXNzaWduZWVOYW1lIjoiIiwiYXNzaWduZWVFbWFpbCI6IiIsImxpY2Vuc2VSZXN0cmljdGlvbiI6IiIsImNoZWNrQ29uY3VycmVudFVzZSI6ZmFsc2UsInByb2R1Y3RzIjpbeyJjb2RlIjoiSUkiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTAxIiwicGFpZFVwVG8iOiIyMDIwLTA2LTMwIn0seyJjb2RlIjoiQUMiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTAxIiwicGFpZFVwVG8iOiIyMDIwLTA2LTMwIn0seyJjb2RlIjoiRFBOIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IlBTIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IkdPIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IkRNIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IkNMIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0wMSIsInBhaWRVcFRvIjoiMjAyMC0wNi0zMCJ9LHsiY29kZSI6IlJTMCIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSRCIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJQQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSTSIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJXUyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJEQiIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJEQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMDEiLCJwYWlkVXBUbyI6IjIwMjAtMDYtMzAifSx7ImNvZGUiOiJSU1UiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTAxIiwicGFpZFVwVG8iOiIyMDIwLTA2LTMwIn1dLCJoYXNoIjoiMTM1NTgzMjIvMCIsImdyYWNlUGVyaW9kRGF5cyI6NywiYXV0b1Byb2xvbmdhdGVkIjpmYWxzZSwiaXNBdXRvUHJvbG9uZ2F0ZWQiOmZhbHNlfQ==-i/ZK8vfXLX80OFpkhwEo9QxMhsWaOu3SfBmNPup63N0kjM2XBIoR67s8fk0Li45CreS2zQcPZdypLPeyRrdrUYGTw77tkK/kUygxEwRKauqgdJhUs+881TGitcmZvk8obLXjjpv+tZEbV31ee6Fb2/iuK36Q1NCuhKGlo8mA68kGXLOk5ppRYCqQUnHY2zk8spzxC/yJtG+JAQGlPDyvQmkQ5taRxM77b1/v2/62t5Xa2HqnPkuJBrS+XXuGz++RBuYEv6cVe5hmsUaQJZe9/Z4BrhMy48fVEG6bsKTmJ4yILs9sSyUM6uA05AOm8lXWmCG3m9AdVyawsWqBJIn7Rw==-MIIElTCCAn2gAwIBAgIBCTANBgkqhkiG9w0BAQsFADAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBMB4XDTE4MTEwMTEyMjk0NloXDTIwMTEwMjEyMjk0NlowaDELMAkGA1UEBhMCQ1oxDjAMBgNVBAgMBU51c2xlMQ8wDQYDVQQHDAZQcmFndWUxGTAXBgNVBAoMEEpldEJyYWlucyBzLnIuby4xHTAbBgNVBAMMFHByb2QzeS1mcm9tLTIwMTgxMTAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcQkq+zdxlR2mmRYBPzGbUNdMN6OaXiXzxIWtMEkrJMO/5oUfQJbLLuMSMK0QHFmaI37WShyxZcfRCidwXjot4zmNBKnlyHodDij/78TmVqFl8nOeD5+07B8VEaIu7c3E1N+e1doC6wht4I4+IEmtsPAdoaj5WCQVQbrI8KeT8M9VcBIWX7fD0fhexfg3ZRt0xqwMcXGNp3DdJHiO0rCdU+Itv7EmtnSVq9jBG1usMSFvMowR25mju2JcPFp1+I4ZI+FqgR8gyG8oiNDyNEoAbsR3lOpI7grUYSvkB/xVy/VoklPCK2h0f0GJxFjnye8NT1PAywoyl7RmiAVRE/EKwIDAQABo4GZMIGWMAkGA1UdEwQCMAAwHQYDVR0OBBYEFGEpG9oZGcfLMGNBkY7SgHiMGgTcMEgGA1UdIwRBMD+AFKOetkhnQhI2Qb1t4Lm0oFKLl/GzoRykGjAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBggkA0myxg7KDeeEwEwYDVR0lBAwwCgYIKwYBBQUHAwEwCwYDVR0PBAQDAgWgMA0GCSqGSIb3DQEBCwUAA4ICAQAF8uc+YJOHHwOFcPzmbjcxNDuGoOUIP+2h1R75Lecswb7ru2LWWSUMtXVKQzChLNPn/72W0k+oI056tgiwuG7M49LXp4zQVlQnFmWU1wwGvVhq5R63Rpjx1zjGUhcXgayu7+9zMUW596Lbomsg8qVve6euqsrFicYkIIuUu4zYPndJwfe0YkS5nY72SHnNdbPhEnN8wcB2Kz+OIG0lih3yz5EqFhld03bGp222ZQCIghCTVL6QBNadGsiN/lWLl4JdR3lJkZzlpFdiHijoVRdWeSWqM4y0t23c92HXKrgppoSV18XMxrWVdoSM3nuMHwxGhFyde05OdDtLpCv+jlWf5REAHHA201pAU6bJSZINyHDUTB+Beo28rRXSwSh3OUIvYwKNVeoBY+KwOJ7WnuTCUq1meE6GkKc4D/cXmgpOyW/1SmBz3XjVIi/zprZ0zf3qH5mkphtg6ksjKgKjmx1cXfZAAX6wcDBNaCL+Ortep1Dh8xDUbqbBVNBL4jbiL3i3xsfNiyJgaZ5sX7i8tmStEpLbPwvHcByuf59qJhV/bZOl8KqJBETCDJcY6O2aqhTUy+9x93ThKs1GKrRPePrWPluud7ttlgtRveit/pcBrnQcXOl1rHq7ByB8CFAxNotRUYL9IF5n3wJOgkPojMy6jetQA5Ogc8Sm7RG6vg1yow== 
    ```

    可用

    ```
    D6KY031L1G-eyJsaWNlbnNlSWQiOiJENktZMDMxTDFHIiwibGljZW5zZWVOYW1lIjoi5o6I5p2D5Luj55CG5ZWGOiB3d3cuaTkub3JnIiwiYXNzaWduZWVOYW1lIjoiIiwiYXNzaWduZWVFbWFpbCI6IiIsImxpY2Vuc2VSZXN0cmljdGlvbiI6IiIsImNoZWNrQ29uY3VycmVudFVzZSI6ZmFsc2UsInByb2R1Y3RzIjpbeyJjb2RlIjoiSUkiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTIyIiwicGFpZFVwVG8iOiIyMDIwLTA3LTIxIn0seyJjb2RlIjoiQUMiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTIyIiwicGFpZFVwVG8iOiIyMDIwLTA3LTIxIn0seyJjb2RlIjoiRFBOIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0yMiIsInBhaWRVcFRvIjoiMjAyMC0wNy0yMSJ9LHsiY29kZSI6IlBTIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0yMiIsInBhaWRVcFRvIjoiMjAyMC0wNy0yMSJ9LHsiY29kZSI6IkdPIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0yMiIsInBhaWRVcFRvIjoiMjAyMC0wNy0yMSJ9LHsiY29kZSI6IkRNIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0yMiIsInBhaWRVcFRvIjoiMjAyMC0wNy0yMSJ9LHsiY29kZSI6IkNMIiwiZmFsbGJhY2tEYXRlIjoiMjAxOS0wNy0yMiIsInBhaWRVcFRvIjoiMjAyMC0wNy0yMSJ9LHsiY29kZSI6IlJTMCIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJSQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJSRCIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJQQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJSTSIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJXUyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJEQiIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJEQyIsImZhbGxiYWNrRGF0ZSI6IjIwMTktMDctMjIiLCJwYWlkVXBUbyI6IjIwMjAtMDctMjEifSx7ImNvZGUiOiJSU1UiLCJmYWxsYmFja0RhdGUiOiIyMDE5LTA3LTIyIiwicGFpZFVwVG8iOiIyMDIwLTA3LTIxIn1dLCJoYXNoIjoiMTM3ODQzMjAvMCIsImdyYWNlUGVyaW9kRGF5cyI6NywiYXV0b1Byb2xvbmdhdGVkIjpmYWxzZSwiaXNBdXRvUHJvbG9uZ2F0ZWQiOmZhbHNlfQ==-hBov92HEvNGvBzS2z190KAPxc9F6XY6jT1daMLlPrpCSEAdQX/955WkyGz+hCa3w/aeNExMEZIv2tALkFDOt857w4PZM8oYZ07s7My1NL7DxX9coFswbC6IIBijkAne9cPV9fSnGt5XcfsAkrF8KW1gj21H4EZGR6Jm4Cn7/j37rG1ASu2uvdoJ4dgCicfi78fvIw+zVvGm7L4cMjsmsilNNrUFPpDuVCp2kfU2ncDWm/M0lu+Dfeo3UO61/ICs9FvYAw0V4d8Q6pExzoqbAGH0IgkrHKJ2YQpKKOz3/+w4SGKhAX+85XYmfmLcUoqAZWaI95yhXN/czf/eeAf3ZEg==-MIIElTCCAn2gAwIBAgIBCTANBgkqhkiG9w0BAQsFADAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBMB4XDTE4MTEwMTEyMjk0NloXDTIwMTEwMjEyMjk0NlowaDELMAkGA1UEBhMCQ1oxDjAMBgNVBAgMBU51c2xlMQ8wDQYDVQQHDAZQcmFndWUxGTAXBgNVBAoMEEpldEJyYWlucyBzLnIuby4xHTAbBgNVBAMMFHByb2QzeS1mcm9tLTIwMTgxMTAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcQkq+zdxlR2mmRYBPzGbUNdMN6OaXiXzxIWtMEkrJMO/5oUfQJbLLuMSMK0QHFmaI37WShyxZcfRCidwXjot4zmNBKnlyHodDij/78TmVqFl8nOeD5+07B8VEaIu7c3E1N+e1doC6wht4I4+IEmtsPAdoaj5WCQVQbrI8KeT8M9VcBIWX7fD0fhexfg3ZRt0xqwMcXGNp3DdJHiO0rCdU+Itv7EmtnSVq9jBG1usMSFvMowR25mju2JcPFp1+I4ZI+FqgR8gyG8oiNDyNEoAbsR3lOpI7grUYSvkB/xVy/VoklPCK2h0f0GJxFjnye8NT1PAywoyl7RmiAVRE/EKwIDAQABo4GZMIGWMAkGA1UdEwQCMAAwHQYDVR0OBBYEFGEpG9oZGcfLMGNBkY7SgHiMGgTcMEgGA1UdIwRBMD+AFKOetkhnQhI2Qb1t4Lm0oFKLl/GzoRykGjAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBggkA0myxg7KDeeEwEwYDVR0lBAwwCgYIKwYBBQUHAwEwCwYDVR0PBAQDAgWgMA0GCSqGSIb3DQEBCwUAA4ICAQAF8uc+YJOHHwOFcPzmbjcxNDuGoOUIP+2h1R75Lecswb7ru2LWWSUMtXVKQzChLNPn/72W0k+oI056tgiwuG7M49LXp4zQVlQnFmWU1wwGvVhq5R63Rpjx1zjGUhcXgayu7+9zMUW596Lbomsg8qVve6euqsrFicYkIIuUu4zYPndJwfe0YkS5nY72SHnNdbPhEnN8wcB2Kz+OIG0lih3yz5EqFhld03bGp222ZQCIghCTVL6QBNadGsiN/lWLl4JdR3lJkZzlpFdiHijoVRdWeSWqM4y0t23c92HXKrgppoSV18XMxrWVdoSM3nuMHwxGhFyde05OdDtLpCv+jlWf5REAHHA201pAU6bJSZINyHDUTB+Beo28rRXSwSh3OUIvYwKNVeoBY+KwOJ7WnuTCUq1meE6GkKc4D/cXmgpOyW/1SmBz3XjVIi/zprZ0zf3qH5mkphtg6ksjKgKjmx1cXfZAAX6wcDBNaCL+Ortep1Dh8xDUbqbBVNBL4jbiL3i3xsfNiyJgaZ5sX7i8tmStEpLbPwvHcByuf59qJhV/bZOl8KqJBETCDJcY6O2aqhTUy+9x93ThKs1GKrRPePrWPluud7ttlgtRveit/pcBrnQcXOl1rHq7ByB8CFAxNotRUYL9IF5n3wJOgkPojMy6jetQA5Ogc8Sm7RG6vg1yow==
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

## Postman

下载postman的linux版本    

    https://www.getpostman.com/

    wget https://dl.pstmn.io/download/latest/linux64 -O postman.tar.gz

解压

    sudo tar -xzf postman.tar.gz -C /opt/

vim建立文件路径

```
sudo vim  /usr/share/applications/postman.desktop

[Desktop Entry]
Encoding=UTF-8
Name=postman
Exec=/opt/Postman/Postman
Icon=/opt/Postman/app/resources/app/assets/icon.png
Terminal=false
Type=Application
Categories=Development;

sudo chmod +x /usr/share/applications/postman.desktop
    
打开applications文件夹，把postman.desktop文件拖动到Launcher条上sudo nautilus /usr/share/applications
```

## Kibana 6.3.2

1. 下载 LINUX 64-BIT 的 kibana-6.3.2-linux-x86_64.tar.gz

    ```
    proxychains wget https://artifacts.elastic.co/downloads/kibana/kibana-6.3.2-linux-x86_64.tar.gz

    官网 https://www.elastic.co/cn/downloads/kibana
    ```

2. 解压

    ```bash
    sudo tar -zxvf kibana-6.3.2-linux-x86_64.tar.gz -C /opt/
    sudo mv /opt/kibana-6.3.2-linux-x86_64 /opt/kibana-6.3.2
    ```

3. 配置系统环境变量

    ```bash
    sudo vim /etc/profile

    在末尾添加以下几行文字（添加错了可能导致无限循环登录）
    # set kibana environment
    export KIBANA_HOME=/opt/kibana-6.3.2
    export PATH=$PATH:${KIBANA_HOME}/bin
    ```

4. 使生效

    ```bash
    source /etc/profile //在当前terminal下生效
    或
    logout->login //在当前用户下生效
    ```

5. 配置 config/kibana.yml，确保如下

    ```bash
    server.port: 5601
    server.host: "localhost"
    elasticsearch.url: "http://192.168.100.57:9200"
    ```

6. 打开 命令提示行 验证一下

    ```bash
    kibana
    ``` 

## Zookeeper

1. 下载

    `https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/`

2. 解压

    ```bash
    sudo tar -zxvf zookeeper-3.4.14.tar.gz -C /opt/
    ```

3. 配置系统环境变量

    ```bash
    sudo vim /etc/profile

    在末尾添加以下几行文字（添加错了可能导致无限循环登录）
    # set zookeeper environment
    export ZOOKEEPER_HOME=/opt/zookeeper-3.4.14
    export PATH=$PATH:${ZOOKEEPER_HOME}/bin
    ```

4. 使生效

    ```bash
    source /etc/profile //在当前terminal下生效
    或
    logout->login //在当前用户下生效
    ```

5. 打开 命令提示行 验证一下

    ```bash
    zkCli.sh -server 192.168.100.55:2181
    ```

## SSH远程登录服务器

1. 生成秘钥 (如果不存在)

    ssh-keygen -t rsa

2. 免密登录方法
    
2.1 通过ssh-copy-id的方式（推荐）
    
    ssh-copy-id -i ~/.ssh/id_rsa.pub root@<remote_ip>

2.2　通过scp将内容写到对方的文件中（不推荐，会覆盖原文件）

    scp -p ~/.ssh/id_rsa.pub root@<remote_ip>:/root/.ssh/authorized_keys

    scp -P <port> ~/.ssh/id_rsa.pub root@<remote_ip>:/root/.ssh/authorized_keys

3. 设置别名

    vim ~/.bashrc

    添加如下

    ```bash
    alias 99='ssh root@192.168.100.99'
    alias 135='ssh root@192.168.100.135'
    alias 140='ssh root@192.168.100.140'
    alias terry='ssh root@118.24.149.18'
    alias wall='ssh root@97.64.21.41'
    alias aliyun='ssh pifeng@jms.holderzone.com'
    alias hwyun='ssh dev@49.4.11.170'
    alias ctyun='echo todo'
    ```

    立即生效

    source ~/.bashrc

## 抓包

### Wireshark

应用商店下载安装即可

报错：Couldn't run /usr/bin/dumpcap in child process: 权限不够解决办法

解决：
```
sudo apt-get install libcap2-bin wireshark 
sudo chgrp terry /usr/bin/dumpcap 
sudo chmod 750 /usr/bin/dumpcap 
sudo setcap cap_net_raw,cap_net_admin+eip /usr/bin/dumpcap
```

### Charles

https://blog.csdn.net/huuinn/article/details/82762952