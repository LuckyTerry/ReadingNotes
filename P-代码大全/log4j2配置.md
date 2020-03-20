# log4j2配置

## 导入依赖

推荐配置如下：

全局排除默认的logback依赖，然后添加log4j2依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
    <exclusions>
        <exclusion>
            <groupId>*</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

另一种常见配置如下：

但由于 starter-logging 可能存在于多个依赖项中，所以并不是个好办法：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
    <exclusions><!-- 去掉默认log配置 -->
	  <exclusion>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-logging</artifactId>
	  </exclusion>
	</exclusions>
</dependency>
<!-- 引入log4j2依赖 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

## 根节点Configuration

**两个属性**

- status 用来指定log4j本身的打印日志的级别
- monitorinterval 用于指定log4j自动重新配置的监测间隔时间，单位是s,最小是5s

**两个子节点**

- Appenders
- Loggers(表明可以定义多个Appender和Logger).

## Appender

**三种子节点**

- Console 用来定义输出到控制台的Appender

> name:指定Appender的名字.

> target:SYSTEM_OUT 或 SYSTEM_ERR,一般只设置默认:SYSTEM_OUT.

> PatternLayout:输出格式，不设置默认为:%m%n.

- RollingFile 用来定义超过指定大小自动删除旧的创建新的的Appender.

> name:指定Appender的名字.

> fileName:指定输出日志的目的文件带全路径的文件名.

> Filters:组合过滤器额标签，它包含的子标签是具体的过滤器.

> PatternLayout:输出格式，不设置默认为:%m%n.

> filePattern:指定新建日志文件的名称格式.

> Policies:指定滚动日志的策略，就是什么时候进行新建日志文件输出日志.

> TimeBasedTriggeringPolicy:Policies子节点，基于时间的滚动策略，interval属性用来指定多久滚动一次，默认是1 hour。modulate=true用来调整时间：比如现在是早上3am，interval是4，那么第一次滚动是在4am，接着是8am，12am...而不是7am.

> SizeBasedTriggeringPolicy:Policies子节点，基于指定文件大小的滚动策略，size属性用来定义每个日志文件的大小.

> DefaultRolloverStrategy:用来指定同一个文件夹下最多有几个日志文件时开始删除最旧的，创建新的(通过max属性)。　
　　　　　　　　
- File 用来定义输出到指定位置的文件的Appender

> name:指定Appender的名字.

> fileName:指定输出日志的目的文件带全路径的文件名.

> PatternLayout:输出格式，不设置默认为:%m%n.
　　　　
## Loggers

**两种字节点**

- Root

> Root节点用来指定项目的根日志，如果没有单独指定Logger，那么就会默认使用该Root日志输出

> level:日志输出级别，共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < Fatal < OFF.

> AppenderRef：Root的子节点，用来指定该日志输出到哪个Appender.

- Logger

> Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。

> level:日志输出级别，共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < Fatal < OFF.

> name:用来指定该Logger所适用的类或者类所在的包全路径,继承自Root节点.

> AppenderRef：Logger的子节点，用来指定该日志输出到哪个Appender,如果没有指定，就会默认继承自Root.如果指定了，那么会在指定的这个Appender和Root的Appender中都会输出，此时我们可以设置Logger的additivity="false"只在自定义的Appender中进行输出。

## 日志等级 level

共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < Fatal < OFF.

- All:最低等级的，用于打开所有日志记录.

- Trace:是追踪，就是程序推进以下，你就可以写个trace输出，所以trace应该会特别多，不过没关系，我们可以设置最低日志级别不让他输出.

- Debug:指出细粒度信息事件对调试应用程序是非常有帮助的.

- Info:消息在粗粒度级别上突出强调应用程序的运行过程.

- Warn:输出警告及warn以下级别的日志.

- Error:输出错误信息日志.

- Fatal:输出每个严重的错误事件将会导致应用程序的退出的日志.

- OFF:最高等级的，用于关闭所有日志记录.

程序会打印高于或等于所设置级别的日志，设置的日志等级越高，打印出来的日志就越少。

## 过滤器

<Filters>是组合过滤器额标签，它包含的子标签是具体的过滤器，这三个具体过滤器分别是

- 日志等级过滤器
- 正则表达式过滤器
- 时间过滤器

```
 <!-- 该过滤器只将info级别输入到文件-->
<Filters>
    <ThresholdFilter level="warn" onMatch="DENY" onMismatch="NEUTRAL"/>
    <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
</Filters>

 <!-- 该过滤器只将info及以上级别输入到文件-->
<Filters>
    <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
</Filters>

 <!-- 同上的简写，该过滤器只将info及以上级别输入到文件-->
<ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>

 <!-- 一个多过滤器示例-->
<Filters>
    <ThresholdFilter level="TRACE" onMatch="NEUTRAL" onMismatch="DENY"/>
    <RegexFilter regex=".* test .*" onMatch="NEUTRAL" onMismatch="DENY"/>
    <TimeFilter start="05:00:00" end="05:30:00" onMatch=" NEUTRAL " onMismatch="DENY"/>
</Filters>
```

- ACCEPT 接受：日志信息直接写入日志文件
- DENY 拒绝：日志信息不会写入日志文件，也不再向下传递
- NEUTRAL 中立：组合过滤器中，被当前过滤器接受的日志信息，会继续用后面的过滤器进行过滤，只有符合所有过滤器条件的日志信息，才会被最终写入日志文件。

当只有一个过滤器时，可不使用<Filters>嵌套，直接使用<ThresholdFilter>即可。

## PatternLayout

[官方PatternLayout文档](http://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout)

[Log4j2进阶使用(Pattern Layout详细设置)](https://www.jianshu.com/p/37ef7bc6d6eb)

推荐格式如下

```
// 完整版
%magenta{[%d{yyyy-MM-dd HH:mm:ss:SSS}{GMT+8}]}[%-5p][%thread][%class.%method:%line][%X{traceId}][%highlight{%message}{WARN=bright yellow}] %n

// 缩写版
%magenta{[%d{yyyy-MM-dd HH:mm:ss:SSS}{GMT+8}]}[%-5p][%t][%C.%M:%L][%X{traceId}][%highlight{%m}{WARN=bright yellow}] %n
```

## 推荐配置

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="WARN" monitorInterval="30">
    <properties>
        <property name="pattern">%highlight{[%d{yyyy-MM-dd HH:mm:ss:SSS}{GMT+8}][%5p][- %l - %m] %n}</property>
        <property name="filePath">./logs</property>
    </properties>
    <appenders>
        <console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="${pattern}"/>
        </console>
        <RollingFile name="RollingFileDebug" fileName="${filePath}/debug.log"
                     filePattern="${filePath}/$${date:yyyy-MM}/debug-%i.log">
            <Filters>
                <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="debug" onMatch="NEUTRAL" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="${pattern}"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="50 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="20">
                <Delete basePath="${filePath}" maxDepth="2">
                    <IfFileName glob="*/debug-*.log">
                        <IfLastModified age="14d"/>
                    </IfFileName>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        <RollingFile name="RollingFileInfo" fileName="${filePath}/info.log"
                     filePattern="${filePath}/$${date:yyyy-MM}/info-%i.log">
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${pattern}"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="50 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="20">
                <Delete basePath="${filePath}" maxDepth="2">
                    <IfFileName glob="*/info-*.log">
                        <IfLastModified age="14d"/>
                    </IfFileName>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        <RollingFile name="RollingFileWarn" fileName="${filePath}/warn.log"
                     filePattern="${filePath}/$${date:yyyy-MM}/warn-%i.log">
            <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${pattern}"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="50 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="20">
                <Delete basePath="${filePath}" maxDepth="2">
                    <IfFileName glob="*/warn-*.log">
                        <IfLastModified age="14d"/>
                    </IfFileName>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        <RollingFile name="RollingFileError" fileName="${filePath}/error.log"
                     filePattern="${filePath}/$${date:yyyy-MM}/error-%i.log">
            <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${pattern}"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="50 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="20">
                <Delete basePath="${filePath}" maxDepth="2">
                    <IfFileName glob="*/error-*.log">
                        <IfLastModified age="14d"/>
                    </IfFileName>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
    </appenders>
    <loggers>
        <logger name="com.holderzone" level="DEBUG"/>
        <logger name="org.springframework" level="INFO"/>
        <logger name="org.mybatis" level="INFO"/>
        <root level="all">
            <appender-ref ref="Console"/>
            <appender-ref ref="RollingFileDebug"/>
            <appender-ref ref="RollingFileInfo"/>
            <appender-ref ref="RollingFileWarn"/>
            <appender-ref ref="RollingFileError"/>
        </root>
    </loggers>
</configuration>
```

## 参考

[log4j2配置文件log4j2.xml详解](https://www.cnblogs.com/new-life/p/9246143.html)

[Log4j2介绍和特性实例(四)--过滤器Filter](https://blog.csdn.net/chenhaotong/article/details/50487557)