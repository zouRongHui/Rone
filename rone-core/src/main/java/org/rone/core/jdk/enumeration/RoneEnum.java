package org.rone.core.jdk.enumeration;

/**
 * 枚举类
 * 一般来说，枚举的属性都是固定的不需要也不应该通过set方法去修改属性值
 * @author rone
 */
public enum RoneEnum {
    SUCCESS(200, "成功"),
    NOT_FOUND(404, "资源未找到"),
    ERROR(500, "错误");

    private Integer code;
    private String message;

    RoneEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void test() {
        System.out.println("枚举类的正常方法.");
    }
}
