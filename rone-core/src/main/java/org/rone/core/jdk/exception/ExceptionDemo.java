package org.rone.core.jdk.exception;

import java.io.*;

/**
 * 异常机制
 * ●.异常捕获的语法：
 *   try {
 *  	// 可能出错的程序
 *   } catch (Exception e) {
 *  	// 出错后的处理
 *   } finally {
 *  	// 最终执行的部分
 *   }
 * ●.异常的分类：
 * 	Error，一般由于运行环境导致，例如JVM退出等。
 * 	Exception，程序本身可以处理的错误。
 * 		不受检查异常：派生于RuntimeException的所有异常，编译时无需强制处理(捕获异常try{}cath{}、抛出异常throws声明抛出)。
 * 		受检查异常：除去不受检查异常的所有异常，需要强制处理。
 * ●.异常机制设计注意项
 *     ●. 在Finally块中清理资源或者使用try-with-resource语句，不能在try块的最后关闭资源，
 *         因为一旦之前的代码抛出异常，最后的关闭资源的代码时不会执行的。
 *     ●. 尽可能的使用最具体的异常来声明方法，这样才能使得代码更容易理解，不要一股脑的都是Exception。
 *     ●. 在Javadoc中加入throws声明，并且描述抛出异常的场景。
 *     ●. 在抛出异常时，需要尽可能精确地描述问题和相关信息，这样无论是打印到日志中还是监控工具中，都能够更容易被人阅读。
 *     ●. 首先捕获最具体的异常，多catch时首先捕获叶子异常(没有子类的异常)。
 *     ●. 不要捕获Throwable。Throwable是所有异常和错误的父类。如果catch了throwable，那么不仅仅会捕获所有exception，
 *         还会捕获error。而error是表明无法恢复的jvm错误。
 *     ●. 不要忽略异常，光捕获不做后续的处理，不要太过自信，至少要记录异常的信息。
 *     ●. 不要日志记录后又抛出异常，如果调用方也输出了日志会导致日志重复，增加排查错误的复杂性。
 *     ●. 包装自己的异常时不要抛弃原始的异常，便于查错。
 * @author rone
 */
public class ExceptionDemo {

    public static void main(String[] args) {
        System.out.println(tryCatchFinally());
        System.out.println("**********************");
        tryWithResource();
        System.out.println("**********************");
    }

    /**
     * try-catch-finally 语法
     * ●.当有多个catch时，基类(父类)必须在派生类(子类)后面，因为派生类异常会被基类catch捕获
     * ●.finally会在try{}catch{}之后执行，如果try{}catch{}中有return等控制转移语句(return、throw、break、continue)，会在return之前执行。
     * ●.如果return有返回值，try{}catch{}子句会保留其返回值到本地变量表中，待finally子句执行完毕之后，再恢复保留的返回值到操作数栈中，然后再返回值。
     * @return
     */
    public static String tryCatchFinally() {
        String result = "hello world!";
        try {
            throw new RoneException("this may caused by test.", new NullPointerException("空指针"));
        } catch (NullPointerException e) {
            System.out.println("NullPointerException, " + e.getMessage());
            return result;
        } catch (RoneException e) {
            System.out.println("RoneException, " + e.getMessage());
            return result;
        } catch (Exception e) {
            System.out.println("Exception, " + e.getMessage());
            return result;
        } finally {
            result = "fuck the world!";
            System.out.println("finally block. result: " + result);
        }
    }

    /**
     * try-with-resource 语法
     * 当使用类似InputStream这种需要使用后关闭的资源时，可以使用 try-with-resource 语法
     * 在 try (...) 中的内容会在try-catch 执行完毕后自动释放
     */
    public static void tryWithResource() {
        File file = new File("C:\\Windows\\System32\\drivers\\etc\\hosts");
        try (
                InputStream inputStream = new FileInputStream(file)
        ) {
            System.out.println(inputStream.read());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException, " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException, " + e.getMessage());
        }
    }
}
