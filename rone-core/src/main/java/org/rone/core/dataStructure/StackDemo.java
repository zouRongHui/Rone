package org.rone.core.dataStructure;

import java.util.Stack;

/**
 * 栈的相关内容
 * @author rone
 */
public class StackDemo {

    public static void main(String[] args) {
        MiniStack miniStack = new MiniStack();
        miniStack.push(4);
        miniStack.push(9);
        miniStack.push(7);
        miniStack.push(3);
        miniStack.push(8);
        miniStack.push(5);
        System.out.println(miniStack.mini());
        miniStack.pop();
        miniStack.pop();
        miniStack.pop();
        miniStack.pop();
        System.out.println(miniStack.mini());


        StackImplQueue<Integer> queue = new StackImplQueue<Integer>();
        queue.offer(1);
        queue.offer(2);
        System.out.println(queue.poll());
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue.poll());
        queue.offer(6);
        System.out.println(queue.poll());
        queue.offer(7);
        queue.offer(8);
        queue.offer(9);
        queue.offer(10);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println("输出全部");
        while (queue.peek() != null) {
            System.out.println(queue.poll());
        }
    }

    /**
     * 最小栈
     * 出栈、入栈、获取最小元素 三个方法的时间复杂度都是 O(1)
     * 实现：
     *  额外创建一个最小值得栈，
     *  当入栈时：入栈值和最小栈栈顶的值比较，小于则压入最小栈；
     *  当出栈时：出栈的值和最小栈栈顶的值比较，相等则最小栈也出栈；
     *  获取最小值时，直接获取最小栈栈顶的值即可；
     */
    static class MiniStack {

        private final Stack<Integer> mainStack = new Stack<>();
        private final Stack<Integer> miniStack = new Stack<>();

        public void push(Integer i) {
            mainStack.push(i);
            if (miniStack.empty() || i < miniStack.peek()) {
                miniStack.push(i);
            }
        }

        public Integer pop() {
            if (mainStack.peek().equals(miniStack.peek())) {
                miniStack.pop();
            }
            return mainStack.pop();
        }

        public int mini() {
            return miniStack.peek();
        }
    }

    /**
     * 用栈来实现一个队列
     * 实现：
     *  用两个栈来实现，一个管入队一个管出队。
     *  当第一次需要出队的时候，将入队的栈的数据出栈压入出队的栈，后续出队的时候判断出队栈是否为空，
     *      为空的话再一次将入堆栈出栈压入到出队栈。
     * @author Rone
     */
    private static class StackImplQueue<T> {
        /** 入队栈 */
        private final Stack<T> offerStack;
        /** 出队栈 */
        private final Stack<T> pollStack;

        public StackImplQueue() {
            offerStack = new Stack<>();
            pollStack = new Stack<>();
        }

        /**
         * 入队
         */
        public boolean offer(T t) {
            offerStack.push(t);
            return true;
        }

        /**
         * 出队
         */
        public T poll() {
            if (pollStack.isEmpty()) {
                if (offerStack.isEmpty()) {
                    return null;
                } else {
                    transferDataFromOfferStackToPollStack();
                }
            }
            return pollStack.pop();
        }

        /**
         * 查看队首数据
         */
        public T peek() {
            if (pollStack.isEmpty()) {
                if (offerStack.isEmpty()) {
                    return null;
                } else {
                    transferDataFromOfferStackToPollStack();
                }
            }
            return pollStack.peek();
        }

        /**
         * 将入堆栈的数据压人出对栈中
         */
        private void transferDataFromOfferStackToPollStack() {
            while (!offerStack.isEmpty()) {
                pollStack.push(offerStack.pop());
            }
        }
    }
}
