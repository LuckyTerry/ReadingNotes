# k8s

[eip-work/kuboard-press，讲了很多关于k8s的东西](https://github.com/eip-work/kuboard-press)

[推荐：eip-work/kuboard-press 的 install-k8s 教程。傻瓜式安装，本文的步骤也是源于此，略作修改](https://github.com/eip-work/kuboard-press/blob/master/install/install-k8s.md)

## 安装docker及kubelet

将脚本最后的 1.17.3 替换成需要的版本号

    export REGISTRY_MIRROR=https://registry.cn-hangzhou.aliyuncs.com
    curl -sSL https://kuboard.cn/install-script/v1.17.x/install_kubelet.sh | sh -s 1.17.3

## 初始化 master 节点

将脚本最后的 1.17.3 替换成需要的版本号

    export MASTER_IP=172.31.227.55
    export APISERVER_NAME=k8s.terry.wiki
    export POD_SUBNET=10.100.0.1/16
    echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
    curl -sSL https://kuboard.cn/install-script/v1.17.x/init_master.sh | sh -s 1.17.3
    
检查 master 初始化结果

    # 只在 master 节点执行
    
    # 执行如下命令，等待 3-10 分钟，直到所有的容器组处于 Running 状态
    watch kubectl get pod -n kube-system -o wide
    
    # 查看 master 节点初始化结果
    kubectl get nodes -o wide


发生错误，需要重新初始化 master 节点前，请先执行 

    kubeadm reset -f # 然后重复第一步
    
## 初始化 worker节点

    export MASTER_IP=172.31.227.55
    export APISERVER_NAME=k8s.terry.wiki
    echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
    # 替换为 master 节点上 kubeadm token create 命令的输出
    kubeadm join apiserver.demo:6443 --token mpfjma.4vjjg8flqihor4vt     --discovery-token-ca-cert-hash sha256:6f7a8e40a810323672de5eee6f4d19aa2dbdb38411845a1bf5dd63485c43d303
    
    
[Kuboard for K8S](https://www.kuboard.cn/)

[安装看我：kuboard 出品的安装教程](https://www.kuboard.cn/install/install-k8s.html)

[学习看我：吹爆！！！Kubernetes教程](https://www.kuboard.cn/learning/)