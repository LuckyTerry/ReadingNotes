# Covid服务部署

covid的nginx配置如下

```conf
user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


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
        location /covid {
            proxy_buffer_size  128k;
            proxy_buffers   32 32k;
            proxy_busy_buffers_size 128k;
	        rewrite ^/covid/(.*)$ /$1 break;
            proxy_pass http://127.0.0.1:9001;
        }
    }
    server {
        listen 443 ssl;
        server_name covid.terry.wiki;
        ssl on;
        ssl_certificate /etc/nginx/cert/3498786_covid.terry.wiki.pem;
        ssl_certificate_key /etc/nginx/cert/3498786_covid.terry.wiki.key;
        ssl_session_timeout 5m;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;
        location / {
            proxy_buffer_size  128k;
            proxy_buffers   32 32k;
            proxy_busy_buffers_size 128k;
            proxy_pass http://127.0.0.1:9001;
        }
    }
    server {
        listen 80;
        server_name terry.wiki www.terry.wiki covid.terry.wiki;
        rewrite ^(.*)$ https://$host$1 permanent;
    }
}
```