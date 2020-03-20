# Aliyun Cloud Toolkit 实践

## 安装配置

1. 下载安装

plugin在线安装，或者离线安装 [Alibaba Cloud Toolkit](https://plugins.jetbrains.com/plugin/11386-alibaba-cloud-toolkit) 

2. 配置

根据初次向导配置即可

Setting - Alibaba Cloud Toolkit 
- Accounts - 填入 Access Key ID 和 Secret
- SSH Profile - 填入 ssh 账号密码

## 介绍

在 Alibaba Cloud View - Alibaba Cloud ECS 中 选中一个实例，可以进行上传、ssh连接等动作.

项目、模块上 右键，点击 Alibaba Cloud，支持如下功能

- Deploy to ECS...
- 其他

### Maven Build

![image](https://note.youdao.com/yws/res/4992/A97DF64B67C146288EBD6C8A4D302D34)

项目是多模块，结构如下：

holder-saas-covid-resource
- holder-saas-covid-api
- holder-saas-covid-form 这个才是要deploy的jar包
- holder-saas-covid-integration

Target Directory 指定要上传到服务器哪个目录下。

Command 指定上传后要执行的命令。

Before launch 修改为子模块 form 的 clean install。

### Upload File

![image](https://note.youdao.com/yws/res/4995/DD81920CA5014615B7EC379B3FE7FFD6)

Target Directory 指定要上传到服务器哪个目录下。

Command 指定上传后要执行的命令。

Before launch 由于是上传文件，所以该步一般不需要做什么
 
### Spring Boot 推荐 Command


```bash
# restart-covid.sh 如下

source ~/.bash_profile
jps -ml | grep holder-saas-covid-form | awk '{print$1}' | xargs kill -9
nohup java -jar /data/holder-saas-covid-form-0.0.1-SNAPSHOT.jar --spring.profiles.active=terry > nohup.log 2>&1 &
```