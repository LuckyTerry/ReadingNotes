# Gitlab部署

## Reference

[官网Install](https://about.gitlab.com/install/#centos-7)

[阿里云CentOs7安装Gitlab 亲测可用](https://blog.csdn.net/weixin_33769125/article/details/91679600)

[GitLab 502问题的解决](https://blog.csdn.net/alittleyatou/article/details/81557678)

[阿里云，腾讯云搭建 gitlab 服务器卡顿问题解决方法](https://blog.csdn.net/qq_30745307/article/details/82829064)

## 安装Gitlab

打开ssh访问

    sudo yum install -y curl policycoreutils-python openssh-server
    sudo systemctl enable sshd
    sudo systemctl start sshd
 
安装Postfix邮件通知（可不安装）
    
    sudo yum install postfix
    sudo systemctl enable postfix
    sudo systemctl start postfix
    
    # 修改 /etc/postfix/main.cf的设置  
    inet_protocols = ipv4  
    inet_interfaces = all

    # 启动
    sudo systemctl start postfix

安装gitlab

    curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ee/script.rpm.sh | sudo bash
    # 如果出现packages.gitlab.com无法识别，就用浏览器打开地址，拷贝脚本文字，后去ecs上touch一个sh文件，手动执行
    sudo vim ~/script.rpm.sh
    sudo bash ~/script.rpm.sh
    
    sudo EXTERNAL_URL="http://47.104.227.121:8088" yum install -y gitlab-ee
    
    # 或者使用域名方式，而不是ip:port方式
    vim /etc/gitlab/gitlab.rb   #打开配置文件
    external_url 'http://gitlab.terry.wiki'
    nginx['listen_port'] = 800     #找到取消注释，修改端口

修改unicorn端口，因为8080已被占用

    sudo vim /etc/gitlab/gitlab.rb
    unicorn['port'] = 8089
    
配置QQ邮箱

    gitlab_rails['smtp_enable'] = true
    gitlab_rails['smtp_address'] = "smtp.qq.com"
    gitlab_rails['smtp_port'] = 465
    gitlab_rails['smtp_user_name'] = "1018498538@qq.com"
    gitlab_rails['smtp_password'] = "wiazivzuwimcbdfe"
    gitlab_rails['smtp_domain'] = "qq.com"
    gitlab_rails['smtp_authentication'] = "login"
    gitlab_rails['smtp_enable_starttls_auto'] = true
    gitlab_rails['smtp_tls'] = true
    
    user['git_user_email'] = "1018498538@qq.com"
    gitlab_rails['gitlab_email_from'] = '1018498538@qq.com'
    
重启
    
    gitlab-ctl reconfigure

测试右键服务器

    gitlab-rails console
    
    Notify.test_email('1018498538@qq.com','title','content').deliver_now
    
配置ngnix反向代理

    略

登录gitlab.terry.wiki

    修改默认密码

登录

    默认用户root,密码即为刚才设置的密码。
    
上传project

    cd existing_repo
    git remote rename origin old-origin
    git remote add origin http://47.104.227.121:8088/saas-platform/holder-saas-aggregation-app.git
    git push -u origin --all
    git push -u origin --tags
    
解决卡顿问题：开启swap

    查看文章开头的Ref