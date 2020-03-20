# Maven使用FAQ

> 推荐配置

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
	  <url>http://nexus.holderzone.cn/nexus/content/repositories/snapshots/</url>
	  <snapshots><enabled>true</enabled></snapshots>
	</repository>
	<repository>
	  <id>thirdparty</id>
	  <name>Nexus ThirdParty</name>
	  <url>http://nexus.holderzone.cn/nexus/content/repositories/thirdparty/</url>
	</repository>
	<repository>
	  <id>releases</id>
	  <name>Nexus Releases</name>
	  <url>http://nexus.holderzone.cn/nexus/content/repositories/releases/</url>
	</repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
	  <id>snapshots</id>
	  <name>Nexus Snapshots</name>
	  <url>http://nexus.holderzone.cn/nexus/content/repositories/snapshots/</url>
	  <snapshots><enabled>true</enabled></snapshots>
	</pluginRepository>
	<pluginRepository>
	  <id>thirdparty</id>
	  <name>Nexus ThirdParty</name>
	  <url>http://nexus.holderzone.cn/nexus/content/repositories/thirdparty/</url>
	</pluginRepository>
	<pluginRepository>
	  <id>releases</id>
	  <name>Nexus Releases</name>
	  <url>http://nexus.holderzone.cn/nexus/content/repositories/releases/</url>
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

> Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:2.22.2:test (default-test) on project feign-spring-boot-starter: There are test failures

解决办法：

一是命令行，

```
mvn clean package -Dmaven.test.skip=true
```

二是写入pom文件，

```pom
<plugin>  
        <groupId>org.apache.maven.plugins</groupId>  
        <artifactId>maven-surefire-plugin</artifactId>  
        <version>2.22.2</version>  
        <configuration>  
          <skipTests>true</skipTests>  
        </configuration>  
</plugin>  
```

> 无法nexus仓库

```
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
  ```