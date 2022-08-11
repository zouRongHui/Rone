package org.rone.core.jdk.exception;

import java.io.*;

/**
 * 异常机制
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
