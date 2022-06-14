package org.rone.core.dataStructure.linked;

/**
 * 链表
 * 判断单向链表是否有环、环长、入环节点
 * @author rone
 */
public class LinkedLoop {

    public static void main(String[] args) {
        Note head = createHasLoopLinked();
        hasLoop(head);
        loopLength(head);
        loopStartNote(head);
    }

    /**
     * 创建有环的链表
     */
    private static Note createHasLoopLinked() {
        Note head = new Note("1");
        Note nowNote = head;
        Note loopStartNote = null;
        for (int i = 2; i <= 11; i++) {
            Note note = new Note(String.valueOf(i));
            nowNote.next = note;
            nowNote = note;
            if (i == 9) {
                loopStartNote = note;
            }
        }
        nowNote.next = loopStartNote;
        return head;
    }

    /**
     * 单向链表是否有环
     * 实现1：
     *  遍历链表，额外用一个集合来存储遍历的情况，每次遍历时检查是否之前遇到过，若在链表结束前发现某个元素遇到过则说明有环。
     *  时间复杂度：O(n)，空间复杂度：O(n)
     * 实现2：
     *  遍历链表，每遍历到一个节点，则重头遍历链表到当前节点检查是否有重复，重复则有环。
     *  时间节点：O(n^2)，空间复杂度：O(1)
     * 实现3：效率高于实现1和实现2，代码的实现，采用此方法
     *  快慢追击法(快慢指针)，两个指针一个每次前进一步，另一个每次前进两步，如果在某个时间点他们相遇则说明链表有环。
     *  可对应为数学上的追击问题，只有在环形跑道上一块一慢的两人才会相遇，直线跑道不会相遇。
     *  时间复杂度：O(n)，空间复杂度：O(1)
     * @param head 链表头结点
     * @return true:有环;false:无环
     */
    private static boolean hasLoop(Note head) {
        Note fast = head;
        Note slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                System.out.printf("存在环，首次相遇点为 %s%n", slow);
                return true;
            }
        }
        System.out.println("链表无环");
        return false;
    }

    /**
     * 单向链表有环时，环长
     * 实现1：
     *  在快慢追击法(快慢指针)下找到了首次相遇的节点，某个指针从该节点再次出发当再一次到达首次相遇点的时候，此时走过的长度即为环长。
     * 实现2：该实现比实现1效率稍高一些
     *  在快慢追击法(快慢指针)下，快慢的两个指针首次相遇后继续出发，此时的情况就像是在环形跑道上一块一慢的两人跑步，
     *  当两个人再次相遇的时候(此时慢节点肯定还未走完一圈，这就是比实现1效率高的地方)，此时快指针比慢指针多走一圈。快指针走过的长度比慢指针大N个环长，N为快指针步长 - 慢指针步长 = 1。
     *  所以，环长为首次相遇快指针步数 - 慢指针步数
     * @param head 链表头结点
     * @return 环长，无环时返回0
     */
    private static int loopLength(Note head) {
        Note fast = head;
        Note slow = head;
        int loopLength = 0;
        int meetCount = 0;
        while (meetCount < 2 && fast.next != null && fast.next.next != null) {
            if (meetCount > 0) {
                loopLength++;
            }
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                meetCount++;
            }
        }
        System.out.printf("环长为 %d%n", loopLength);
        return loopLength;
    }

    /**
     * 单向链表有环时，求入环节点
     * 实现：
     *  在快慢追击法(快慢指针)下，设头结点到入环点长度为 L ，入环点到首次相遇点为 S1，剩余的环长为 S2。
     *  则首次相遇时，慢指针走了 L+S1 ，快指针走了 L+S1+n(S1+S2)。而快指针步长为2，慢指针步长为1，
     *  故2(L+S1) = l+S1+n(S1+S2) 变形为 L = (n-1)(S1+S2)+S2。
     *  等式可以理解为头结点到入环点的距离=n-1圈环长 + 首次相遇点继续前进到入环点的距离，
     *  所以解法为A指针从头结点出发，B指针从首次相遇点出发，两者每次都前进一步，两者相遇的点即为入环点
     * @param head 链表头结点
     * @return 入环节点
     */
    private static Note loopStartNote(Note head) {
        Note fast = head;
        Note slow = head;
        Note note2 = null;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                note2 = slow;
                break;
            }
        }
        if (note2 == null) {
            System.out.println("无环，故无入环点");
            return null;
        }
        Note note1 = head;
        while (note2 != note1) {
            note2 = note2.next;
            note1 = note1.next;
        }

        System.out.printf("入环点为 %s%n", note1.toString());
        return note1;
    }

    /**
     * 单向节点
     */
    static class Note {
        private String data;
        private Note next;

        public Note(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Note{" +
                    "data='" + data + '\'' +
                    '}';
        }
    }
}
