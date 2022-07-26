package org.rone.core.jdk.jdk8;

import java.util.Optional;

/**
 * jdk8 针对NPE的工具类
 * 这是一个可以包含或者不包含非 null 值的容器。如果值存在则 isPresent()方法会返回 true，调用 get() 方法会返回该对象。
 * @author rone
 */
public class OptionalDemo {

    public static void main(String[] args) throws Throwable {
        Object object = new Object();
        //通过一个非 null 的 value 来构造一个 Optional
        Optional optional = Optional.of(object);
        object = null;
        //传入的参数可以为 null
        optional = Optional.ofNullable(object);
        //构造一个空的 Optional
        Optional.empty();
        //获取value值
        optional.get();
        //不为null返回true
        optional.isPresent();
        //如果 Optional 中有值，则对该值调用 consumer.accept，否则什么也不做
        optional.ifPresent(item -> System.out.println(item.toString()));
        //如果 Optional 中有值则将其返回，否则返回 orElse 方法传入的参数
        optional.orElse(new Object());
        //当 Optional 中有值的时候，返回值；当 Optional 中没有值的时候，返回从该 Supplier 获得的值
        optional.orElseGet(() -> new Object());
        //当 Optional 中有值的时候，返回值；没有值的时候会抛出异常，抛出的异常由传入的 exceptionSupplier 提供
        optional.orElseThrow(() -> new Exception("空值"));
        //当前 Optional 为 Optional.empty，则依旧返回 Optional.empty；否则返回一个新的 Optional，该 Optional 包含的是：函数 mapper 在以 value 作为输入时的输出值
        optional.map(item -> item.toString());
        //相比map()方法，flatMap 要求参数中的函数 mapper 输出的就是 Optional
        optional.flatMap(item -> Optional.ofNullable(item.toString()));
        //接受一个 Predicate 来对 Optional 中包含的值进行过滤，如果包含的值满足条件，那么还是返回这个 Optional；否则返回 Optional.empty
        optional.filter(item -> item.toString().length() > 10);
    }
}
