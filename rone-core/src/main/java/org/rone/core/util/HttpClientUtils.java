package org.rone.core.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
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

    /** http连接池最大连接数 */
    private static final int MAX_TOTAL = 200;
    /** http连接池每条链路的最大连接数 */
    private static final int MAX_PER_ROUTE = 100;
    /** socket超时配置，单位毫秒 */
    private static final int SOCKET_TIMEOUT = 120000;
    /** 编码格式 */
    private static final String UTF8 = "UTF-8";

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
        return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
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
        //配置链接池
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        //最大连接数
        pool.setMaxTotal(MAX_TOTAL);
        //每条链路的最大连接数
        pool.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        //设置编码格式
        pool.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Charsets.toCharset(Charset.forName(UTF8))).build());
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build();
        pool.setDefaultSocketConfig(socketConfig);
        httpClient = HttpClients.custom().setConnectionManager(pool).build();
    }
}
