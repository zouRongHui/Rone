package org.rone.core.jdk;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author rone
 */
public class StringDemo {

    public static void main(String[] args) throws Exception {
        randomString();
        // format();
    }

    /**
     * 随机字符串
     */
    public static void randomString() {
        System.out.println("32位随机字符串：" + RandomUtil.randomString(32));
    }

    /**
     * String拆分成数组
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
    }

    /**
     * 格式化
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
}
