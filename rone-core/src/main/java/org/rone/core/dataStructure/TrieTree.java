package org.rone.core.dataStructure;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 前缀树/字典树
 * ●.Trie树，又叫前缀树、字典树。
 *     利用字符串前缀来查找指定的字符串，缩短大数据量字符串查找时间，主要用于字符串的快速查找和匹配。
 * ●.特性
 *     根节点不包含字符，除根节点外每一个节点都只包含一个字符。
 *     从根节点到某一节点，路径上经过的字符连接起来，为该节点对应的字符串。
 *     每个节点的所有子节点包含的字符都不相同。
 * @author rone
 */
public class TrieTree {

    public static void main(String[] args) {
        findRepeatString(new String[]{"fuck", "the", "world", "and", "hello", "world", "rone", "world"});
    }

    /**
     * 100000个限长的单词，判断有没有出现过，如果出现过打印出首次出现的位置
     */
    public static void findRepeatString(String[] strs) {
        Node root = new Node();
        for (int i = 0; i < strs.length; i++) {
            char[] chars = strs[i].toCharArray();
            Node nextNode = root;
            Node tempNode;
            for (char c : chars) {
                tempNode = nextNode.findNextNode(c);
                if (tempNode == null) {
                    tempNode = new Node();
                    nextNode.putNextNode(c, tempNode);
                }
                nextNode = tempNode;
            }
            if (StrUtil.isNotEmpty(nextNode.data)) {
                System.out.println(strs[i] + " 第一次出现在 " + nextNode.data + "号位置");
            } else {
                nextNode.data = String.valueOf(i+1);
            }
        }
    }

    static class Node {
        private final Map<Character, Node> nextNodes;
        private String data;

        public Node() {
            this.nextNodes = new HashMap<>(26);
        }

        public Node findNextNode(Character key) {
            return this.nextNodes.get(key);
        }

        public void putNextNode(Character key, Node node) {
            this.nextNodes.put(key, node);
        }
    }
}
