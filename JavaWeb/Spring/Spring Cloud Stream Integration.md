# Spring Cloud Stream 集成

## 常见线程命名

1. reactor-http-server-epoll

    服务端的事件驱动线程，默认有4个，不可执行耗时操作，阻塞Event-Loop。形如：

    ```java
    reactor-http-server-epoll-1
    reactor-http-server-epoll-2
    reactor-http-server-epoll-3
    reactor-http-server-epoll-4
    ```

    以下代码毫无疑问，阻塞Event-Loop，禁止！！！

    ```java
    @GetMapping("/test")
    public Mono<String> getPreUser(@PathVariable("id") String id) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.just(id);
    }
    ```

    然后以下代码也会阻塞Event-Loop，因为sql查询是耗时操作，禁止！！！需要将query语句包装在ThreadPool中执行（无奈，mysql还不支持异步）。

    ```java
    @GetMapping("/test")
    public Mono<String> getPreUser(@PathVariable("id") String id) {
        return mysqlService.query(id);
    }
    ```

2. reactor-http-client-epoll

    WebClient的事件驱动线程，默认有2个，不可执行耗时操作，阻塞Event-Loop。形如：

    ```java
    reactor-http-client-epoll-1
    reactor-http-client-epoll-2
    ```

    以下代码毫无疑问，阻塞Event-Loop，禁止！！！

    ```java
    @Override
    public Mono<String> getName(String id) {
        return webClientBuilder.build()
                .method(HttpMethod.GET)
                .uri("http://arch-provider/user/id/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(s -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
    ```

    然后以下代码也会阻塞Event-Loop，因为sql插入是耗时操作，禁止！！！需要将insert语句包装在ThreadPool中执行（无奈，mysql还不支持异步）

    ```java
    @Override
    public Mono<String> getName(String id) {
        return webClientBuilder.build()
                .method(HttpMethod.GET)
                .uri("http://arch-provider/user/id/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    remoteService.getName(id)
        .flatMap(userDTO -> {
            UserDO userDO = new UserDO(userDTO.getId(), userDTO.getName());
            return userService.insert(userDO);
        })
    ```

3. main

    响应式的处理通道，申明式代码，在项目启动的时候就执行了，所以执行在main线程。Flux内部才是真正消费消息时的逻辑。

    ```java
    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<Boolean> receive(@Input(Processor.INPUT) Flux<String> input) {
        // 如下打印语句： 'receive方法执行线程:main'
        System.out.println("receive方法执行线程:"+Thread.currentThread().getName());
        return Flux.just(false);
    }
    ```

## 生产者、消费者配置

指定生成的消息队列的routing key:

```spring.cloud.stream.rabbit.bindings.[channelName].consumer.bindingRoutingKey```

指定生产者消息投递的routing key（此处真是大坑啊！）:

```spring.cloud.stream.rabbit.bindings.[channelName].producer.routing-key-expression```


```yml
spring:
  cloud:
    stream:
      rabbit:
        bindings:
          input:
            consumer:
              bindingRoutingKey: default-routing-key
          output:
            producer:
              routing-key-expression: '''default-routing-key'''
      binders:
        rabbitmq:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                addresses: localhost
                username: guest
                password: guest
                virtual-host: /
      bindings:
        input:
          destination: default-exchange
          group: default-queue
          binder: rabbitmq
          consumer:
            concurrency: 10
        output:
          destination: default-exchange
          content-type: text/plain # 或application/json
          binder: rabbitmq
```

## 配合其他Reactive组件

### Spring Data Reactive Redis

lettuce-epollEventLoop