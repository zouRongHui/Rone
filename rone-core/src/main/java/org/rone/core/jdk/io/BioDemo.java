package org.rone.core.jdk.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * BIO （Blocking I/O）：同步阻塞 I/O 模式，数据的读取写入必须阻塞在一个线程内等待其完成。BIO 就是传统的 java.io 包下面的代码实现。
 * BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4 以前的唯一选择，但程序直观简单易理解。
 * BIO 实现文件的读取和写入
 * @author zouRongHui
 * @date 2022/12/6 16:15
 */
public class BioDemo {

    private static Logger logger = LoggerFactory.getLogger(BioDemo.class);

    public static void main(String[] args) {
        writeToFile();
        logger.info("#########################################");
        readFromFile();
    }


    /**
     * 字符流转成字节流
     * @throws Exception
     */
    private static void outputWriter() throws Exception {
        File f = new File("/rone.txt");
        // OutputStreamWriter 是字符流通向字节流的桥梁,创建了一个字符流通向字节流的对象
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        osw.write("我是字符流转换成字节流输出的");
        osw.close();
    }

    /**
     * 字节流转成字符流
     * @throws Exception
     */
    private static void inputReader() throws Exception {
        File f = new File("/rone.txt");
        InputStreamReader inr = new InputStreamReader(new FileInputStream(f), "UTF-8");
        char[] buf = new char[1024];
        int len = inr.read(buf);
        logger.info(new String(buf,0,len));
        inr.close();
    }

    private static void writeToFile() {
        User user = new User();
        user.setName("hollis");
        user.setAge(23);
        logger.info(user.toString());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/bio.txt"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void readFromFile() {
        File file = new File("/bio.txt");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            User newUser = (User) ois.readObject();
            logger.info(newUser.toString());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static class User implements Serializable {
        private String name;
        private Integer age;

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
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
