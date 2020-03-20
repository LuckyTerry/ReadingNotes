# Spring Feign、Hystirx、Ribbon 超时时间

## Feign

### 1. feign的默认配置

- 对于默认值，可以使用 feign.client.config.default.. 的方式进行配置
- 对于每个feign客户端，可以使用 feign.client.config.<feginName>.. 的方式进行配置
- 该配置项在Spring Cloud中，使用FeignClientProperties类表示

```yml
feign:
  client:
    config:
        default:
            connectTimeout: 10000
            readTimeout: 60000
            loggerLevel: full
            errorDecoder: com.example.SimpleErrorDecoder
            retryer: com.example.SimpleRetryer
            requestInterceptors:
            - com.example.FooRequestInterceptor
            - com.example.BarRequestInterceptor
            decode404: false
            encoder: com.example.SimpleEncoder
            decoder: com.example.SimpleDecoder
            contract: com.example.SimpleContract
        <feignName>:
            connectTimeout: 5000
            readTimeout: 5000
            loggerLevel: full
            errorDecoder: com.example.SimpleErrorDecoder
            retryer: com.example.SimpleRetryer
            requestInterceptors:
            - com.example.FooRequestInterceptor
            - com.example.BarRequestInterceptor
            decode404: false
            encoder: com.example.SimpleEncoder
            decoder: com.example.SimpleDecoder
            contract: com.example.SimpleContract
```

### 2. Spring Cloud 加载feign配置项的原理：

2.0 入口

    FeignClientsConfiguration定义了Feign.Bulder的Bean

2.1 **feign初始化**的过程，其实就是构造Feign.Builder的过程

```java
// FeignClientFacotryBean#getObject 的 feign(context) 方法调用
public Object getObject() throws Exception {
    FeignContext context = applicationContext.getBean(FeignContext.class);
    Feign.Builder builder = feign(context);

    return ...;
}

protected Feign.Builder feign(FeignContext context) {
    FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
    Logger logger = loggerFactory.create(this.type);

    // @formatter:off
    Feign.Builder builder = get(context, Feign.Builder.class)
            // required values
            .logger(logger)
            .encoder(get(context, Encoder.class))
            .decoder(get(context, Decoder.class))
            .contract(get(context, Contract.class));
    // @formatter:on

    configureFeign(context, builder);

    return builder;
}
```

2.2 **FeignClientFacotryBean#configureFeign**中定义了 config的加载优先级

> 1. 检查是否Feign是否制定了上述的配置项，即是否有FeignClientProperties实例；
> 2. 如果有上述的配置项，则表明Feign是通过properties初始化的,即configureUsingProperties;
> 3. 根据配置项feign.client.defaultToProperties的结果，使用不同的配置覆盖策略。

```java
protected void configureFeign(FeignContext context, Feign.Builder builder) {
    FeignClientProperties properties = applicationContext.getBean(FeignClientProperties.class);
    if (properties != null) {
        if (properties.isDefaultToProperties()) {
            configureUsingConfiguration(context, builder);
            configureUsingProperties(properties.getConfig().get(properties.getDefaultConfig()), builder);
            configureUsingProperties(properties.getConfig().get(this.name), builder);
        } else {
            configureUsingProperties(properties.getConfig().get(properties.getDefaultConfig()), builder);
            configureUsingProperties(properties.getConfig().get(this.name), builder);
            configureUsingConfiguration(context, builder);
        }
    } else {
        configureUsingConfiguration(context, builder);
    }
}
```

- 场景1：没有通过配置文件配置
在这种模式下，将使用configureUsingConfiguration,此时将会使用Spring 运行时自动注入的Bean完成配置，默认是：
    ```yml
    connectTimeoutMillis = 10 * 1000
    readTimeoutMillis = 60 * 1000
    ```
- 场景2：配置了FeignClientProperties，并且配置了feign.client.defaultToProperties = true，此时的这种场景，其配置覆盖顺序如下所示：
configureUsingConfiguration---> configurationUsingPropeties("default")----> configurationUsingProperties("<client-name>")
- 场景3：配置了FeignClientProperties，并且配置了feign.client.defaultToProperties = false，此时的这种场景，配置覆盖顺序是：
configurationUsingPropeties("default")----> configurationUsingProperties("<client-name>")---> configureUsingConfiguration

2.3 **Request.Options** 中定义了 readTimeoutMillis 和 connectTimeoutMillis 的默认值

```java
public static class Options {
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    public Options(int connectTimeoutMillis, int readTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public Options() {
        this(10000, 60000);
    }

    public int connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public int readTimeoutMillis() {
        return this.readTimeoutMillis;
    }
}
```

## Hystirx

## Ribbon

ribbon-core
DefaultClientConfigImpl.DEFAULT_CONNECT_TIMEOUT
DefaultClientConfigImpl.DEFAULT_READ_TIMEOUT

open-feign
FeignOptionsClientConfig 构造函数


LoadBalanceFeignClient#execute
LoadBalanceFeignClient#getClientConfig

当 Ribbon 与 Feign 集成时，Feign 会使用 LoadBalancerFeignClient 来执行请求，并且根据 Ribbon的接口IClientConfig来设置参数，

LoadBalancerFeignClient

```java
public Response execute(Request request, Request.Options options) throws IOException {
        try {
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);

            // 某种条件下，根据 feign 配置 覆盖 ribbon 的配置
            IClientConfig requestConfig = getClientConfig(options, clientName);
            // 依次调用 ribbonLoadBalancer 的executeWithLoadBalancer，ribbon 的 execute
            return lbClient(clientName).executeWithLoadBalancer(ribbonRequest,
                    requestConfig).toResponse();
        }
        catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
    }
```

跳转到 AbstractLoadBalancerAwareClient

```java

public T executeWithLoadBalancer(final S request, final IClientConfig requestConfig) throws ClientException {
    // 构造LoadBalancerCommand，里面根据重试规则配置重试Handler
    LoadBalancerCommand command = this.buildLoadBalancerCommand(request, requestConfig);

    try {
        return (IResponse)command.submit(new ServerOperation<T>() {
                public Observable<T> call(Server server) {
                    URI finalUri = AbstractLoadBalancerAwareClient.this.reconstructURIWithServer(server, request.getUri());
                    ClientRequest requestForServer = request.replaceUri(finalUri);

                    try {
                        // 这里Reacvie异步调用了同步方法execute，execute详细内容见下方
                        return Observable.just(AbstractLoadBalancerAwareClient.this.execute(requestForServer, requestConfig));
                    } catch (Exception var5) {
                        return Observable.error(var5);
                    }
                }
            }).toBlocking().single();
    } catch (Exception var6) {
            Throwable t = var6.getCause();
            if (t instanceof ClientException) {
                throw (ClientException)t;
            } else {
                throw new ClientException(var6);
            }
    }
}

protected LoadBalancerCommand<T> buildLoadBalancerCommand(S request, IClientConfig config) {
    // 获取重试Handler并配置到Command中
    RequestSpecificRetryHandler handler = this.getRequestSpecificRetryHandler(request, config);
    Builder<T> builder = LoadBalancerCommand.builder().withLoadBalancerContext(this).withRetryHandler(handler).withLoadBalancerURI(request.getUri());
    this.customizeLoadBalancerCommandBuilder(request, config, builder);
    return builder.build();
}
```

跳转到实现类 FeignLoadBalancer

```java
/**
 * 根据不同参数，选择不同重试Handler
 * GET请求默认会重试1次
 **/
public RequestSpecificRetryHandler getRequestSpecificRetryHandler(
        RibbonRequest request, IClientConfig requestConfig) {
    if (this.ribbon.isOkToRetryOnAllOperations()) {
        return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                requestConfig);
    }
    if (!request.toRequest().method().equals("GET")) {
        return new RequestSpecificRetryHandler(true, false, this.getRetryHandler(),
                requestConfig);
    }
    else {
        return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                requestConfig);
    }
}

    IClientConfig getClientConfig(Request.Options options, String clientName) {
        IClientConfig requestConfig;
        // 如果 Feign 的配置是默认配置（即没有自定义Properties），
        // 否则，使用 FeignOptionsClientConfig。那么，Ribbon 超时的值将和 Feign 的配置保持一致
        if (options == DEFAULT_OPTIONS) {
            requestConfig = this.clientFactory.getClientConfig(clientName);
        } else {
            requestConfig = new FeignOptionsClientConfig(options);
        }
        return requestConfig;
    }

    static class FeignOptionsClientConfig extends DefaultClientConfigImpl {

        public FeignOptionsClientConfig(Request.Options options) {
            // 将 Feign 的配置 options 覆盖到 Ribbon 的 IClientConfig 中
            setProperty(CommonClientConfigKey.ConnectTimeout,
                    options.connectTimeoutMillis());
            setProperty(CommonClientConfigKey.ReadTimeout, options.readTimeoutMillis());
        }

        @Override
        public void loadProperties(String clientName) {
        }

        @Override
        public void loadDefaultValues() {
        }
    }

    ...
}

public class DefaultClientConfigImpl implements IClientConfig{
    public static final int DEFAULT_READ_TIMEOUT = 5000;
    public static final int DEFAULT_CONNECT_TIMEOUT = 2000;

    ...
}

public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride)
        throws IOException {
    Request.Options options;
    if (configOverride != null) {
        // 如果需要override的config不为空，则构造一个RibbonProperties，再取RibbonProperties.connectTimeout方法，里面会根据动态的Prop进行赋值，见下面的几个方法。
        RibbonProperties override = RibbonProperties.from(configOverride);
        options = new Request.Options(
                override.connectTimeout(this.connectTimeout),
                override.readTimeout(this.readTimeout));
    }
    else {
        options = new Request.Options(this.connectTimeout, this.readTimeout);
    }
    Response response = request.client().execute(request.toRequest(), options);
    return new RibbonResponse(request.getUri(), response);
}



RibbonProperties.from(configOverride) -> RibbonProperties.connectTimeout -> this.config.get(key, defaultValue) -> 实现类是DefaultClientConfigImpl

然后DefaultClientConfigImpl中：

protected Object getProperty(String key) {
    if (enableDynamicProperties) {
        String dynamicValue = null;
        // 通过？？？
        DynamicStringProperty dynamicProperty = dynamicProperties.get(key);
        if (dynamicProperty != null) {
            dynamicValue = dynamicProperty.get();
        }
        if (dynamicValue == null) {
            // 通过指定服务的属性，如：holder-saas-store-staff.ribbon.ConnectTimeout
            dynamicValue = DynamicProperty.getInstance(getConfigKey(key)).getString();
            if (dynamicValue == null) {
                // 通过全局的属性，如：ribbon.ConnectTimeout
                dynamicValue = DynamicProperty.getInstance(getDefaultPropName(key)).getString();
            }
        }
        if (dynamicValue != null) {
            return dynamicValue;
        }
    }
    return properties.get(key);
}

另外：DynamicProperty里的ALL_PROPS存储了所有的默认的或自定义的属性（即它完成了自定义配置的加载）
```


FeignLoadBaalancer.execute


[SpringCloud Feign的重试功能理解](https://blog.csdn.net/zyt807/article/details/80553666)

[SpringCloud的各种超时时间配置效果](https://blog.csdn.net/johnf_nash/article/details/89737447)

[简单谈谈什么是Hystrix，以及SpringCloud的各种超时时间配置效果，和简单谈谈微服务优化](https://blog.csdn.net/zzzgd_666/article/details/83314833)

[springcloud2.x 设置feign、ribbon和hystrix的超时问题（配置文件）](https://blog.csdn.net/ab52262879/article/details/84100596)

[Feign超时配置](https://www.jianshu.com/p/90fd88a2f18b)

[Spring Cloud组件那么多超时设置，如何理解和运用？](https://www.jianshu.com/p/c836a283631e)