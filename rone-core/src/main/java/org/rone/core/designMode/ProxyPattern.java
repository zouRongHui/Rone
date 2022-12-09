package org.rone.core.designMode;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
        System.out.println("###############################################################################");

        // 动态代理
        Demo demo1 = new DemoImpl();
        DemoInvocationHandler handler = new DemoInvocationHandler(demo1);
        Demo proxy = (Demo) handler.getProxy();
        System.out.println("动态代理执行结果：" + proxy.test("动态代理", 333));
        System.out.println("###############################################################################");

        CglibProxy cglibProxy = new CglibProxy();
        //通过生成子类的方式创建代理类
        DemoImpl proxyImp = (DemoImpl)cglibProxy.getProxy(DemoImpl.class);
        System.out.println("cglib代理执行结果：" + proxyImp.test("cglib代理", 444));
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

    private static class DemoProxy {

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

    /**
     * JDK 动态代理：java.lang.reflect 包中的 Proxy 类
     *      和 InvocationHandler 接口提供了生成动态代理类的能力。
     * JDK 的动态代理有一个限制，就是使用动态代理的对象必须实现一个或多个接口。
     */
    private static class DemoInvocationHandler implements InvocationHandler {
        private Object target;

        public DemoInvocationHandler(Object target) {
            super();
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("-----------------begin " + method.getName() + "-----------------");
            Object result = method.invoke(target, args);
            System.out.println("-----------------end " + method.getName() + "-----------------");
            return result;
        }

        public Object getProxy() {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
        }
    }

    /**
     * Cglib 动态代理：Cglib (Code Generation Library )是一个第三方代码生成类库，
     *      运行时在内存中动态生成一个子类对象从而实现对目标对象功能的扩展。
     * 使用 cglib 代理的对象则无需实现接口，达到代理类无侵入。
     */
    private static class CglibProxy implements MethodInterceptor {
        private Enhancer enhancer = new Enhancer();

        public Object getProxy(Class clazz) {
            //设置需要创建子类的类
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(this);
            //通过字节码技术动态创建子类实例
            return enhancer.create();
        }

        //实现 MethodInterceptor 接口方法
        public Object intercept(Object obj, Method method, Object[] args,
                                MethodProxy proxy) throws Throwable {
            System.out.println("前置代理");
            //通过代理类调用父类中的方法
            Object result = proxy.invokeSuper(obj, args);
            System.out.println("后置代理");
            return result;
        }
    }

}
