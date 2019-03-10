# Ubuntu 16.04 Sophix热修复开发环境搭建

## 创建产品

[产品列表](https://mhub.console.aliyun.com/#/productList) 

## 创建App

[App管理](https://hotfix.console.aliyun.com/#/list) 

## SDK下载

[传送门](https://mhub.console.aliyun.com/#/download) 

## 移动热修复主页

[传送门](https://www.aliyun.com/product/hotfix?spm=5176.2020520107.0.0.65b86bdeZAuMzK) 

## 移动热修复文档与视频

[传送门](https://help.aliyun.com/product/51340.html?spm=5176.doc53240.6.30.RqtnTL) 

## 快速接入

[传送门](https://help.aliyun.com/document_detail/53240.html?spm=5176.product51340.6.546.cMVhec) 

官方文档说：initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类

意思是紧跟super.attachBaseContext(this)后面，而不是放在最前面，否则拿不到context初始化会失败。


## 下载 Sophix SDK

[传送门](https://help.aliyun.com/document_detail/53239.html?spm=5176.doc53248.6.550.oS6cH1) 

## 下载 SophixPatchTool

[传送门](https://help.aliyun.com/document_detail/53247.html?spm=5176.doc53240.6.548.ZiGNWu) 

## 下载DebugTool

[传送门](https://help.aliyun.com/document_detail/53248.html?spm=5176.doc53239.6.549.6iWIkK) 

## 常见问题

Q: SophixPatchTool运行出错
A: 动态链接库查找冲突，使用了qt5-ss所依赖的版本（在安装了Qt5版本Shadowsocks的前提下）

动态链接库查找原理：[详见传送门](http://www.cnblogs.com/vczh/p/5809069.html) 
linux的excutable在执行的时候缺省是先搜索/lib和/usr/lib这两个目录，然后按照ld.so.conf里面的配置搜索绝对路径，linux缺省是不会在当前目录搜索动态库的。

linux也可以支持“加载当前目录的动态库”。只要设置合适的环境变量LD_LIBRARY_PATH就可以了。设置方法有以下三种： 

1、临时修改，log out之后就失效 
在terminal中执行：export LD_LIBRARY_PATH=./ 

2、让当前帐号以后都优先加载当前目录的动态库 
修改~/.profile在文件末尾加上两行： 
LD_LIBRARY_PATH=./
export LD_LIBRARY_PATH

3、让所有帐号从此都优先加载当前目录的动态库 
修改/etc/profile在文件末尾加上两行： 
LD_LIBRARY_PATH=./
export LD_LIBRARY_PATH  

PS：修改ld.so.conf不能达到我们的目的，因为ld.so.conf只支持绝对路径。
