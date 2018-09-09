# Spring Cloud Netflix Eureka Integration 集成

## EurekaServer集成

```pom
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

```yml
server：
  port：8761
eureka：
  instance：
    hostname：localhost
  client：
    registerWithEureka：false
    fetchRegistry：false
    serviceUrl：
      defaultZone：http://${eureka.instance.hostname}:${server.port}/eureka/
```

```java
@EnableEurekaServer
@SpringBootApplication
public class PaymentFmbkRegistryApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentFmbkRegistryApplication.class, args);
  }
}
```

### EurekaServer配置解析

eureka.server.enable-self-preservation # 自我保护，默认true
eureka.server.eviction-interval-timer-in-ms # 驱逐间隔计时器，默认60*1000
eureka.instance.hostname # 注册中心主机Host
eureka.instance.lease-expiration-duration-in-seconds # 租约续订间隔（即心跳间隔），默认90
eureka.instance.lease-renewal-interval-in-seconds # # 租约到期时间（即剔除时间），默认30
eureka.client.registerWithEureka # 是否注册到Eureka，默认true
eureka.client.fetchRegistry # 是否拉取注册信息，默认true
eureka.client.serviceUrl.defaultZone # 为Client指定注册中心地址

作为服务中心，在独立模式下，您可能更愿意关闭客户端行为，以便它不会继续尝试并且无法访问其对等方。
以下示例显示如何关闭客户端行为：
eureka.client.registerWithEureka=false
eureka.client.fetchRegistry=false

### EurekaServer单机

```yml
server:
  port: 8761
eureka:
  server:
    enable-self-preservation: true
    eviction-interval-timer-in-ms: 60000
  instance:
    hostname: localhost
    lease-expiration-duration-in-seconds: 90
    lease-renewal-interval-in-seconds: 30
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

### EurekaServer集群

```yml
# 注册中心实例1
spring:
  profiles: peer1
eureka:
  instance:
    hostname: peer1
  client:
    serviceUrl:
      defaultZone: http://peer2/eureka/

# 注册中心实例2
spring:
  profiles: peer2
eureka:
  instance:
    hostname: peer2
  client:
    serviceUrl:
      defaultZone: http://peer1/eureka/
```

## EurekaClient集成

```pom
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

```java
@EnableEurekaClient
@SpringBootApplication
public class SomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentFmbkRegistryApplication.class, args);
    }
}
```

### EurekaClient配置解析

eureka.client.serviceUrl.defaultZone # 注册中心地址
eureka.instance.leaseRenewalIntervalInSeconds # 租约续订间隔（即心跳间隔）
eureka.instance.leaseExpirationDurationInSeconds # 租约到期时间（即剔除时间）

作为实例还涉及到注册表（通过客户端serviceUrl）的定期心跳，默认持续时间为30秒。
在实例，服务器和客户端在其本地缓存中都具有相同的元数据之前，客户端无法发现服务（因此可能需要3次心跳）。
您可以通过设置更改期间eureka.instance.leaseRenewalIntervalInSeconds。
将其设置为小于30的值可加快使客户端连接到其他服务的过程。
在生产中，最好坚持使用默认值，因为服务器中的内部计算会对租赁续订期做出假设。

### EurekaClient单机常规配置

1. UAT环境

    ```yml
    eureka:
      instance:
        lease-expiration-duration-in-seconds: 10
        lease-renewal-interval-in-seconds: 5
      client:
        serviceUrl:
          defaultZone: http://localhost:8761/eureka/
    ```

2. Prod环境

    ```yml
    eureka:
      instance:
        lease-expiration-duration-in-seconds: 90 # 默认值90
        lease-renewal-interval-in-seconds: 30 # 默认值30
      client:
        serviceUrl:
          defaultZone: http://localhost:8761/eureka/
    ```

### EurekaClient集群常规配置

1. UAT环境

    TODO

2. Prod环境

    TODO
