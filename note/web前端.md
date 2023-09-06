
### 基础
#### html
常见的标签元素和元素属性见下面的代码示例
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!-- Keywords 为搜索引擎提供的关键字列表。当数个META元素提供文档语言从属信息时，搜索引擎会使用lang特性来过滤并通过用户的语言优先参照来显示搜索结果 -->
    <meta name="Kyewords" Lang="EN" Content="rone,test">
    <meta name="Kyewords" Lang="FR" Content="rone,test">
    <!-- Description 用来告诉搜索引擎你的网站主要内容 -->
    <meta name="Description" Content="你网页的简述">
    <!-- Author 标注网页的作者或制作组 -->
    <meta name="Author" Content="rone，rone@rone.com">
    <!-- Copyright 标注版权 -->
    <meta name="Copyright" Content="本页版权归rone所有">
    <title>html示例</title>
    <!-- 设置网页图标 -->
    <link rel="SHORTCUT ICON" type="text/css" href="/images/favicon.ico" />
    <style>
        /* 这里写css样式 */
        table {
            /* 设置表格边框合并 */
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 5px;
        }
        form > div {
            padding: 5px;
            border: 1px solid black;
        }
    </style>
</head>
<body>
    <hr/><!-- 水平线 -->
    <!-- 内容块，一般用来组织排版 -->
    <div>
        <h3>标题，h1~h6</h3>
        <div>
            <hr/>
            <!-- &nbsp; 空格 -->
            <em>斜体</em>&nbsp;&nbsp;&nbsp;<strong>加粗</strong>
        </div>
        <div>
            <hr/>
            <span>段落，一般用来给包裹的内容设置单独的样式</span>
        </div>
        <div>
            <hr/>
            <q>设置短文本引用（给修饰内容加上双引号）</q>
        </div>
        <div>
            <hr/>
            <blockquote>设置长文本引用，<br/><!-- 回车换行 -->效果为：另起一段，段落前后有缩进</blockquote>
        </div>
        <div>
            <hr/>
            <address>地址信息（单独段落，斜体）</address>
        </div>
        <div>
            <hr/>
            <code>添加单行代码</code>
        </div>
        <div>
            <hr/>
            <pre>预显示格式（空格，换行，保留），常用来展示多行代码</pre>
        </div>
        <div>
            <hr/>
            <ul><li>无序列表内容</li><li>无序列表内容</li></ul>
        </div>
        <div>
            <hr/>
            <ol><li>有序列表内容</li><li>有序列表内容</li><li>有序列表内容</li></ol>
        </div>
        <div>
            <hr/>
            <a href="#" title="鼠标划过时显示文字" target="_blank">超链接，target 属性为连接打开方式</a>
        </div>
        <div>
            <hr/>
            <img src="#" alt="下载失败时替换文本" title="图片，提示文本"/>
        </div>
    </div>
    <hr/>
    <!-- 表格 -->
    <table>
        <caption>表格标题</caption>
        <tr>
            <th>姓名</th>
            <th>年龄</th>
            <th>城市</th>
        </tr>
        <tr>
            <!-- rowspan：该单元格横跨行；colspan：该单元格横跨列 -->
            <td rowspan="2">张三</td>
            <td colspan="2">25</td>
        </tr>
        <tr>
            <td>30</td>
            <td>上海</td>
        </tr>
    </table>
    <hr/>
    <!--
    表单，一般用于往后端发请求时配置参数。
    action 请求的地址
    -->
    <form action="#">
        <h3>表单</h3>
        <div>
            <!-- for属性将label和控件相关联，点击label也就是点击与之相关联的控件 -->
            <label for="name">单行文本输入框：</label>
            <!-- 
            onchange事件在内容改变（两次内容有可能相等）且失去焦点时触发; 
            autocomplete="off" 文本框设置不记忆以往的输入记录
            -->
            <input type="text" id="name" name="name" value="初始值" onchange="changeName()" autocomplete="off" />
        </div>
        <div>
            <label for="password">密码输入框：</label>
            <input type="password" id="password" name="password" value="初始值" oninput="onInputName()" />
        </div>
        <div>
            <label for="textarea">多行文本输入框：</label>
            <textarea rows="2" cols="30" id="textarea">初始值</textarea>
        </div>
        <div>
            <span>单选：</span>
            <label for="radio1">选项1</label><input type="radio" id="radio1" value="1" name="radio" checked="checked" />
            <label for="radio2">选项2</label><input type="radio" id="radio2" value="2" name="radio" />
        </div>
        <div>
            <span>多选：</span>
            <label for="checkbox1">选项1</label><input type="checkbox" id="checkbox1" value="11" name="checkbox" checked="checked" />
            <label for="checkbox2">选项2</label><input type="checkbox" id="checkbox2" value="22" name="checkbox" />
        </div>
        <div>
            <label for="select">下拉单选框：</label>
            <select id="select">
                <option value="0" selected="selected">默认选中</option>
                <option value="1">选项1</option>
                <option value="2">选项2</option>
            </select>
        </div>
        <div>
            <!-- datalist和input配合使用，来定义input可能的值 -->
            <label for="limitInput">提供参考值的输入框：</label>
            <input type="text" id="limitInput" list="limitItems" name="limitInput" />
            <datalist id="limitItems">
                <option value="水星">
                <option value="金星">
                <option value="地球">
                <option value="火星">
                <option value="木星">
                <option value="土星">
                <option value="天王星">
                <option value="海王星">
                <option value="冥王星">
            </datalist>
        </div>
        <div>
            <label for="file">文件选择：</label>
            <input type="file" id="file" name="file" multiple="multiple">
            <!-- 当使用表单提交文件时，需要给表单设置 enctype="multipart/form-data" -->
        </div>
        <div>
            <input type="submit" value="提交按钮"/>
            <input type="reset" value="重置按钮" />
        </div>
    </form>
</body>
<script type="text/javascript">
    // 这里写js的代码
</script>
</html>
```
* 常见的触发事件
  * onclick 鼠标单击
  * onmouseover 鼠标经过
  * onmouseout 鼠标移开
  * onchange 文本框内容改变，input元素的触发时机为焦点离开的时候
  * onselect 文本框内容被选中
  * onfocus 光标聚集
  * onblur 光标离开

#### css
[参考文档](https://developer.mozilla.org/zh-CN/docs/Web/CSS)
```css
  /************* 选择器 开始 *************/
  /* 通用选择器，其作用范围为html中所有标签元素 */
  * { display: none; }
  /* 标签选择器，用 html 中的标签作为标识来匹配，标识这些标签都适用这里定义的样式 */
  p { display: none; }
  /* 类选择器，用标签的class属性作为标识来匹配，使用了这个class的标签都适用这里定义的样式 */
  .className { display: none; }
  /* ID选择器，用标签的 id 属性作为标识来匹配，给使用这个id的标签设置样式，需要注意 id 唯一特性 */
  #idName { display: none; }
  /* 复杂选择器，依托于上面的三种基础选择器来实现复杂的选择 */
  /* 子选择器，给id为form的元素的直接子元素p标签设置样式（该子元素的子元素无效） */
  #form > p { display: none; }
  /* 后代选择器，给div标签下的所有使用了 rone 这个class的标签设置样式，与子选择器不同的是这里会对子元素、孙元素等等所有后代元素都起作用 */
  div .rone { display: none; }
  /* 伪类选择符，选择特定状态的标签，一下是一些常用的伪类选择器用法 */
  a:hover { display: none; } /* 当鼠标悬停在元素上时应用样式。例如，可以使用:hover选择器来改变链接的颜色或背景色。*/
  a:active { display: none; } /* 当元素被激活时（例如，当用户点击并按住鼠标按钮时）应用样式。这通常用于创建按钮或链接按下时的效果。*/
  a:focus { display: none; } /* 当元素获得焦点时（例如，当用户在表单字段中输入时）应用样式。可以使用:focus选择器来突出显示当前输入的表单字段。*/
  a:first-child { display: none; } /* 第一个子元素的元素。这可以用于选择列表中的第一个项目或某个容器的第一个子元素。*/
  a:last-child { display: none; } /* 最后一个子元素的元素。类似于:first-child，但选择的是最后一个子元素。*/
  a:nth-child(n) { display: none; } /* 第n个子元素的元素。*/
  a:nth-child(2n) { display: none; } /* 偶数位的子元素，即选择 第2、第4、第6…… */
  a:nth-child(2n-1) { display: none; } /* 奇数位的子元素，即选择 第1、第3、第5、第7…… */
  a:nth-child(n+3) { display: none; } /* 从第3个子元素开始到最后一个子元素 */
  a:nth-child(-n+3) { display: none; } /* 从0到3个子元素，即小于3的个子元素 */
  a:nth-last-child(n) { display: none; } /* 倒数第3个子元素 */
  /* 属性选择器，用于选择具有特定属性或属性值的标签 */
  [attribute] { display: none; } /* 选择具有指定属性的元素。例如，[href]选择具有href属性的元素。*/
  [attribute=value] { display: none; } /* 选择具有指定属性值的元素。例如，[type="text"]选择具有type属性值为"text"的元素。*/
  [attribute^=value] { display: none; } /* 选择属性值以指定值开头的元素。例如，[class^="btn"]选择class属性值以"btn"开头的元素。*/
  [attribute$=value] { display: none; } /* 选择属性值以指定值结尾的元素。例如，[href$=".pdf"]选择href属性值以".pdf"结尾的元素。*/
  [attribute*=value] { display: none; } /* 选择属性值中包含指定值的元素。例如，[src*="image"]选择src属性值中包含"image"的元素。*/
  [attribute~=value] { display: none; } /* 选择具有指定属性值的元素，其中属性值是以空格分隔的词列表。例如，[class~= "btn"]选择class属性值包含单词"btn"的元素。*/
  /* 分组选择符，将以上几种选择器用 , 相隔可以同时为多个选择器配置样式 */
  p, span, .rone { display: none; }
  /************* 选择器 结束 *************/

  /************* 文字样式 开始 *************/
  .rone {
    font-family: "Microsoft Yahei";/* 字体 */
    font-size: 10px;/* 字号 */
    color: red;/* 颜色 */
    color: rgb(133,45,200);
    color: #00ffff;
    font-weight: bold;/* 粗体 */
    font-style: italic;/* 斜体 */
    text-decoration: underline;/* 下划线 */
    text-decoration: line-through;/* 删除线 */
    text-indent: 2em;/* 首行缩进(缩进2倍的文字大小) */
    line-height: 2em;/* 行间距 */
    letter-spacing: 20px;/* 字间距(中文字，英文字母) */
    word-spacing:20px;/* 字间距(以空格为界设置间距，一般用于英文单词) */
    text-align: center;/* 对齐 */
    text-align: left;
    text-align: right;
  }
  /************* 文字样式 结束 *************/
```
* CSS样式三种关联方式，优先级一般为：内联式>嵌入式>外联式
  * 内联式，即写在现有HTML标签中，语法为<标签 style="属性:value"></标签>。
  * 嵌入式，css样式必须写在<style></style>间，一般情况下放在<head>下。
  * 外联式，将css代码写在一个以".css"为扩展名的单独外部文件中，在html页面中一般在<head>标签中使用<link>标签将css文件连接到HTML中去，
    `<link href="cssFileName" rel="stylesheet" type="text/css" />`

* 样式的继承性，并非所有的样式属性都会被继承。每个属性的继承性是由CSS规范决定的。如果您想要阻止某个属性继承，可以在子元素上明确设置该属性的值，以覆盖继承的样式。
  * 文本相关属性（如颜色、字体、字号、行高等）通常会被子元素继承。这意味着，如果您在父元素上设置了字体颜色为红色，那么子元素的文本也会继承这个红色颜色。
  * 某些布局属性（如文本对齐、浮动、宽度等）也可能会被子元素继承。例如，如果父元素设置了文本居中对齐，子元素也会继承这个居中对齐样式。
  * 背景属性（如背景颜色、背景图片等）通常不会被子元素继承。子元素的背景通常会被父元素的背景遮盖。
  * 边框属性和定位属性（如边框样式、边框宽度、定位方式等）通常不会被子元素继承。子元素的边框和定位通常需要单独设置。

* 样式的权值，是用来确定应用于元素的样式规则的优先级顺序。当多个样式规则同时应用到同一个元素上时，权值决定了哪个规则将被优先应用。
  以下是CSS样式权值的一般规则：
  * !important：具有!important声明的样式具有最高的权值，将覆盖其他所有样式规则。`.rone {width: 100px!important}`
  * 内联样式：直接应用在HTML元素上的样式（使用style属性）具有较高的权值。
  * ID选择器：具有ID选择器的样式规则比其他选择器的权值更高。
  * 类选择器、属性选择器和伪类选择器：这些选择器的权值较低于ID选择器，但高于元素选择器。
  * 元素选择器：具有相同权值的情况下，后定义的规则将覆盖先定义的规则。
  * 通用选择器（*）、子选择器（>）和相邻选择器（+）：这些选择器的权值较低，通常被用作补充其他选择器的样式。
  * 权值是根据选择器的特定组合来计算的。例如，如果一个样式规则同时使用了类选择器和元素选择器，那么它的权值将高于只使用元素选择器的样式规则。

* 样式的层叠，是指在CSS中，当多个样式规则应用到同一个元素上时，通过层叠规则来确定最终应用的样式。
  以下是CSS样式层叠的一般规则：
  * 选择器特殊性：每个选择器都有一个特殊性值，用于确定其优先级。特殊性值由选择器中ID选择器、类选择器、属性选择器和伪类选择器的数量和顺序决定。特殊性值越高的样式规则具有更高的优先级。
  * 样式规则的位置：后面定义的样式规则将覆盖先定义的规则，即后来者居上。
  * !important：具有!important声明的样式规则具有最高的优先级，将覆盖其他所有样式规则。
  * 继承性：某些样式属性具有继承性，即子元素会继承父元素的样式。但是，继承的样式规则的优先级较低，可以被直接应用在元素上的样式规则覆盖。

* 在CSS中，html中的标签元素大体被分为三种不同的类型：
  * 块状元素，独占一行。宽、高、行高、顶底边距可设置。`<div> <p> <form> <ul> ...`
  * 内联(行内)元素，和别的元素同行。宽、高、顶底边距不可设置。元素的宽就是其包含的内容的宽度。`<span> <label> <a>`
  * 内联块状元素，和别的元素同行。宽、高、行高、顶底边距可设置。`<img> <input>`
  * 可通过 display 来改变元素的类型。`状->内联 display:inline    内联->块状  display:block    块状,内联->内联块状  display:inline-block`

* 盒子模型 一般的块状元素，padding内填充，margin外边距，border边框
  * border 边框，语法：`border：宽度 样式(dashed虚线,dotted点线,solid实线) 颜色`
    为单独一边设置时 `border-top、border-bottom、border-left、border-right`
  * padding 内边距，语法: `padding:20px(top) 10px(right) 15px(bottom) 30px(left); padding:10px(上下) 20px(左右);`
    可为单独一遍设置 `padding-top、padding-bottom、padding-left、padding-right`
  * margin 外边距，语法: `margin:20px(top) 10px(right) 15px(bottom) 30px(left); margin:10px(上下) 20px(左右);`
    可为单独一遍设置 `margin-top、margin-bottom、margin-left、margin-right`

* css布局模型 flow(),float(),layer()
  * flow 流动，网页的默认布局模型。
  * float 浮动，根据设置的值在该行的浮动在相应位置，浮动处无法显示别的内容，空白处无影响，仍旧可以显示别的内容。
    ` float: left; 向左浮动，使其右侧的内容环绕在其左侧 `
  * layer 层，通过定位属性(position)指定元素在文档中的位置，然后配合 left、right、top、bottom 属性以实现更精确的元素定位和布局。
    position 常用的属性如下：
      * static（默认值）：元素按照正常的文档流进行布局，不进行特殊的定位。top、right、bottom、left和z-index属性对static定位的元素没有影响。
      * relative：元素相对于其正常位置进行定位。可以使用top、right、bottom和left属性来指定元素相对于其正常位置的偏移量。相对定位不会影响其他元素的布局。
      * absolute：元素相对于最近的已定位祖先元素进行定位（如果不存在已定位的祖先元素，则相对于文档的初始包含块）。可以使用top、right、bottom和left属性来指定元素相对于其定位参考点的偏移量。绝对定位会从文档流中脱离，可能会影响其他元素的布局。
      * fixed：元素相对于浏览器窗口进行定位，即使页面滚动，元素也会保持在固定位置。可以使用top、right、bottom和left属性来指定元素相对于窗口的偏移量。
      * sticky：元素根据用户的滚动位置进行定位。当元素在视口中可见时，它按照正常的文档流进行布局，当元素滚动出视口时，它将固定在指定位置。可以使用top、right、bottom和left属性来指定元素相对于其父元素的偏移量。
    ```css
    /* 定位于浏览器底部 */
    #fixed_bottom {
        position: fixed;
        bottom: 0;
        width: 100%; /* 固定某块区域后，需要设置宽度后，其中的滚动条才能生效 */
    }
    /* 定位于父容器的右上角 */
    .absolute_right {
        position: absolute;
        right: 0;
    }
    ```

#### JavaScript
```javascript
  var rone = 3; //js变量是弱类型，也就是只有在具体赋值的时候才知道是什么类型的数据，在定义变量的时候不会指定类型
  var rones = new Array();
  rones = ["a", "b", "c"];
  
  // 获取标签元素并对其进行操作
  document.getElementsByName("p"); //获取所有的p标签元素
  document.getElementsByClassName("rone"); //获取class中有rone的元素们
  var roneDocument = document.getElementById("rone");//获取id为rone的元素
  roneDocument.innerHTML; //获取或替换HTML中的内容
  roneDocument.style.display = "none"; //获取或修改HTML样式
  roneDocument.className = "rone"; //获取和修改class属性
  roneDocument.getAttribute("属性名"); //获取属性值
  roneDocument.setAttribute("属性名", "值"); //设置属性值
  roneDocument.childNodes; //获取该节点的子节点集合
  roneDocument.firstChild; //获取第一个子节点
  roneDocument.lastChild; //获取最后一个子节点
  roneDocument.parentNode; //获取父节点
  roneDocument.nextSibling; //获取下一个兄弟节点
  roneDocument.previousSibling; //获取上一个兄弟节点
  roneDocument.appendChild(新的节点); //在该节点的子节点列的最后添加新的节点
  roneDocument.insertBefore(newNode, node); //在该节点的子节点node前插入newnode节点
  roneDocument.removeChild(node); //删除该节点指定的子节点，返回删除的节点
  roneDocument.replaceChild(newNode, oldNode); //用newnode替换该节点的子节点oldnode，返回oldnode的引用
  roneDocument.closest("div"); //获取当前节点的祖先元素中第一个div元素
  document.createElement("div"); //创建一个元素节点，配合增改
  document.createTextNode("文本内容"); //创建一个文本节点
  roneDocument.submit(); //触发表单的提交，仅表单对象可用
  roneDocument.remove(); //移除当前元素
  
  // 时间对象，使用set赋值
  var date = new Date();
  date.toLocaleString(); //时间字符串
  date.getFullYear(); //年份，用四位数表示
  date.getMonth(); //月份，0:一月...11:十二月
  date.getDate(); //几号
  date.getDay(); //星期,0:周日..6:周六
  date.getHours(); //小时，24小时制
  date.getMinutes(); //分钟数
  date.getSeconds(); //秒钟数
  date.getTime(); //时间(单位为毫秒),计算从1970.1.1到日期所指日期的毫秒数
  
  // String对象
  var roneStr = "Hello World!";
  roneStr.toLowerCase(); //转换为小写
  roneStr.toUpperCase(); //转换为大写
  roneStr.charAt(0); //返回下标为index的字符
  roneStr.indexOf("llo", 0); //返回指定的字符串首次出现的下标若没有返回-1，"llo"需要检索的字符串，0检索的起始位置，0省略标识从0开始
  roneStr.split("o"); //分割字符串返回数组，"o"指定分割符
  roneStr.substring(6, 11); //提取字符串，返回6(包含)和11(不包含)之间的字符，11省略的话就是直到最后一个字符
  roneStr.substr(6, 5); //提取从第6位(包含)开始的5位字符，5省略时就是不限长度
  "yyyy-MM-dd hh-mm-ss".replace("-", "/"); //用 / 替换 - ，只替换一次
  "yyyy-MM-dd hh-mm-ss".replace(/-/g, "/"); //用 / 替换 - ，替换所有
  roneStr.match(RegExp(/o/)); //判断是否符合某个正则表达式，返回一个包含结果的数组，如果未匹配上则返回null
  roneStr.match(RegExp(/o/g)); //返回所有匹配正则的结果，没有则返回null
  RegExp(/o/).exec(roneStr); //同match()用法
  RegExp(/o/).test(roneStr); //判断字符串是否匹配正则，匹配返回true，否则返回false
  roneStr.search("llo"); //返回指定的字符串首次出现的下标若没有返回-1

  // Math 数学计算对象，无需创建直接使用
  Math.ceil(4.53); //向上取整
  Math.floor(4.53); //向下取整
  Math.round(4.8); //四舍五入取整，x.5取偏右的
  Math.random(); //随机获取[0,1)间的一个数

  // Number数字对象
  Number("123.4567").toFixed(2); //四舍五入保留两位小数
  Number("123456789.345").toLocaleString(); //展示千分位

  // Array 数组对象
  var roneArray = ["hello", "world"];
  // 遍历数组
  for (i in roneArray) {
      console.log(roneArray[i]);
  }
  roneArray.push("!"); //在数组最后添加一个元素
  roneArray.concat("hello", "js"); //连接两个或多个数组，返回一个新的数组，不会影响原数组
  roneArray.join("_"); //将数组按"_"分隔符组成字符串，若未指定分隔符默认使用,
  roneArray.reverse(); //颠倒数组下标，改变原数组
  roneArray.slice(1, 2); //截取元素，返回一个新数组。包含1，不包含2
  roneArray.sort(); //排序，按unicode码排序。可方法函数进行自定义的排序，有两个参数a、b，需要返回一个说明两个值相对顺序的数字，返回值<=-1时a在b之前、返回值>=-1且<1时a和b按照原顺序、返回值>=1时a在b后
  roneArray.indexOf("hello"); //查找某个元素在数组中第一次出现的索引
  roneArray.splice(0, 2); //移除从0开始的2个元素并返回

  // 计时器/定时器
  var timerId = setTimeout(function () {
      console.log("一秒种后执行一次");
  }, 1000);
  setTimeout("roneTest('参数1', '参数2')", 1000); //1秒后执行一次roneTest()方法，只运行一次
  clearTimeout(timerId); //取消计时器的执行
  var intervalId = setInterval(function () {
      console.log("每隔两秒执行一次");
  }, 1000 * 2);
  clearInterval(intervalId); //取消定时器的执行

  // window对象
  window.history.length; //获取浏览器历史列表中url的数量
  window.history.back(); //返回前一个页面，效果等同go(-1)
  window.history.forward(); //下一个页面，效果等同go(1)
  window.history.go(int); //1：前一个页面、0：当前页、-1：后一个页面、其他数值：要访问的URL在history列表中的相对位置
  window.open(url, target, features); //url: 要打开的网页。target: 指定打开的窗口的位置。_blank(默认值，新窗口)、_parent(父级窗口)、_self(当前窗口)、_top(在顶级窗口)、指定的名称(新增或覆盖掉该名字的窗口)。features: 可选的字符窗参数，用来定义新窗口的一些属性。"width: 500px, height: 400px" 
  window.close(); //关闭本窗体
  window.location.replace(url); //通过加载 URL 指定的文档来替换当前文档，这个方法是替换当前窗口页面，前后两个页面共用一个窗口，所以是没有后退返回上一页的。
  window.location.reload(); // 页面重新加载

  // 其他常用的方法
  console.log("控制台输出");
  alert("弹框提示");
  confirm("确认对话框，返回Boolean值");
  prompt("提示语，返回用户数据的内容", "默认值");
  
  // js中的异常机制，和java差不多
  try {
      throw new Error("抛出异常!");
  } catch (e) {
      console.log(e);
  }

  // json的使用
  var json = {}; //空json对象，也可以直接定义并赋值
  // var json = {name: "我"};
  json.name = "星星"; //给json对象设置值,此时json数据为 {name: "星星"}
  var childs = []; //一个数组
  childs.push("hello"); //往数组中添加一个值
  json.childs = childs; //添加一个数组，{name: "星星", childs: ["hello"]}
  json.next = {name: '嘿嘿', sex: 'man'}; //添加一个子json，{name: "星星", childs: Array(1), next: {name: "嘿嘿", sex: "man"}}
  var txt = '{"firstName":"Thomas","lastName":"Carter"}';
  var obj = JSON.parse(txt); //字符串转json格式
  console.log(obj.firstName);
  console.log(obj.lastName);
  console.log(JSON.stringify(obj)); //json格式转为字符串
  // 特殊key值操作
  var txt = "{'0':'hello'}";
  var obj = JSON.parse(txt);
  // console.log(obj.0);会报错
  console.log(obj["0"]);
  // json遍历
  for (var key in json) {
      var value = json[key];
  }
  
  // 定义方法，等同于java中的函数
  function roneTest(param1, param2) {
    console.log(param1, param2);
  }
  
  // js原生的ajax的使用
  function ajaxTest() {
      var xmlHttp;
      // 判断是否支持内建 XMLHttpRequest 对象，不支持时则创建 ActiveXObject 对象。
      if (window.XMLHttpRequest) {
          xmlHttp = new XMLHttpRequest();
      } else {
          xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
      }
      // onreadystatechange：每当 readyState 发生改变时就会触发该事件，一次完整的请求会触发4次，分别是readyState从： 0-1、1-2、2-3、3-4；
      xmlHttp.onreadystatechange = function () {
          // readyState：标识 XMLHttpRequest 的状态。0: 请求未初始化；1: 服务器连接已建立；2: 请求已接收；3: 请求处理中；4: 请求已完成，且响应已就绪；
          console.log(xmlHttp.readyState);
          // status：服务器状态值(例如200，404，500等)；
          if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
              // responseText：服务器返回的文本数据；responseXML：服务器返回的XML格式的数据；
              console.log(xmlHttp.responseText);
              document.getElementById("testDiv").innerHTML = xmlHttp.responseText;
          }
      }
      // 请求的类型(GET/POST)，url：请求路径，async：true（异步）或 false（同步）
      xmlHttp.open("GET", "/ajax/ajaxTest.html?name=rone&sex=man", true);
      // 用于向服务器发送请求, 如果声明为异步, 那么该方法将立即返回, 否则将等到接收到服务器响应为止。
      xmlHttp.send();

      xmlHttp.open("POST","/ajax/ajaxTest.html",true);
      xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
      // 当使用json格式的参数请求时需要添加如下两个的配置
      xmlHttp.setRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
      xmlHttp.setRequestHeader("Content-type","application/json; charset=utf-8");
      // 参数，用于 POST 请求传递参数
      xmlHttp.send("name=rone&sex=man");
  }
  
  // if条件判断
  if (0) {}; // false
  if (null) {}; // false
  if ("") {}; // false
  if (1) {}; // true
  if (2) {}; // true
  if ("0") {}; // true

```

#### jQuery
```javascript
  // [参考教程](http://www.runoob.com/jquery/jquery-ref-ajax.html)
  $.ajax({
      // 常用的配置
      url: "/test",
      async: true, // 布尔值，表示请求是否异步处理。默认是 true。
      contentType: "application/x-www-form-urlencoded", // 发送数据到服务器时所使用的内容类型。默认是："application/x-www-form-urlencoded"。
      type: GET, // 规定请求的类型（GET 或 POST）。
      data: { // 规定要发送到服务器的数据。
          name: "loginName",
          password: "loginPwd"
      },
      success: function (result, status, xhr) { // 当请求成功时运行的函数。
          console.log(result);
      },
      error: function (xhr, status, error) { // 如果请求失败要运行的函数。
          console.log("error", xhr, status, error);
      },
      complete: function (xhr, status) { // 请求完成时运行的函数（在请求成功或失败之后均调用，即在 success 和 error 函数之后）。
          console.log("complete", xhr, status);
      },
      // 不常用的配置
      beforeSend: function (xhr) { // 发送请求前运行的函数
          console.log("beforeSend", xhr);
      },
      // cache: true, // 布尔值，表示浏览器是否缓存被请求页面。默认是 true。
      // context: null, // 为所有 AJAX 相关的回调函数规定 "this" 值。
      // dataFilter: function (data, type) { // 用于处理 XMLHttpRequest 原始响应数据的函数。
      //     console.log("dataFilter", data, type);
      // },
      dataType: xxx, // 预期的服务器响应的数据类型。
      // global: true, // 布尔值，规定是否为请求触发全局 AJAX 事件处理程序。默认是 true。
      headers: {}, // json对象，设置请求头。
      // ifModified: false, // 布尔值，规定是否仅在最后一次请求以来响应发生改变时才请求成功。默认是 false。
      // jsonp: xxx, // 在一个 jsonp 中重写回调函数的字符串。
      // jsonpCallback: xxx, // 在一个 jsonp 中规定回调函数的名称。
      // password: xxx, // 规定在 HTTP 访问认证请求中使用的密码。
      // processData: xxx, // 布尔值，规定通过请求发送的数据是否转换为查询字符串。默认是 true。
      // scriptCharset: xxx, // 规定请求的字符集。
      timeout: 1000 * 60, // 设置本地的请求超时时间（以毫秒计）。
      traditional: xxx, // 布尔值，规定是否使用参数序列化的传统样式。当参数需要传数组时，需要设置 traditional: true
      // username: xxx, // 规定在 HTTP 访问认证请求中使用的用户名。
      // xhr: xxx, // 用于创建 XMLHttpRequest 对象的函数。
  });

// 查找选择元素
$(selector).siblings(); //获取选择器的所有同级元素
$(selector).siblings('.rone'); //获取选择器的同级元素中符合.rone选择逻辑的元素
    
// 常用的元素方法
$(selector)[0]; //获取原生JS对象
$(selector).empty(); //将当前元素置空，包括所有文本和子节点
$(selector).remove(); //删除被选元素（及其子元素）
$(selector).focus(); //将焦点(光标)移动到此元素上

// 在当前元素后面添加元素
// content 规定要插入的内容（可包含 HTML 标签）。
$(selector).append(content);
// function(index,html) 规定返回待插入内容的函数。index - 可选。接收选择器的 index 位置。html - 可选。接收选择器的当前 HTML。
$(selector).append(function(index, html) {});

// 操作属性
$(selector).attr("href", "www.baidu.com"); //操作html文档节点属性
$(selector).prop("outerHTML"); //操作js对象属性，例如 outerHTML
$(selector).data("key", "rone"); // 操作一些自定义的data-XXXX属性
$(selector).removeAttr("href"); // 移除属性
$(selector).addClass("rone"); // 添加一个class属性
$(selector).removeClass("rone"); // 移除一个class属性

// 操作css样式
$("p").css("background-color","yellow");
$("p").css({"background-color":"yellow","font-size":"200%"});
$("p").css("background-color");

// 表单
$(selector).serialize(); //获取表单参数，结果为拼接的键值对，param1=value1&param2=value2&....
$(selector).serializeArray(); //获取表单参数，结果为json格式数组，[{"name":"FirstName","value":"Bill"},{"name":"LastName","value":"Gates"}] 

```


************************************************************************************************************************
### 坑
#### 字体设置小于 12px 时不生效
font-size 设置小于 12px 的字体时，发现并不生效。
* 原因是 Chrome 以及 Chromium 内核的浏览器在中文语言下最小字体只能是 12px，大部分浏览器都是如此。
* 如果执意要设置小于 12px 的字体，那只能使用 transform: scale() 缩放属性来实现。
  `transform: scale(0.5);`

#### for循环中使用var来定义索引，循环体中调用异步方法索引作为参数，出错。2021.04.27
* 原因：for循环是同步执行的，循环体中的异步方法属于微任务，会先放到微任务队列，等待同步执行任务都执行完毕才回去执行。
  而var是函数作用域，for循环执行完之后再去执行异步方法时索引已经固定为了循环的最后一次索引了。
* 解决方案：for循环使用let来定义索引。
  * var 和 let的区别：https://zhuanlan.zhihu.com/p/265002815
    * var是函数作用域，let是块作用域。
    * let不能在定义之前访问该变量，但是var可以。
    * let不能被重新定义，但是var是可以的。

#### 引用的js中定义了一个变量，在引用者页面访问提示undefined。2021.05.25
chrome浏览器、微信开发者工具、PC端的微信、PC端的企业微信都支持。
Android微信、企业微信提供的内嵌浏览器不支持，提示undefined。
但我在本地上模拟又都是可以的，太玄学了。应该是和js加载循序有关。

#### ie的坑
* 判断是否是ie浏览器，实测IE11可以检测出
```javascript
  if (!!window.ActiveXObject || "ActiveXObject" in window) {
      return true;
  } else {
      return false;
  }
```
* ie中自带removeNode()方法，自定义方法时请勿重名。
* removeNode()：仅删除当前元素的结构其html内容保留；
* removeNode(this)：删除整个元素
* ie目前(2019.4.15)不支持<datalist>
* ie好像不支持jQuery的$("#ipt_runCycle").attr("value", data.result[i].dicVal);
* ie下url中文乱码，可通过js的encodeURI()方法进行编码
  `encodeURI("中文")`

#### 有时给元素添加事件不能生效
有时给元素添加事件(click、change ...)，不能生效，可能原因是 添加事件的JS代码执行时该元素还没有加载(模块异步加载)。
此时可将该事件挂载在已加载的元素上。
```javascript
  //执行下面代码时 class 为 keep-open 的元素还未加载，此时事件是不会触发的
  $(".keep-open").find("input").off('change').on('change', function () {
      console.log("fuck....");
  });
  //可将该 change 事件挂载在 body 下面
  $("body").on('change','keep-open input', function () {
      console.log("fuck....");
  });
```


************************************************************************************************************************
### solutions
#### 模块可拖动
```javascript
  // 原生js实现
  // id为需要拖动的d元素的的id
  function dragFunc(id) {
      var drag = document.getElementById(id);
      drag.onmousedown = function(event) {
          var ev = event || window.event;
          event.stopPropagation();
          var disX = ev.clientX - drag.offsetLeft;
          var disY = ev.clientY - drag.offsetTop;
          document.onmousemove = function(event) {
              var ev = event || window.event;
              drag.style.left = ev.clientX - disX + "px";
              drag.style.top = ev.clientY - disY + "px";
              drag.style.cursor = "move";
          };
      };
      drag.onmouseup = function() {
          document.onmousemove = null;
          this.style.cursor = "default";
      };
  };
```

#### div重叠显示
我们使用position实现绝对定位，对父级设置position:relative属性，对其子级设置position:absolute(按代码顺序重叠)加上left或right和top或bottom实现子级在父级内任意定位。
```html
  <!-- 蓝色在最前面，其次黄色，最后红色 -->
  <div style="position:relative; color:#000; border:1px solid #000; width:500px; height:400px">
      <div style="position:absolute; left:30px; top:30px; background:#F00; width:200px; height:100px">我背景为红色</div>
      <div style="position:absolute; left:50px; top:60px; background:#FF0; width:400px; height:200px">我背景为黄色</div>
      <div style="position:absolute; left:80px; top:80px; background:#00F; width:300px; height:300px">我背景为蓝色</div>
  </div>
```

#### 用css实现箭头
```html
  <span class="arrow_up"></span>
  <span class="arrow_down"></span>
  <style>
    /* 向上的箭头 */
    .arrow_up {
      /* 此处可将左右的颜色设置上，以及上边框的配置也加上查看具体的边框原理 */
      border-bottom: 10px solid red; /* 底边框10px高度，即为箭头的高度，红色 */
      border-left: 5px solid transparent; /* 左边框5高度，无色 */
      border-right: 5px solid transparent; /* 左边框右高度，无色 */
      position: relative; /* 调整箭头位置 */
      bottom: calc(50% + 3px); /* 调整箭头位置 */
    }
    /* 向下的箭头 */
    .arrow_down {
      border-top: 10px solid green;
      border-left: 5px solid transparent;
      border-right: 5px solid transparent;
      position: relative;
      top: calc(50% + 3px);
    }
  </style>
```

#### 导出文件
```html
    <a href="url">下载文件</a>
    <form action="url"></form>
```

#### 生成临时表单提交
提交后将其删除
```javascript
    var tempForm = document.createElement("form");
    tempForm.id = "tempForm";
    tempForm.method = "post";
    tempForm.action = "url";

    var hideInput = document.createElement("input");
    hideInput.type = "hidden";
    hideInput.name = "name";
    hideInput.value = "value";
    tempForm.appendChild(hideInput);

    document.body.appendChild(tempForm); //添加临时form
    tempForm.submit(); //必须手动的触发，否则只能看到页面刷新而没有打开新窗口
    document.body.removeChild(tempForm); //移除临时创建的form
```

#### 
#### h5页面展示控制台
```html
  <script src="https://cdnjs.cloudflare.com/ajax/libs/vConsole/3.15.0/vconsole.min.js"></script>
  <script type="text/javascript">
      new VConsole();
  </script>
```

#### 点击拨打电话
不同厂家的手机展示的效果不一致，但最终都应该能拨打电话
```html
  <div>
      <a href="tel:手机号码">拨打电话</a>
  </div>
```

#### 
#### 滚动条设置
```css
  /* 强制设置出现滚动条 */
  .show_overflow {
      overflow: scroll; /* 横向和纵向的滚动条都有 */
      overflow-x: scroll; /* 横向滚动条 */
      overflow-y: scroll; /* 纵向滚动条 */
  }
  /* 自动出现滚动条，当字内容超出高度限制时自动出现 */
  .auto_overflow {
      overflow: auto;
  }
  /* 禁用滚动条 */
  .hidden_overflow {
      overflow: hidden;
  }
```

#### <a>去掉下划线
`text-decoration: none;`

#### 设置文本是否能被选中
不同浏览器内核支持程度不同
```css
  /* 允许选择文本内容 */
  .able_select {
      user-select: auto; /* 默认配置 */
  }
  /* 文本不能被选择 */
  .no_select {
      user-select: none;
  }
  /* 允许选择文本内容，一般用于父元素不允许和子元素允许的情况 */
  .text_select {
      user-select: text;
  }
  /* 文本的内容只能在其容器内进行选择，如果文本溢出容易则溢出部分无法选择 */
  .contain_select {
      user-select: contain;
  }
  /* 允许选择所有文本内容，包含该元素之外的文本内容 */
  .all_select {
      user-select: all;
  }
```

#### 百分比和绝对值组合配置宽度
`width: calc(100% - 50px);`

#### 边框圆角
```css
  .rone {
    /* border-radius：同时设置4个边框的圆角样式 */
    border-radius: 20px; /*表示圆角的水平半径和垂直半径都为20px长度*/
    border-radius: 20px/40px; /*表示圆角的水平半径的长度为20px，垂直半径的长度为20px*/
    border-radius: 20%; /*表示圆角的水平半径和垂直半径都为各自边框长度的20%*/
    border-radius: 20%/30%; /*表示圆角的水平半径为边框宽度的20%，垂直半径都为边框高度的20%*/
    border-radius: 20px/30%; /*表示圆角的水平半径长度20px，垂直半径都为边框高度的20%*/
    border-top-left-radius: 20px; /*设置左上角边框的圆角样式*/
    border-top-right-radius: 20px; /*设置右上角边框的圆角样式*/
    border-bottom-left-radius: 20px; /*设置左下角边框的圆角样式*/
    border-bottom-right-radius: 20px; /*设置右下角边框的圆角样式*/
  }
```

#### 固定图片的长势高度，超出部分隐藏
```html
<div style="height: 300px; overflow: hidden;">
  <img style="width: 100%;" src="">
</div>
```

#### 
#### iframe嵌套结构中将当前页面置于最高级
常见场景：登录失效时，返回的登录页不是在top层级，而是嵌套在其中一个层级中。
```javascript
    // 解决登录页面嵌套问题
    // 前置将当前页面置于最高级
    if (window != top){
        top.location.href = location.href;
    }
```

#### 页面加载完成后执行
```javascript
  // 原生的js写法，等到页面内包括图片的所有元素加载完毕后才能执行
  window.onload = function (ev) {
      // do something
  }
  // jquery的写法，DOM文档加载完成后执行，无需等待图片等元素加载完成
  $(function() {
    // JS code
  });
  $(document).ready(function(){
    // JS code
  })
```

#### 判断是否为空
```javascript
  var str;
  if (str == '' || str == null) {
      
  }
  // 判断变量是否被声明
  if (typeof rone === 'undefined') {
      
  }
```

#### 金额展示千分位
```javascript
    console.log(String(1232323.110).replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,'));
    console.log(String(1232323.110).replace(/(?!^)(?=(\d{3})+(?:$|\.))/g, ','));
    console.log(1232323.110.toLocaleString());
    Number("123456789.345").toLocaleString();
```

#### 元素option事件操作
```javascript
  // 绑定点击事件，原生js
  document.getElementsByName("p").addEventListener('click', function (e) {
      consol.log('原生事件绑定')
  });
  // jquery写法，参数只能是function名不能带有()，否则变成执行该function一次
  $(selector).change(functionName);
  $(selector).click(functionName);
  $(document).on('click', function (e) {
    consol.log('jquery事件绑定')
  });
  $("p").off("click"); //移除所有 <p> 元素上的 click 事件
```

#### 判断是否包含某子元素
```javascript
    // 方案一
    if ($(this).is(':has(ul)')) {
        // Code
    }
    // 方案二
    if ($(this).find('ul').length == 0) {
        // Code
    }
```

#### 获取子元素
```javascript
    // 获取所有子元素并遍历
    $(selector).children().each(function (index, child) {});
    // 获取第几个，index从0计数
    $(selector).children().eq(0); //获取第一个
    $("#liAreaConfigSellPriceAna").children("a:first-child");//获取第一个
```

#### 获取同级元素
```javascript
  $(selector).siblings(); //获取选择器的所有同级元素
  $(selector).siblings('.rone'); //获取选择器的同级元素中符合.rone选择逻辑的元素
  $(selector).siblings().addBack(); // 选中包含自己在内的同级元素
```

#### 操作下拉选框
```javascript
    // 设置value为pxx的项选中
    $(".selector").val("pxx");
    // 设置text为pxx的项选中
    $(".selector").find("option[text='pxx']").attr("selected",true);
    // 获取当前选中项的value
    $(".selector").val();
    $(".selector option:selected").val();
    // 获取当前选中项的text
    $(".selector").find("option:selected").text();
    $(".selector option:selected").text();
    // 很多时候用到select的级联，即第二个select的值随着第一个select选中的值变化。这在jquery中是非常简单的。
    $(".selector1").change(function(){
        // 先清空第二个
        $(".selector2").empty();
        // 实际的应用中，这里的option一般都是用循环生成多个了
        var option = $("<option>").val(1).text("pxx");
        $(".selector2").append(option);
    });
```

#### 判断checkbox是否选中
```javascript
    if ($("#checkbox-id").get(0).checked) {}
    if ($('#checkbox-id').is(':checked')) {}
    if ($('#checkbox-id').attr('checked')) {}
```

#### jquery 添加复杂子元素
```javascript
var $div = $("<div class='rone'></div>");
for (var i = 0; i < 3; i++) {
  $div.append('<div class="rone" onclick="clickRone(event, ' + i + ')">test' + i + '</div>');
}
$("#rone").append($div);
```

#### 判断是微信打开还是企业微信打开
```javascript
    const userAgent = window.navigator.userAgent;
    if (/wxwork/i.test(userAgent)) {
        alert("企业微信");
    } else {
        alert("微信");
    }
```


