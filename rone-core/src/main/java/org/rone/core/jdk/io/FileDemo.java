package org.rone.core.jdk.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件对象的操作
 * 创建文件
 *     if (!file.exists()) {
 *         //文件
 *         file.createNewFile();
 *         //文件夹
 *         file.mkdirs();
 *     }
 * 删除文件，若文件为文件夹，则需要删除其所有的子文件才能删除该文件夹，否则删除不成功
 *     file.delete();
 * 读取文件
 *     InputStream inputStream = new FileInputStream(file);
 *     int byteLength;
 *     byte[] bytes = new byte[1024];
 *     while ((byteLength = inputStream.read(bytes)) != -1) {
 *         System.out.println(new String(bytes, "utf-8"));
 *     }
 *     inputStream.close();
 * 写入文件
 *     OutputStream outputStream = new FileOutputStream(file);
 *     //true：写入的方式为为 追加，false：覆盖
 *     //OutputStream outputStream = new FileOutputStream(file, true);
 *     //write()方法支持很多参数
 *     outputStream.write("");
 *     outputStream.flush();
 *     outputStream.close();
 * 读取一个文件，写入到另一个文件中
 *     InputStream inputStream = new FileInputStream(new File(filePath));
 *     OutputStream outputStream = new FileOutputStream(new File(filePath));
 *     int byteLength;
 *     byte[] bytes = new byte[1024];
 *     while ((byteLength = inputStream.read(bytes)) != -1) {
 *         outputStream.write(bytes, 0, byteLength);
 *     }
 *     outputStream.flush();
 *     outputStream.close();
 *     inputStream.close();
 * @author rone
 */
public class FileDemo {

    public static void main(String[] args) {
        readFile(new File("E:\\rone.txt"), "txt");
    }

    private static void readFile(File file, String fileType) {
        // 判断文件是否存在
        if(file.exists()) {
            System.out.println(file.getName());
            if(file.isDirectory()) {
                // 获取子文件
                File[] files = file.listFiles();
                if(files != null) {
                    for (int i = 0; i< files.length; i++) {
                        readFile(files[i], fileType);
                    }
                }
            } else if(file.isFile()) {
                if(file.getName().endsWith("." + fileType)) {
                    System.out.println(file.getName() + "的内容:");
                    System.out.println("*******************************");
                    byte[] a = new byte[10];
                    try {
                        InputStream in = new FileInputStream(file);
                        while(in.read(a) != -1) {
                            String str = new String(a, "UTF-8");
                            System.out.print(str);
                        }
                        System.out.println();
                        System.out.println("*******************************");
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
