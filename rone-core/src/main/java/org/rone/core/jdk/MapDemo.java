package org.rone.core.jdk;

import org.rone.core.jdk.enumeration.RoneEnum;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Map数据结构
 * @author rone
 */
public class MapDemo {

    public static void main(String[] args) {
        // demo();
        enumMapDemo();
    }

    public static void demo() {
       // initialCapacity要大于0，不然会报错
        Map<String, String> map = new HashMap<>(3);
        map.put("1", "rone");
        map.put("2", "fuck");
        map.put("3", "snow");
        // 遍历 键、值
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        // 在for-each循环中遍历keys或values
        //遍历map中的键
        for (String key : map.keySet()) {
            System.out.println("Key = " + key);
        }
        //遍历map中的值
        for (String value : map.values()) {
            System.out.println("Value = " + value);
        }
        // 使用Iterator遍历
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        // 通过键找值遍历（效率低）
        for (String key : map.keySet()) {
            String value = map.get(key);
            System.out.println("Key = " + key + ", Value = " + value);
        }

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
