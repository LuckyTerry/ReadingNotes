# JVM性能分析工具

## 分析工具概述

略

### JPS

> java process status 查看进程

**jps**

```
terry@terry-company:~$ jps
13317 HolderSaasStoreMessageApplication
4248 Main
11641 Jps
19897 HolderSaasStoreHwApplication
15627 NewDriver
15835 Launcher
15854 HolderSaasStoreKdsApplication
```

**jps -m**

输出主函数传入的参数

```
terry@terry-company:~$ jps -m
11713 Jps -m
13317 HolderSaasStoreMessageApplication
4248 Main
19897 HolderSaasStoreHwApplication
15627 NewDriver
15835 Launcher /opt/idea-IU/lib/jdom.jar:/opt/idea-IU/lib/aether-api-1.1.0.jar:/opt/idea-IU/lib/maven-model-builder-3.3.9.jar:/opt/idea-IU/lib/httpclient-4.5.5.jar:/opt/idea-IU/lib/jna.jar:/opt/idea-IU/lib/httpcore-4.4.9.jar:/opt/idea-IU/lib/annotations.jar:/opt/idea-IU/lib/forms-1.1-preview.jar:/opt/idea-IU/lib/jna-platform.jar:/opt/idea-IU/lib/aether-transport-http-1.1.0.jar:/opt/idea-IU/lib/log4j.jar:/opt/idea-IU/lib/trove4j.jar:/opt/idea-IU/lib/lz4-1.3.0.jar:/opt/idea-IU/lib/guava-23.6-jre.jar:/opt/idea-IU/lib/jps-builders-6.jar:/opt/idea-IU/lib/nanoxml-2.2.3.jar:/opt/idea-IU/lib/idea_rt.jar:/opt/idea-IU/lib/aether-transport-file-1.1.0.jar:/opt/idea-IU/lib/aether-impl-1.1.0.jar:/opt/idea-IU/lib/protobuf-java-3.4.0.jar:/opt/idea-IU/lib/netty-buffer-4.1.25.Final.jar:/opt/idea-IU/lib/aether-util-1.1.0.jar:/opt/idea-IU/lib/commons-lang3-3.4.jar:/opt/idea-IU/lib/netty-resolver-4.1.25.Final.jar:/opt/idea-IU/lib/util.jar:/opt/idea-IU/lib/netty-common-4.1.25.Final.jar:/opt/idea-IU/lib/asm-all.
15854 HolderSaasStoreKdsApplication
```

**jps -v**

列出jvm参数

```
terry@terry-company:~$ jps -v
11760 Jps -Denv.class.path=.::/opt/java/jdk1.8.0_181/lib:/opt/java/jdk1.8.0_181/jre/lib -Dapplication.home=/opt/java/jdk1.8.0_181 -Xms8m
13317 HolderSaasStoreMessageApplication -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:40075,suspend=y,server=n -Dvisualvm.id=5794325316538 -XX:TieredStopAtLevel=1 -Xverify:none -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=41957 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -javaagent:/opt/idea-IU/plugins/Groovy/lib/agent/gragent.jar -javaagent:/opt/idea-IU/lib/rt/debugger-agent.jar=file:/tmp/capture.props -Dfile.encoding=UTF-8
4248 Main -Xms512m -Xmx4096m -XX:ReservedCodeCacheSize=240m -XX:+UseConcMarkSweepGC -XX:SoftRefLRUPolicyMSPerMB=50 -ea -Dsun.io.useCanonCaches=false -Djava.net.preferIPv4Stack=true -Djdk.http.auth.tunneling.disabledSchemes="" -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -Dawt.useSystemAAFontSettings=lcd -Dsun.java2d.renderer=sun.java2d.marlin.MarlinRenderingEngine -XX:ErrorFile=/home/terry/java_error_in_IDEA_%p.log -XX:HeapDumpPath=/home/terry/java_error_in_IDEA.hprof -Didea.paths.selector=IntelliJIdea2018.2 -Djb.vmOptionsFile=/home/terry/.IntelliJIdea2018.2/config/idea64.vmoptions -Didea.jre.check=true
19897 HolderSaasStoreHwApplication -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:42345,suspend=y,server=n -Dvisualvm.id=9700689565447 -XX:TieredStopAtLevel=1 -Xverify:none -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=45885 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -javaagent:/opt/idea-IU/plugins/Groovy/lib/agent/gragent.jar -javaagent:/opt/idea-IU/lib/rt/debugger-agent.jar=file:/tmp/capture.props -Dfile.encoding=UTF-8
15627 NewDriver -Djmeter.home=/usr/share/jmeter
15835 Launcher -Xmx700m -Djava.awt.headless=true -Djava.endorsed.dirs="" -Djdt.compiler.useSingleThread=true -Dpreload.project.path=/home/terry/WsJavaWeb/saas-platform -Dpreload.config.path=/home/terry/.IntelliJIdea2018.2/config/options -Dcompile.parallel=false -Drebuild.on.dependency.change=true -Djava.net.preferIPv4Stack=true -Dio.netty.initialSeedUniquifier=8171934176406906681 -Dfile.encoding=UTF-8 -Duser.language=zh -Duser.country=CN -Didea.paths.selector=IntelliJIdea2018.2 -Didea.home.path=/opt/idea-IU -Didea.config.path=/home/terry/.IntelliJIdea2018.2/config -Didea.plugins.path=/home/terry/.IntelliJIdea2018.2/config/plugins -Djps.log.dir=/home/terry/.IntelliJIdea2018.2/system/log/build-log -Djps.fallback.jdk.home=/opt/idea-IU/jre64 -Djps.fallback.jdk.version=1.8.0_152-release -Dio.netty.noUnsafe=true -Djava.io.tmpdir=/home/terry/.IntelliJIdea2018.2/system/compile-server/saas-platform_f54353f5/_temp_ -Djps.backward.ref.index.builder=true -Dkotlin.incremental.compilation=true -Dkotlin.daemon.enabled -Dkotlin.daemon.clie
15854 HolderSaasStoreKdsApplication -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:44807,suspend=y,server=n -Dvisualvm.id=195567790432090 -XX:TieredStopAtLevel=1 -Xverify:none -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=41935 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -javaagent:/opt/idea-IU/plugins/Groovy/lib/agent/gragent.jar -javaagent:/opt/idea-IU/lib/rt/debugger-agent.jar=file:/tmp/capture.props -Dfile.encoding=UTF-8
```

### jstat

> 类装载、内存、垃圾收集、jit编译信息

**jstat -gc <pid> 200 50**

略，搞清楚与下方的区别

**jstat -gcutil <pid> 200 50**

将内存使用、gc 回收状况打印出来（每隔 200ms 打印 50次）

关注点

- Eden区，百分比占用
- Old区，百分比占用
- YGC回收次数及频率
- FGC回收次数及频率。eg: (3 - 3) / (200 * 50)，10秒内发生0次

```
terry@terry-company:~$ jstat -gcutil 15854 200 50
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  59.75  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.02  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.03  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.03  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.03  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.03  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.05  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.08  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.08  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.08  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.08  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
  0.00  99.09  60.09  45.74  94.79  92.78    221    2.016     3    0.197    2.213
```

### jmap

> 打印JVM堆内对象情况,查询Java堆和永久代的详细信息，使用率，使用大小

**jmap -finalizerinfo <pid>**

打印正等候回收的对象的信息。

**jmap -heap <pid>**

打印heap的概要信息，GC使用的算法，heap的配置及wise heap的使用情况

关注点

- 1
- 2
- 3

```
terry@terry-company:~$ jmap -heap 15854
Attaching to process ID 15854, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.181-b13

using thread-local object allocation.
Parallel GC with 8 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 8405385216 (8016.0MB)
   NewSize                  = 175112192 (167.0MB)
   MaxNewSize               = 2801795072 (2672.0MB)
   OldSize                  = 351272960 (335.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 123207680 (117.5MB)
   used     = 17630416 (16.813674926757812MB)
   free     = 105577264 (100.68632507324219MB)
   14.309510575964095% used
From Space:
   capacity = 9961472 (9.5MB)
   used     = 9842312 (9.386360168457031MB)
   free     = 119160 (0.11363983154296875MB)
   98.80379124691612% used
To Space:
   capacity = 31981568 (30.5MB)
   used     = 0 (0.0MB)
   free     = 31981568 (30.5MB)
   0.0% used
PS Old Generation
   capacity = 357564416 (341.0MB)
   used     = 163954528 (156.35922241210938MB)
   free     = 193609888 (184.64077758789062MB)
   45.85314440237811% used

42415 interned Strings occupying 4540248 bytes.
```

**jmap -histo <pid> | head -20**

查看top20的实例个数以及内存占用情况

**jmap -histo:live <pid> | head -20**

查看top20的实例个数以及内存占用情况，只计算活动的对象

**jmap -dump:format=b,file=dump.hprof <pid>**

生成堆转储快照dump文件

> 这个命令执行，JVM会将整个heap的信息dump写入到一个文件，heap如果比较大的话，就会导致这个过程比较耗时，并且执行的过程中为了保证dump的信息是可靠的，所以会暂停应用， 线上系统慎用。

**jmap -dump:live,format=b,file=dump.hprof <pid>**

生成堆转储快照dump文件，只计算活动的对象

## Jstack

> 打印线程堆栈信息

**jstack <pid>**

查看某个Java进程内的线程堆栈信息

**jstack -l <pid>**

在发生死锁时推荐用来观察锁持有情况

**jstack <pid> > jstack.log**


```
printf "%x\n" <thread-pid>
```

## Java VisualVM

### 监控本地的java进程

### 监控远程的java进程

> 远程Java进程启动配置

**配置简单说明：**
- -Dcom.sun.management.jmxremote添加一个jmx远程连接属性
- -Dcom.sun.management.jmxremote.port=10099 指定连接的端口号
- -Dcom.sun.management.jmxremote.authenticate=false 是否启用验证
- -Dcom.sun.management.jmxremote.ssl=false 是否启用ssl
- -Djava.net.preferIPv4Stack=true 是否优先使用ipv4
- -Djava.rmi.server.hostname=192.168.102.99 指定远程主机的ip地址

**Idea启动**

EditConfiguration -> Environment -> VM options 添加启动参数。注意不是在 Program arguments 中添加！！！
```
-Djava.rmi.server.hostname=118.24.149.18 -Dcom.sun.management.jmxremote.port=10099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false
```

**Jar启动**

```
java -Djava.rmi.server.hostname=118.24.149.18 -Dcom.sun.management.jmxremote.port=10099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -jar  holder-saas-store-print-0.0.1-SNAPSHOT.jar
```

**检测是否启动成功**

```
terry@terry-develop:~$ sudo netstat -lnptu | grep java
tcp        0      0 0.0.0.0:46285           0.0.0.0:*               LISTEN      16114/java          
tcp        0      0 127.0.0.1:63342         0.0.0.0:*               LISTEN      16114/java          
tcp        0      0 127.0.0.1:17434         0.0.0.0:*               LISTEN      16114/java          
tcp        0      0 127.0.0.1:38781         0.0.0.0:*               LISTEN      16114/java          
tcp        0      0 127.0.0.1:6942          0.0.0.0:*               LISTEN      16114/java          
tcp        0      0 127.0.0.1:35811         0.0.0.0:*               LISTEN      16114/java          
tcp6       0      0 :::42575                :::*                    LISTEN      18571/java          
tcp6       0      0 127.0.0.1:21487         :::*                    LISTEN      16473/java          
tcp6       0      0 :::10099                :::*                    LISTEN      18571/java          
tcp6       0      0 :::8917                 :::*                    LISTEN      18571/java          
tcp6       0      0 :::38521                :::*                    LISTEN      16473/java
```

可以看到10099端口，与配置一致，说明启动成功！

> 连接到远程Java进程

打开jvisualvm，双击 “远程” 选项，添加一个远程主机，即远程的服务器

右键远程主机，单击“添加JMX连接”，输入主机ip:port，如192.168.102.99:10099

`enjoy it ^_^ !`

## Java Console

## 工具使用中遇到的问题

执行异常：

```
terry@terry-company:~$ jmap -heap 15854
Attaching to process ID 15854, please wait...
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 15854: 不允许的操作
```

解决方法（其他jvm工具一样）：

```
echo 0 | sudo tee /proc/sys/kernel/yama/ptrace_scope
```

## 好文章Reference

[JVM系列文章汇总](https://m.wang1314.com/doc/webapp/topic/20994908.html)

[一次生产 CPU 100% 排查优化实践](https://crossoverjie.top/2018/12/17/troubleshoot/cpu-percent-100/)

[又一次生产 CPU 高负载排查实践](https://crossoverjie.top/2019/06/18/troubleshoot/cpu-percent-100-02/)

[java命令学习系列](http://www.hollischuang.com/archives/tag/java%E5%91%BD%E4%BB%A4%E5%AD%A6%E4%B9%A0%E7%B3%BB%E5%88%97)

[jvm调优工具分析指南](https://juejin.im/entry/59cd9a446fb9a00a4843c588)