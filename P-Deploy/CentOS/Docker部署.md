# CentOS 安装 Docker

根据官网 [Get Docker Engine - Community for CentOS](https://docs.docker.com/install/linux/docker-ce/centos/) 所述，支持 yum 和 script 安装，推荐 script。

## 脚本安装（推荐）

执行脚本

    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    
    # to use Docker as a non-root user，ECS并不需要
    # sudo usermod -aG docker terry
    
启动

    systemctl start/stop/restart/status docker

## 其他 Ref

[centos8安装docker](https://www.jianshu.com/p/1a9e7e752897)