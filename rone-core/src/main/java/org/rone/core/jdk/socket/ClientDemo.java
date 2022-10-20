package org.rone.core.jdk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * Socket客户机
 * @author rone
 */
public class ClientDemo {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClientDemo.class);

    public static void main(String[] args) {
        // String content = "{\"businessCode\": \"001\",\"channel\":\"other\",\"flowNo\":\"2021090200001\",\"time\": \"2021-09-03 12:01:01\",\"data\": [{\"businessNo\": \"210902966401\",\"userNo\": \"35802012150100043063\",\"status\": \"0\"},{\"businessNo\": \"210902966410\",\"userNo\": \"35802012150100043063\",\"status\": \"1\"}]}";
        String content = "{\"businessCode\": \"002\",\"channel\":\"other\",\"flowNo\":\"2021090200001\",\"time\": \"2021-09-03 12:01:01\",\"data\": [{\"businessNo\": \"210902966401\",\"userNo\": \"35802012150100043063\",\"status\": \"0\"},{\"businessNo\": \"210902966410\",\"userNo\": \"35802012150100043063\",\"status\": \"1\"}]}";
        sendMessage(content, "127.0.0.1", 3333, 1000 * 3, "%08d", "UTF-8", 8);
    }

    public static void sendMessage(String sendContent,String socketIp,Integer port,Integer timeout,String headFormat,
                                   String encoding,Integer headLength) {
        try (
                Socket socket = new Socket(socketIp, port);
                InputStream in = socket.getInputStream();
        ) {
            socket.setSoTimeout(timeout);
            String seqNo = UUID.randomUUID().toString();
            LOGGER.info("初始化SOCKET服务,目标服务IP地址：{},目标服务端口：{}。接口描述：{}。发送内容：{}。序号为：{}", socketIp, port, sendContent, seqNo);
            long len = sendContent.getBytes(encoding).length;
            String head=String.format(headFormat, len);
            socket.getOutputStream().write(String.format("%s%s", head, sendContent).getBytes(encoding));
            socket.shutdownOutput();

            // 获取请求具体数据长度
            byte[] bodyLengthBytes = new byte[headLength];
            in.read(bodyLengthBytes);
            int bodyLength = Integer.parseInt(new String(bodyLengthBytes).trim());
            // 获取请求数据
            byte[] bytes = new byte[bodyLength];
            in.read(bytes);
            String result = new String(bytes, encoding).trim();
            LOGGER.info("目标服务IP地址：{},目标服务端口：{}。接口描述：{}。返回内容：{}。序号为：{}", socketIp, port, result, seqNo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try {
        // 	//向本机的xxxx端口发出客户请求
        // 	Socket socket = new Socket("127.0.0.1", 6668);
        // 	//由系统标准输入设备构建BufferedReader对象
        // 	BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        // 	//由socket对象得到输出流，并构造PrintWriter对象
        // 	PrintWriter os = new PrintWriter(socket.getOutputStream());
        // 	//由socket对象得到输入流，并构造相应的BufferedReader对象
        // 	BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 	//从系统标准输入读入一个字符串
        // 	String readline = sin.readLine();
        // 	//若读入字符串为 bye 则停止循环
        // 	while(!readline.equals("bye")) {
        // 		//将字符串输出到Server
        // 		os.println(readline);
        // 		//刷新输出流，使Server马上收到该字符串
        // 		os.flush();
        // 		//在系统标准输出上打印读入字符串
        // 		System.out.println("Client:" + readline);
        // 		//从Server读入一个字符串，并打印到标准输出上
        // 		System.out.println("Server:" + is.readLine());
        // 		//从系统标准输入读入一个字符串
        // 		readline = sin.readLine();
        // 	}
        // 	//关闭socket输出流
        // 	os.close();
        // 	//关闭socket输入流
        // 	is.close();
        // 	//关闭socket
        // 	socket.close();
        // } catch (Exception e) {
        // 	e.printStackTrace();
        // }
    }
}
