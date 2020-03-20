# IDEA版权设置

1. 在idea中找到settings->Editor->copyright->copyright

    profiles,然后点击+,输入名字，在copyright text中输入模板.然后点击右下角apply.

    ```
    Copyright (c) 2018-2028 成都掌控者科技有限公司 All Rights Reserved.
    ProjectName:$project.name
    FileName:$file.fileName
    Date:$today.format("yyyy-M-d")
    Author:terry
    ```
   
2. 在settings->Editor->copyright中default project

    copyright选择1中的copyright名字,点击右上方+.添加scope，scope列选择project file,然后点击右下角apply.

3. 测试

    此时在新建项目时就会自动加入copyright.
    
    可以右击目录或者project file选择update copyright手动加入coperight
 
4. 参考模板

```
版权所有(C)，XXX公司，$today.format("yyyy")，所有权利保留。

项目名： $module.name
文件名： $file.fileName
模块说明：
修改历史:
$today.format("yyyy-M-d") - dongtangqiang - 创建。
```
 
```
Copyright(c)2002-2019, xxxxxxxxx有限公司
项目名称:$project.name
文件名称:$file.fileName
Date:$today
Author:xxxxxxxx
```

```
Copyright (c) 2008-2018 百度知道 All Rights Reserved.
FileName: $file.fileName
@author: jack
@date: $today
@version: 1.0
```