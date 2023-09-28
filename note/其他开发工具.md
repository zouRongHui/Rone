
### 其他工具
* api接口管理
    ApiPost
    ApiFox
* shell连接
    FinalShell
* Java虚拟机内存分析
    JProFiler
* 数据库
    PDMan 数据库设计
    Another Redis Desktop Manager
* 压测工具
    jmeter Apache开源的压测工具
* 其他工具
  arbon	一个生成美观的代码图片的工具


************************************************************************************************************************
### git
[中文文档](https://git-scm.com/book/zh/v2)
#### 常用命令
##### 设置全局用户名和邮箱
```
git config --global user.name 用户名
git config --global user.email 邮箱
```

##### 查看全局设置
`git config -l`

##### 初始化git库
`git init`

##### 查看远程分支
`git branch -a`

##### 查看本地分支
`git branch`

##### 切换分支
`git checkout 分支名`

##### 目标分支合并到当前分支
`git merge --no-ff 分支名`

##### 版本回退
`git reset --hard 版本号(可省略至前几位)`

##### 查看工作区状态
`git status`

##### 查看版本信息
`git log`
`git log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --date=relative`

##### 工作区新增至暂存区
`git add .   //增加所有的新内容`
`git add 文件名 //增加单个`

##### 暂存区提交到分支
`git commit -m "提交的一些说明"`

##### 上传至远程
`git remote add origin 地址(支持Http、SSH)    //添加一个新的远程库，origin为默认的名字`
`git push [-u] origin master   //上传文件至远程库，首次需要 -u 参数，origin为远程库的名字，master上传的分支`

##### 远程库克隆至本地
`git clone 地址(支持Http、SSH)`

##### 同步到本地
`git pull 远程库名 分支名`

##### 撤销merging状态
`git reset --hard HEAD`

##### 标签
```
查看 $ git tag
新增标签
    $ git tag 标签
    $ git tag 标签 提交的id
    $ git tag -a 标签 -m "标签说明" 提交的id
移除标签 $ git tag -d 标签
推送标签到远程
    一个 $ git push origin 标签
    所有 $ git push origin --tags
```

##### 对提交进行计数
`git rev-list --count [branchName]`


************************************************************************************************************************
### swagger
一个API文档生成工具，可结合spring自动生成API文档(查看路径一般为 http://localhost:8111/swagger-ui.html)
[参考文档](https://www.jianshu.com/p/c79f6a14f6c9)
#### 常用的注解
* @Api: 用在类上，说明该类的作用。可以标记一个Controller类做为swagger 文档资源
    * value, url的路径值
    * tags, 如果设置这个值、value的值会被覆盖
    * description, 对api资源的描述
    * produces, 指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回
    * consumes, 指定处理请求的提交内容类型,eg("application/json, application/xml")
* @ApiOperation：用在方法上，说明方法的作用，每一个url资源的定义
    * value, 接口说明
    * notes, 接口发布说明
    * httpMethod, 接口请求方式
    * response, 接口返回参数类型
* @ApiParam: 用在请求属性上，说明属性的一些定义
    * name, 参数名
    * value, 说明
    * required, 是否必传
    * allowableValues, 限定传值的范围
* @ApiModel: 用在返回的类上
* @ApiModelProperty: 用在返回类的属性上，对属性进行一些说明
    * value, 字段的一些说明
    * required, 是否必须
    * hidden, 是否隐藏，默认false


#### 坑
* swagger-ui.html 页面上try it now出错提示not fetch，而且给出的Request URL，域名不对。
  * reason：大概率是服务器的配置跨域导致的。
  * solution：
    * nginx做代理内网域名时，增加反向代理-做跨域支持。（未测试）
      ```
      location /apis {
      rewrite? ^.+apis/?(.*)$ /$1 break;
      include? uwsgi_params;
      ? ?proxy_pass?? http://server:port;
      }
      ```
    * 配置文件中增加swagger访问域名。实测有效
      `springfox.documentation.swagger.v2.host = xxx.xxx.com(.cn)`

* 当使用静态内部类当做返回的model时，存在多个静态内部类时，当这些静态内部类同名时，swagger的文档会出错。
  * 原因：返回的model不支持重名
