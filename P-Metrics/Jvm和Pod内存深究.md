# Jvm和Pod内存深究


## fff

最大堆内存计算公式
Max memory = [-Xmx] + [-XX:MaxPermSize] + number_of_threads * [-Xss]

jinfo -flags 6
java -XX:+PrintFlagsInitial 显示所有可设置参数及默认值
java -XX:+PrintFlagsFinal 显示所有可设置参数的当前值
java -XX:+PrintCommandLineFlags 显示出JVM所有跟默认值不同的参数及值

默认情况下：
MaxNewSize占MaxHeapSize的1/3


-XX:InitialHeapSize=33554432 -XX:MaxHeapSize=536870912 -XX:MaxNewSize=178782208 -XX:MinHeapDeltaBytes=524288 -XX:NewSize=11010048 -XX:OldSize=22544384


640 = 512 + 64 + 120*0.5 
->  952 = 640 + ?
supposed: 312
actual: 224M

Commited = Max memory + Non Heap memory

512M

number_of_threads
arthas -> jvm -> THREAD -> COUNT
arthas -> thread -> Threads Total

heap
arthas -> dashboard -> Memory -> heap -> max
arthas -> jvm -> MEMORY -> HEAP-MEMORY-USAGE -> max

nonheap
arthas -> dashboard -> Memory -> noheap -> committed
arthas -> jvm -> MEMORY -> NO-HEAP-MEMORY-USAGE -> used

-Xmx
ps -ef|grep <pid>

-XX:MaxPermSize
ps -ef|grep <pid>
jinfo -flag MaxPermSize <pid>
default 64M

-Xss
ps -ef|grep <pid>

## 好文章

[arthas](https://github.com/alibaba/arthas/blob/master/README_CN.md)

[此次部署在k8s集群中的SpringBoot项目OOMKilled问题汇总](https://www.cnblogs.com/ynx01/p/10762886.html)
[为什么jvm 提交的内存比实际进程占用的内存更大](https://www.cnblogs.com/ynx01/p/10876460.html)

[今晚咱们来聊聊堆外内存泄漏的Bug是如何查找的](http://www.360doc.com/content/18/0422/13/54737980_747780842.shtml)

[教你认识Linux内存管理方式，分析Swap被程序占用情况 ](https://zhaoshijie.iteye.com/blog/2375269)

[【JVM】内存和SWAP问题](https://www.cnblogs.com/wangzhongqiu/p/10868562.html)

[容器中的JVM资源该如何被安全的限制？](https://www.jianshu.com/p/3f99a6e898cd?tdsourcetag=s_pcqq_aiomsg)

[JVM内存配置详解（推荐配置）](https://www.cnblogs.com/qmfsun/p/5396710.html)

[JVM启动参数大全及默认值（默认配置）](https://blog.csdn.net/lsziri/article/details/81200334)

[k8s pod自动重启原因（jvm内存设置）](https://blog.csdn.net/yzh_1346983557/article/details/89216494)

[深入详解JVM内存模型与JVM参数详细配置](https://www.cnblogs.com/rinack/p/9888692.html)

![JV内存布局](https://uploadfiles.nowcoder.com/files/20190110/7380095_1547132982664_4685968-2502bef3bd1d1692.png)