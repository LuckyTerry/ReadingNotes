# Swagger2在线文档配置

1. 略

略

2. 坑

巨坑，groupName不能使用中文，否则 swagger-ui.html 点击 /v2/api-docs 链接跳转的时候会跳转到错误页面（因为链接中的中文被截掉了）

```java
@Bean
public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .groupName("holder-saas-covid-form")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.holderzone.saas.covid.form.controller"))
            .paths(PathSelectors.any())
            .build();
}
```


# Swagger2离线文档配置

1. 配置仓库 

```xml
<repositories>
	<repository>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
		<id>jcenter-releases</id>
		<name>jcenter</name>
		<url>https://jcenter.bintray.com</url>
	</repository>
	<repository>
		<id>jcenter-snapshots</id>
		<name>jcenter</name>
		<url>http://oss.jfrog.org/artifactory/oss-snapshot-local/</url>
	</repository>
	<repository>
		<id>spring-snapshots</id>
		<name>Spring Snapshots</name>
		<url>https://repo.spring.io/snapshot</url>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</repository>
	<repository>
		<id>spring-milestones</id>
		<name>Spring Milestones</name>
		<url>https://repo.spring.io/milestone</url>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
</repositories>
```

2. 配置插件仓库 

```xml
<pluginRepositories>
	<pluginRepository>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
		<id>jcenter-releases</id>
		<name>jcenter</name>
		<url>https://jcenter.bintray.com</url>
	</pluginRepository>
	<pluginRepository>
		<id>jcenter-snapshots</id>
		<name>jcenter</name>
		<url>http://oss.jfrog.org/artifactory/oss-snapshot-local/</url>
	</pluginRepository>
	<pluginRepository>
		<id>spring-snapshots</id>
		<name>Spring Snapshots</name>
		<url>https://repo.spring.io/snapshot</url>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</pluginRepository>
	<pluginRepository>
		<id>spring-milestones</id>
		<name>Spring Milestones</name>
		<url>https://repo.spring.io/milestone</url>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</pluginRepository>
</pluginRepositories>
```

3. 添加依赖

```xml
<dependencies>
    <!-- swagger2markup 相关依赖 -->
    <dependency>
        <groupId>io.github.swagger2markup</groupId>
        <artifactId>swagger2markup</artifactId>
        <version>1.3.3</version>
    </dependency>
    <!--离线文档 -->
    <dependency>
        <groupId>org.springframework.restdocs</groupId>
        <artifactId>spring-restdocs-mockmvc</artifactId>
        <scope>test</scope>
    </dependency>
    <!--springfox-staticdocs 生成静态文档 -->
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-staticdocs</artifactId>
        <version>2.6.1</version>
    </dependency>
</dependencies>
```

4. 添加 plugin，配置 swaggerInput 为对应 group 的地址

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.swagger2markup</groupId>
            <artifactId>swagger2markup-maven-plugin</artifactId>
            <version>1.3.1</version>
            <configuration>
                <swaggerInput>http://127.0.0.1:9001/v2/api-docs?group=holder-saas-covid-form</swaggerInput>
                <outputFile>./docs/asciidoc/generated/index</outputFile>
<!--                    <outputDir>./docs/asciidoc/generated</outputDir>-->
                <config>
                    <swagger2markup.markupLanguage>ASCIIDOC</swagger2markup.markupLanguage>
                </config>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctor-maven-plugin</artifactId>
            <version>1.5.6</version>
            <!-- <version>2.0.0-RC.1</version> -->
            <!-- Include Asciidoctor PDF for pdf generation -->
            <dependencies>
                <dependency>
                    <groupId>org.asciidoctor</groupId>
                    <artifactId>asciidoctorj-pdf</artifactId>
                    <version>1.5.0-alpha.10.1</version>
                </dependency>
                <dependency>
                    <groupId>org.jruby</groupId>
                    <artifactId>jruby-complete</artifactId>
                    <version>1.7.26</version>
                </dependency>
            </dependencies>
            <configuration>
                <sourceDirectory>./docs/asciidoc/generated</sourceDirectory>
                <outputDirectory>./docs/asciidoc/html</outputDirectory>
                <backend>html</backend>
<!--                    <outputDirectory>./docs/asciidoc/pdf</outputDirectory>-->
<!--                    <backend>pdf</backend>-->
                <headerFooter>true</headerFooter>
                <doctype>book</doctype>
                <sourceHighlighter>coderay</sourceHighlighter>
                <attributes>
                    <!-- 菜单栏在左边 -->
                    <toc>left</toc>
                    <!-- 多标题排列 -->
                    <toclevels>3</toclevels>
                    <!-- 自动打数字序号 -->
                    <sectnums>true</sectnums>
                </attributes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

5. 执行

```mvn

# 生成 asciidoc 文档
mvn swagger2markup:convertSwagger2markup

# 生成 html 或 pdf 文档，取决于配置
mvn asciidoctor:process-asciidoc
```