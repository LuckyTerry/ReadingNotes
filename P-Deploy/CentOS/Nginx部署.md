# 安装

[centos8 安装 nginx](https://www.cnblogs.com/shiyuelp/p/11945882.html)

http://nginx.org/ 官网

通过 wget http://nginx.org/download/nginx-1.17.8.tar.gz 下载文件

安装必要插件
yum -y install gcc pcre pcre-devel zlib zlib-devel openssl openssl-devel

解压下载好的文件
tar -zxvf nginx-1.17.8.tar.gz
进入到 nginx-1.17.8文件夹下面

开始安装

指定安装路径
./configure --prefix=/software/nginx
这句话的意思是指定安装路径
--prefix=/software/nginx

编译
make

安装
make install

进入到安装nginx目录下面的sbin
启动命令
./nginx

阿里云安全组添加 80和443 入方向的规则

打开浏览器访问你的IP地址，显示此页面说明nginx启动成功1

# 阿里云centos nginx安装

[阿里云centos nginx安装](https://blog.csdn.net/qq_39785489/article/details/87102051)

1.安装nginx

添加源

    sudo rpm -Uvh http://nginx.org/packages/centos/8/x86_64/RPMS/nginx-1.16.1-1.el8.ngx.x86_64.rpm

安装Nginx

    sudo yum install -y nginx  

启动和开机自动运行Nginx

    sudo systemctl start nginx.service
    sudo systemctl enable nginx.service 

顺利的话nginx就安装好了

阿里云安全组添加80的规则
    
    略

证书、配置位置

    /etc/nginx/cert/aaa.pem
    /etc/nginx/cert/aaa.key
    /etc/nginx/nginx.conf

自测配置如下

```
http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    #include /etc/nginx/conf.d/*.conf;
    server {
    	listen 443 ssl;
	    server_name terry.wiki www.terry.wiki;
	    ssl on;
        ssl_certificate /etc/nginx/cert/3462853_terry.wiki.pem;
        ssl_certificate_key /etc/nginx/cert/3462853_terry.wiki.key;
        ssl_session_timeout 5m;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;
        location / {
            proxy_buffer_size  128k;
            proxy_buffers   32 32k;
            proxy_busy_buffers_size 128k;
            proxy_pass http://127.0.0.1:8080;
        }
    }
    server {
        listen 80;
        server_name terry.wiki www.terry.wiki;
        rewrite ^(.*)$ https://$host$1 permanent;
    }
}

```

重启即可

    sudo systemctl restart nginx

备注：

由于阿里云有安全组，所以不清楚时不要操作防火墙。
    
    查看防火墙状态，如果是dead状态，防火墙未开启，running则开启
    systemctl status firewalld
    
    开启防火墙
    systemctl start firewalld
    
    开启80端口
    firewall-cmd  --add-port=80/tcp --permanent
    
    重启防火墙
    sudo firewall-cmd --reload
    
    查看谁否有80
    firewall-cmd --list-all