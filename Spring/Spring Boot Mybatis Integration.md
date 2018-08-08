# Spring Boot Mybaits 集成

## Mybatis Spring Boot Starter

spring-boot-starter Github地址

https://github.com/mybatis/spring-boot-starter

spring-boot-starter docs地址

http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/index.html#

1. Started

    导入依赖

    ```pom
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.2</version>
    </dependency>
    ```

    最简配置

    ```yml
    mybatis:
        type-aliases-package: com.example.domain.model
        mapperLocations: classpath:mapper/*.xml
    ```

2. 打印日志

    ```yml
    mybatis:
        configuration:
            log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    ```

    第三方参考资料

    mybatis打印sql日志 https://www.cnblogs.com/zhangmms/p/8973068.html

3. 开启二级缓存

    ```yml
    mybatis:
        configuration:
            cache-enabled: true #默认即为true
    ```

    ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="com.example.ExampleMapper">
        <cache/>

        <select id="xxx" resultType="String" useCache="true"
            parameterType="com.example.ExampleDO">
            <!--other sql-->
        </select>

        <insert id="insert" useGeneratedKeys="true" keyProperty="id" flushCache="true"
                parameterType="com.example.ExampleDO">
            <!--other sql-->
        </insert>

        <update id="update" flushCache="true"
                parameterType="com.example.ExampleDO">
            <!--other sql-->
        </update>

        <delete id="delete" flushCache="true"
                parameterType="com.example.ExampleDO">
            <!--other sql-->
        </delete>

    </mapper>
    ```

    第三方参考资料

    springBoot中配置mybatis的二级缓存 https://blog.csdn.net/qq_28929589/article/details/79127268
