package org.rone.core.dataStructure.linked;

import java.util.Random;

/**
 * 跳表，通过额外的维护一个或多个索引链表来提升有序链表的查询效率，相应的会有维护索引链表的开销
 * 提高有序链表中查询的效率
 * 解决方案：
 *  在有序链表之上再创建一层or多层链表用于当做主链表的索引。
 *  索引链表从主链表中跳跃的取节点存储，当有多层索引链表时第二层索引链表基于第一层索引链表跳跃取节点存储。
 *  在做查询时，从最上层的索引链表依次查询，当发现节点大于查询值时则往下层链表查询，直到到达最下面的主链表。
 *  插入节点时，首先在主链表上完成插入操作。
 *      然后根据配置的晋升概率判断其是否需要晋升为索引，若无需晋升则插入操作完成，若需要则在上一层的索引链表中也插入该节点。
 *      若有多层索引链表，在完成主链表的插入操作后，依次往上层的索引节点中随机晋升，直到无需晋升。
 *  删除节点，首先在主链表上完成删除操作。
 *      然后依次在索引节点中删除存在的节点。
 *  实现：
 *      节点上下左右都有指针
 * @author rone
 */
public class SkipLinked {

    /** 结点“晋升”的概率 */
    private static final double PROMOTE_RATE = 0.5;
    /** 最上层索引链表的头结点 */
    private Node head;
    /** 最上层索引链表的尾结点 */
    private Node tail;
    private int maxLevel;

    public SkipLinked() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.right = tail;
        tail.left = head;
    }

    public static void main(String[] args) {
        SkipLinked list=new SkipLinked();
        list.insert(50);
        list.insert(15);
        list.insert(13);
        list.insert(20);
        list.insert(100);
        list.insert(75);
        list.insert(99);
        list.insert(76);
        list.insert(83);
        list.insert(65);
        list.printList();
        list.search(50);
        list.remove(50);
        list.search(50);
    }

    /**
     * 查找结点
     */
    public Node search(int data){
        Node p = findNode(data);
        if (p.data == data) {
            System.out.println("找到结点：" + data);
            return p;
        }
        System.out.println("未找到结点：" + data);
        return null;
    }

    /**
     * 找到值对应的前置结点
     */
    private Node findNode(int data){
        Node node = head;
        while(true){
            while (node.right.data != Integer.MAX_VALUE && node.right.data <= data) {
                node = node.right;
            }
            if (node.down == null) {
                break;
            }
            node = node.down;
        }
        return node;
    }

    /**
     * 插入结点
     */
    public void insert(int data){
        Node preNode = findNode(data);
        //如果data相同，直接返回
        if (preNode.data == data) {
            return;
        }
        Node node = new Node(data);
        appendNode(preNode, node);
        int currentLevel = 0;
        //随机决定结点是否“晋升”
        Random random = new Random();
        while (random.nextDouble() < PROMOTE_RATE) {
            //如果当前层已经是最高层，需要增加一层
            if (currentLevel == maxLevel) {
                addLevel();
            }
            //找到上一层的前置节点，（若是在最上一层的头结点的话，会不会出现bug）
            while (preNode.up == null) {
                preNode = preNode.left;
            }
            preNode = preNode.up;
            //把“晋升”的新结点插入到上一层
            Node upperNode = new Node(data);
            appendNode(preNode, upperNode);
            upperNode.down = node;
            node.up = upperNode;
            node = upperNode;
            currentLevel++;
        }
    }

    /**
     * 在前置结点后面添加新结点
     */
    private void appendNode(Node preNode, Node newNode){
        newNode.left = preNode;
        newNode.right = preNode.right;
        preNode.right.left = newNode;
        preNode.right = newNode;
    }

    /**
     * 增加一层索引链表
     */
    private void addLevel(){
        maxLevel++;
        Node p1 = new Node(Integer.MIN_VALUE);
        Node p2 = new Node(Integer.MAX_VALUE);
        p1.right = p2;
        p2.left = p1;
        p1.down = head;
        head.up = p1;
        p2.down = tail;
        tail.up = p2;
        head = p1;
        tail = p2;
    }

    /**
     * 删除结点
     */
    public boolean remove(int data){
        Node removedNode = search(data);
        if (removedNode == null){
            return false;
        }

        int currentLevel = 0;
        while (removedNode != null){
            removedNode.right.left = removedNode.left;
            removedNode.left.right = removedNode.right;
            //如果不是最底层，且只有无穷小和无穷大结点，删除该层
            if (currentLevel != 0 && removedNode.left.data == Integer.MIN_VALUE && removedNode.right.data == Integer.MAX_VALUE){
                removeLevel(removedNode.left);
            } else {
                currentLevel ++;
            }
            removedNode = removedNode.up;
        }

        return true;
    }

    /**
     * 删除一层索引链表
     */
    private void removeLevel(Node leftNode){
        Node rightNode = leftNode.right;
        //如果删除层是最高层
        if (leftNode.up == null){
            leftNode.down.up = null;
            rightNode.down.up = null;
        }else {
            leftNode.up.down = leftNode.down;
            leftNode.down.up = leftNode.up;
            rightNode.up.down = rightNode.down;
            rightNode.down.up = rightNode.up;
        }
        maxLevel --;
    }

    /**
     * 输出底层链表
     */
    public void printList() {
        Node node = head;
        while (node.down != null) {
            node = node.down;
        }
        while (node.right.data != Integer.MAX_VALUE) {
            System.out.print(node.right.data + " ");
            node = node.right;
        }
        System.out.println();
    }

    /**
     * 链表节点类
     */
    public static class Node {
        public int data;
        //跳表结点的前后和上下都有指针
        public Node up, down, left, right;

        public Node(int data) {
            this.data = data;
        }
    }
}
