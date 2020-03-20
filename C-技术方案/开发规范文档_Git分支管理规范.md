# Git分支管理规范

该文档阐述、规定了现阶段的分支管理策略。基于资源、测试人员的限制，未能以现有成熟的策略进行分支管理。即使如此，也请务必理解该分支管理规定，有能改进的地方可提出讨论。

## 发布环境

- 开发环境：开发人员以develop分支构建发布

- 测试环境：Jenkins从master分支拉取最新代码，构建发布

- 正式环境：根据Jenkins构建发布的Jar运行服务以发布

- 正式体验环境：根据Jenkins构建发布的Jar运行服务以发布，与正式环境隔离，内测使用（暂未提供）

## 两个长期分支

- master 正常流程的发布分支

- develop 开发分支

## 一个临时分支

- hotfix-#{tag.version}-#{bug.code or bug.description} 热修复(Bug修复)分支

    从master拉取与线上环境一致的Tag分支做快速修复工作(如果当前已存在hotfix分支，需与分支创建人建立联系，并在该分支进行Bug修复。建议如此处理，避免以下第2点的第3个步骤)。修复完成后：

    1. 如果此时master分支是干净的：

        将hotfix-*分支代码合并到master分支;

        在master分支进行UAT测试;

        测试通过后发布到生产环境;

        并以master为source添加新的Tag;

        再将hotfix-*分支代码合并到develop分支;

        最后删除hotfix-*分支。

    2. 如果此时master分支是不干净的：

        直接在hotfix-*分支进行UAT测试；

        测试通过后(测试通过后不得再次修改代码，否则造成新tag含有未被测试的代码)发布到生产环境；

        检查最新tag是否与hotfix分支命名中的tag一致：如果一致，以hotfix-*为source添加新的tag；否则，merge最新tag分支到当前hotfix-*分支，并以合并后的hotfix-*为source添加新的tag。

        再将(合并后的)hotfix-*代码合并到master分支，develop分支;

        最后删除(合并后的)hotfix-*分支。
        
    上述步骤完成后develop会比master领先一个提交(develop的merge操作对应的commit)，落后一个提交(master的merge操作对应的commit)，但两个分支代码没有差异。

## 注意

不能确认自己操作正确性时，需告知组长，共同进行分支处理。

## 其他分支管理策略

[版本管理之gitlab实践教程：进阶篇(1)](https://blog.csdn.net/liumiaocn/article/details/79256312)

[Git分支管理及命名规范](https://blog.csdn.net/fifteen718/article/details/80347550)

[为什么使用 Git-flow 工作流](https://blog.csdn.net/aaaaaaliang/article/details/79451598)

[git分支管理和工作流规范：具体规范](https://juejin.im/post/5aa7e8a6f265da239f070d82)

[成熟的 Git 分支模型](https://juejin.im/post/5c1a4d1df265da6170071422)

[Sourcetree](https://www.sourcetreeapp.com/)

    