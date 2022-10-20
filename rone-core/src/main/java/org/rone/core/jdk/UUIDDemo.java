package org.rone.core.jdk;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

/**
 * UUID在Java中的实现
 * jdk支持版本4和版本3的UUID
 * com.fasterxml.uuid 支持版本1的UUID
 * @author rone
 */
public class UUIDDemo {

    public static void main(String[] args) {
        type1();
        type3();
        type4();
    }

    /**
     * 基于伪随机数
     * 根据随机数/伪随机数生成，重复的概率可计算。
     */
    private static void type4() {
        UUID uuid4 = UUID.randomUUID();
        System.out.println("UUID:" + uuid4 + " 版本 " + uuid4.version());
    }

    /**
     * 基于名字(MD5)
     * 基于名字的UUID通过计算名字和名字空间的MD5散列值得到。
     * 相同名字空间中不同名字生成的UUID唯一;
     * 不同名字空间中UUID唯一;
     * 相同名字空间中相同名字UUID重复。
     */
    private static void type3() {
        UUID uuid3 = UUID.nameUUIDFromBytes("test".getBytes());
        System.out.println("UUID:" + uuid3 + " 版本 " + uuid3.version());
    }

    /**
     * 基于时间
     * 计算当前时间戳、随机数和机器MAC地址得到。除非同一机器并发肯会重复，其余全球唯一。
     */
    private static void type1() {
        UUID uuid1 = Generators.timeBasedGenerator().generate();
        System.out.println("UUID:" + uuid1 + " 版本 " + uuid1.version());
    }
}
