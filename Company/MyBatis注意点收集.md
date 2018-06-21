# Mybatis注意点收集

## Mybatis没有扫描jar中的Mapper接口

在spring boot启动类上加上注解，并指定jar包中接口文件包路径即可

```text
@MapperScan(basePackages = "com.xx.**.dao")
```

如此com.xx包下的任意级子目录下的dao包下的所有接口都会被扫描到，包括jar包中的。

详见[出处](https://www.cnblogs.com/flying607/p/8507546.html)

## Mybatis没有扫描jar中的Bean和Mapper

问题描述：

相同配置直接在ide上运行都是ok的，但是经过打包成jar包后，就一直报错提示找打不到对应的bean或者对应的mapper。

* 可能原因一：

bean的扫描是通过ResolverUtil这个类进行的，并且ResolverUtil扫描是通过VFS进行扫描的，工程上默认使用的是Mybatis的DefaultVFS进行扫描。

```text
@Bean(name = "sqlSessionFactory")
@ConditionalOnMissingBean
public SqlSessionFactory sqlSessionFactory() throws Exception {
    // 添加如下这一句
    VFS.addImplClass(SpringBootVFS.class);
    // 原来配置
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//    // 或这添加这一句
//    factoryBean.setVfs(SpringBootVFS.class);
    factoryBean.setDataSource(datasource());
    factoryBean.setTypeAliasesPackage(typeAliasesPackage);
    factoryBean.setMapperLocations(mapperLocations);
    return factoryBean.getObject();
}
```

详见

[出处1](https://blog.csdn.net/cml_blog/article/details/53138851)

[出处2](https://blog.csdn.net/doctor_who2004/article/details/70163144)

但是测试均未成功。

* 可能原因二

依赖Jar以Spring-boot-maven-plugin进行的打包，目录结构是BOOT-INF下才是字节码文件，而以普通mavne插件构建的话，目录结构是字节码文件在最外层

解决：

去掉parent的pom.xml和被依赖jar的pom.xml中的如下插件

```text
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

亲测成功！！！！

## Maven合并打包方法

以下插件表示将payment-fmbk-infrastructure-1.0.0.jar解压至当前Module的target/classes目录中，详见[出处](https://blog.csdn.net/hpb21/article/details/51284640)

```text
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>unpack</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>unpack</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>com.holderzone.framework</groupId>
                        <artifactId>payment-fmbk-infrastructure</artifactId>
                        <version>1.0.0</version>
                        <type>jar</type>
                        <overWrite>true</overWrite>
                        <outputDirectory>./target/classes</outputDirectory>
                    </artifactItem>
                </artifactItems>
            </configuration>
        </execution>
    </executions>
</plugin>
```