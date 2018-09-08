# SonarQube 基础使用

## 基础扫盲

[SonarSource](https://www.sonarsource.com/) ：一个公司/组织

[SonarQube](https://www.sonarqube.org/) ：一款可独立运行的代码审查服务。

[SonarLint](https://www.sonarlint.org/) ：一款可用于IDEA等工具的Lint插件，帮助修复代码问题

## SonarQube服务介绍
SonarQube是一个开源的平台，以执行与代码的静态分析，自动审查，以检测在20种+编程语言如Java，C＃，JavaScript中，打字稿，C / C ++，COBOL和错误，代码味道和安全漏洞更多。SonarQube是市场上唯一支持泄漏方法作为编码质量实践的产品。

[SonarQube文档](https://docs.sonarqube.org/display/SONAR/Documentation) 

[Maven集成SonarQubeScanner以执行代码分析](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven) 

[Gradle集成SonarQubeScanner以执行代码分析](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle) 

[Jenkins集成SonarQubeScanner以执行代码分析](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Jenkins) 

[Java项目集成SonarJava插件](https://docs.sonarqube.org/display/PLUG/SonarJava) 

## SonarQube使用上手


1、SSH登录118测试服务器
```
ssh root@118.31.38.15
```
2、在路径/opt使用git clone命令拉取需要分析的项目
```
git clone http://101.37.252.104/holderRD/holder-java/operation-master.git
```
3、完毕后拷贝同目录下其他项目(如operation-master)下的sonar-project.properties文件至当前项目根目录下
```
cp /opt/operation-master/sonar-project.properties /opt/some-project
```
4、使用vim修改该properties文件的"sonar.projectKey"、 "sonar.projectName"属性后保存修改并退出。
```
cd /opt/some-project
vim sonar-project.properties
```
5、于当前项目根目录执行scanner分析
```
sonar-scanner
```
6、打开[Sonar服务地址](http://118.31.38.15:9000/projects) ，即可看到分析结果。

## SonarQube使用上手之idea中使用maven

方法一：Terminal中使用以下命令。url应使用正确地址，Dsonar.login应使用正确token

```
mvn sonar:sonar \
  -Dsonar.host.url=http://118.31.38.15:9000 \
  -Dsonar.login=e38122ff22c63e4731e32b03aee7ca304d9f7c42
```

方法二：

右键项目 
- Run Maven
- New Goal...
- 输入 
```
sonar:sonar -Dsonar.host.url=http://118.31.38.15:9000 -Dsonar.login=e38122ff22c63e4731e32b03aee7ca304d9f7c42
```

方法三：

在maven/conf/settings.xml中配置

在 profiles 标签中添加
```
<profile>
<id>sonar</id>
<properties>
<sonar.jdbc.url>jdbc:mysql://118.31.38.15:3306/sonar</sonar.jdbc.url>
<sonar.jdbc.driver>com.mysql.jdbc.Driver</sonar.jdbc.driver>
<sonar.jdbc.username>sonar</sonar.jdbc.username>
<sonar.jdbc.password>sonar</sonar.jdbc.password>
<sonar.host.url>http://118.31.38.15:9000</sonar.host.url>
</properties>
</profile>
```

在 activeProfiles 标签中添加
```
<activeProfile>sonar</activeProfile>
```

右键项目

初次审查
- Run Maven
- New Goal
- 输入 sonar:sonar

若已有Goal
- Run Maven
- sonar:sonar

## Jenkins构建增加步骤SonarQube Scanner（最方便）

- 打开[Jenkins服务地址](http://192.168.100.135:10090/) ，在All标签下选择一个项目(opmaster-build)。
- 打开配置，滑动到构建环境区域， Prepare SonarQube Scanner environment
- 滑动到构建区域，增加构建步骤，选择 Execute SonaQube Scanner，填入以下参数
```
Task to run: scan
```
```
JDK: jdk1.8.0 151
```
```
Path to project properties: 不填
```
```
Analysis properties: 
sonar.projectKey=data-sync（must be unique in a given SonarQube instance）
sonar.projectName=data-sync（this is the name displayed in the SonarQube UI）
sonar.projectVersion=1.0（随意）
sonar.sources=${WORKSPACE}
sonar.sourceEncoding=UTF-8
sonar.language=java
sonar.login=admin
sonar.password=admin
sonar.my.property=value
sonar.java.binaries=/var/jenkins_home/build
```
```
Additional arguments:不填
```
```
JVM Options:不填
```
- 保存生效即可

## SonarLint 插件基础使用

### 下载安装

- IDEA Settings 
- Plugin 
- Broswer Repositories 
- 输入框键入SonarLint 
- 下载即可

### 代码自动审查

- Settings 
- Other Settings 
- SonarLint General Settings 
- 选中Automatically trigger analysis
SonarLint会检测代码改动，并实时地进行审查。
在界面下方Tool Buttons标签中能看到具体的问题代码。

### 拉取服务端审查规则至IDEA

- Settings 
- Other Settings 
- SonarLint General Settings 
- SonarQube servers 点击+号新增服务配置
成功后点击下方Update binding

### 项目绑定到SonarQube服务，并使用某个项目的规则

- Settings 
- Other Settings 
- SonarLint Project Settings 
- 选中 Enable binding to remote SonarQube server
- Bind to server 一栏选择一个服务端
- SonarQube project 一栏选择需绑定的项目

## SonarQube 插件基础使用

略






