package org.rone.core.jdk;

import org.rone.core.jdk.enumeration.RoneEnum;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Map数据结构
 * @author rone
 */
public class MapDemo {

    public static void main(String[] args) {
        enumMapDemo();
    }

    /**
     * EnumMap的使用
     *  key值只能使用枚举值，能很好的掌控key的数量以及范围。
     *  底层是使用数组，效率上回避HashMap高，但枚举的数据量不大，就算使用HashMap性能影像忽略不计。
     *  无序
     * 相同的还有 EnumSet
     */
    public static void enumMapDemo() {
        EnumMap map = new EnumMap(RoneEnum.class);
        map.put(RoneEnum.ERROR, "3");
        map.put(RoneEnum.NOT_FOUND, "2");
        map.put(RoneEnum.SUCCESS, "1");
        map.forEach((key, value) -> System.out.println(key + " : " + value));


        Map<RoneEnum, String> hashMap = new HashMap(8);
        hashMap.put(RoneEnum.ERROR, "3");
        hashMap.put(RoneEnum.NOT_FOUND, "2");
        hashMap.put(RoneEnum.SUCCESS, "1");
        hashMap.forEach((key, value) -> System.out.println(key + " : " + value));
    }
}
