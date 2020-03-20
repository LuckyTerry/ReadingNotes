在互联网的业务系统中，涉及到各种各样的ID，如在支付系统中就会有支付ID、退款ID等。那一般生成ID都有哪些解决方案呢？特别是在复杂的分布式系统业务场景中，我们应该采用哪种适合自己的解决方案是十分重要的。下面我们一一来列举一下，不一定全部适合，这些解决方案仅供你参考，或许对你有用。

**一个ID一般来说有下面几种要素**：

- 全局唯一性：不能出现重复的ID号，既然是唯一标识，这是最基本的要求。
- 趋势递增：在MySQL InnoDB引擎中使用的是聚集索引，由于多数RDBMS使用B-tree的数据结构来存储索引数据，在主键的选择上面我们应该尽量使用有序的主键保证写入性能。
- 单调递增：保证下一个ID一定大于上一个ID，例如事务版本号、IM增量消息、排序等特殊需求。
- 信息安全：如果ID是连续的，恶意用户的扒取工作就非常容易做了，直接按照顺序下载指定URL即可；如果是订单号就更危险了，竞对可以直接知道我们一天的单量。所以在一些应用场景下，会需要ID无规则、不规则。
- 带时间：ID里面包含时间，一眼扫过去就知道哪天的交易。
- 高可用性：确保任何时候都能正确的生成ID。

详细的，高可用性ID生成系统应该做到如下几点：

- 平均延迟和TP999延迟都要尽可能低；
- 可用性5个9；
- 高QPS。

# 系统时间毫秒数

我们可以使用当前系统时间精确到毫秒数+业务属性+用户属性+随机数+...等参数组合形式来确保ID的唯一性，缺点是ID的有序性难以保证，要保证有序性就要依赖数据库或者其他中间存储媒介。

# UUID/GUID

UUID(Universally Unique Identifier)的标准型式包含32个16进制数字，以连字号分为五段，形式为8-4-4-4-12的36个字符，示例：550e8400-e29b-41d4-a716-446655440000，到目前为止业界一共有5种方式生成UUID，详情见IETF发布的UUID规范 A Universally Unique IDentifier (UUID) URN Namespace (https://www.ietf.org/rfc/rfc4122.txt)

优点：

- 性能非常高：本地生成，没有网络消耗。

缺点：

- 不易于存储：UUID太长，16字节128位，通常以36长度的字符串表示，很多场景不适用。
- 信息不安全：基于MAC地址生成UUID的算法可能会造成MAC地址泄露，这个漏洞曾被用于寻找梅丽莎病毒的制作者位置。
- ID作为主键时在特定的环境会存在一些问题，比如做DB主键的场景下，UUID就非常不适用：
    1. MySQL官方有明确的建议主键要尽量越短越好[4]，36个字符长度的UUID不符合要求。
    2. 对MySQL索引不利：如果作为数据库主键，在InnoDB引擎下，UUID的无序性可能会引起数据位置频繁变动，严重影响性能。
    我们可以使用当前系统时间精确到毫秒数+业务属性+用户属性+随机数+...等参数组合形式来确保ID的唯一性，缺点是ID的有序性难以保证，要保证有序性就要依赖数据库或者其他中间存储媒介。

Java自带的生成UUID的方式就能生成一串唯一随机32位长度数据，而且够我们用N亿年，保证唯一性肯定是不用说的了，但缺点是它不包含时间、业务数据可读性太差了，而且也不能ID的有序递增。

这是一种简单的生成方式，简单，高效，但在一般业务系统中我还没见过有这种生成方式。

# 数据库自增ID

以MySQL举例，利用给字段设置auto_increment_increment和auto_increment_offset来保证ID自增，每次业务使用下列SQL读写MySQL得到ID号。

```
begin;
REPLACE INTO Tickets64 (stub) VALUES ('a');
SELECT LAST_INSERT_ID();
commit;
```

![image](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2017/8a4de8e8.png)

这种方案的优缺点如下：

优点：

- 非常简单，利用现有数据库系统的功能实现，成本小，有DBA专业维护。
- ID号单调自增，可以实现一些对ID有特殊要求的业务。

缺点：

- 强依赖DB，当DB异常时整个系统不可用，属于致命问题。配置主从复制可以尽可能的增加可用性，但是数据一致性在特殊情况下难以保证。主从切换时的不一致可能会导致重复发号。
- ID发号性能瓶颈限制在单台MySQL的读写性能。

对于MySQL性能问题，可用如下方案解决：在分布式系统中我们可以多部署几台机器，每台机器设置不同的初始值，且步长和机器数相等。比如有两台机器。设置步长step为2，TicketServer1的初始值为1（1，3，5，7，9，11…）、TicketServer2的初始值为2（2，4，6，8，10…）。这是Flickr团队在2010年撰文介绍的一种主键生成策略（Ticket Servers: Distributed Unique Primary Keys on the Cheap ）。如下所示，为了实现上述方案分别设置两台机器对应的参数，TicketServer1从1开始发号，TicketServer2从2开始发号，两台机器每次发号之后都递增2。

```
TicketServer1:
auto-increment-increment = 2
auto-increment-offset = 1

TicketServer2:
auto-increment-increment = 2
auto-increment-offset = 2
```

假设我们要部署N台机器，步长需设置为N，每台的初始值依次为0,1,2…N-1那么整个架构就变成了如下图所示：

![image](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2017/6d2c9ec8.png)

这种架构貌似能够满足性能的需求，但有以下几个缺点：

- 系统水平扩展比较困难，比如定义好了步长和机器台数之后，如果要添加机器该怎么做？假设现在只有一台机器发号是1,2,3,4,5（步长是1），这个时候需要扩容机器一台。可以这样做：把第二台机器的初始值设置得比第一台超过很多，比如14（假设在扩容时间之内第一台不可能发到14），同时设置步长为2，那么这台机器下发的号码都是14以后的偶数。然后摘掉第一台，把ID值保留为奇数，比如7，然后修改第一台的步长为2。让它符合我们定义的号段标准，对于这个例子来说就是让第一台以后只能产生奇数。扩容方案看起来复杂吗？貌似还好，现在想象一下如果我们线上有100台机器，这个时候要扩容该怎么做？简直是噩梦。所以系统水平扩展方案复杂难以实现。
- ID没有了单调递增的特性，只能趋势递增，这个缺点对于一般业务需求不是很重要，可以容忍。
- 数据库压力还是很大，每次获取ID都得读写一次数据库，只能靠堆机器来提高性能。

所以，这也不是合适的ID生成方法。

# 批量生成ID

一次按需批量生成多个ID，每次生成都需要访问数据库，将数据库修改为最大的ID值，并在内存中记录当前值及最大值。这样就避免了每次生成ID都要访问数据库并带来压力。

这种方案服务就是单点了，如果服务重启势必会造成ID丢失不连续的情况，而且这种方式也不利于水平扩展。

# Twitter-Snowflake

GitHub 地址：https://github.com/twitter/snowflake

这种方案大致来说是一种以划分命名空间（UUID也算，由于比较常见，所以单独分析）来生成ID的一种算法，这种方案把64-bit分别划分成多段，分开来标示机器、时间等，比如在snowflake中的64-bit分别表示如下图（图片来自网络）所示：

![image](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2017/eee18df9.png)

Twitter的snowflake算法下面几部分组成：

- 41位的时间序列，精确到毫秒，可以使用69年
- 10位的机器标识，最多支持部署1024个节点
- 12位的序列号，支持每个节点每毫秒产生4096个ID序号，最高位是符号位始终为0。

1-bit的时间可以表示（1L<<41）/(1000L*3600*24*365)=69年的时间，10-bit机器可以分别表示1024台机器。如果我们对IDC划分有需求，还可以将10-bit分5-bit给IDC，分5-bit给工作机器。这样就可以表示32个IDC，每个IDC下可以有32台机器，可以根据自身需求定义。12个自增序列号可以表示2^12个ID，理论上snowflake方案的QPS约为409.6w/s，这种分配方式可以保证在任何一个IDC的任何一台机器在任意毫秒内生成的ID都是不同的。

这种方式的优缺点是：

优点：

- 毫秒数在高位，自增序列在低位，整个ID都是趋势递增的。
- 不依赖数据库等第三方系统，以服务的方式部署，稳定性更高，生成ID的性能也是非常高的。
- 可以根据自身业务特性分配bit位，非常灵活。

缺点：

- 强依赖机器时钟，如果机器上时钟回拨，会导致发号重复或者服务会处于不可用状态。

这个项目在2010就停止维护了，但这个设计思路还是应用于其他各个ID生成器及变种。

# 基于redis的分布式ID生成器

GitHub 地址：https://github.com/hengyunabc/redis-id-generator

利用redis的lua脚本执行功能，在每个节点上通过lua脚本生成唯一ID。 生成的ID是64位的：

- 使用41 bit来存放时间，精确到毫秒，可以使用41年。
- 使用12 bit来存放逻辑分片ID，最大分片ID是4095
- 使用10 bit来存放自增长ID，意味着每个节点，每毫秒最多可以生成1024个ID、

比如GTM时间 Fri Mar 13 10:00:00 CST 2015 ，它的距1970年的毫秒数是 1426212000000，假定分片ID是53，自增长序列是4，则生成的ID是：

`5981966696448054276 = 1426212000000 << 22 + 53 << 10 + 4`

**注意事项**

- 要求redis server版本是3.2以上，因为使用到了redis.replicate_commands()

- 因为是利用了redis的time命令来获取到redis服务器上的时间，所以reids服务器的时间要保证是只增长的，要关闭服务器上的ntp等时间同步机制。

# MongoDB的ObjectId

[MongoDB官方文档 ObjectID](https://docs.mongodb.com/manual/reference/method/ObjectId/#description)
可以算作是和snowflake类似方法，通过“时间+机器码+pid+inc”共12个字节，通过4+3+2+3的方式最终标识成一个24长度的十六进制字符。

Mongodb集合中的每个document中都必须有一个"_id"键，这个键的值可以是任何类型的，在默认的情况下是个Objectid对象。mongodb的ObejctId生产思想在很多方面挺值得我们借鉴的，特别是在大型分布式的开发，如何构建轻量级的生产，如何将生产的负载进行转移，如何以空间换取时间提高生产的最大优化等等。

# Zookeeper的znode

TODO

# 百度的UidGenerator

GitHub 地址：https://github.com/baidu/uid-generator

UidGenerator是百度开源的分布式ID生成器，基于于snowflake算法的实现。

**Snowflake算法**

![image](https://raw.githubusercontent.com/baidu/uid-generator/master/doc/snowflake.png)

Snowflake算法描述：指定机器 & 同一时刻 & 某一并发序列，是唯一的。据此可生成一个64 bits的唯一ID（long）。默认采用上图字节分配方式：

- sign(1bit) 

    固定1bit符号标识，即生成的UID为正数。

- delta seconds (28 bits)

    当前时间，相对于时间基点"2016-05-20"的增量值，单位：秒，最多可支持约8.7年

- worker id (22 bits)

    机器id，最多可支持约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。

- sequence (13 bits)

    每秒下的并发序列，13 bits可支持每秒8192个并发。

**DefaultUidGenerator**

- 传统Snowflake算法的实现，同步阻塞以等待时间到达

**CachedUidGenerator**

- 借用未来时间来解决sequence天然存在的并发限制
- RingBuffer环形数组缓存已生成的UID, 并行化UID的生产和消费
- 对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题
- 初始化预填充、即时填充、周期填充
- 能提供600万/s的稳定吞吐量

**CachedUidGenerator关于UID比特分配的建议**

对于并发数要求不高、期望长期使用的应用, 可增加timeBits位数, 减少seqBits位数. 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天, 那么配置成{"workerBits":23,"timeBits":31,"seqBits":9}时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年.

对于节点重启频率频繁、期望长期使用的应用, 可增加workerBits和timeBits位数, 减少seqBits位数. 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天, 那么配置成{"workerBits":27,"timeBits":30,"seqBits":6}时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.

计算公式 

```
(24 * 60 * 60) / (2 ^ (31 - 23)) = 337.5 (个WorkerId)
337.5 / 12 = 28.125 (重启次数/每个节点)
28.125 * (2 ^ 9) = 14400 (UID/s)
```

# 美团的Leaf

GitHub 地址：https://github.com/Meituan-Dianping/Leaf

Leaf是美团开源的分布式ID生成器，能保证全局唯一性、趋势递增、单调递增、信息安全，里面也提到了几种分布式方案的对比，但也需要依赖关系数据库、Zookeeper等中间件。

**Segment**

- Leaf节点Round-robin调用
- 双Buffer优化
- 超过阈值预加载
- 动态调整Step
- MySql高可用
- 解决时钟回拨
- Leaf监控

**Snowflake**

- Zookeeper生成机器号
- 本机文件系统缓存workerID文件，弱依赖Zookeeper

# ecp-uid

GitHub 地址：https://github.com/linhuaichuan/ecp-uid

基于美团leaf、百度UidGenerator、原生snowflake 进行整合的 唯一ID生成器

# 方案切换

## redis-id-generator -> uid-generator.CachedUidGenerator（Client部署）

**41-12-10**

- 时间 2004-11-04 03:53:47
- 时间戳 1099511627775
- 时间戳二进制 01111111111111111111111111111111111111111

掌控着系统中的ID可以确保都是大于2018年的，所以时间戳首位肯定是1。如果当id-generator的时间戳首位固定为0时，可以确保ID不会重复。

**32-21-10**

timeBits的32位中首位固定位1，剩下31位还能持续运行68年。
seqBits的10位，可以支持单节点1024UID/s的并发量持续运行68年。

整体分析如下：
节点采取用完即弃的WorkerIdAssigner策略, 重启频率为1次/天, 那么配置成{"workerBits":21,"timeBits":32,"seqBits":9}时, 可支持84个节点以整体并发量43008UID/s的速度持续运行68年。68年后继续使用的话就存在ID重复的风险。

**QPS**

由于采用“借用未来时间（automicLong.increaseAndGet）”，故最大并发量并不是由seqBits所限定，而是如官方所述支持600万QPS的并发。

## redis-id-generator -> uid-generator.DefaultUidGenerator（Client部署）

UID比特分配 同 uid-generator.CachedUidGenerator

**QPS**

由于采用“阻塞时间法（while循环等待时间到达）”，故最大并发量是由seqBits所限定，即单节点最大支持1024UID/s的并发。

## redis-id-generator -> leaf.Snowflake（Server部署）

UID比特分配同“传统Snowflake算法”完全一致，无需更改。

## redis-id-generator -> leaf.Segment（Server部署）

41-12-10

- 时间 2019-08-01 00:00:00
- 时间戳 1564588800000
- 时间戳二进制 10110110001001000110000100111100000000000

10110110001001000110000100111100000000000 << 22 + 0 << 10 + 1 =

计算得到2进制 101101100010010001100001001111000000000000000000000000000000001

转换为10进制得到 6562361062195200000

因此，设置 biz_tag 的 maxId 为 6562361062195200000，可保证新生成的ID不与已有ID重复。

# 参考

[你不得不知的几个互联网ID生成器方案 - 歪脖贰点零 - CSDN博客](https://blog.csdn.net/hero272285642/article/details/79168601)

[分布式ID生成器的解决方案总结 - 简书](https://www.jianshu.com/p/c915190da1eb?from=timeline)

[Leaf——美团点评分布式ID生成系统](https://tech.meituan.com/2017/04/21/mt-leaf.html)

[Leaf：美团分布式ID生成服务开源](https://tech.meituan.com/2019/03/07/open-source-project-leaf.html)