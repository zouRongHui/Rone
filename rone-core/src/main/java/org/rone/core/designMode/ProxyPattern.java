package org.rone.core.designMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 代理模式
 * ●.定义：就是将部分逻辑操作代理给其他类来做
 * ●.使用场景：日志的记录、spring的aop
 * @author rone
 */
public class ProxyPattern {

    public static void main(String[] args) {
        Demo demo = new DemoImpl();
        demo = new DemoProxy(demo).getDemo();
        String result = demo.test("rone", 123);
        System.out.println("Result: " + result);
    }

    interface Demo {
        String test(String str, Integer num);
    }

    static class DemoImpl implements Demo {
        @Override
        public String test(String str, Integer num) {
            System.out.println("Demo test..");
            return str + num;
        }
    }

    static class DemoProxy {

        private final Demo demo;

        public DemoProxy(Demo demo) {
            this.demo = demo;
        }

        public Demo getDemo() {
            Demo proxy;

            ClassLoader loader = demo.getClass().getClassLoader();
            Class<?>[] interfaces = new Class[] {Demo.class};
            InvocationHandler h = (proxy1, method, args) -> {
                String methodName = method.getName();
                //打印日志
                System.out.println("[before] The method " + methodName + " begins with " + Arrays.asList(args));

                //调用目标方法
                Object result = null;

                try {
                    //前置通知
                    result = method.invoke(demo, args);
                    //返回通知, 可以访问到方法的返回值
                } catch (Exception e) {
                    e.printStackTrace();
                    //异常通知, 可以访问到方法出现的异常
                }

                //后置通知. 因为方法可以能会出异常, 所以访问不到方法的返回值

                //打印日志
                System.out.println("[after] The method ends with " + result);

                return result;
            };

            proxy = (Demo) Proxy.newProxyInstance(loader, interfaces, h);

            return proxy;
        }
    }

}
