package org.rone.core.jdk;

/**
 * 枚举类
 * ●.枚举实际上也是一个类，但是枚举的实例是在代码中写死的，无法手动创建新的实例，和 static final 异曲同工，但更为丰富
 * ●.用枚举是实现单例模式是一个很不错的选择
 * @author rone
 */
public class EnumDemo {

    public static void main(String[] args) {
        Color.RED.test();
        Color.BLACK.test();
        for (Color c : Color.values()) {
            System.out.println("name: " + c.getName() + ", message: " + c.getMessage());
            c.test();
        }
    }

    /**
     * 一般来说，枚举的属性都是固定的不需要也不应该通过set方法去修改属性值
     */
    enum Color {
        //在首行就必须列出所有的可能实例
        RED("red", "like fire"),
        BLACK("black", "like death");

        private String name;
        private String message;

        Color(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }
        public String getMessage() {
            return message;
        }
        public void test() {
            System.out.println("枚举类的正常方法.");
        }
    }
}
