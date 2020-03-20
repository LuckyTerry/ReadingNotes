# logback配置

## 根节点Configuration

**3个属性**

属性名称 | 默认值 | 介绍
---|---|---
debug | false | 要不要打印 logback内部日志信息，true则表示要打印。建议开启
scan | true | 配置发送改变时，要不要重新加载
scanPeriod | 1 seconds | 检测配置发生变化的时间间隔。如果没给出时间单位，默认时间单位是毫秒

**3个子节点**

- contextName
> 设置日志上下文名称，后面输出格式中可以通过定义 %contextName 来打印日志上下文名称
- property
> 用来设置相关变量,通过key-value的方式配置，然后在后面的配置文件中通过 ${key}来访问
- appender
> 日志输出组件，主要负责日志的输出以及格式化日志。常用的属性有name和class

## Appender

**2个属性**

属性名称 | 默认值 | 介绍
---|---|---
name | 无默认值 | appender组件的名称，后面给logger指定appender使用
class | 无默认值 | appender的具体实现类。常用的有 ConsoleAppender、FileAppender、RollingFileAppender

**3个appender具体实现类**

- ConsoleAppender
> 向控制台输出日志内容的组件，只要定义好encoder节点就可以使用。
- FileAppender
> 向文件输出日志内容的组件，用法也很简单，不过由于没有日志滚动策略，一般很少使用
- RollingFileAppender
> 向文件输出日志内容的组件，同时可以配置日志文件滚动策略，在日志达到一定条件后生成一个新的日志文件。

**若干属性**

rollingPolicy
filter
encoder

appender节点中有一个子节点filter，配置具体的过滤器，比如上面的例子配置了一个内置的过滤器ThresholdFilter，然后设置了level的值为DEBUG。这样用这个appender输出日志的时候都会经过这个过滤器，日志级别低于DEBUG的都不会输出来。
在RollingFileAppender中，可以配置相关的滚动策略，具体可以看配置样例的注释。

## Loggers

root节点和logger节点其实都是表示Logger组件。个人觉的可以把他们之间的关系可以理解为父子关系，root是最顶层的logger，正常情况getLogger("name/class")没有找到对应logger的情况下，都是使用root节点配置的logger。
如果配置了logger，并且通过getLogger("name/class")获取到这个logger，输出日志的时候，就会使用这个logger配置的appender输出，同时还会使用rootLogger配置的appender。我们可以使用logger节点的additivity="false"属性来屏蔽rootLogger的appender。这样就可以不使用rootLogger的appender输出日志了。
关于logger的获取，一般logger是配置name的。我们再代码中经常通过指定的CLass来获取Logger，比如这样LoggerFactory.getLogger(Test.class);,其实这个最后也是转成对应的包名+类名的字符串com.kongtrio.Test.class。假设有一个logger配置的那么是com.kongtrio，那么通过LoggerFactory.getLogger(Test.class)获取到的logger就是这个logger。
也就是说，name可以配置包名，也可以配置自定义名称。

## 日志等级 level

在slf4j中，从小到大的日志级别依旧是trace、debug、info、warn、error。

## 过滤器

TOOD

## 异步输出

之前的日志配置方式是基于同步的，每次日志输出到文件都会进行一次磁盘IO。采用异步写日志的方式而不让此次写日志发生磁盘IO，阻塞线程从而造成不必要的性能损耗。异步输出日志的方式很简单，添加一个基于异步写日志的 appender，并指向原先配置的 appender即可

```xml
<appender name="ASYNC-INFO" class="ch.qos.logback.classic.AsyncAppender">
    <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
    <discardingThreshold>0</discardingThreshold>
    <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
    <queueSize>256</queueSize>
    <!-- 添加附加的appender,最多只能添加一个 -->
    <appender-ref ref="INFO"/>
</appender>
<root level="info">
    <appender-ref ref="STDOUT"/>
    <appender-ref ref="INFO"/>
    <appender-ref ref="ASYNC-INFO"/>
</root>
```

## Spring Boot 集成

TODO

## LayoutPattern

[官网LayoutPattern文档](http://logback.qos.ch/manual/layouts.html)

```
%magenta([%d{yyyy-MM-dd HH:mm:ss:SSS}])[%highlight(%-5p)][%t][%C.%M:%L][%X{traceId}][%green(%m)] %n
```

## 推荐配置

```
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
    <contextName>SpringBootDemo</contextName>
    <property name="filePath" value="./logs"/>
    <property name="projectName" value="table"/>
    <timestamp key="bySecond" datePattern="yyyyMMdd'T'HHmmss"/>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                [%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5level] [%logger{35}] [ %msg] %n
            </pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>debug</level>
        </filter>
    </appender>
    <appender name="DEBUG"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${filePath}/debug.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${filePath}/debug/%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <maxHistory>15</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>200MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5level] [%logger{35}] [ %msg] %n
            </pattern>
        </encoder>
    </appender>
    <appender name="INFO"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${filePath}/info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${filePath}/info/%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <maxHistory>15</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>200MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5level] [%logger{35}] [ %msg] %n
            </pattern>
        </encoder>
    </appender>
    <appender name="WARN"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${filePath}/warn.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${filePath}/warn/%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <maxHistory>15</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>200MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5level] [%logger{35}] [ %msg] %n
            </pattern>
        </encoder>
    </appender>
    <appender name="ERROR"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${filePath}/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${filePath}/error/%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <maxHistory>15</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>200MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5level] [%logger{35}] [ %msg] %n
            </pattern>
        </encoder>
    </appender>
    <appender name="ASYNC-DEBUG" class="ch.qos.logback.classic.AsyncAppender">
        <discardingThreshold>0</discardingThreshold>
        <queueSize>256</queueSize>
        <appender-ref ref="DEBUG"/>
    </appender>
    <appender name="ASYNC-INFO" class="ch.qos.logback.classic.AsyncAppender">
        <discardingThreshold>0</discardingThreshold>
        <queueSize>256</queueSize>
        <appender-ref ref="INFO"/>
    </appender>
    <appender name="ASYNC-WARN" class="ch.qos.logback.classic.AsyncAppender">
        <discardingThreshold>0</discardingThreshold>
        <queueSize>256</queueSize>
        <appender-ref ref="WARN"/>
    </appender>
    <appender name="ASYNC-ERROR" class="ch.qos.logback.classic.AsyncAppender">
        <discardingThreshold>0</discardingThreshold>
        <queueSize>256</queueSize>
        <appender-ref ref="ERROR"/>
    </appender>

    <logger name="com.holderzone" level="debug"/>
    <root level="info">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="DEBUG"/>
        <appender-ref ref="INFO"/>
        <appender-ref ref="WARN"/>
        <appender-ref ref="ERROR"/>
        <appender-ref ref="ASYNC-DEBUG"/>
        <appender-ref ref="ASYNC-INFO"/>
        <appender-ref ref="ASYNC-WARN"/>
        <appender-ref ref="ASYNC-ERROR"/>
    </root>
</configuration>
```

## 参考

[logback介绍和配置详解](https://www.jianshu.com/p/04065d8cb2a9)