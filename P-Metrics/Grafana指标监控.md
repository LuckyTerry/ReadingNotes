# Prometheus&Grafana

## 内存指标

container_memory_rss
RSS内存，即常驻内存集（Resident Set Size），是分配给进程使用实际物理内存，而不是磁盘上缓存的虚拟内存。RSS内存包括所有分配的栈内存和堆内存，以及加载到物理内存中的共享库占用的内存空间，但不包括进入交换分区的内存。
container_memory_usage_bytes
当前使用的内存量，包括所有使用的内存，不管有没有被访问。
container_memory_max_usage_bytes
最大内存使用量的记录。
container_memory_cache
高速缓存（cache）的使用量。cache是位于CPU与主内存间的一种容量较小但速度很高的存储器，是为了提高cpu和内存之间的数据交换速度而设计的。
container_memory_swap
虚拟内存使用量。虚拟内存（swap）指的是用磁盘来模拟内存使用。当物理内存快要使用完或者达到一定比例，就可以把部分不用的内存数据交换到硬盘保存，需要使用时再调入物理内存
container_memory_working_set_bytes
当前内存工作集（working set）使用量。todo

由此可见，

container_memory_max_usage_bytes(最大可用内存) >
container_memory_usage_bytes(已经申请的内存+工作集使用的内存) >
container_memory_working_set_bytes(工作集内存) >
container_memory_rss(常驻内存集)

## Reference

[kubernetes+docker监控之简介](https://blog.csdn.net/ztsinghua/article/details/54580241)

[Spring Boot Actuator:健康检查、审计、统计和监控](https://bigjar.github.io/2018/08/19/Spring-Boot-Actuator-%E5%81%A5%E5%BA%B7%E6%A3%80%E6%9F%A5%E3%80%81%E5%AE%A1%E8%AE%A1%E3%80%81%E7%BB%9F%E8%AE%A1%E5%92%8C%E7%9B%91%E6%8E%A7/)

[Spring Boot Metrics监控之Prometheus&Grafana](https://bigjar.github.io/2018/08/19/Spring-Boot-Metrics%E7%9B%91%E6%8E%A7%E4%B9%8BPrometheus-Grafana/)

[Prometheus 监控 Java 应用](https://blog.csdn.net/qq_25934401/article/details/82185236)

[Prometheus监控k8s](https://www.jianshu.com/p/e76053b6f3f5)

[监控指标以及prometheus规则-不断完善中](http://www.bubuko.com/infodetail-3011545.html)