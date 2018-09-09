# Spring Cloud Turbine 集成

```yml
eureka:
  instance:
    status-page-url-path: /actuator/info
    health-check-url-path: /actuator/health
    metadata-map:
      cluster: default

turbine:
  app-config: ${spring.application.name}, payment-fmbk-mchnt-v1
  aggregator:
    clusterConfig: default
  clusterNameExpression: new String("default")
  combine-host: true
  instanceUrlSuffix:
    default: actuator/hystrix.stream
management:
  endpoints:
    web:
      exposure:
        include: "*"
      cors:
        allowed-origins: "*"
        allowed-methods: "*"
```