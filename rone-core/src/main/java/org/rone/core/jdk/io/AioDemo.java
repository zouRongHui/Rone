package org.rone.core.jdk.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AIO （ Asynchronous I/O）：异步非阻塞 I/O 模型。异步非阻塞与同步非阻塞的区别在哪里？异步非阻塞无需一个线程去轮询所有 IO 操作的状态改变，在相应的状态改变后，系统会通知对应的线程来处理。
 * AIO 方式适用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用 OS 参与并发操作，编程比较复杂，JDK7 开始支持。
 * AIO 实现文件的读取和写入
 * @author zouRongHui
 * @date 2022/12/6 16:16
 */
public class AioDemo {

    private static Logger logger = LoggerFactory.getLogger(AioDemo.class);

    public static void main(String[] args) throws Exception {
        readFromFile();
        logger.info("############################################");
        writeToFile();
    }

    private static void readFromFile() throws IOException, ExecutionException, InterruptedException {
        Path file = Paths.get("/aio.txt");
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(file);
        ByteBuffer buffer = ByteBuffer.allocate(10000);
        Future<Integer> result = channel.read(buffer, 0);
        while (!result.isDone()) {
            AioDemo.done();
        }
        Integer bytesRead = result.get();
        logger.info("Bytes read [" + bytesRead + "]");
    }

    private static void writeToFile() throws IOException {
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                Paths.get("/aio.txt"), StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        CompletionHandler<Integer, Object> handler = new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                logger.info("Attachment: " + attachment + " " + result + " bytes written");
                logger.info("CompletionHandler Thread ID: " + Thread.currentThread().getId());
            }
            @Override
            public void failed(Throwable e, Object attachment) {
                System.err.println("Attachment: " + attachment + " failed with:");
                logger.error(e.getMessage(), e);
            }
        };
        logger.info("Main Thread ID: " + Thread.currentThread().getId());
        fileChannel.write(ByteBuffer.wrap("Sample".getBytes()), 0, "First Write", handler);
        fileChannel.write(ByteBuffer.wrap("Box".getBytes()), 0, "Second Write", handler);
    }

    private static void done() {
        logger.info("done.................");
    }
}
