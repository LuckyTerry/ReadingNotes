# Netty基本概念简介

## 同步 vs 异步 

同步和异步关注的是消息通信机制

同步，就是在发出一个调用时，在没有得到结果之前，该调用就不返回。但是一旦调用返回，就得到返回值了；

异步，调用在发出之后，这个调用就直接返回了，所以没有返回结果。换句话说，当一个异步过程调用发出后，调用者不会立刻得到结果。而是在调用发出后，被调用者通过状态、通知来通知调用者，或通过回调函数处理这个调用。

## 阻塞 vs 非阻塞

阻塞和非阻塞关注的是程序在等待调用结果（消息，返回值）时的状态

阻塞调用是指调用结果返回之前，当前线程会被挂起。调用线程只有在得到结果之后才会返回。

非阻塞调用指在不能立刻得到结果之前，该调用不会阻塞当前线程。

## 同步/异步 vs 阻塞/非阻塞

老张爱喝茶，废话不说，煮开水。

出场人物：老张，水壶两把（普通水壶，简称水壶；会响的水壶，简称响水壶）。

1 老张把水壶放到火上，立等水开。（同步阻塞）老张觉得自己有点傻

2 老张把水壶放到火上，去客厅看电视，时不时去厨房看看水开没有。（同步非阻塞）老张还是觉得自己有点傻，于是变高端了，买了把会响笛的那种水壶。水开之后，能大声发出嘀~~~~的噪音。

3 老张把响水壶放到火上，立等水开。（异步阻塞）老张觉得这样傻等意义不大

4 老张把响水壶放到火上，去客厅看电视，水壶响之前不再去看它了，响了再去拿壶。（异步非阻塞）老张觉得自己聪明了。

所谓同步异步，只是对于水壶而言。普通水壶，同步；响水壶，异步。虽然都能干活，但响水壶可以在自己完工之后，提示老张水开了。这是普通水壶所不能及的。同步只能让调用者去轮询自己（情况2中），造成老张效率的低下。

所谓阻塞非阻塞，仅仅对于老张而言。立等的老张，阻塞；看电视的老张，非阻塞。情况1和情况3中老张就是阻塞的，媳妇喊他都不知道。虽然3中响水壶是异步的，可对于立等的老张没有太大的意义。所以一般异步是配合非阻塞使用的，这样才能发挥异步的效用。

来源：知乎
链接：https://www.zhihu.com/question/19732473/answer/23434554

## IO模型



## Netty

### 1、基本构建模块

#### 1.1、Bootstrap

Bootstrap(引导)类提供了一个用于应用程序网络层配置的容器。

#### 1.2、Channel

Channel 是 NIO 基本的结构。它代表了一个用于连接到实体如硬件设备、文件、网络套接字或程序组件,能够执行一个或多个不同的	I/O	操作(例如读或写)的开放连接。其定义了与 socket 丰富交互的操作集。

#### 1.3、ChannelHandler

处理者角色，由事件触发。

#### 1.4、ChannelPipeline

提供了一个容器给 ChannelHandler 链并提供了一个 API 用于管理沿着链入站和出站事件的流动。

#### 1.5、Eventloop

EventLoop 用于处理 Channel 的 I/O 操作。一个单一的 EventLoop通常会处理多个 Channel 事件。

#### 1.6、EventGroup

一个 EventLoopGroup 可以含有多于一个的 EventLoop 和提供了一种迭代用于检索清单中的下一个。

#### 1.7、Callback

callback(回调)是一个简单的方法,提供给另一种方法作为引用,这样后者就可以在某个合适的时间调用前者。Netty 内部使用回调处理事件时。

#### 1.8、ChannelFuture

Netty 所有的 I/O 操作都是异步。因为一个操作可能无法立即返回,我们需要有一种方法在以后确定它的结果。

JDK 附带接口 java.util.concurrent.Future ，但所提供的实现只允许您手动检查操作是否完成或阻塞了。

所以 Netty 提供自己了的实现 ChannelFuture 以提供更多支持，用于在执行异步操作时使用。并且 ChannelFuture 提供了 addListener 方法注册一个或多个 ChannelFutureListener实例，用于通知执行结果。

### 1、Bootstrap

Bootstrapping（引导） 是 Netty 中配置程序的过程，当你需要连接客户端或服务器绑定指定端口时需要使用 Bootstrapping。

Bootstrapping 有两种类型，一种是用于客户端的Bootstrap，一种是用于服务端的ServerBootstrap。

Bootstrap用来连接远程主机，有1个EventLoopGroup

ServerBootstrap用来绑定本地端口，有2个EventLoopGroup

一个 ServerBootstrap 可以认为有2个 Channel 集合，第一个集合包含一个单例 ServerChannel，代表持有一个绑定了本地端口的 socket；第二集合包含所有创建的 Channel，处理服务器所接收到的客户端进来的连接。

![Server with two EventLoopGroups.png ](https://github.com/LuckyTerry/ReadingNotes/blob/master/Netty/Server%20with%20two%20EventLoopGroups.png)

### 2、Channel

### 2.1、EventLoop 注册

当创建一个 Channel，Netty 通过 一个单独的 EventLoop 实例来注册该 Channel(并同样是一个单独的 Thread)的通道的使用寿命。

![Channel Registry](https://github.com/LuckyTerry/ReadingNotes/blob/master/Netty/Channel%20Registry.png) 

### 2.2、Channel 生命周期

Channel 存在四种状态

channelUnregistered：channel 已创建但未注册到一个 EventLoop.
channelRegistered：channel 注册到一个	EventLoop.
channelActive：channel 变为活跃状态
channelInactive：channel	处于非活跃状态

生命周期流程如下

channelRegistered -> channelActive -> channelInactive -> channelUnregistered

当状态出现变化，就会触发对应的事件，这样就能与 ChannelPipeline 中的 ChannelHandler 进行及时的交互

### 3、ChannelHandler

#### 3.1、ChannelHandler 的类继承关系

![ChannelHandler class hierarchy](https://github.com/LuckyTerry/ReadingNotes/blob/master/Netty/ChannelHandler%20class%20hierarchy.png) 

#### 3.2、ChannelHandler 生命周期

handlerAdded：当 ChannelHandler 添加到 ChannelPipeline 调用

handlerRemoved：当 ChannelHandler 从 ChannelPipeline 移除时调用

exceptionCaught：当 ChannelPipeline 执行抛出异常时调用

#### 3.3、ChannelHandler 的共享

可以通过注解 @ChannelHandler.Sharable 开启可共享，但是使用者需确保 ChannelHandler 是线程安全的。

### 4、ChannelPipeline

#### 4.1、与 ChannelHandler、Channel的关联

ChannelPipeline 是一系列的 ChannelHandler 实例，用于拦截流经一个 Channel 的入站和出站事件。每一次创建了新的 Channel，都会新建一个新的 ChannelPipeline 并绑定到Channel 上，并且这个关联是永久性的。

![ChannelPipeline with inbound and outbound ChannelHandlers](https://github.com/LuckyTerry/ReadingNotes/blob/master/Netty/ChannelPipeline%20with%20inbound%20and%20outbound%20ChannelHandlers.png) 

#### 4.2、ChannelHandlerContext

管理通过同一个 ChannelPipeline 关联的 ChannelHandler 之间的交互。

![event stream](https://github.com/LuckyTerry/ReadingNotes/blob/master/Netty/event%20stream.png) 

### 5、Buffer

Java NIO 提供 ByteBuffer 作为字节的容器，但它不够好。所以Netty提供了一个强大的缓冲实现类 ByteBuf，效率与JDK的ByteBuffer相当，并且提供了如下几个优势:

可以自定义缓冲类型；

通过一个内置的复合缓冲类型实现零拷贝；

扩展性好，比如	StringBuilder；

不需要调用 flip() 来切换读/写模式；

读取和写入索引分开；

方法链；

引用计数；

Pooling(池)。

### 6、Codec（编解码器）

Netty提供了一些 built-in 的 Decoder(解码器)、Encoder(编码器)和 Codec(编解码器)。

### 7、EventGroup



### 8、EventLoop 和线程模型



