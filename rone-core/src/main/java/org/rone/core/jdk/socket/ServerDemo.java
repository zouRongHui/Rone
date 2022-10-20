package org.rone.core.jdk.socket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.rone.core.jdk.exception.RoneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Socket服务机
 * @author rone
 */
public class ServerDemo {

    public static void main(String[] args) {
        Thread socketServerThread = new Thread(new SocketServer());
        socketServerThread.setName("Socket Server");
        socketServerThread.start();
        System.out.println("主线程可继续处理其他业务流程");
    }

    /**
     * socket服务，另起一个线程来当做socket服务线程，避免阻塞主线程
     */
    static class SocketServer implements Runnable {

        public static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

        /**
         * 服务端口
         */
        private Integer port = 3333;
        /**
         * ServerSocket接收客户端请求时的队列长度
         */
        private int backlog = 0;
        /**
         * 指定ServerSocket服务绑定的地址，null表示当前地址(单网卡情况无需指定)
         */
        private String bindAddress = null;
        /**
         * 读取超时时间
         */
        private Integer timeout = 1000 * 60;

        @Override
        public void run() {
            // 具体处理业务的线程池
            ThreadPoolExecutor businessThreadPool = new ThreadPoolExecutor(2, 5, 0L,
                    TimeUnit.NANOSECONDS, new LinkedBlockingDeque(10000),
                    Executors.defaultThreadFactory());
            try (
                    ServerSocket serverSocket = new ServerSocket(this.port, this.backlog, StrUtil.isNotEmpty(this.bindAddress) ? InetAddress.getByName(this.bindAddress) : null);
            ) {
                LOGGER.info("ServerSocket服务初始化成功，端口：{}", this.port);

                while (true) {
                    try {
                        // 阻塞当前线程直到获取到客户端的请求
                        Socket socket = serverSocket.accept();
                        if (socket != null) {
                            // 配置重用地址
                            socket.setReuseAddress(true);
                            // 使用线程池来处理具体的业务
                            SocketTask socketTask = new SocketTask(socket, this.timeout);
                            businessThreadPool.execute(socketTask);
                        }
                    } catch (IOException e) {
                        LOGGER.error("ServerSocket服务异常：{}", e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("ServerSocket服务({})初始化异常:{}", this.port, e.getMessage(), e);
                return;
            } finally {
                businessThreadPool.shutdown();
            }
        }
    }

    /**
     * Socket通信具体的业务处理
     */
    static class SocketTask implements Runnable {

        public static final Logger LOGGER = LoggerFactory.getLogger(SocketTask.class);

        private Socket socket;
        /**
         * 超时时间
         */
        private Integer timeout;
        /**
         * Json报文中业务编号的参数名
         */
        public static final String BUSINESS_CODE = "businessCode";
        /**
         * Json报文中业务数据的参数名
         */
        public static final String DATA = "data";
        /**
         * 报文中记录具体数据长度的字段的字节个数
         */
        private static final int LENGTH_LENGTH = 8;
        /**
         * 报文中记录具体数据长度的格式
         */
        public static final String LENGTH_FORMAT = "%08d";
        /**
         * 报文编码
         */
        private static final String DEFAULT_ENCODING = "UTF-8";

        public SocketTask(Socket socket, Integer timeout) {
            this.socket = socket;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            // 用一个计时器来控制服务端响应超时时间
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    closeConnection();
                }
            }, this.timeout);


            if (this.socket != null && this.socket.isConnected()) {
                try {
                    // 处理数据
                    String requestData = getRequestBody(this.socket.getInputStream());
                    String responseData = doBusiness(requestData);
                    this.socket.getOutputStream().write((String.format(LENGTH_FORMAT, responseData.getBytes(DEFAULT_ENCODING).length) + responseData).getBytes(DEFAULT_ENCODING));
                    this.socket.shutdownOutput();
                    LOGGER.info("Socket通信接收报文：{}，返回报文：{}", requestData, responseData);
                } catch (SocketException e) {
                    // 存在socket连接已关闭的情况
                } catch (IOException e) {
                    LOGGER.info("Socket通信出错，详见error日志文件");
                    LOGGER.error("Socket通信出错,", e);
                } finally {
                    closeConnection();
                }
            }
        }

        /**
         * 获取客户端的请求数据
         *
         * @param in
         * @return
         * @throws IOException
         */
        private String getRequestBody(InputStream in) throws IOException {
            // 获取请求具体数据长度
            byte[] bodyLengthBytes = new byte[LENGTH_LENGTH];
            in.read(bodyLengthBytes);
            int bodyLength = Integer.parseInt(new String(bodyLengthBytes).trim());
            // 获取请求数据
            byte[] bytes = new byte[bodyLength];
            in.read(bytes);
            return new String(bytes, DEFAULT_ENCODING).trim();
        }

        /**
         * 根据业务编号进行具体的业务处理
         *
         * @param requestDataJsonStr
         * @return
         */
        private String doBusiness(String requestDataJsonStr) {
            try {
                JSONObject requestData = JSON.parseObject(requestDataJsonStr);
                String businessCode = requestData.getString(BUSINESS_CODE);
                Class<Business> clazz = SocketBusinessCodeEnum.getClass(businessCode);
                Business business = clazz.newInstance();
                return JSON.toJSONString(business.execute(requestData.getString(DATA)));
            } catch (RoneException e) {
                return JSON.toJSONString(new Result<>(Result.ResultCodeEnum.PARAM_ERROR.getCode(), e.getMessage()));
            } catch (Exception e) {
                LOGGER.error("Socket通信，业务处理出错：{}", e.getMessage(), e);
                return JSON.toJSONString(Result.fault("系统异常"));
            }
        }

        /**
         * 关闭连接
         */
        private void closeConnection() {
            if (this.socket != null && this.socket.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 接口编码
     */
    static enum SocketBusinessCodeEnum {
        business_1("001", Business1Impl.class, "交易1"),
        business_2("002", Business2Impl.class, "交易2");

        /**
         * 接口交易码
         */
        private String businessCode;
        /**
         * 服务处理实现类
         */
        private Class<Business> clazz;
        /**
         * 描述
         */
        private String desc;

        SocketBusinessCodeEnum(String businessCode, Class clazz, String desc) {
            this.businessCode = businessCode;
            this.clazz = clazz;
            this.desc = desc;
        }

        public static Class<Business> getClass(String businessCode) throws RoneException {
            for (SocketBusinessCodeEnum socketBusinessCodeEnum : SocketBusinessCodeEnum.values()) {
                if (socketBusinessCodeEnum.getBusinessCode().equals(businessCode)) {
                    return socketBusinessCodeEnum.getClazz();
                }
            }
            throw new RoneException("业务编号不存在");
        }

        public String getBusinessCode() {
            return businessCode;
        }

        public Class<Business> getClazz() {
            return clazz;
        }

        public String getDesc() {
            return desc;
        }

    }

    /**
     * 业务处理
     */
    interface Business {
        /**
         * 业务执行器
         *
         * @param dataJsonStr 请求参数的，json的字符串
         * @return
         * @throws RoneException
         */
        Result execute(String dataJsonStr) throws RoneException;
    }

    /**
     * 业务1
     */
    static class Business1Impl implements Business {
        @Override
        public Result execute(String dataJsonStr) throws RoneException {
            System.out.println("业务1处理，处理的数据为：" + dataJsonStr);
            return Result.success("操作成功！");
        }
    }

    /**
     * 业务2
     */
    static class Business2Impl implements Business {
        @Override
        public Result execute(String dataJsonStr) {
            System.out.println("业务2处理，处理的数据为：" + dataJsonStr);
            return Result.success("操作成功！");
        }
    }

    /**
     * 返回的数据格式
     * @param <T>
     */
    static class Result<T> {
        /**
         * 状态值
         */
        private Integer code;
        /**
         * 当状态值说明
         */
        private String message;
        /**
         * 返回的具体数据
         */
        private T data;

        public static Result success(String message) {
            return new Result(0, message, null);
        }

        public static Result fault(String message) {
            return new Result(1, message, null);
        }

        public Result(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Result(Integer code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }

        /**
         * 状态码
         */
        public enum ResultCodeEnum {
            SUCCESS(00000, "操作成功"),
            FAIL(10000, "操作失败，详细原因见错误说明"),
            PARAM_ERROR(10001, "参数出错，请检查参数后重新操作");

            private Integer code;
            private String message;

            ResultCodeEnum(Integer code, String message) {
                this.code = code;
                this.message = message;
            }

            public Integer getCode() {
                return code;
            }

            public String getMessage() {
                return message;
            }
        }
    }
}
