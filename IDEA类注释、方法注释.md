# IDEA类注释、方法注释

## 类注释配置

1.设置 File Header (推荐这个)

File -> Settings -> File and Code Templates -> Includes -> File Header -> 编辑

```text
/**
 *@author ${USER}
 *@version 1.0
 *@className ${NAME}
 *@date ${DATE} ${TIME}
 *@description //TODO
 *@program ${PROJECT_NAME}
 */
 ```

2.设置 Class / Enum / Interface etc..

File -> Settings -> File and Code Templates -> Files -> Class -> 编辑

去除 #parse("File Header.java")，添加如下

```text
/**
 *@author ${USER}
 *@version 1.0
 *@className ${NAME}
 *@date ${DATE} ${TIME}
 *@description ${Description}
 *@program ${PROJECT_NAME}
 */
 ```

 这样的好处是description能直接弹出对话框让你填写

 Enum / Interface 类似处理

## 方法注释配置

File -> Settings -> Live Templates

点击 "+" 添加 Template Group (HolderGroup)

点击 "+" 添加 Live Template

1.Abbreviation: *

2.Description: 方法注释模板

3.Template Text: 不要更改空格等字符

```text
*
 $param$* @return $return$
 * @author $USER$
 * @description //TODO
 * @date $date$ $time$
 */
```

4.Edit variables

param -> 不要更改以下代码空格等字符

```groovy
groovyScript(
    "def result='';
    def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList();
    for(i = 0; i < params.size(); i++) {
        if (params[i] == '') {
            return result
        };
        result+='* @param ' + params[i] + '\\n '
    };
    return result",
    methodParameters()
)
```

return -> methodReturnType

USER -> user()

time -> time()

date -> date()

## 有用链接

[IDEA类和方法注释模板设置（非常详细）](https://blog.csdn.net/xiaoliulang0324/article/details/79030752)

## export

File -> Export Settings -> Select None -> Select "File Templates(schemes)","Live Templates(schemes)" -> OK

## import

File -> Import Settings -> 选择"HolderSettings.jar" -> OK