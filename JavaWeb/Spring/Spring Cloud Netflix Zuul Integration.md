# Spring Cloud Netflix Zuul 集成

## 1. 导入

```pom
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
```

## 2. Zuul Http 客户端

Zuul使用的默认HTTP客户端现在由Apache HTTP Client而不是已弃用的功能区支持RestClient。分别使用RestClient或okhttp3.OkHttpClient设置ribbon.restclient.enabled=true或ribbon.okhttp.enabled=true。如果要自定义Apache HTTP客户端或OK HTTP客户端，请提供类型为ClosableHttpClient或的bean OkHttpClient。

### 3. SensitiveHeaders(敏感标题)

zuul.sensitiveHeaders=Cookie，Set-Cookie，Authorization

该sensitiveHeaders是一个黑名单，默认是不为空。
因此，要使Zuul发送所有标头（除了ignored那些标头），您必须将其明确设置为空列表。

```yml
zuul:
  routes:
    users:
      path: / myusers / **
      sensitiveHeaders:

zuul:
  sensitiveHeaders:
```

### 4. IgnoredHeaders(忽略的标题)

```yml
zuul:
  ignoredHeaders:
```

### 5. 管理端点

### 6. 为指定路由提供 Hystrix Fallbacks

```yml
zuul:
  routes:
    customers: /customers/**
```

```java
class MyFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
        return "customers";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, final Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("fallback".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
```

### 7. Zuul超时设置

如果要为通过Zuul代理的请求配置套接字超时和读取超时，则有两种选择，具体取决于您的配置：

- 如果Zuul使用服务发现，则需要使用ribbon.ReadTimeout和ribbon.SocketTimeout功能区属性配置这些超时 。

- 如果您通过指定URL配置了Zuul路由，则需要使用 zuul.host.connect-timeout-millis和zuul.host.socket-timeout-millis。