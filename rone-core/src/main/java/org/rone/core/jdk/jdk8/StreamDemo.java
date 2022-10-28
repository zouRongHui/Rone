package org.rone.core.jdk.jdk8;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * java8 的Stream API
 * ●.语法：
 *     流的操作类型分为两种：
 *         Intermediate  中间操作：一个流可以后面跟随零个或多个 intermediate 操作。
 *             其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。
 *             这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历，而是到了 Terminal 操作时才真正执行。
 *         Terminal：一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。
 *         所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果，或者一个 side effect。
 *     Intermediate：
 *         map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、limit、 skip、 parallel、 sequential、 unordered
 *     Terminal：
 *       forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 *     并行流与串行流
 *         并行流就是把一个内容分成多个数据块，并用不同的线程分别处理每个数据块的流。
 *         Stream API 可以声明性地通过 parallel() 与 sequential() 在并行流与顺序流之间进行切换。
 * @author rone
 */
public class StreamDemo {

    public static void main(String[] args) throws InterruptedException {
        structure();
        System.out.println("########################################################################################");
        testFilter();
        System.out.println("########################################################################################");
        testDistinct();
        System.out.println("########################################################################################");
        testLimit();
        System.out.println("########################################################################################");
        testSkip();
        System.out.println("########################################################################################");
        testMap();
        System.out.println("########################################################################################");
        testFlatMap();
        System.out.println("########################################################################################");
        testSort();
        System.out.println("########################################################################################");
        testPeek();
        System.out.println("########################################################################################");

        testFindFirst();
        System.out.println("########################################################################################");
        testFindAny();
        System.out.println("########################################################################################");
        testCount();
        System.out.println("########################################################################################");
        testMax();
        System.out.println("########################################################################################");
        testMin();
        System.out.println("########################################################################################");
        testForEach();
        System.out.println("########################################################################################");
        testReduce();
        System.out.println("########################################################################################");
        testToArray();
        System.out.println("########################################################################################");
        testCollect();
        System.out.println("########################################################################################");
        testMatch();
    }

    /**
     * 创建 Stream 的几种方法
     * @return
     */
    public static void structure() {
        Stream stream;
        // 由值创建流 Stream.of(T… values) 接受一个泛型数组
        stream = Stream.of("a", "b", "c");
        // 由数组创建Stream
        String [] strArray = new String[] {"a", "b", "c"};
        stream = Arrays.stream(strArray);
        // 通过 Collections 实现类直接获取
        List<String> list = Arrays.asList(strArray);
        stream = list.stream();
    }

    /** 以下为中间操作(Intermediate)，需要终止操作来触发执行 */

    /**
     * 过滤
     */
    public static void testFilter() {
        //过滤掉非偶数
        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        Integer[] evens = Stream.of(sixNums).filter(n -> n%2 == 0).toArray(Integer[]::new);
        Arrays.stream(evens).forEach(System.out::print);
        System.out.println();
    }

    /**
     * 去重，通过元素的 hashCode() 和 equals() 来去重
     */
    public static void testDistinct() {
        System.out.println(Stream.of("java", "c", "php", "java", "c++", "c#").distinct().collect(Collectors.toList()));
    }

    /**
     * 从头部截取
     */
    public static void testLimit() {
        System.out.println(Stream.of("one", "two", "three", "four", "five").limit(3).collect(Collectors.toList()));
    }

    /**
     * 从头部跳过多少个元素，若不足则返回空流
     */
    public static void testSkip() {
        System.out.println(Stream.of("one", "two", "three", "four", "five").skip(3).collect(Collectors.toList()));
        System.out.println(Stream.of("one", "two", "three", "four", "five").skip(6).collect(Collectors.toList()));
    }

    /**
     * 接收一个函数作为参数，该函数会作用到每一个元素上，并返回一个新元素
     */
    public static void testMap() {
        //转成大写
        List<String> wordList = Arrays.asList("Java", "C", "C++", "JavaScript", "Python");
        List<String> output = wordList.stream().
                map(String::toUpperCase).
                collect(Collectors.toList());
        System.out.println(output);

        //平方数
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> squareNums = nums.stream().
                map(n -> n * n).
                collect(Collectors.toList());
        System.out.println(squareNums);
    }

    /**
     * 接收一个函数，作用到每个元素上，并返回一个流，然后将所有返回的流组成一个新的流
     */
    public static void testFlatMap() {
        // flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，
        // 最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.flatMap(childList -> childList.stream());
        System.out.println(outputStream.collect(Collectors.toList()));
    }

    /**
     * 排序
     *  使用默认的排序，需要流中的元素实现 Comparable 接口
     *  也可当场指定排序器
     */
    public static void testSort() {
        // 使用默认排序器
        System.out.println(Stream.of("one", "two", "three", "four").sorted().collect(Collectors.toList()));

        // 指定排序器
        System.out.println(Stream.of(35, 43, 2, 103).sorted((p1, p2) -> p1.compareTo(p2)).collect(Collectors.toList()));

        List<Person> persons = new ArrayList();
        persons.add(new Person(4, "java"));
        persons.add(new Person(3, "c"));
        persons.add(new Person(6, "php"));
        persons.add(new Person(0, "c++"));
        persons.add(new Person(1, "object-c"));
        persons.add(new Person(9, "python"));

        persons = persons.stream().sorted((p1, p2) -> p1.getNo().compareTo(p2.getNo())).collect(Collectors.toList());
        System.out.println(persons);
        persons = persons.stream().sorted(Comparator.comparing(Person::getName)).collect(Collectors.toList());
        System.out.println(persons);
    }

    /**
     * 接收一个函数，作用到每个元素上，函数无返回值。可实现遍历、修改元素等操作
     */
    public static void testPeek() {
        List<String> list = Stream.of("one", "two", "three", "four")
                .peek(e -> System.out.println("original value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("upperCase value: " + e))
                .collect(Collectors.toList());
        System.out.println(list);
    }

    /** 一下为终止操作(Terminal)，调用立即执行 */

    /**
     * 返回第一个元素
     */
    public static void testFindFirst() {
        System.out.println(Stream.of("one", "two", "three", "four").findFirst());
    }

    /**
     * 随机返回一个元素
     */
    public static void testFindAny() {
        System.out.println(Stream.of("one", "two", "three", "four").findAny());
    }

    /**
     * 返回 Stream 中元素个数
     */
    public static void testCount() {
        System.out.println(Stream.of("one", "two", "three", "four").count());
    }

    /**
     * 最大值，元素需要实现 Comparable 接口
     */
    public static void testMax() {
        System.out.println(IntStream.of(12, 3, 4, 32, 98, 3).max().getAsInt());
    }

    /**
     * 最小值，元素需要实现 Comparable 接口
     */
    public static void testMin() {
        System.out.println(IntStream.of(12, 3, 4, 32, 98, 3).min().getAsInt());
    }

    /**
     * 迭代
     */
    public static void testForEach() {
        Stream.of("one", "two", "three", "four").forEach(System.out::println);
    }

    /**
     * 接收一个元素作为初始值(初始值可省略不要)，接收一个函数 BinaryOperator 作用到每个元素上，最终返回一个元素对象。
     *  函数的两个参数第一个为之前一个元素处理的返回值(若为第一个元素则为初始值)，第二个为当前元素
     */
    public static void testReduce() {
        Stream.of("A", "B", "C", "D").reduce("fuck ", (a, b) -> {
            System.out.print("First: " + a);
            System.out.print("   Second: " + b);
            System.out.println();
            return a + b;
        });
        // 字符串连接，concat = "ABCD"
        System.out.println("concat = " + Stream.of("A", "B", "C", "D").reduce(String::concat));
        // 求最小值，minValue = -3.0
        System.out.println("minValue = " + Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min));
        // 求和，sumValue = 10, 有起始值
        System.out.println("sumValue = " + Stream.of(1, 2, 3, 4).reduce(10, Integer::sum));
        // 求和，sumValue = 10, 无起始值
        System.out.println("sumValue = " + Stream.of(1, 2, 3, 4).reduce(Integer::sum).get());
    }

    /**
     * toArray 接收一个函数(IntFunction)，参数为元素个数，函数返回值为一个数组对象
     */
    public static void testToArray() {
        // 转换成 Array
        String[] strArray = Stream.of("A", "B", "C", "D").toArray(value -> new String[value]);
        strArray = Stream.of("A", "B", "C", "D").toArray(String[]::new);
        Arrays.stream(strArray).forEach(System.out::println);
    }

    /**
     * collect 接收一个对象(Collector)，最终返回一种数据结构对象
     */
    public static void testCollect() {
        // 转换成 Collection
        List<String> list = Stream.of("A", "B", "C", "D").collect(Collectors.toList());
        System.out.println(list);
        list = Stream.of("A", "B", "C", "D").collect(Collectors.toCollection(ArrayList::new));
        System.out.println(list);
        Set set = Stream.of("A", "B", "C", "D").collect(Collectors.toSet());
        System.out.println(set);
        Stack stack = Stream.of("A", "B", "C", "D").collect(Collectors.toCollection(Stack::new));
        System.out.println(stack);
        // 转换成 String
        String str = Stream.of("A", "B", "C", "D").collect(Collectors.joining());
        System.out.println(str);
    }

    /**
     * 匹配，接收一个函数(Predicate)，参数为当前元素，函数返回值为 boolean，函数体为具体的匹配规则
     * allMatch 全部满足则返回true
     * anyMatch 只要一个满足则返回true
     * noneMatch 没有满足则返回true
     */
    public static void testMatch() {
        System.out.println(Stream.of("java", "c", "c++").allMatch(p -> p.length() > 1));
        System.out.println(Stream.of("java", "c", "c++").anyMatch(p -> p.equals("c")));
        System.out.println(Stream.of("java", "c", "c++").noneMatch(p -> p.equals("php")));
    }

    public static class Person {
        public Integer no;
        private final String name;
        public Person (Integer no, String name) {
            this.no = no;
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public Integer getNo() {
            return no;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
