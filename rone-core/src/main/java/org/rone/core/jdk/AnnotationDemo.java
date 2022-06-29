package org.rone.core.jdk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 自定义注解
 * ●.使用注解时，在传参时没有指定属性会默认给该注解的value属性赋值
 *     @RequestMapping("/test") 等价于 @RequestMapping(value = "/test")
 * @author rone
 */
public class AnnotationDemo {

    public static void main(String[] args) throws NoSuchMethodException {
        AnnotationDemo annotation = new AnnotationDemo();
        Method method = annotation.getClass().getMethod("test", (Class<?>) null);
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            System.out.println(method.getAnnotation(MyAnnotation.class));
            Arrays.stream(method.getAnnotation(MyAnnotation.class).value()).forEach(System.out::println);
            System.out.println(method.getAnnotation(MyAnnotation.class).name());
        }
    }

    @MyAnnotation({"fuck", "the", "world"})
    public void test() {}

    /**
     * 用来说明该注解使用的地方
     *  ElementType.TYPE：说明该注解只能被声明在一个类前。
     *  ElementType.FIELD：说明该注解只能被声明在一个类的字段前。
     *  ElementType.METHOD：说明该注解只能被声明在一个类的方法前。
     *  ElementType.PARAMETER：说明该注解只能被声明在一个方法参数前。
     *  ElementType.CONSTRUCTOR：说明该注解只能声明在一个类的构造方法前。
     *  ElementType.LOCAL_VARIABLE：说明该注解只能声明在一个局部变量前。
     *  ElementType.ANNOTATION_TYPE：说明该注解只能声明在一个注解类型前。
     *  ElementType.PACKAGE：说明该注解只能声明在一个包名前。
     */
    @Target(ElementType.METHOD)
    /**
     * 用来说明该注解类的生命周期,这种类型的注解会被保留到哪个阶段. 有三个值
     *  1. RetentionPolicy.RUNTIME----Annotations注释被JVM保留,能在运行时被JVM或其他使用反射机制的代码所读取和使用.
     *  2. RetentionPolicy.SOURCE----注释将被编译器丢弃。
     *  3. RetentionPolicy.CLASS-----注释将由编译器在类文件中记录,但在运行时不需要由JVM保留。这是默认的的行为。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        String[] value();
        String name() default "test";
    }
}
