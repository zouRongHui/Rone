package org.rone.core.jdk;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.StringTokenizer;

/**
 * @author rone
 */
public class StringDemo {

    public static void main(String[] args) throws Exception {
        demo();
        // randomString();
        // splitTest();
        // format();
        // stringJoiner();
        // arraysToString();
    }

    public static void demo() throws UnsupportedEncodingException {
        String str = "hello world !";

        // 截取字符串
        // 截取从beginIndex开始到结尾的字符串并返回
        System.out.println(str.substring(3));
        // 截取从beginIndex开始到endIndex的字符串并返回
        System.out.println(str.substring(3, 5));

        // 查找字符
        // 返回此字符串中第一次出现str的索引
        // tips: 参数为int时会按照ascii码来转换成char来查询
        System.out.println(str.indexOf("!"));

        // 替换字符
        System.out.println(str.replace("!", "?"));
        System.out.println(str.replaceAll("l", "L"));
        // 支持正则表达式，且需要转义字符
        // 例如剔除ab123you456中的除数字外的其余部分
        System.out.println("ab123you456".replaceAll("\\p{Alpha}", ""));

        // String解码后重新编码，用于解决中文乱码问题
        System.out.println(new String("客户分析报告.pdf".getBytes("UTF-8"), "ISO-8859-1"));

        // 忽略大小写的比较大小
        System.out.println(str.equalsIgnoreCase("HELLO WORLD !") ? "比较时忽略了大小写" : "比较时大小写不一样");

        // 随机字符串
        System.out.println("32位随机字符串：" + RandomUtil.randomString(32));

        // intern()方法用于复用字符串，将相同的字符串指向同一引用；
        String s1 = "Java";
        String s2 = new String("Java");
        System.out.println(s1 == s2 ? "" : "输出");
        System.out.println(s1.intern() == s2.intern() ? "输出" : "");
    }

    /**
     * String拆分成数组
     *  1.转义字符,必须得加"\\"，转义字符有 . | +
     *  2.如果分隔符为 \ ，则需要如下写法：String.split("\\\\")
     *  3.如果在一个字符串中有多个分隔符,可以用“|”作为连字符,比如,“acount=? and uu =? or n=?”,把三个都分隔出来,可以用String.split("and|or")
     *  4.可以指定拆分的最大数量，
     */
    public static void splitTest() {
        String str = "a.b.4x5.e";
        // str = "a b c d e f g";
        //这里的\\s是正则表达式，代表空白字符
        String[] strs = str.split("\\s");
        for (String temp : strs) {
            System.out.print(temp + "_");
        }
        System.out.println("************");
        //这里\\..表示.字符+任意字符
        strs = str.split("\\..");
        for (String temp : strs) {
            System.out.print(temp + "_");
        }

        // split(regex, limit) limit参数可以指定拆分的最大次数
        str = "testjava|hello world|";
        Arrays.stream(str.split("\\|", 1)).forEach(s -> System.out.println(s));
        System.out.println("*************************");
        Arrays.stream(str.split("\\|", 2)).forEach(s -> System.out.println(s));
        System.out.println("*************************");
        Arrays.stream(str.split("\\|", 3)).forEach(s -> System.out.println(s));
        System.out.println("*************************");
        Arrays.stream(str.split("\\|", 4)).forEach(s -> System.out.println(s));
        System.out.println("*************************");
    }

    /**
     * 字符串的格式化，占位符替换。https://www.cnblogs.com/fsjohnhuang/p/4094777.html
     *   占位符完整格式为： %[index$][标识][最小宽度][.精度]转换符 。
     *     针对不同数据类型的格式化，占位符的格式将有所裁剪。
     *     % ，占位符的其实字符，若要在占位符内部使用%，则需要写成 %% 。
     *     [index$] ，位置索引从1开始计算，用于指定对索引相应的实参进行格式化并替换掉该占位符。
     *     [标识] ，用于增强格式化能力，可同时使用多个 [标识] ，但某些标识是不能同时使用的。
     *         文本类型标识，支持的转换符：s、c、b、n
     *             - ，在最小宽度内左对齐，右边用空格补上。
     *         数值类型标识，支持的转换符：b、d、x、o、n、f、a、e、g
     *                 - ，在最小宽度内左对齐,不可以与0标识一起使用。
     *                 0 ，若内容长度不足最小宽度，则在左边用0来填充。
     *                 # ，对8进制和16进制，8进制前添加一个0,16进制前添加0x。
     *                 + ，结果总包含一个+或-号。
     *                 空格，正数前加空格，负数前加-号。
     *                 , ，仅十进制，每3位数字间用,分隔。
     *                 ( ，若结果为负数，则用括号括住，且不显示符号。
     *     [最小宽度] ，用于设置格式化后的字符串最小长度，若使用 [最小宽度] 而无设置 [标识] ，那么当字符串长度小于最小宽度时，则以左边补空格的方式凑够最小宽度。
     *     [.精度] ，对于浮点数类型格式化使用，设置保留小数点后多少位。
     *     转换符 ，用于指定格式化的样式，和限制对应入参的数据类型。
     *         %s,字符串类型；
     *             %c,字符类型，实参必须为char或int、short等可转换为char类型的数据类型，否则抛IllegalFormatConversionException异常；
     *             %b,布尔类型，只要实参为非false的布尔类型，均格式化为字符串true，否则为字符串false；
     *             %d,整数类型(十进制)  99
     *             %x,整数类型(十六进制) FF
     *             %o,整数类型(八进制)  77
     *             %f,浮点类型 99.99
     *             %a,十六进制浮点类型 FF.35AE
     *             %e,指数类型 9.38e+5
     *             %g,通用浮点类型（f和e类型中较短的）
     *             %h,散列码
     *             %%,百分比类型  ％
     *             %n,换行符
     *             %tx,日期与时间类型（x代表不同的日期与时间转换符)
     *                 日期：
     *                     c，星期二 十一月 26 10:52:22 CST 2019
     *                     F，2019-11-26
     *                     D，11/26/19
     *                     r，10:52:22 上午
     *                     T，10:52:22
     *                     R，10:52
     *                     b, 十一月  月份简称
     *                     B, 十一月  月份全称
     *                     a, 星期二  星期简称
     *                     A, 星期二  星期全称
     *                     C, 20      年前两位（不足两位补零）
     *                     y, 19      年后两位（不足两位补零）
     *                     j, 330     当年的第几天
     *                     m, 11      月份（不足两位补零）
     *                     d, 26      日期（不足两位补零）
     *                     e, 26      日期（不足两位不补零）
     *                 时间：
     *                     H, 10               24小时制的小时（不足两位补零）
     *                     k, 10               24小时制的小时（不足两位不补零）
     *                     I, 10               12小时制的小时（不足两位补零）
     *                     M, 58               分钟（不足两位补零）
     *                     S, 05               秒（不足两位补零）
     *                     L, 742              毫秒（不足三位补零）
     *                     N, 742000000        毫秒（不足9位补零）
     *                     p, 上午             小写字母的上午或下午标记，如中文为“下午”，英文为pm
     *                     z, +0800            相对于GMT的时区偏移量，如+0800
     *                     Z, CST              时区缩写，如CST
     *                     s, 1574737085       自1970-1-1 00:00:00起经过的秒数
     *                     Q, 1574737085743    自1970-1-1 00:00:00起经过的豪秒
     */
    public static void format() throws ParseException {
        //#####   rone#####
        System.out.println(String.format("#####%7s#####", "rone"));
        //#####rone   #####
        System.out.println(String.format("#####%-7s#####", "rone"));
        //#####007#####
        System.out.println(String.format("#####%03d#####", 7));
        //##### +7#####
        System.out.println(String.format("#####%+3d#####", 7));
        //##### -7#####
        System.out.println(String.format("#####% 3d#####", -7));
        //#####7,007#####
        System.out.println(String.format("#####%,3d#####", 7007));
        //#####(7)#####
        System.out.println(String.format("#####%(3d#####", -7));


        // 日期的格式化
        Date date = DateUtil.parse("2019-11-26 10:52:22.742", "yyyy-MM-dd HH:mm:ss.SSS");
        // 星期二 十一月 26 10:52:22 CST 2019
        System.out.println(String.format("%tc", date));
        // 2019-11-26
        System.out.println(String.format("%tF", date));
        // 11/26/19
        System.out.println(String.format("%tD", date));
        // 10:52:22 上午
        System.out.println(String.format("%tr", date));
        // 10:52:22
        System.out.println(String.format("%tT", date));
        // 10:52
        System.out.println(String.format("%tR", date));
        // 十一月
        System.out.println(String.format("%tb", date));
        // 十一月
        System.out.println(String.format("%tB", date));
        // 星期二
        System.out.println(String.format("%ta", date));
        // 星期二
        System.out.println(String.format("%tA", date));
        // 20
        System.out.println(String.format("%tC", date));
        // 19
        System.out.println(String.format("%ty", date));
        // 330
        System.out.println(String.format("%tj", date));
        // 11
        System.out.println(String.format("%tm", date));
        // 26
        System.out.println(String.format("%td", date));
        // 26
        System.out.println(String.format("%te", date));
        System.out.println();
        // 10
        System.out.println(String.format("%tH", date));
        // 10
        System.out.println(String.format("%tk", date));
        // 10
        System.out.println(String.format("%tI", date));
        // 52
        System.out.println(String.format("%tM", date));
        // 22
        System.out.println(String.format("%tS", date));
        // 742
        System.out.println(String.format("%tL", date));
        // 742000000
        System.out.println(String.format("%tN", date));
        // 上午
        System.out.println(String.format("%tp", date));
        // +0800
        System.out.println(String.format("%tz", date));
        // CST
        System.out.println(String.format("%tZ", date));
        // 1574736742
        System.out.println(String.format("%ts", date));
        // 1574736742742
        System.out.println(String.format("%tQ", date));
    }

    /**
     * 字符串拼接工具
     * @since jdk1.8
     */
    public static void stringJoiner() {
        StringJoiner sj = new StringJoiner(",");
        StringBuilder sb;
        StringBuffer sb2;
        sj.add("hello").add("world").add("!");
        // hello,world,!
        System.out.println(sj.toString());
        System.out.println();

        StringJoiner sj1 = new StringJoiner("|", "[", "]");
        sj1.add("hello").add("world").add("!");
        // [hello|world|!]
        System.out.println(sj1.toString());
        System.out.println();

        // fuck#the#world#!
        System.out.println(String.join("#", "fuck", "the", "world", "!"));
    }

    /**
     * String、StringBuffer、StringBuilder
     *     三者在频繁操作字符串时执行速度方面的比较：StringBuilder >  StringBuffer  >  String
     *         每当用String操作字符串时，实际上是在不断的创建新的对象，而原来的对象就会变为垃圾被ＧＣ回收掉
     *         而StringBuffer与StringBuilder就不一样了，他们是字符串变量，是可改变的对象，每当我们用它们对字符串做操作时，实际上是在一个对象上操作的
     *     StringBuilder与 StringBuffer
     *         StringBuilder：线程非安全的
     *         StringBuffer：线程安全的
     *     适用情况
     *         如果要操作少量的数据用 = String
     *         单线程操作字符串缓冲区 下操作大量数据 = StringBuilder
     *         多线程操作字符串缓冲区 下操作大量数据 = StringBuffer
     */
    public static void stringStringBuilderStringBuffer() {
        System.out.println(new String("hello world !"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello").append(" ").append("world").append(" ").append("!");
        System.out.println(stringBuilder.toString());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("hello").append(" ").append("world").append(" ").append("!");
        System.out.println(stringBuffer.toString());
    }

    /**
     * 数组转字符串
     */
    public static void arraysToString() {
        String[] strs = new String[3];
        strs[0] = "hello";
        strs[1] = "world";
        strs[2] = "!";
        System.out.println(StrUtil.join("", strs));
        // 将数组用逗号分隔变成字符串
        System.out.println(StrUtil.join(",", strs));
    }

    /**
     * 对字符串的分解、重组，StringTokenizer 用来对目标String进行分解
     */
    public static void stringTokenizerDemo() {
        // StringTokenizer三种构造方法
        // 1.StringTokenizer(String str)。默认以” \t\n\r\f”（前有一个空格，引号不是）为分割符。
        StringTokenizer st = new StringTokenizer("a  b  c    d");
        while (st.hasMoreTokens()) {
            // 输出：a*b*c*d*
            System.out.print(st.nextToken() + "*");
        }
        // 2.StringTokenizer(String str, String delim)。指定delim为分割符
        StringTokenizer st1 = new StringTokenizer("www.ooobj.com", ".b");
        while (st1.hasMoreTokens()) {
            // 输出：www*ooo*j*com*
            System.out.print(st1.nextToken() + ".");
        }
        // 3.StringTokenizer(String str, String delim, boolean returnDelims)。returnDelims为true的话则delim分割符也被视为标记。
        StringTokenizer st2 = new StringTokenizer("www.ooobj.com", ".", true);
        while (st2.hasMoreTokens()) {
            // 输出：www*.*ooobj*.*com*
            System.out.print(st2.nextToken() + ".");
        }
    }
}
