# 服务部署

## 拷贝Jar到服务器
```
sudo scp operation-master-0.1.0.jar root@118.31.38.15:/root/app/opmaster
```

## 运行Jar
```
nohup java -jar operation-master-0.1.0.jar  &
```