package org.rone.core.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.Charsets;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 使用HttpClient实现http交互的工具类
 * @author rone
 */
public class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    private HttpClientUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static CloseableHttpClient httpClient = null;

    static {
        init();
    }

    /**
     * get请求，参数为form表单格式
     * @param url   地址
     * @param paramMap  key-value形式的参数
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void getWithFormParam(String url, Map<String, String> paramMap) throws URISyntaxException, IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (paramMap != null) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
        }

        HttpGet httpGet = new HttpGet(new URIBuilder(url).setParameters(nameValuePairs).build());
        String resultString = executeRequest(httpGet);
        LOGGER.info("请求URL：{} \n参数为：{} \n返回结果：{}", url, JSON.toJSONString(paramMap), resultString);
    }

    /**
     * post请求，参数为json格式
     * @param url   地址
     * @param jsonStr   json格式的参数
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void postWithJsonParam(String url, String jsonStr) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost((new URIBuilder(url)).build());
        httpPost.setEntity(new StringEntity(jsonStr, "utf-8"));
        String resultString = executeRequest(httpPost);
        LOGGER.info("请求URL：{} \n参数为：{} \n返回结果：{}", url, jsonStr, resultString);
    }

    /**
     * 执行请求
     * @param httpRequestBase   请求体
     * @return  用String格式返回接口数据
     * @throws IOException
     */
    private static String executeRequest(HttpRequestBase httpRequestBase) throws IOException {
        CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
        CloseableHttpResponse httpResponse = httpClient.execute(httpRequestBase);
        HttpEntity responseEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        // 释放http实体所占用的系统资源，包含内存和链接(会将链接归还到连接池)
        EntityUtils.consume(httpResponse.getEntity());
        httpResponse.close();
        return result;
    }

    /**
     * 获取HttpClient
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpClientUtils.class) {
                if (httpClient == null) {
                    init();
                }
            }
        }
        return httpClient;
    }

    /**
     * 初始化，配置连接池等属性
     */
    private static void init() {
        //配置链接池，PoolingHttpClientConnectionManager是HttpClient中连接池管理器的实现类，用于管理HTTP连接池
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        // 设置连接池最大连接数。该值为整型数，默认为20
        pool.setMaxTotal(10);
        // 设置单个路由最大连接数。该值为整型数，默认为2
        pool.setDefaultMaxPerRoute(5);
        // 设置连接空闲时间。该值为整型数，表示连接在指定时间内未被使用，则需要进行一次验证，默认为2000毫秒
        pool.setValidateAfterInactivity(2000);
        // ConnectionConfig，用于配置HTTP连接相关的参数
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                // 设置HTTP连接缓冲区的大小。该值为整型数
                .setBufferSize(8 * 1024)
                // 设置HTTP连接片段大小提示。该值为整型数
                .setFragmentSizeHint(8 * 1024)
                // 设置HTTP连接字符集编码。该值为Charset对象
                .setCharset(Charsets.toCharset(Charset.forName("UTF-8")))
                // 设置HTTP连接约束条件。通过MessageConstraints对象配置HTTP连接约束条件，包括请求头最大长度、响应头最大长度、HTTP消息体最大长度等
                .setMessageConstraints(null)
                .build();
        // 设置HTTP连接默认Connection配置，可以通过ConnectionConfig对象进行详细配置
        pool.setDefaultConnectionConfig(connectionConfig);
        // SocketConfig，用于配置HTTP请求的Socket参数
        SocketConfig socketConfig = SocketConfig.custom()
                // 是否立即发送数据。该值为true表示不使用Nagle算法，立即发送数据，默认为true
                .setTcpNoDelay(true)
                // 是否启用SO_REUSEADDR。该值为true表示启用SO_REUSEADDR，允许多个Socket绑定到相同的本地端口，默认为false
                .setSoReuseAddress(false)
                // 设置SO_TIMEOUT的值。即读取数据超时时间。该值为0表示无穷大超时时间，默认为0
                .setSoTimeout(1000 * 60)
                // 设置SO_LINGER的值。即关闭Socket时，是否立即释放资源。该值为-1表示无穷大等待时间，0表示关闭Socket时立即释放资源，大于0表示等待指定时间后再关闭Socket，默认为-1
                .setSoLinger(-1)
                // 是否开启TCP KeepAlive机制。该值为true表示启用TCP KeepAlive机制，在空闲时发送探测包，检测连接是否可用，默认为false
                .setSoKeepAlive(false)
                .build();
        // 设置HTTP连接默认Socket配置，可以通过SocketConfig对象进行详细配置
        pool.setDefaultSocketConfig(socketConfig);

        httpClient = HttpClients.custom()
                // 配置连接池
                .setConnectionManager(pool)
                // 设置HTTP连接保持策略的方法，默认使用如下的配置。 当使用HTTP协议发送请求时，通常可以在请求头中设置“Connection: keep-alive”，这样TCP连接就可以被重复使用，以减少每个请求的连接建立和关闭所需的成本和处理时间。 通过HttpClientBuilder.setKeepAliveStrategy()方法，可以自定义保持连接的策略。保持连接的策略定义了一个连接保持时间，即一个连接在空闲状态下能保持多长时间，以及失效连接检测的策略。这样可以最大程度地利用现有的连接，提高请求效率。
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                // 定期清除过期HTTP连接的方法，默认不清除。 当使用HTTP协议发送请求时，HTTP连接池会缓存已经建立的连接并进行重用，而不是在每个请求之前都建立一个新的连接。但是，一旦某个连接的使用时间超过了一定的阈值，这个连接就会过期而被清除掉。 通过HttpClientBuilder.evictExpiredConnections()方法，可以定期清除过期的HTTP连接，这个方法会在一段时间间隔内执行，自动关闭已过期的连接。 这个方法可以保证使用HTTP连接池的应用程序不会使用过期的连接，从而提高应用程序的性能和可靠性。
                .evictExpiredConnections()
                // 定期清除空闲HTTP连接的方法，默认不清除。 当使用HTTP协议发送请求时，HTTP连接池会缓存已经建立的连接并进行重用，而不是在每个请求之前都建立一个新的连接。如果连接在一段时间内没有被使用，那么这个连接就会被认为是空闲连接，这些空闲连接会占据宝贵的系统资源。 通过HttpClientBuilder.evictIdleConnections()方法，可以定期清除空闲HTTP连接，这个方法会在一段时间间隔内执行，自动关闭空闲的连接。 这个方法可以保证使用HTTP连接池的应用程序不会占用过多的系统资源，从而提高应用程序的性能和可靠性。
                .evictIdleConnections(10, TimeUnit.SECONDS)
                // 设置HTTP连接重用策略的方法，默认使用如下的配置。  当使用HTTP协议发送请求时，HTTP连接池会缓存已经建立的连接并进行重用，而不是在每个请求之前都建立一个新的连接。通过设置HTTP连接重用策略，可以控制连接的重用行为。不同的重用策略会影响连接的生命周期和应用程序的性能。常用的重用策略有两种： 1. DefaultConnectionReuseStrategy（默认策略） - 如果HTTP响应头中包含了“Connection: Keep-Alive”标签，则重用连接。否则，关闭连接并创建一个新的连接。 2. NoConnectionReuseStrategy - 不重用连接。在发出每个请求之前，都会创建一个新的连接。 这个方法可以根据应用程序的特点和需求灵活地控制连接的重用行为
                .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                // 设置代理服务器
                // .setProxy(new HttpHost("proxy.rone.com", 8080))
                // 禁用重定向
                // .disableRedirectHandling()
                .build();

        // //定时打印连接池状态
        // Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
        //     // leased：表示正在使用的连接数。即已经被某个请求获取到的连接数
        //     // available：表示可用的连接数。即没有被任何请求占用的连接数
        //     // pending：表示等待获取连接的请求数。即已经发送了获取连接请求，但还没有获取到连接的请求数
        //     LOGGER.info("当前线程池的状态：{}", pool.getTotalStats().toString());
        // }, 100L, 1000L, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    getWithFormParam("http://localhost:8111/user/getAllUsers", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
