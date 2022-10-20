package org.rone.core.jdk.enumeration;

/**
 * 枚举类
 * ●.枚举实际上也是一个类，但是枚举的实例是在代码中写死的，无法手动创建新的实例，和 static final 异曲同工，但更为丰富
 * ●.用枚举是实现单例模式是一个很不错的选择
 * @author rone
 */
public class EnumDemo {

    public static void main(String[] args) {
        RoneEnum.SUCCESS.test();
        RoneEnum.NOT_FOUND.test();
        for (RoneEnum r : RoneEnum.values()) {
            System.out.println("code: " + r.getCode() + ", message: " + r.getMessage());
            r.test();
        }
    }
}
