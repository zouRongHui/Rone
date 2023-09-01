
### 快捷键
* 后缀补全
```
.var    声明变量
.if     if表达式
.null   判断为空
.notnull    判断非空
.sout   控制台输出
```

* 复制当前行到下一行
`Ctrl + D`

* 生成类的各项基本方法
`Alt + Insert`

* 前进/后退
`Ctrl + Alt + Left/Right（方向键）`

* 查看历史记录
`ctrl + E`

* 查看最近修改代码的位置
`ctrl + shift + E`

* 自动补全代码
`ctrl + shift + Enter（回车键）`

* 代码折叠
```
ctrl + "-"/"+"  折叠/展开，折叠光标所在处，此处按照最侧的状态栏的折叠符号折叠
ctrl + shift + "-"/"+"  折叠/展开，全局折叠/展开
ctrl + .    折叠或展开选中的代码块
ctrl + shift + .    折叠或展开当前光标所在的代码块
```

* 快速匹配大括号
`ctrl + [ 和 ctrl + ]`

* 代码结尾补全
`ctrl + shift + enter`

* 模糊搜索方法
`ctrl+shift+alt+n`

* 查看方法在哪里被调用
`ctrl+alt+h 选中/光标移动到要查看的方法后按快捷键`

* 格式化
`ctrl + alt + l`


************************************************************************************************************************
### 插件

| 插件                                        | 说明                                          | 用法                                                                         |
|-------------------------------------------|---------------------------------------------|----------------------------------------------------------------------------|
| activate-power-mode/activate-power-mode-x | 代码动效                                        | windows菜单下配置                                                               |
| Alibaba Java Coding Guidelines            | 阿里的Java规范检查                                 | 右击or菜单中调用                                                                  |
| Alibaba Cloud AI Coding Assistant (Cosy)  | 阿里的代码智能提示工具                                 | 配置中开启                                                                      |
| CamelCase                                 | 在几种字符串格式之间来回切换                              | 按住Shift + Alt再不停的按U，不停的转换                                                  |
| easy code                                 | 可根据数据表自动生成相关代码                              | DB工具中表右击                                                                   |
| FishBook                                  | 单独的窗口看TXT文档                                 | 设置中配置                                                                      |
| GenerateAllSetter                         | 自动调用所有 Setter 函数（可填充默认值）                    | 光标停留在需要set的对象上，Alt+Enter 然后调用相应的选项即可                                       |
| GenerateSerialVersionUID                  | 自动生成 随机的serialVersionUID                    | 实现了 Serializable 接口后 Alt + Insert 选择 serialVersionUID                      |
| MyBatis Log Plugin                        | 输出 Mybatis 完整的sql语句                         | Tools中打开窗口 or Ctrl+Alt+Shift+O 即可                                          |
| MyBatisX                                  | Java 与 XML 快捷转跳、Mapper 方法自动生成 XML           | 自动生成xml，光标停留在需要创建的方法上，Alt+Enter后条用 Generate statement 会专挑到xml文件中并自动生成相应的代码 |
| Rainbow Brackets                          | 彩虹括号                                        | 配置中设置                                                                      |
| RestfulToolkit                            | 在SpringMVC项目中通过url定位到controller类方法          | Ctrl + \ 或Ctrl + Alt + N                                                   |
| MarkDown Editor                           | markdown语法的编辑工具，比官方的好用些他支持立即渲染就和编辑world一样丝滑 | 启用后打开markdown文件底部有和官方的编辑器的切换栏                                              |


************************************************************************************************************************
### 配置
* idea 指定不对某些单词拼音进行检查
  * 方法 1）鼠标放到被提示的单词上，然后按时按键：Alt+Enter，选中：save xxx to project-level dictionary, 然后回车确认
  * 方法 2）File->settings->Editor->Spelling->Accepted Words，添加不需要被检查的单词即可

* 大小写提示不敏感
    File->setting->Editor->General->Code Completion，Match case 取消勾选

* 在项目中不展示文件的配置
    File->setting->Editor->File Types 在Ignore files and folders中添加不要展示的文件(.idea;*.iml;)

* 自定义getter/setter模板
    Alt+Insert 后选中 Getter and Setter 在最上方可选择本次自动填充的模板，选择后面的 ... 按钮可选择模板，同时可增加自定义的模板。

* 自定义代码补全
  * setting -> Editor -> Live Templates
  * Abbreviation: 触发语句，@rone
  * Template text: 补全的模板，@author Rone $date$
  * Edit variables: 配置函数，Name: date, Default value: date()

* 配置注释符号的位置
    setting -> Editor -> Code style -> Java -> Code Generation -> comment code 区域设置

* 打开多个文件时多个bar展示
  setting -> Editor -> General -> Editor Tabs -> Show tabs in one row

* 设置最多打开多少个tab页
  setting -> Editor -> General -> Editor Tabs 配置 Tab limit 的值

* 对于新建的文件添加默认的内容
  Editor -> File and Code Templates
  ```
  #if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
  #parse("File Header.java")
  
  /**
   * @author rone
   * @date ${DATE}
   */
  public class ${NAME} {
  }
  ```

* 显示方法分隔符
    Editor -> General -> Appearance -> Show method separators

* 代码模板(代码快捷键)
    Editor -> General -> Postfix Completion

* 给代码配置标签
    F11添加标签(或者行数位置右击)，在左下角的 favorites 中 ，找到bookmarks 我们还可以将添加的书签右键重命名

* 设置过长自动换行
    Editor -> Code Style 中Hard wrap at设置每行最大长度(就是窗口中那道暗实线)，Warp on typing勾选及自动换行
    tips：过长的字符串会使用 + 做拼接，编译时会自动优化。


************************************************************************************************************************
### 坑
* 控制台输出中文乱码
  * idea安装目录中\bin\idea64.exe.vmoptions、\bin\idea.exe.vmoptions两个配置文件增加
    `-Dfile.encoding=UTF-8 的jvm配置`
  * 如果是tomcat启动，idea配置tomcat server时在VM options配置中增加
    `-Dfile.encoding=UTF-8`

* idea中使用tomcat配置web项目，当项目增加依赖包时需要去 Artifacts 中手动将包配置到tomcat的exploded包中

* IDEA编译项目出现 非法字符 '\65279' 错误
  * solution1：Settings -> ... -> Java Complier 将 Use complier 改为 Eclipse
  * solution2：用Notepad打开报错的文件，然后在【编码】中选择【以UTF-8无BOM格式编码】即可



