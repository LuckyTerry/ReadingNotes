# Android Studio 中 Gradle 的基本配置

标签（空格分隔）： 未分类

---

Issue 1 ： 下载依赖jar包非常缓慢
是因为 gradle 没有走代理。

如果是 socks5 代理 ，如下这样设置其实并没有什么卵用
systemProp.socks.proxyHost=127.0.0.1
systemProp.socks.proxyPort=7077
systemProp.https.proxyHost=127.0.0.1
systemProp.https.proxyPort=7077
systemProp.https.proxyHost=socks5://127.0.0.1
systemProp.https.proxyPort=7077

正确设置方法应该是这样：
org.gradle.jvmargs=-DsocksProxyHost=127.0.0.1 -DsocksProxyPort=7077

修改 $HOME/.gradle/gradle.properties 文件,加入上面那句，这样就可以全局开启 gradle 代理






