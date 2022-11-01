package org.rone.core.jdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射机制，在程序运行时(动态)，通过资源文件、外部输入等方式灵活的去对类进行使用。
 * @author rone
 */
public class ReflectionDemo {

    public static void main(String[] args) throws Exception {
        test0();
        test1();
        test2();
        test3();
        test4();
    }

    public static void test0() throws Exception {
        //三种获取Class对象的方式
        //静态class属性
        Class<?> clazz = Demo.class;
        //Object类的getClass()方法
        Class<?> clazz2 = (new Demo("", 0)).getClass();
        //Class类的静态方法forName(全类名)
        Class<?> clazz3 = Class.forName("Demo");
        System.out.println(clazz.getName());
        System.out.println(clazz2.getName());
        System.out.println(clazz3.getName());
		/*
		全参构造器,name:,age:0
		Demo
		Demo
		Demo
		 */
    }

    public static void test1() throws Exception {
        Class<?> clazz = Demo.class;
        //通过Class来实例化一个对象
        //实例化的对象必须要有默认无参的构造器，否则出错
        Demo demo = (Demo) clazz.newInstance();
        demo.setName("rone");
        demo.setAge(110);
        System.out.println(demo.toString());
		/*
		反射机制实例化对象的构造器
		Demo [name=rone, age=110]
		 */
    }

    public static void test2() throws Exception {
        Class<?> clazz = Demo.class;
        //获取并调用构造器
        //获取所有public构造方法
        Constructor<?>[] cons = clazz.getConstructors();
        //获取所有构造方法
        cons = clazz.getDeclaredConstructors();
        for(Constructor<?> c : cons){
            System.out.println("cons: " + c);
        }
        //获取单个public构造器，参数为构造器参数的类class，要获取无参的构造器时无需参数
        Constructor<?> con = clazz.getConstructor();
        //获取单个构造器
        con = clazz.getDeclaredConstructor(String.class, Integer.class);
        System.out.println("con: " + con);
        //暴力访问(忽略掉访问修饰符),cons[1]为private构造器
        cons[1].setAccessible(true);
        Object obj = cons[1].newInstance("jack");
        obj = con.newInstance("rose", 22);
		/*
		cons: public Demo(java.lang.String,java.lang.Integer)
		cons: private Demo(java.lang.String)
		cons: public Demo()
		con: public Demo(java.lang.String,java.lang.Integer)
		带参的构造器,name:jack
		全参构造器,name:rose,age:22
		 */
    }

    public static void test3() throws Exception {
        Class<?> clazz = Demo.class;
        //获取成员变量
        //获取所有的public成员变量，包含父类的成员
        Field[] fields = clazz.getFields();
        //获取所有的成员变量
        fields = clazz.getDeclaredFields();
        for(Field f : fields){
            System.out.println("fields: " + f);
        }
        //获取单个public成员变量，包含父类的成员，参数为变量(区分大小写)
        Field field = clazz.getField("name");
        //获取单个成员变量
        field = clazz.getDeclaredField("age");
        System.out.println("field: " + field);
        Demo demo = (Demo) clazz.newInstance();
        //为对象的属性赋值
        fields[0].set(demo, "jack");
        ////暴力访问(忽略掉访问修饰符)，age为private
        field.setAccessible(true);
        field.set(demo, 18);
        System.out.println(demo);
        /*
        fields: public java.lang.String Demo.name
		fields: private java.lang.Integer Demo.age
		field: private java.lang.Integer Demo.age
		反射机制实例化对象的构造器
		Demo [name=jack, age=18]
         */
    }

    public static void test4() throws Exception {
        Class<?> clazz = Demo.class;
        //获取成员方法
        //获取全部public方法，包含继承父类/Object的方法
        Method[] methods = clazz.getMethods();
        //获取全部方法，不包含父类/Object方法
        methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("methods: " + m);
        }
        //获取单个public方法，包含继承父类/Object的方法
        Method method = clazz.getMethod("method", String.class, Integer.class);
        //获取全部方法，不包含父类/Object方法
        method = clazz.getDeclaredMethod("method");
        System.out.println("method: " + method);
        Demo demo = (Demo) clazz.newInstance();
        //调用方法
        methods[3].invoke(demo, "rose", 18);
        method.setAccessible(true);
        method.invoke(demo);
		/*
		methods: public java.lang.String Demo.toString()
		methods: public java.lang.String Demo.getName()
		methods: public void Demo.setName(java.lang.String)
		methods: public void Demo.method(java.lang.String,java.lang.Integer)
		methods: protected void Demo.method(java.lang.String)
		methods: private void Demo.method()
		methods: public java.lang.Integer Demo.getAge()
		methods: public void Demo.setAge(java.lang.Integer)
		method: private void Demo.method()
		反射机制实例化对象的构造器
		Demo method3 name:rose age:18
		Demo method1
		 */
    }

    static class Demo {

        public String name;
        private Integer age;

        public Demo() {
            System.out.println("反射机制实例化对象的构造器");
        }

        private Demo(String name) {
            System.out.println("带参的构造器,name:" + name);
        }

        public Demo(String name, Integer age) {
            System.out.println("全参构造器,name:" + name + ",age:" + age);
        }

        private void method() {
            System.out.println("Demo method1");
        }

        protected void method(String name) {
            System.out.println("Demo method2 name:" + name);
        }

        public void method(String name, Integer age) {
            System.out.println("Demo method3 name:" + name + " age:" + age);
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
        @Override
        public String toString() {
            return "Demo [name=" + name + ", age=" + age + "]";
        }

    }
}
