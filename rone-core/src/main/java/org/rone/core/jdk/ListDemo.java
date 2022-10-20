package org.rone.core.jdk;

import cn.hutool.core.collection.ListUtil;

import java.util.*;

/**
 * List
 * @author rone
 */
public class ListDemo {

    public static void main(String[] args) {
        // copy();
        // removeItem();
        partition();
    }

    /**
     * List 的复制
     */
    public static void copy() {
        List<String> list = new ArrayList<>(2);
        list.add("a");
        list.add("b");
        // planA:
        List<String> copyList = new ArrayList(Arrays.asList(new String[list.size()]));
        Collections.copy(copyList, list);
        for (String s : copyList) {
            System.out.println(s);
        }
        // planB:
        List<String> copyList2 = (List<String>) ((ArrayList<String>) list).clone();
        for (String s : copyList2) {
            System.out.println(s);
        }
    }

    /**
     * 在循环中对 ArrayList 的删除
     * 增加同理
     */
    public static void removeItem() {
        List<String> list = new ArrayList<>(2);
        list.add("a");
        list.add("b");

        /*
         * 按照如下的写法会抛出 java.util.ConcurrentModificationException 异常
         * 具体查看 ArrayList 源码 java.util.ArrayList.Itr.next()/hasNext() 方法
         * list1在第二次循环的时候移除了一个元素，size也就变成了1.
         * 而在第三次判断调用的 hasNext() size为1 cursor为2 导致后续调用next()方法，
         * 而next()方法中调用了 checkForComodification() remove() 方法会是的modCount++ 而expectedModCount 不变
         * 导致 modCount != expectedModCount 判断没通过抛出异常
         */
        List<String> list1 = (List<String>) ((ArrayList<String>) list).clone();
        try {
            for (String s : list1) {
                System.out.println("list1: " + s);
                if("b".equals(s)){
                    list1.remove(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
        System.out.println("list1 size: " + list1.size());
        System.out.println();

        /*
         * list2没有抛出异常因为
         * 第一次循环移除了一个元素，size变成了1,，
         * 第二次判断的时候调用 hasNext() 时 cursor为1 size也是1，认为遍历结束了，就没有调用 next() 方法而触发异常
         */
        List<String> list2 = (List<String>) ((ArrayList<String>) list).clone();
        for (String s : list2) {
            System.out.println("list2: " + s);
            if("a".equals(s)){
                list2.remove(s);
            }
        }
        System.out.println("list2 size: " + list2.size());
        System.out.println();

        // 推荐使用迭代器Iterator删除元素
        List<String> list3 = (List<String>) ((ArrayList<String>) list).clone();
        Iterator<String> ite = list3.iterator();
        while(ite.hasNext()) {
            String s = ite.next();
            System.out.println("list3: " + s);
            if("b".equals(s)) {
                ite.remove();
            }
        }
        System.out.println("list3 size: " + list3.size());
    }

    /**
     * 对list进行分割
     */
    public static void partition() {
        List<String> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }
        List<List<String>> childList = ListUtil.partition(list, 10);
        for (List<String> strs : childList) {
            StringJoiner sj = new StringJoiner("、", "子集：", "");
            for (String str : strs) {
                sj.add(str);
            }
            System.out.println(sj.toString());
        }
    }
}
