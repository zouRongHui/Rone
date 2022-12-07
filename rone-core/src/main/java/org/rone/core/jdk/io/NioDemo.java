package org.rone.core.jdk.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * NIO （New I/O）：同时支持阻塞与非阻塞模式。
 * NIO 与原来的 I/O 有同样的作用和目的, 他们之间最重要的区别是数据打包和传输的方式。原来的 I/O 以流的方式处理数据，而 NIO 以块的方式处理数据。
 * NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，并发局限于应用中，编程比较复杂，JDK1.4 开始支持。
 * NIO 实现文件的读取和写入
 * @author zouRongHui
 * @date 2022/12/6 11:28
 */
public class NioDemo {

    private static Logger logger = LoggerFactory.getLogger(NioDemo.class);

    public static void main(String[] args) {
        writeToFile();
        logger.info("#########################################");
        readFromFile();
    }

    private static void writeToFile() {
        try (FileOutputStream fos = new FileOutputStream(new File("/nio.txt"))) {
            FileChannel channel = fos.getChannel();
            ByteBuffer src = Charset.forName("utf8").encode("你好你好你好你好你好");
            // 字节缓冲的容量和 limit 会随着数据长度变化，不是固定不变的
            System.out.println("初始化容量和 limit：" + src.capacity() + "," + src.limit());
            int length;
            while ((length = channel.write(src)) != 0) {
                // 注意，这里不需要 clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                System.out.println("写入长度:" + length);
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void readFromFile() {
        try (FileInputStream fin = new FileInputStream(new File("/nio.txt"))) {
            FileChannel channel = fin.getChannel();
            // 字节
            ByteBuffer bf = ByteBuffer.allocate(100);
            logger.info("限制是：" + bf.limit() + "容量是：" + bf.capacity() + "位置是：" + bf.position());
            int length;
            while ((length = channel.read(bf)) != -1) {
                // 注意，读取后，将位置置为 0，将 limit 置为容量, 以备下次读入到字节缓冲中，从 0 开始存储
                bf.clear();
                byte[] bytes = bf.array();
                System.out.write(bytes, 0, length);
                System.out.println();
                logger.info("限制是：" + bf.limit() + "容量是：" + bf.capacity() + "位置是：" + bf.position());
            }
            channel.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
