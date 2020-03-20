# Git实战指南

TODO

[Pro Git（中文版）](https://gitee.com/progit/)

[Git Community Book 中文版](http://gitbook.liuhui998.com/index.html)

文件名有空格时用“单引号/双引号”括起来

## 得到一个仓库

git init //初始化本地git环境
git clone XXX//克隆一份代码到本地仓库

## 简单使用

git pull origin master //把远程库的代码更新到工作台
git status //查看当前分支有哪些修改
git add <文件> //把本地的修改加到stage中
git checkout -- <文件> //丢弃工作区的改动
git commit -m 'msg'  //把stage中的修改提交到本地库
git reset HEAD <文件> // 撤销暂存
git push origin master //把远程库的代码更新到工作台

git branch -r //查看远程分支
git branch -a //查看全部分支
git checkout master //切换到master分支
git checkout develop //切换到develop分支
git checkout -b develop //新建并切换到develop分支

git checkout -d test //删除test分支


fetch：相当于是从远程获取最新版本到本地，不会自动merge
git pull：相当于是从远程获取最新版本并merge到本地
git fetch orgin master //将远程仓库的master分支下载到本地当前branch中
git pull origin master    //相当于git fetch 和 git merge

## 进阶使用

git fetch //把远程库的代码更新到本地库
git pull --rebase origin master //强制把远程库的代码跟新到当前分支上面


## 分支合并

## 分支关联

git remote -v
git remote set-url origin git@github.com:LuckyTerry/ReadingNotes.git

## 文件忽略

从Repo中移除文件但不删除本地文件
git rm --cached -r holder-saas-aggregation-merchant.iml
git commit -m "remove idea and iml files"
git push origin develop


## 工作流管理

### 正常团队协作

### 紧急Bug处理

暂存更改
git add .
git stash

修复紧急bug
git checkout bugfix_branch
git pull --rebase origin master
git add .
git commit -m "msg"
git push

返回工作现场，继续新功能开发
git checkout test
git stash pop

----------分割线-----------



Git常用命令

git checkout -d test //删除test分支
git merge master //假设当前在test分支上面，把master分支上的修改同步到test分支上
git merge tool //调用merge工具
git stash //把未完成的修改缓存到栈容器中
git stash list //查看所有的缓存
git stash pop //恢复本地分支到缓存状态
git blame someFile //查看某个文件的每一行的修改记录（）谁在什么时候修改的）

git log //查看当前分支上面的日志信息
git diff //查看当前没有add的内容
git diff --cached //查看已经add但是没有commit的内容
git diff HEAD //上面两个内容的合并
git reset HEAD <文件> // 撤销暂存
git reset HEAD // 撤销所有暂存（回到有改动但未add）
git reset --hard HEAD //撤销本地修改（直接回到未修改）
echo $HOME //查看git config的HOME路径
export $HOME=/c/gitconfig //配置git config的HOME路径
 

团队协作git操作流程：

克隆一个全新的项目，完成新功能并且提交：
git clone XXX //克隆代码库
git checkout -b test //新建分支
modify some files //完成修改
git add . //把修改加入stage中
git commit -m '' //提交修改到test分支
review代码
git checkout master //切换到master分支
git pull //更新代码
git checkout test //切换到test分支
git meger master //把master分支的代码merge到test分支
git push origin 分支名//把test分支的代码push到远程库
