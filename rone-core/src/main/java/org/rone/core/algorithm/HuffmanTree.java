package org.rone.core.algorithm;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 哈夫曼树的java实现
 * 哈夫曼树：在叶子结点和权重确定的情况下，树的带权路径长度最小的二叉树，也被称为最优二叉树。
 * 树的带权路径长度：所有叶子结点的带权路径长度之和，简称为WPL。
 * 结点的带权路径长度：树的根结点到该结点的路径长度和该结点权重的乘积。
 *
 * 实现思路：
 * 原则上，我们应该让权重小的叶子结点远离根节点，权重大的叶子结点靠近根节点。
 * 1.将所有叶子节点按照权重升序
 * 2.将权重最小的两个节点组合一起形成一个新的节点，将这两个节点排除在下一次排序中并将新组合成的节点放置下一次排序中。这里使用优先队列PriorityQueue来辅助实现排序节点的移除、新增、再排序
 * 3.循环第二步，知道需要排序的节点只剩一个，此时剩余的那个节点就是根节点。
 *
 * 应用：
 * 哈夫曼编码，用于压缩信息
 * @author rone
 */
public class HuffmanTree {

    /** 最终的根节点 */
    private Node root;

    public static void main(String[] args) {
        int[] weights = {2, 3, 7, 9, 18, 25};
        HuffmanTree huffmanTree = new HuffmanTree();
        huffmanTree.createHuffman(weights);
        System.out.println(huffmanTree.root);
    }

    /**
     * 创建哈夫曼树
     */
    public void createHuffman(int[] weights) {
        //优先队列
        Queue<Node> nodeQueue = new PriorityQueue<>();

        //将叶节点添加到有限队列中
        for (int weight : weights) {
            nodeQueue.add(new Node(weight));
        }

        //循环处理，将最小的两个节点组合到一起，直至只剩一个节点即根节点
        while (nodeQueue.size() > 1) {
            Node left = nodeQueue.poll();
            Node right = nodeQueue.poll();
            Node parent = new Node(left.weight + right.weight, left, right);
            nodeQueue.add(parent);
        }
        root = nodeQueue.poll();
    }

    private static class Node implements Comparable<Node> {
        int weight;
        Node lChild;
        Node rChild;

        public Node(int weight) {
            this.weight = weight;
        }

        public Node(int weight, Node lChild, Node rChild) {
            this.weight = weight;
            this.lChild = lChild;
            this.rChild = rChild;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.weight, o.weight);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "weight=" + weight +
                    ", lChild=" + lChild +
                    ", rChild=" + rChild +
                    '}';
        }
    }
}
