# 事务、数据库事务、分布式事务

[TOC]

## 什么是事务

### 事务

事务是一个非常广义的词汇，各行各业解读都不一样。

对于程序员，事务等价于Transaction，是指一组连续的操作，这些操作组合成一个逻辑的、完整的操作。即这组操作执行前后，系统需要处于一个可预知的、一致的状态。因此，这一组操作要么都成功执行，要么都不能执行；如果部分成功，部分失败，成功的部分需要回滚（rollback）。

### 数据库事务

数据库事务（简称：事务，Transaction）是指数据库执行过程中的一个逻辑单位，由一个有限的数据库操作序列构成。

### 事务的核心特性

事务拥有以下四个特性，习惯上被称为 ACID 特性：

- 原子性（Atomicity）：事务作为一个整体被执行，包含在其中的对数据库的操作要么全部被执行，要么都不执行。

- 一致性（Consistency）：事务应确保数据库的状态从一个一致状态转变为另一个一致状态。一致状态是指数据库中的数据应满足完整性约束。除此之外，一致性还有另外一层语义，就是事务的中间状态不能被观察到（这层语义也有说应该属于原子性）。

- 隔离性（Isolation）：多个事务并发执行时，一个事务的执行不应影响其他事务的执行，如同只有这一个操作在被数据库所执行一样。

- 持久性（Durability）：已被提交的事务对数据库的修改应该永久保存在数据库中。在事务结束时，此操作将不可逆转。

### 事务的发展

起初，事务仅限于对单一数据库资源的访问控制：

![alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/resource-based-transaction.jpg )

架构服务化以后，事务的概念延伸到了服务中。倘若将一个单一的服务操作作为一个事务，那么整个服务操作只能涉及一个单一的数据库资源：

![alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/service-based-transaction.jpg )

这类基于单个服务单一数据库资源访问的事务，被称为本地事务（Local Transaction）。

本地事务主要限制在单个会话内，不涉及多个数据库资源。但是在基于SOA（Service-Oriented Architecture，面向服务架构）的分布式应用环境下，越来越多的应用要求对多个数据库资源，多个服务的访问都能纳入到同一个事务当中，分布式事务应运而生。

最早的分布式事务应用架构很简单，不涉及服务间的访问调用，仅仅是服务内操作涉及到对多个数据库资源的访问。

![alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/multi-resource-based-transaction.jpg )

当一个服务操作访问不同的数据库资源，又希望对它们的访问具有事务特性时，就需要采用分布式事务来协调所有的事务参与者。

对于上面介绍的分布式事务应用架构，尽管一个服务操作会访问多个数据库资源，但是毕竟整个事务还是控制在单一服务的内部。如果一个服务操作需要调用另外一个服务，这时的事务就需要跨越多个服务了。在这种情况下，起始于某个服务的事务在调用另外一个服务的时候，需要以某种机制流转到另外一个服务，从而使被调用的服务访问的资源也自动加入到该事务当中来。下图反映了这样一个跨越多个服务的分布式事务：

![alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/multi-service-based-transaction.jpg )

如果将上面这两种场景（一个服务可以调用多个数据库资源，也可以调用其他服务）结合在一起，对此进行延伸，整个分布式事务的参与者将会组成如下图所示的树形拓扑结构。在一个跨服务的分布式事务中，事务的发起者和提交均系同一个，它可以是整个调用的客户端，也可以是客户端最先调用的那个服务。

![alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/multi-service-based-multi-res-transaction.jpg )

较之基于单一数据库资源访问的本地事务，分布式事务的应用架构更为复杂。在不同的分布式应用架构下，实现一个分布式事务要考虑的问题并不完全一样，比如对多资源的协调、事务的跨服务传播等，实现机制也是复杂多变。尽管有这么多工程细节需要考虑，但分布式事务最核心的还是其 ACID 特性。因此，想要了解一个分布式事务，就先从了解它是怎么实现事务 ACID 特性开始。

### 两阶段提交（2pc, two-phase commit protocol），又叫做 XA Transactions

XA 是一个两阶段提交协议，该协议分为以下两个阶段：

- 第一阶段：

	事务协调器要求每个涉及到事务的数据库预提交(precommit)此操作，并反映是否可以提交.

- 第二阶段：

	事务协调器要求每个数据库提交数据。

2pc是非常经典的强一致性、中心化的原子提交协议。

- 强一致性

	todo

- 中心化

	指协议中有两类节点：一个中心化协调者节点（coordinator）和N个参与者节点（participant、cohort）。
	
讨论2PC的优缺点：

- 优点：

	强一致性，只要节点或者网络最终恢复正常，协议就能保证顺利结束；部分关系型数据库（Oracle）、框架直接支持

- 缺点：

	两阶段提交协议的容错能力较差，比如在节点宕机或者超时的情况下，无法确定流程的状态，只能不断重试；两阶段提交协议的性能较差， 消息交互多，且受最慢节点影响

## 分布式理论

### CAP定理

当我们的单个数据库的性能产生瓶颈的时候，我们可能会对数据库进行分区，这里所说的分区指的是物理分区，分区之后可能不同的库就处于不同的服务器上了，这个时候单个数据库的ACID已经不能适应这种情况了，而在这种ACID的集群环境下，再想保证集群的ACID几乎是很难达到，或者即使能达到那么效率和性能会大幅下降，最为关键的是再很难扩展新的分区了，这个时候如果再追求集群的ACID会导致我们的系统变得很差，这时我们就需要引入一个新的理论原则来适应这种集群的情况，就是 CAP 原则或者叫CAP定理。

CAP定理是由加州大学伯克利分校Eric Brewer教授提出来的，他指出WEB服务无法同时满足一下3个属性：

- 一致性(Consistency) ： 

	客户端知道一系列的操作都会同时发生(生效)
	
- 可用性(Availability) ： 

	每个操作都必须以可预期的响应结束

- 分区容错性(Partition tolerance) ： 

	即使出现单个组件无法可用,操作依然可以完成

具体地讲在分布式系统中，在任何数据库设计中，一个Web应用至多只能同时支持上面的两个属性。显然，任何横向扩展策略都要依赖于数据分区。因此，设计人员必须在一致性与可用性之间做出选择。

### BASE理论

在分布式系统中，我们往往追求的是可用性，它的重要程序比一致性要高，那么如何实现高可用性呢？ 前人已经给我们提出来了另外一个理论，就是BASE理论，它是用来对CAP定理进行进一步扩充的。

BASE理论指的是：

- Basically Available（基本可用）

- Soft state（软状态）

- Eventually consistent（最终一致性）

BASE理论是对CAP中的一致性和可用性进行一个权衡的结果，理论的核心思想就是：我们无法做到强一致，但每个应用都可以根据自身的业务特点，采用适当的方式来使系统达到最终一致性（Eventual consistency）。

## 分布式事务模型

### 基于XA的分布式事务模型

最早的分布式事务模型是 X/Open 国际联盟提出的 X/Open Distributed Transaction Processing（DTP）模型，也就是大家常说的 X/Open XA 协议，简称XA 协议。

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/xa-protocol-model.jpg) 

DTP 模型中包含一个全局事务管理器（TM，Transaction Manager）和多个资源管理器（RM，Resource Manager）。全局事务管理器负责管理全局事务状态与参与的资源，协同资源一起提交或回滚；资源管理器则负责具体的资源操作。

XA 协议描述了 TM 与 RM 之间的接口，允许多个资源在同一分布式事务中访问。

基于 DTP 模型的分布式事务流程大致如下：

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/dtp-model.jpg) 

应用程序（AP，Application）向 TM 申请开始一个全局事务。

针对要操作的 RM，AP 会先向 TM 注册（TM 负责记录 AP 操作过哪些 RM，即分支事务），TM 通过 XA 接口函数通知相应 RM 开启分布式事务的子事务，接着 AP 就可以对该 RM 管理的资源进行操作。

当 AP 对所有 RM 操作完毕后，AP 根据执行情况通知 TM 提交或回滚该全局事务，TM 通过 XA 接口函数通知各 RM 完成操作。TM 会先要求各个 RM 做预提交，所有 RM 返回成功后，再要求各 RM 做正式提交，XA 协议要求，一旦 RM 预提交成功，则后续的正式提交也必须能成功；如果任意一个 RM 预提交失败，则 TM 通知各 RM 回滚。

所有 RM 提交或回滚完成后，全局事务结束。

#### 保证分布式事务的 ACID 特性

##### 原子性

XA 协议使用 2PC（Two Phase Commit，两阶段提交）原子提交协议来保证分布式事务原子性。

两阶段提交是指将提交过程分为两个阶段，即准备阶段（投票阶段）和提交阶段（执行阶段）：


准备阶段

TM 向每个 RM 发送准备消息。如果 RM 的本地事务操作执行成功，则返回成功；如果 RM 的本地事务操作执行失败，则返回失败。

提交阶段

如果 TM 收到了所有 RM 回复的成功消息，则向每个 RM 发送提交消息；否则发送回滚消息；RM 根据 TM 的指令执行提交或者回滚本地事务操作，释放所有事务处理过程中使用的锁资源。

##### 隔离性

XA 协议中没有描述如何实现分布式事务的隔离性，但是 XA 协议要求DTP 模型中的每个 RM 都要实现本地事务，也就是说，基于 XA 协议实现的分布式事务的隔离性是由每个 RM 本地事务的隔离性来保证的，当一个分布式事务的所有子事务都是隔离的，那么这个分布式事务天然的就实现了隔离性。

以 MySQL 来举例，MySQL 使用 2PL（Two-Phase Locking，两阶段锁）机制来控制本地事务的并发，保证隔离性。2PL 与 2PC 类似，也是将锁操作分为加锁和解锁两个阶段，并且保证两个阶段完全不相交。加锁阶段，只加锁，不放锁。解锁阶段，只放锁，不加锁。

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/xa-isolation.jpg) 

如上图所示，在一个本地事务中，每执行一条更新操作之前，都会先获取对应的锁资源，只有获取锁资源成功才会执行该操作，并且一旦获取了锁资源就会持有该锁资源直到本事务执行结束。

MySQL 通过这种 2PL 机制，可以保证在本地事务执行过程中，其他并发事务不能操作相同资源，从而实现了事务隔离。

##### 一致性

前面提到一致性有两层语义，一层是确保事务执行结束后，数据库从一个一致状态转变为另一个一致状态。另一层语义是事务执行过程中的中间状态不能被观察到。

前一层语义的实现很简单，通过原子性、隔离性以及 RM 自身一致性的实现就可以保证。至于后一层语义，我们先来看看单个 RM 上的本地事务是怎么实现的。还是以 MySQL 举例，MySQL 通过 MVCC（Multi Version Concurrency Control，多版本并发控制）机制，为每个一致性状态生成快照（Snapshot），每个事务看到的都是各Snapshot对应的一致性状态，从而也就保证了本地事务的中间状态不会被观察到。

虽然单个 RM 上实现了Snapshot，但是在分布式应用架构下，会遇到什么问题呢？

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/xa-consistency.jpg) 

如上图所示，在 RM1 的本地子事务提交完毕到 RM2 的本地子事务提交完毕之间，只能读到 RM1 上子事务执行的内容，读不到 RM2 上的子事务。也就是说，虽然在单个 RM 上的本地事务是一致的，但是从全局来看，一个全局事务执行过程的中间状态被观察到了，全局一致性就被破坏了。

XA 协议并没有定义怎么实现全局的 Snapshot，像 MySQL 官方文档里就建议使用串行化的隔离级别来保证分布式事务一致性：

“As with nondistributed transactions, SERIALIZABLE may be preferred if your applications are sensitive to read phenomena. REPEATABLE READ may not be sufficient for distributed transactions.”（对于分布式事务来说，可重复读隔离级别不足以保证事务一致性，如果你的程序有全局一致性读要求，可以考虑串行化隔离级别.）

当然，由于串行化隔离级别的性能较差，所以很多分布式数据库都自己实现了分布式 MVCC 机制来提供全局的一致性读。一个基本思路是用一个集中式或者逻辑上单调递增的东西来控制生成全局 Snapshot，每个事务或者每条 SQL 执行时都去获取一次，从而实现不同隔离级别下的一致性。比如 Google 的 Spanner 就是用 TrueTime 来控制访问全局 Snapshot。


XA 直接作用于资源层

##### 小结

#### 两阶段提交（2PC, two-phase commit）

##### 没想好

##### 2PC故障情况分析

1.协调者正常，参与者宕机

- 发生在第二阶段：无论协调者发起的是提交还是终止，那宕机的参与者在重启之后，都将执行对应操作，不存在不一致情况。

- 发生在第一阶段：由于协调者无法收集到所有参与者的反馈，会陷入阻塞情况。

解决办法：引入超时机制。
超过指定时间未收到反馈，事务失败，向所有节点发送终止事务请求。
宕机的节点启动后，收到终止事务请求，该事务失败。

小结
简单来说，由于协调者没挂，无论参与者是否宕机，都可以从协调者这里获取正确的状态。

2.协调者宕机，参与者正常

- 无论处于哪个阶段，由于协调者宕机，无法发送提交请求，所有处于执行了操作但是未提交状态的参与者都会陷入阻塞情况。

解决办法：引入协调者备份，同时协调者需记录操作日志。
当检测到协调者宕机一段时间后，协调者备份取代协调者，并读取操作日志，向所有参与者询问状态。
如果存在commit或者abort，则对所有节点执行对应的操作。（是不会存在既有commit和abort的情况的）
如果只有prepare和未执行，可以继续完成该事务，也可以终止该事务。最终的状态是一致的。

小结
由于参与者正常，引入协调者备份和日志机制后，总能根据日志获取未完成的事务，并向各个参与者查询出对应的状态，并针对性的处理。

3.协调者、参与者都发生了宕机

首先协调者宕机，那我们需要使用协调者备份，该协调者备份启动后，通过日志找到未完成的事务，对各参与者发起询问。由于参与者存在宕机，我们细分为以下3种情况： 

- 存在commit或者abort，则执行对应操作。 

- 存在未开始的节点，说明没有节点到第二阶段，可以直接终止。 

- 剩下的情况就是：未宕机的节点都是prepare状态。这种情况最复杂，因为无法判断宕机节点的状态，它可能是commit、abort、prepare或者未开始。

下面具体分析第三种情况： 

- 如果宕机节点是prepare或者未开始的话，则终止就可以了，这种情况没问题。 

- 如果宕机节点是commit或者abort的话，由于该结点的事务已经结束，无法改变，所以对于其他节点必须执行相同的操作，否则就会不一致。但是你不知道该节点到底是什么状态，除非它重新启动了。

小结
出现第三种情况的话，由于无法判断事务状态，则无法完成该事务。该问题无法解决，只能等待参与者恢复，并确认参与者的状态。

#### 三阶段提交（3PC, three-phase commit）

##### 没想好

##### 3PC故障情况分析

2PC与3PC异同

- 2PC在第一阶段就执行对应操作了（资源被锁定），而第二阶段是提交。

- 3PC第一阶段只是询问能否提交，并不锁定资源；第二阶段再锁定资源执行对应操作；第三阶段是提交。

- 3PC如果第二阶段执行后，超过一定时间未收到协调者信息，会自动提交。

3PC的状态

- 处于第一阶段（CanCommit）：参与者返回YES、NO。

- 处于第二阶段（PreCommit）：协调者如果第一阶段存在NO或者超时则ABORT，如果全部是YES则PREPARE。参与者返回ACK。

- 处于第三阶段（doCommit）：协调者如果第二阶段存在NO或者超时则ABORT，如果全部是ACK则COMMIT。参与者返回ACK。

3PC故障情况分析

这里跳过只有协调者或者只有参与者宕机的情况，这2种情况的解决方案与2PC一致。直接讨论协调者、参与者都发生了宕机，为了简化，假设参与者只宕机了一台。 

- 存在COMMIT、ABORT，执行对应操作 

- 存在处于第一阶段或者未开始的节点，说明事务未进入第三阶段，直接终止。可能出现宕机节点处于第二阶段的情况，但无论宕机节点是abort或者prepare或者第一阶段或者未开始，都可以执行abort指令。 

- 未宕机节点全是prepare状态。如果宕机参与者是PREPARE状态，重启后会收到COMMIT消息，这种没问题。如果是COMMIT状态，也没问题。如果是ABORT状态，由于其他节点变成了COMMIT，此时出现状态不一致。如果是宕机节点未收到PREPARE消息，也就是处于第一阶段完成后，这个时候他只可能是返回YES，重启后收到PREPARE和COMMIT消息后，状态也正确。

#### 小结

XA 协议通常实现在数据库资源层，直接作用于资源管理器上。因此，基于 XA 协议实现的分布式事务产品，无论是分布式数据库，还是分布式事务框架，对业务几乎都没有侵入，就像使用普通数据库一样。

XA 协议严格保障事务 ACID 特性，能够满足所有业务领域的功能需求，但是，这同样是一把双刃剑。

由于隔离性的互斥要求，在事务执行过程中，所有的资源都被锁定，只适用于执行时间确定的短事务。同时，整个事务期间都是独占数据，对于热点数据的并发性能可能会很低，实现了分布式 MVCC 或乐观锁（optimistic locking）以后，性能可能会有所提升。

同时，为了保障一致性，要求所有 RM 同等可信、可靠，要求故障恢复机制可靠、快速，在网络故障隔离的情况下，服务基本不可用。

我们可以发现3PC使用超时自动提交的方式解决了2PC中无法判断状态需要不断等待的情况，也就是提升了服务可用性。但是3PC比2PC多了一个步骤，所以延时应该是增加了。同时3PC可能存在网络分化情况，也就是协调者在第二阶段发送了PREPARE消息后，如果在第三阶段发送ABORT消息，则如果网络出现问题，导致消息部分节点未送达，就会出现某些节点ABORT了，某些节点COMMIT了。

也就是说，3PC实际并没有解决2PC中的那种协调者和参与者同时宕机的场景，只是做了自动提交，来提升服务可用性。

那2PC能否做自动提交呢？2PC由于只有2个阶段，第一阶段就已经执行对应的操作了（锁资源），如果没有收到第二阶段的COMMIT请求就自动提交，那连一次跟其他节点确认的机会都没有，这样出错的可能性很大。3PC至少是经过第一轮跟其他节点确认后，才会进行超时自动提交的，降低了出错概率。

综上，2PC延迟低，但是参与者故障后可能会阻塞；3PC可用性高，但可能出现数据不一致（网络分化），以及多了个步骤导致延迟会高一些。

### 基于TCC的分布式事务模型

TCC事务机制相对于传统事务机制（X/Open XA），其特征在于它不依赖资源管理器(RM)对XA的支持，而是通过对（由业务系统提供的）业务逻辑的调度来实现分布式事务。

TCC 模型认为对于业务系统中一个特定的业务逻辑，其对外提供服务时，必须接受一些不确定性，即对业务逻辑初步操作的调用仅是一个临时性操作，调用它的主业务服务保留了后续的取消权。如果主业务服务认为全局事务应该回滚，它会要求取消之前的临时性操作，这就对应从业务服务的取消操作。而当主业务服务认为全局事务应该提交时，它会放弃之前临时性操作的取消权，这对应从业务服务的确认操作。每一个初步操作，最终都会被确认或取消。

因此，针对一个具体的业务服务，TCC 分布式事务模型需要业务系统提供三段业务逻辑：

- 初步操作 Try：完成所有业务检查，预留必须的业务资源。

	从执行阶段来看，与传统事务机制中业务逻辑相同。但从业务角度来看，是不一样的。TCC机制中的Try仅是一个初步操作，它和后续的次确认一起才能真正构成一个完整的业务逻辑。因此，可以认为[传统事务机制]的业务逻辑 = [TCC事务机制]的初步操作（Try） + [TCC事务机制]的确认逻辑（Confirm）。TCC机制将传统事务机制中的业务逻辑一分为二，拆分后保留的部分即为初步操作（Try）；而分离出的部分即为确认操作（Confirm），被延迟到事务提交阶段执行。

	TCC事务机制以初步操作（Try）为中心，确认操作（Confirm）和取消操作（Cancel）都是围绕初步操作（Try）而展开。因此，Try阶段中的操作，其保障性是最好的，即使失败，仍然有取消操作（Cancel）可以将其不良影响进行回撤。

- 确认操作 Confirm：真正执行的业务逻辑，不作任何业务检查，只使用 Try 阶段预留的业务资源。因此，只要 Try 操作成功，Confirm 必须能成功。另外，Confirm 操作需满足幂等性，保证一笔分布式事务有且只能成功一次。

	确认操作（Confirm）是对初步操作（Try）的一个补充。当TCC事务管理器认为全局事务可以正确提交时，就会逐个执行初步操作（Try）指定的确认操作（Confirm），将初步操作（Try）未完成的事项最终完成。

- 取消操作 Cancel：释放 Try 阶段预留的业务资源。同样的，Cancel 操作也需要满足幂等性。

	对初步操作（Try）的一个回撤。当TCC事务管理器认为全局事务不能正确提交时，就会逐个执行初步操作（Try）指定的取消操作（Cancel），将初步操作（Try）已完成的事项全部撤回。

TCC 分布式事务模型包括三部分：

- 主业务服务：主业务服务为整个业务活动的发起方，服务的编排者，负责发起并完成整个业务活动。

- 从业务服务：从业务服务是整个业务活动的参与方，负责提供 TCC 业务操作，实现初步操作（Try）、确认操作（Confirm）、取消操作（Cancel）三个接口，供主业务服务调用。

-业务活动管理器：业务活动管理器管理控制整个业务活动，包括记录维护 TCC 全局事务的事务状态和每个从业务服务的子事务状态，并在业务活动提交时调用所有从业务服务的 Confirm 操作，在业务活动取消时调用所有从业务服务的 Cancel 操作。

一个完整的 TCC 分布式事务流程如下：

1. 主业务服务首先开启本地事务；

2. 主业务服务向业务活动管理器申请启动分布式事务主业务活动；

3. 然后针对要调用的从业务服务，主业务活动先向业务活动管理器注册从业务活动，然后调用从业务服务的 Try 接口；

4. 当所有从业务服务的 Try 接口调用成功，主业务服务提交本地事务；若调用失败，主业务服务回滚本地事务；

5. 若主业务服务提交本地事务，则 TCC 模型分别调用所有从业务服务的 Confirm 接口；若主业务服务回滚本地事务，则分别调用 Cancel 接口；

6. 所有从业务服务的 Confirm 或 Cancel 操作完成后，全局事务结束。

#### 保证分布式事务的 ACID 特性

##### 原子性

TCC 模型也使用 2PC 原子提交协议来保证事务原子性。Try 操作对应2PC 的一阶段准备（Prepare）；Confirm 对应 2PC 的二阶段提交（Commit），Cancel 对应 2PC 的二阶段回滚（Rollback），可以说 TCC 就是应用层的 2PC。

##### 隔离性

TCC 分布式事务模型仅提供两阶段原子提交协议，保证分布式事务原子性。事务的隔离交给业务逻辑来实现。

隔离的本质是控制并发，防止并发事务操作相同资源而引起的结果错乱。

举个例子，比如金融行业里管理用户资金，当用户发起交易时，一般会先检查用户资金，如果资金充足，则扣除相应交易金额，增加卖家资金，完成交易。如果没有事务隔离，用户同时发起两笔交易，两笔交易的检查都认为资金充足，实际上却只够支付一笔交易，结果两笔交易都支付成功，导致资损。

可以发现，并发控制是业务逻辑执行正确的保证，但是像两阶段锁这样的并发访问控制技术要求一直持有数据库资源锁直到整个事务执行结束，特别是在分布式事务架构下，要求持有锁到分布式事务第二阶段执行结束，也就是说，分布式事务会加长资源锁的持有时间，导致并发性能进一步下降。

因此，TCC 模型的隔离性思想就是通过业务的改造，在第一阶段结束之后，从底层数据库资源层面的加锁过渡为上层业务层面的加锁，从而释放底层数据库锁资源，放宽分布式事务锁协议，提高业务并发性能。

还是以上面的例子举例：

1. 第一阶段：检查用户资金，如果资金充足，冻结用户本次交易资金，这笔资金被业务隔离，不允许除本事务之外的其它并发事务动用。

2. 第二阶段：扣除第一阶段预冻结的用户资金，增加卖家资金，完成交易。

采用业务加锁的方式，隔离用户冻结资金，在第一阶段结束后直接释放底层资源锁，该用户和卖家的其他交易都可以立刻并发执行，而不用等到整个分布式事务结束，可以获得更高的并发交易能力。

##### 一致性

再来看看 TCC 分布式事务模型下的一致性实现。与 XA 协议实现一致性第一层语义类似，通过原子性保证事务的原子提交、业务隔离性控制事务的并发访问，实现分布式事务的一致性状态转变。

至于第二层语义：事务的中间状态不能被观察到。我们来看看，在 SOA分布式应用环境下是否是必须的。

还是以账务服务举例。转账业务（用户 A -> 用户 B），由交易服务和账务服务组成分布式事务，交易服务作为主业务服务，账务服务作为从业务服务，账务服务的 Try 操作预冻结用户 A 的资金；Commit 操作扣除用户 A 的预冻结资金，增加用户 B 的可用资金；Cancel 操作解冻用户 A 的预冻结资金。

当账务服务执行完 Try 阶段后，交易主业务就可以 Commit 了，然后由TCC 框架调用账务的 Commit 阶段。在账务 Commit 阶段还没执行结束的时候，用户 A 可以查询到自己的余额已扣除，但是，此时用户 B 的可用资金还没增加。

从系统的角度来看，确实有问题与不确定性。在第一阶段执行结束到第二阶段执行结束之间，有一段时间的延时，在这段时间内，看似任何用户都不享有这笔资产。

但是，从用户的角度来考虑这个问题的话，这个时间间隔可能就无所谓或者根本就不存在。特别是当这个时间间隔仅仅是几秒钟，对于具体沟通资产转移的用户来讲，这个过程是隐蔽的或确实可以接受的，且保证了结果的最终一致性。

当然，对于这样的系统，如果确实需要查看系统的某个一致性状态，可以采用额外的方法实现。

一般来讲，服务之间的一致性比服务内部的一致性要更加容易弱化，这也是为什么 XA 等直接在资源层面上实现通用分布式事务的模型会注重一致性的保证，而当上升到服务层面，服务与服务之间已经实现了功能的划分，逻辑的解耦，也就更容易弱化一致性，这就是 SOA 架构下 BASE 理论的最终一致性思想。

BASE 理论是指 BA（Basic Availability，基本业务可用性）；S（Soft state，柔性状态）；E（Eventual consistency，最终一致性）。该理论认为为了可用性、性能与降级服务的需要，可以适当降低一点一致性的要求，即“基本可用，最终一致”。

业内通常把严格遵循 ACID 的事务称为刚性事务；而基于 BASE 思想实现的事务称为柔性事务。柔性事务并不是完全放弃了 ACID，仅仅是放宽了一致性要求：事务完成后的一致性严格遵循，事务中的一致性可适当放宽。

##### 小结

TCC 分布式事务模型的业务实现特性决定了其可以跨 DB、跨服务实现资源管理，将对不同的 DB 访问、不同的业务操作通过 TCC 模型协调为一个原子操作，解决了分布式应用架构场景下的事务问题。

TCC 模型通过 2PC 原子提交协议保证分布式事务的的原子性，把资源层的隔离性上升到业务层，交给业务逻辑来实现。TCC 的每个操作对于资源层来说，就是单个本地事务的使用，操作结束则本地事务结束，规避了资源层在 2PC 和 2PL 下对资源占用导致的性能低下问题。

同时，TCC 模型也可以根据业务需要，做一些定制化的功能，比如交易异步化实现削峰填谷等。

但是，业务接入 TCC 模型需要拆分业务逻辑成两个阶段，并实现 Try、Confirm、Cancel 三个接口，定制化程度高，开发成本高。

#### 通用型TCC解决方案

所有从业务服务都需要参与到主业务服务的决策当中。

![Alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/general-tcc.jpg)

适用场景

由于从业务服务是同步调用，其结果会影响到主业务服务的决策，因此通用型 TCC 分布式事务解决方案适用于执行时间确定且较短的业务

#### 异步确保型TCC解决方案

异步确保型 TCC 解决方案的直接从业务服务是可靠消息服务，而真正的从业务服务则通过消息服务解耦，作为消息服务的消费端，异步地执行。

![Alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/async-tcc.jpg)

可靠消息服务需要提供 Try，Confirm，Cancel 三个接口。Try 接口预发送，只负责持久化存储消息数据；Confirm 接口确认发送，这时才开始真正的投递消息；Cancel 接口取消发送，删除消息数据。

消息服务的消息数据独立存储，独立伸缩，降低从业务服务与消息系统间的耦合，在消息服务可靠的前提下，实现分布式事务的最终一致性。

此解决方案虽然增加了消息服务的维护成本，但由于消息服务代替从业务服务实现了 TCC 接口，从业务服务不需要任何改造，接入成本非常低。

适用场景

由于从业务服务消费消息是一个异步的过程，执行时间不确定，可能会导致不一致时间窗口增加。因此，异步确保性 TCC 分布式事务解决方案只适用于对最终一致性时间敏感度较低的一些被动型业务（从业务服务的处理结果不影响主业务服务的决策，只被动的接收主业务服务的决策结果）。

#### 补偿型TCC解决方案

补偿型 TCC 解决方案与通用型 TCC 解决方案的结构相似，其从业务服务也需要参与到主业务服务的活动决策当中。但不一样的是，前者的从业务服务只需要提供 Do 和 Compensate 两个接口，而后者需要提供三个接口。

![Alt text](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/compensate-tcc.jpg)

Do 接口直接执行真正的完整业务逻辑，完成业务处理，业务执行结果外部可见；Compensate 操作用于业务补偿，抵消或部分抵消正向业务操作的业务结果，Compensate操作需满足幂等性。

与通用型解决方案相比，补偿型解决方案的从业务服务不需要改造原有业务逻辑，只需要额外增加一个补偿回滚逻辑即可，业务改造量较小。但要注意的是，业务在一阶段就执行完整个业务逻辑，无法做到有效的事务隔离，当需要回滚时，可能存在补偿失败的情况，还需要额外的异常处理机制，比如人工介入。

适用场景

由于存在回滚补偿失败的情况，补偿型 TCC 分布式事务解决方案只适用于一些并发冲突较少或者需要与外部交互的业务，这些外部业务不属于被动型业务，其执行结果会影响主业务服务的决策。

#### TCC和两阶段分布式事务处理的区别

#### TCC是如何解决两阶段分布式事务处理的问题

#### 小结

### 基于消息的分布式事务

#### 本地消息表（异步确保）

本地消息表这种实现方式应该是业界使用最多的，其核心思想是将分布式事务拆分成本地事务进行处理，这种思路是来源于ebay。我们可以从下面的流程图中看出其中的一些细节：

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/local-msg-table.jpeg) 

基本思路就是：

消息生产方，需要额外建一个消息表，并记录消息发送状态。消息表和业务数据要在一个事务里提交，也就是说他们要在一个数据库里面。然后消息会经过MQ发送到消息的消费方。如果消息发送失败，会进行重试发送。

消息消费方，需要处理这个消息，并完成自己的业务逻辑。此时如果本地事务处理成功，表明已经处理成功了，如果处理失败，那么就会重试执行。如果是业务上面的失败，可以给生产方发送一个业务补偿消息，通知生产方进行回滚等操作。

生产方和消费方定时扫描本地消息表，把还没处理完成的消息或者失败的消息再发送一遍。如果有靠谱的自动对账补账逻辑，这种方案还是非常实用的。

这种方案遵循BASE理论，采用的是最终一致性，我认为是所有方案里面比较适合实际业务场景的，即不会出现像2PC那样复杂的实现(当调用链很长的时候，2PC的可用性是非常低的)，也不会像TCC那样可能出现确认或者回滚不了的情况。

优点： 一种非常经典的实现，避免了分布式事务，实现了最终一致性。在 .NET中 有现成的解决方案。

缺点： 消息表会耦合到业务系统中，如果没有封装好的解决方案，会有很多杂活需要处理。

#### MQ 非事务消息

通常情况下，在使用非事务消息支持的MQ产品时，我们很难将业务操作与对MQ的操作放在一个本地事务域中管理。通俗点描述，还是以上述提到的“跨行转账”为例，我们很难保证在扣款完成之后对MQ投递消息的操作就一定能成功。这样一致性似乎很难保证。 
先从消息生产者这端来分析，请看伪代码：

```
public void trans() {
	try {
		// 1. 操作数据库
		boolean result = dao.update(model);// 如果执行失败，会抛出异常
		
		// 2. 如果第一步成功，则操作消息队列
		if (result) {
			mq.append(model);// 如果执行失败，会抛出异常
		}
	} catch(Exception e) {
		rollback();// 如果 发生异常，则回滚
	}
}
```

根据上述代码及注释，我们来分析下可能的情况：

> 1. 操作数据库成功，向MQ中投递消息也成功，皆大欢喜 
> 2. 操作数据库失败，不会向MQ中投递消息了
>  3. 操作数据库成功，但是向MQ中投递消息时失败，向外抛出了异常，刚刚执行的更新数据库的操作将被回滚

从上面分析的几种情况来看，貌似问题都不大的。那么我们来分析下消费者端面临的问题：

> 1. 消息出列后，消费者对应的业务操作要执行成功。如果业务执行失败，消息不能失效或者丢失。需要保证消息与业务操作一致 
> 2. 尽量避免消息重复消费。如果重复消费，也不能因此影响业务结果

如何保证消息与业务操作一致，不丢失？

主流的MQ产品都具有持久化消息的功能。如果消费者宕机或者消费失败，都可以执行重试机制的（有些MQ可以自定义重试次数）。

如何避免消息被重复消费造成的问题？

> 1. 保证消费者调用业务的服务接口的幂等性 
> 2. 通过消费日志或者类似状态表来记录消费状态，便于判断（建议在业务上自行实现，而不依赖MQ产品提供该特性）

---

> 总结：这种方式比较常见，性能和吞吐量是优于使用关系型数据库消息表的方案。如果MQ自身和业务都具有高可用性，理论上是可以满足大部分的业务场景的。不过在没有充分测试的情况下，不建议在交易业务中直接使用。

#### MQ 事务消息

以RocketMQ为例：

以RabbitMQ为例：

这里首先探讨下RabbitMQ事务机制。

RabbitMQ中与事务机制有关的方法有三个：txSelect(), txCommit()以及txRollback(), txSelect用于将当前channel设置成transaction模式，txCommit用于提交事务，txRollback用于回滚事务，在通过txSelect开启事务之后，我们便可以发布消息给broker代理服务器了，如果txCommit提交成功了，则消息一定到达了broker了，如果在txCommit执行之前broker异常崩溃或者由于其他原因抛出异常，这个时候我们便可以捕获异常通过txRollback回滚事务了。

关键代码：

```
channel.txSelect();
channel.basicPublish(ConfirmConfig.exchangeName, ConfirmConfig.routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, ConfirmConfig.msg_10B.getBytes());
channel.txCommit();
```

通过wirkshark抓包（ip.addr==xxx.xxx.xxx.xxx && amqp），可以看到：

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/wirkshark-tx.png) 

（注意这里的Tx.Commit与Tx.Commit-Ok之间的时间间隔294ms，由此可见事务还是很耗时的。）

我们先来看看没有事务的通信过程是什么样的： 

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/wirkshark-no-tx.png) 

可以看到带事务的多了四个步骤：

- client发送Tx.Select
- broker发送Tx.Select-Ok(之后publish)
- client发送Tx.Commit
- broker发送Tx.Commit-Ok

下面我们来看下事务回滚是什么样子的。关键代码如下：

```
try {
    channel.txSelect();
    channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
    int result = 1 / 0;
    channel.txCommit();
} catch (Exception e) {
    e.printStackTrace();
    channel.txRollback();
}
```

同样通过wireshark抓包可以看到： 

![](https://github.com/LuckyTerry/ReadingNotes/raw/master/Screenshots/wirkshark-tx-rollback.png) 

代码中先是发送了消息至broker中但是这时候发生了异常，之后在捕获异常的过程中进行事务回滚。

事务确实能够解决producer与broker之间消息确认的问题，只有消息成功被broker接受，事务提交才能成功，否则我们便可以在捕获异常进行事务回滚操作同时进行消息重发，但是使用事务机制的话会降低RabbitMQ的性能，那么有没有更好的方法既能保障producer知道消息已经正确送到，又能基本上不带来性能上的损失呢？从AMQP协议的层面看是没有更好的方法，但是RabbitMQ提供了一个更好的方案，即将channel信道设置成confirm模式。

#### RabbitMQ的confirm机制

##### 概述

上面我们介绍了RabbitMQ可能会遇到的一个问题，即生成者不知道消息是否真正到达broker，随后通过AMQP协议层面为我们提供了事务机制解决了这个问题，但是采用事务机制实现会降低RabbitMQ的消息吞吐量，那么有没有更加高效的解决方式呢？答案是采用Confirm模式。

##### producer端confirm模式的实现原理

生产者将信道设置成confirm模式，一旦信道进入confirm模式，所有在该信道上面发布的消息都会被指派一个唯一的ID(从1开始)，一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者（包含消息的唯一ID）,这就使得生产者知道消息已经正确到达目的队列了，如果消息和队列是可持久化的，那么确认消息会将消息写入磁盘之后发出，broker回传给生产者的确认消息中deliver-tag域包含了确认消息的序列号，此外broker也可以设置basic.ack的multiple域，表示到这个序列号之前的所有消息都已经得到了处理。

confirm模式最大的好处在于他是异步的，一旦发布一条消息，生产者应用程序就可以在等信道返回确认的同时继续发送下一条消息，当消息最终得到确认之后，生产者应用便可以通过回调方法来处理该确认消息，如果RabbitMQ因为自身内部错误导致消息丢失，就会发送一条nack消息，生产者应用程序同样可以在回调方法中处理该nack消息。

在channel 被设置成 confirm 模式之后，所有被 publish 的后续消息都将被 confirm（即 ack） 或者被nack一次。但是没有对消息被 confirm 的快慢做任何保证，并且同一条消息不会既被 confirm又被nack 。

##### 开启confirm模式的方法

生产者通过调用channel的confirmSelect方法将channel设置为confirm模式，如果没有设置no-wait标志的话，broker会返回confirm.select-ok表示同意发送者将当前channel信道设置为confirm模式(从目前RabbitMQ最新版本3.6来看，如果调用了channel.confirmSelect方法，默认情况下是直接将no-wait设置成false的，也就是默认情况下broker是必须回传confirm.select-ok的)。

> 已经在transaction事务模式的channel是不能再设置成confirm模式的，即这两种模式是不能共存的。

##### 编程模式

对于固定消息体大小和线程数，如果消息持久化，生产者confirm(或者采用事务机制)，消费者ack那么对性能有很大的影响.

消息持久化的优化没有太好方法，用更好的物理存储（SAS, SSD, RAID卡）总会带来改善。生产者confirm这一环节的优化则主要在于客户端程序的优化之上。归纳起来，客户端实现生产者confirm有三种编程方式：

1. 普通confirm模式：每发送一条消息后，调用waitForConfirms()方法，等待服务器端confirm。实际上是一种串行confirm了。

2. 批量confirm模式：每发送一批消息后，调用waitForConfirms()方法，等待服务器端confirm。

3. 异步confirm模式：提供一个回调方法，服务端confirm了一条或者多条消息后Client端会回调这个方法。

从编程实现的复杂度上来看： 

第一种 

普通confirm模式最简单，publish一条消息后，等待服务器端confirm,如果服务端返回false或者超时时间内未返回，客户端进行消息重传。 

关键代码如下：
```
channel.basicPublish(ConfirmConfig.exchangeName, ConfirmConfig.routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, ConfirmConfig.msg_10B.getBytes());
if(!channel.waitForConfirms()){
    System.out.println("send message failed.");
}
```

wirkShark抓包可以看到如下： 



第二种 

批量confirm模式稍微复杂一点，客户端程序需要定期（每隔多少秒）或者定量（达到多少条）或者两则结合起来publish消息，然后等待服务器端confirm, 相比普通confirm模式，批量极大提升confirm效率，但是问题在于一旦出现confirm返回false或者超时的情况时，客户端需要将这一批次的消息全部重发，这会带来明显的重复消息数量，并且，当消息经常丢失时，批量confirm性能应该是不升反降的。
 
关键代码：
```
channel.confirmSelect();
for(int i=0;i<batchCount;i++){
    channel.basicPublish(ConfirmConfig.exchangeName, ConfirmConfig.routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, ConfirmConfig.msg_10B.getBytes());
}
if(!channel.waitForConfirms()){
    System.out.println("send message failed.");
}
```

第三种 

异步confirm模式的编程实现最复杂，Channel对象提供的ConfirmListener()回调方法只包含deliveryTag（当前Chanel发出的消息序号），我们需要自己为每一个Channel维护一个unconfirm的消息序号集合，每publish一条数据，集合中元素加1，每回调一次handleAck方法，unconfirm集合删掉相应的一条（multiple=false）或多条（multiple=true）记录。从程序运行效率上看，这个unconfirm集合最好采用有序集合SortedSet存储结构。实际上，SDK中的waitForConfirms()方法也是通过SortedSet维护消息序号的。 

关键代码：
```
SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
 channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        while (true) {
            long nextSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish(ConfirmConfig.exchangeName, ConfirmConfig.routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, ConfirmConfig.msg_10B.getBytes());
            confirmSet.add(nextSeqNo);
        }
```

SDK中waitForConfirms方法实现：
```
/** Set of currently unconfirmed messages (i.e. messages that have
 *  not been ack'd or nack'd by the server yet. */
private final SortedSet<Long> unconfirmedSet =
        Collections.synchronizedSortedSet(new TreeSet<Long>());

public boolean waitForConfirms(long timeout)
        throws InterruptedException, TimeoutException {
    if (nextPublishSeqNo == 0L)
        throw new IllegalStateException("Confirms not selected");
    long startTime = System.currentTimeMillis();
    synchronized (unconfirmedSet) {
        while (true) {
            if (getCloseReason() != null) {
                throw Utility.fixStackTrace(getCloseReason());
            }
            if (unconfirmedSet.isEmpty()) {
                boolean aux = onlyAcksReceived;
                onlyAcksReceived = true;
                return aux;
            }
            if (timeout == 0L) {
                unconfirmedSet.wait();
            } else {
                long elapsed = System.currentTimeMillis() - startTime;
                if (timeout > elapsed) {
                    unconfirmedSet.wait(timeout - elapsed);
                } else {
                    throw new TimeoutException();
                }
            }
        }
    }
}
```

##### 性能测试

Client端机器和RabbitMQ机器配置：CPU:24核，2600MHZ, 64G内存，1TB硬盘。 
Client端发送消息体大小10B，线程数为1即单线程，消息都持久化处理（deliveryMode:2）。 
分别采用事务模式、普通confirm模式，批量confirm模式和异步confirm模式进行producer实验，比对各个模式下的发送性能。 



发送平均速率：

- 事务模式（tx）：1637.484

- 普通confirm模式(common)：1936.032

- 批量confirm模式(batch)：10432.45

- 异步confirm模式(async)：10542.06

可以看到事务模式性能是最差的，普通confirm模式性能比事务模式稍微好点，但是和批量confirm模式还有异步confirm模式相比，还是小巫见大巫。批量confirm模式的问题在于confirm之后返回false之后进行重发这样会使性能降低，异步confirm模式(async)编程模型较为复杂，至于采用哪种方式，那是仁者见仁智者见智了。

##### 消息确认（Consumer端）

为了保证消息从队列可靠地到达消费者，RabbitMQ提供消息确认机制(message acknowledgment)。消费者在声明队列时，可以指定noAck参数，当noAck=false时，RabbitMQ会等待消费者显式发回ack信号后才从内存(和磁盘，如果是持久化消息的话)中移去消息。否则，RabbitMQ会在队列中消息被消费后立即删除它。

采用消息确认机制后，只要令noAck=false，消费者就有足够的时间处理消息(任务)，不用担心处理消息过程中消费者进程挂掉后消息丢失的问题，因为RabbitMQ会一直持有消息直到消费者显式调用basicAck为止。

当noAck=false时，对于RabbitMQ服务器端而言，队列中的消息分成了两部分：一部分是等待投递给消费者的消息；一部分是已经投递给消费者，但是还没有收到消费者ack信号的消息。如果服务器端一直没有收到消费者的ack信号，并且消费此消息的消费者已经断开连接，则服务器端会安排该消息重新进入队列，等待投递给下一个消费者（也可能还是原来的那个消费者）。

RabbitMQ不会为未ack的消息设置超时时间，它判断此消息是否需要重新投递给消费者的唯一依据是消费该消息的消费者连接是否已经断开。这么设计的原因是RabbitMQ允许消费者消费一条消息的时间可以很久很久。

代码示例（关闭自动消息确认，进行手动ack）：
```
  QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(ConfirmConfig.queueName, false, consumer);

        while(true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
     // do something with msg. 
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
```

> broker将在下面的情况中对消息进行confirm：

>> broker发现当前消息无法被路由到指定的queues中（如果设置了mandatory属性，则broker会发送basic.return） 

>> 非持久属性的消息到达了其所应该到达的所有queue中（和镜像queue中） 

>> 持久消息到达了其所应该到达的所有queue中（和镜像中），并被持久化到了磁盘（fsync） 

>> 持久消息从其所在的所有queue中被consume了（如果必要则会被ack）

basicRecover：是路由不成功的消息可以使用recovery重新发送到队列中。 

basicReject：是接收端告诉服务器这个消息我拒绝接收,不处理,可以设置是否放回到队列中还是丢掉，而且只能一次拒绝一个消息,官网中有明确说明不能批量拒绝消息，为解决批量拒绝消息才有了basicNack。 

basicNack：可以一次拒绝N条消息，客户端可以设置basicNack方法的multiple参数为true，服务器会拒绝指定了delivery_tag的所有未确认的消息(tag是一个64位的long值，最大值是9223372036854775807)。

#### 小结

### Sagas 事务模型

#### 1PC（ One-phase commit，一阶段提交）

#### 小结

### 小结

## 总结

对于现在的互联网应用来说，资源横向扩展提供了更多的灵活性，是一种比较容易实现的向外扩展方案，但是同时也明显增加了复杂度，引入一些新的挑战，比如资源之间的数据一致性问题。

横向数据扩展既可以按数据分片扩展，也可以按功能扩展。XA 与 TCC 模型在这一点上的作用类似，都能在横向扩展资源的同时，保证多资源访问的事务属性，只不过前者作用于数据分片时，后者作用于功能扩展时。

XA 模型另外一个意义在于其普适性，抛开性能问题的情况下，几乎可以适用于所有业务模式，这对于一些基础性的技术产品来说是非常有用的，比如分布式数据库、云服务的分布式事务框架等。

TCC 模型除了跨服务的分布式事务这一层作用之外，还具有两阶段划分的功能，通过业务资源锁定，允许第二阶段的异步执行，而异步化思想正是解决热点数据并发性能问题的利器之一。

## 参考资料

[聊聊分布式事务，再说说解决方案](https://www.jianshu.com/p/e70e84dbab72) 

[2PC和3PC中故障情况分析](https://blog.csdn.net/Lnho2015/article/details/78685503) 

[本地消息表（异步确保）](http://baijiahao.baidu.com/s?id=1596808295402419280&wfr=spider&for=pc) 

[RabbitMQ之消息确认机制（事务+Confirm）](https://blog.csdn.net/u013256816/article/details/55515234) 

