package org.rone.core.jdk.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存溢出
 * 调试时把分配的内存设置较小即可很快体现出来
 * @author rone
 */
public class OverflowDemo {

    /**
     * 栈深度溢出
     */
    public static void  testStackOverFlow(){
        OverflowDemo.testStackOverFlow();
    }

    /**
     * 永久代内存溢出
     * 用Class把老年代取堆满
     */
    public static void testPermGenOutOfMemory(){
        class Node {
            private Node next;

            public void setNext(Node next) {
                this.next = next;
            }
        }
        Node root = new Node();
        Node nowNode = new Node();
        root.setNext(nowNode);
        try {
            while (true) {
                Node node = new Node();
                nowNode.setNext(node);
                nowNode = node;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 本地方法栈溢出
     */
    public static void testNativeMethodOutOfMemory(){
        int j = 0;
        while(true){
            System.out.println(j++);
            for (int i = 0; i < 100; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("hello world!");
                    }
                }).start();
            }
        }
    }

    /**
     * JVM栈内存溢出
     */
    public static void testStackOutOfMemory(){
        while (true) {
            Thread thread = new Thread(() -> {
                while(true){
                }
            });
            thread.start();
        }
    }

    /**
     * 堆溢出
     */
    public static void testOutOfHeapMemory(){
        List<StringBuffer> list = new ArrayList<StringBuffer>();
        while(true){
            StringBuffer B = new StringBuffer();
            for(int i = 0 ; i < 10000 ; i++){
                B.append(i);
            }
            list.add(B);
        }
    }
}
