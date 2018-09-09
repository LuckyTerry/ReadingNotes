# Spring Boot Starter Sample

## 创建Starter项目

新建Maven项目，引入如下依赖：

```java
<!-- 自定义starter都应该继承自该依赖 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starters</artifactId>
    <version>2.0.0.RELEASE</version>
</parent>

<dependencies>

    <!-- 自定义starter依赖此jar包 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!--Generating Your Own Metadata by Using the Annotation Processor-->
    <!--https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- lombok用于自动生成get、set方法 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.16.10</version>
    </dependency>
</dependencies>
```

### 编写Properties

```java
@ConfigurationProperties(prefix = "http")
public class HttpProperties {

    private String url = "http://www.baidu.com/";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
```

### 编写AutoConfiguration

```java
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {

    @Resource
    private HttpProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public HttpClient init() {
        HttpClient client = new HttpClient();
        client.setUrl(properties.getUrl());
        return client;
    }
}
```

### 编写业务组件

```java
public class HttpClient {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        try {
            URL url = new URL(this.url);
            URLConnection urlConnection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
```

### Resources 目录下添加 META-INF -> spring.factories

```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.terry.starter.HttpAutoConfiguration
```

### 生成 Meta-Data

由于导入了 spring-boot-configuration-processor 依赖，编译（compile/install/package etc）后 target 中即会出现 spring-configuration-metadata.json，该文件负责 properties 属性名的识别。

## 发布到Maven仓库

略

## 打包为Jar

```java
mvn clean package
```

## 测试项目导入

方法一 Project Structure -> Dependencies -> + -> JARs or directories.

方法二 根目录新建libs文件夹，拷贝jar到该文件夹，右键 Add as Library.

application.properties 中使用即可。